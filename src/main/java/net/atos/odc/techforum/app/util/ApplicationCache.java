package net.atos.odc.techforum.app.util;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ApplicationCache {

	private Map<String,Object> cacheDataMap = new HashMap<String,Object>();
	
	public void set(String key, Object value) {
		//cacheDataMap.put(key, value);
	}
	
	public Object get(String key) {
		//return cacheDataMap.get(key);
		return null;
	}
}
