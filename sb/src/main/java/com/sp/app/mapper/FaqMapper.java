package com.sp.app.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.sp.app.domain.Faq;

@Mapper
public interface FaqMapper {
	public int dataCount(Map<String, Object> map);
	public List<Faq> listFaq(Map<String, Object> map);
	public List<Faq> listCategory(Map<String, Object> map);
}
