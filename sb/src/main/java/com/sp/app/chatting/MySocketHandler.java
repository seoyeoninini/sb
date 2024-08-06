package com.sp.app.chatting;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
 - type : client -> server
   connect : 처음 접속한 경우 - uid, nickName
   message : 채팅 메시지 전송 - chatMsg
   whisper : 귓속말 - receiver, chatMsg
   
 - type : server -> client
   userList : 처음 접속한 경우 접속한 사용자 리스트 전송 - users
   userConnect : 다른 접속 사용자에게 지금 접속한 유저 전송 - uid, nickName
   message : 채팅 메시지 - uid, nickName, chatMag
   whisper : 귓속말 - uid, nickMsg, chatMsg
   userDisconnect : 접속한 사용자에게 접속 해제한 유저 전송 - uid, nickName
 */

public class MySocketHandler extends TextWebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(MySocketHandler.class);
	
	
	// 접속한 클라이언트 정보 <유저id, 유저>
	private Map<String, User> sessionMap = new Hashtable<String, User>(); // Hashtable : 동시성 지원
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		super.afterConnectionEstablished(session);
		
		// WebSocket이 사용될 준비가 될때 호출
		
	}
	
	// session : 핸드폰
	// message : 보낸 메시지
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
		// 클라이언트로부터 메시지가 전송된 경우 호출
		JSONObject jsonReceive = null;
		
		// getPayload() : 클라가 보낸 메세지
		try {
			jsonReceive = new JSONObject(message.getPayload().toString());
		} catch (Exception e) {
			return;
		}
		
		String type = jsonReceive.getString("type").trim();
		if(type == null || type.equals("")) {
			return;
		}
		
		// 채팅 타입 결정
		if(type.equals("connect")) {
			//처음 접속한 경우
			// 접속한 사용자의 아이디를 키로 session 과 유저 정보를 저장
			String uid = jsonReceive.getString("uid");
			String nickName = jsonReceive.getString("nickName");
			
			User user = new User();
			user.setUid(uid);
			user.setNickName(nickName);
			user.setSession(session);
			
			sessionMap.put(uid, user);
			
			// 처음 접속한 사용자에게 현재 접속중인 유저 리스트를 전송
			Iterator<String> it = sessionMap.keySet().iterator();
			
			JSONArray arrUsers = new JSONArray();
			while(it.hasNext()) {
				String key = it.next();
				
				if(uid.equals(key)) {
					continue; // 자기 자신
				}
				
				User vo = sessionMap.get(key);
				
				JSONArray arr = new JSONArray();
				arr.put(vo.getUid());
				arr.put(vo.getNickName());
				arrUsers.put(arr);
			}
			
			JSONObject jsonUsers = new JSONObject();
			jsonUsers.put("type", "userList");
			jsonUsers.put("users", arrUsers);
			
			sendTextMessageOne(jsonUsers.toString(), session);
			
			// 다른 클라이언트에게 접속 사실을 알림
			JSONObject ob = new JSONObject();
			ob.put("type", "userConnect");
			ob.put("uid", uid);
			ob.put("nickName", nickName);
			
			sendTextMessageAll(ob.toString(), uid);
			
		} else if(type.equals("message")) {
			// 채팅문자열을 보낸 경우
			User vo = getUser(session);
			
			String msg = jsonReceive.getString("chatMsg");
			
			JSONObject ob = new JSONObject();
			ob.put("type", "message");
			ob.put("chatMsg", msg);
			ob.put("uid", vo.getUid());
			ob.put("nickName", vo.getNickName());
			
			sendTextMessageAll(ob.toString(), vo.getUid());
			
		} else if(type.equals("whisper")) {
			// 귓속말을 보낸 경우
			User vo = getUser(session);
			
			String msg = jsonReceive.getString("chatMsg");
			String receiver = jsonReceive.getString("receiver");
			
			User receiverVo = sessionMap.get(receiver);
			if(receiverVo == null ) {
				return;
			}
			
			JSONObject ob = new JSONObject();
			ob.put("type", "whisper");
			ob.put("chatMsg", msg);
			ob.put("uid", vo.getUid());
			ob.put("nickName", vo.getNickName());
			
			sendTextMessageOne(ob.toString(), receiverVo.getSession());
			
		}
		
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		// WebSocket 연결이 닫혔을때(클라이언트가 나갔을 때)
		String uid = removeUser(session);
		
		logger.info("remove session : " + uid); // 나간 사람 알림
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// 에러가 발생한 경우
		removeUser(session);
	}
	
	/**
	 * 모든 사용자에게 메시지 전송 
	 * @param message 전송할 메시지
	 * @param exclude 제외할 유저
	 */
	protected void sendTextMessageAll(String message, String exclude) {
		Iterator<String> it = sessionMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key= it.next();
			
			if(exclude != null && exclude.equals(key)) {
				// 자기 자신
				continue;
			}
			
			User user = sessionMap.get(key);
			WebSocketSession session = user.getSession();
			
			try {
				if(session.isOpen()) {
					// 메시지 전송
					session.sendMessage(new TextMessage(message));
				}
			} catch (IOException e) {
				removeUser(session);
			}
			
		}
	}
	
	
	// 특정 유저에게 메시지 보내기 
	protected void sendTextMessageOne(String message, WebSocketSession session) {
		if(session.isOpen()) {
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				removeUser(session);
			}
		}
	}
	
	// session 에 대한 User 객체 반환
	protected User getUser(WebSocketSession session) {
		Iterator<String> it = sessionMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			
			User dto = sessionMap.get(key);
			if(dto.getSession() == session) {
				return dto;
			}
		}
		
		return null;
	}
	
	// 유저가 채팅방을 나간 경우 호출하는 메소드
	protected String removeUser(WebSocketSession session) {
		User user = getUser(session);
		if(user == null) {
			return null;
		}
		
		// 다른 클라이언트에세 접속 해제 사실을 알려줌
		JSONObject job = new JSONObject();
		job.put("type", "userDisconnect");
		job.put("uid", user.getUid());
		job.put("nickName", user.getNickName());
		
		// 모든 사람에게 메시지 보냄
		sendTextMessageAll(job.toString(), user.getUid());
		
		// WebSocketSession 닫음
		try {
			session.close();
		} catch (Exception e) {
		}
		
		// sessionMap 삭제
		sessionMap.remove(user.getUid());
		
		return user.getUid(); // 나간 사람 id 리턴
	}
}
