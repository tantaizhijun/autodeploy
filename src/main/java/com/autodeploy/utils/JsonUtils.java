package com.autodeploy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Json 工具类
 * @author yinjihuan
 *
 */
public class JsonUtils {

	private static ObjectMapper mapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
	


	/**
	 * json 转换为 map
	 */
	public static HashMap<String, Object> JsonToMap(String json) {
		HashMap<String, Object> map1 = new HashMap<>();
		if(StringUtils.isEmpty(json)) {
			return map1;
		}
		try {
			map1 = mapper.readValue(json, HashMap.class);
			return map1;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("json to map error : " + e.getMessage());
		}
		return map1;
	}


	public static String toString(Object obj){
		return toJson(obj);
	}
	/**
	 * 将java对象转换为json字符串
	 */
	public static String toJson(Object obj){
		try{
			String string = mapper.writeValueAsString(obj);
			return string;
		}catch(Exception e){
			throw new RuntimeException("序列化对象【"+obj+"】时出错", e);
		}
	}
}
