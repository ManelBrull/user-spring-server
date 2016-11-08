package com.mbrull.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesMyUtils {

	private static String hostAndPort;
	
	@Value("${hostAndPort}")
	public void setHostAndPort(String hostAndPort) {
		PropertiesMyUtils.hostAndPort = hostAndPort;
	}
	
	public static String hostUri() {
		return (isDev() ? "http://" : "https://") + hostAndPort;
	}
	
	private static String activeProfiles;
	
	@Value("${spring.profiles.active}")
	public void setActiveProfiles(String activeProfiles) {
		PropertiesMyUtils.activeProfiles = activeProfiles;
	}
	
	public static boolean isDev(){
		return activeProfiles.contains("dev");
	}
	
	public static boolean isProd() {
		return activeProfiles.contains("prod");
	}
	
}
