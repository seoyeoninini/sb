package com.sp.app.chatting;

import org.springframework.web.socket.handler.TextWebSocketHandler;

/*
 - type : client -> server
   connect : 처음 접속한 경우 - uid, nickName
   message : 채팅 메시지 전송 - chatMsg
   whisper : 귓속말 - receiver, chatMsg
   
 - type : server -> client
   userList : 처음 접속한 경우 점속한 사용자 리스트 전송 - users
   userConnect : 다른 접속 사용자에게 지금 접속한 유저 전송 - uid, nickName
   message : 채팅 메시지 - uid, nickName, chatMag
   whisper : 귓속말 - uid, nickMsg, chatMsg
   userDisconnect : 접속한 사용자에게 접속 해제한 유저 전송 - uid, nickName
 */

public class MySocketHandler extends TextWebSocketHandler {

}
