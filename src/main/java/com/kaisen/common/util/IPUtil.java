package com.kaisen.common.util;

import java.net.InetAddress;

public class IPUtil {

	public static String getHostIp() {
		String localhost = "";
		try {
			localhost = InetAddress.getByName(getHostName()).getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localhost;
	}

	public static String getHostName() {
		String hostName = "";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hostName;
	}
}
