package com.monitor.argus.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

/**
 * IP工具类
 * 
 * @author null
 * 
 */
public class IpUtil {

	/**
	 * 获取登录用户的IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "本地";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return getIpInfo(ip);
	}

	/**
	 * 通过IP获取地址
	 * 
	 * @param ip
	 * @return
	 */
	public static String getIpInfo(String ip) {
		String info = "";
		try {
			URL url = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);
			HttpURLConnection htpcon = (HttpURLConnection) url.openConnection();
			htpcon.setRequestMethod("GET");
			htpcon.setDoOutput(true);
			htpcon.setDoInput(true);
			htpcon.setUseCaches(false);

			InputStream in = htpcon.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			JSONObject obj = (JSONObject) JSON.parse(temp.toString());
			if (obj.getIntValue("code") == 0) {
				JSONObject data = obj.getJSONObject("data");
				info += data.getString("country") + " ";
				info += data.getString("region") + " ";
				info += data.getString("city") + " ";
				info += data.getString("isp");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static String localIp() throws UnknownHostException {
		String ip = localIp0();
		if (Strings.isNullOrEmpty(ip)) {
			ip = InetAddress.getLocalHost().getHostAddress();
		}
		return ip;
	}

	private static String localIp0() {
		if (System.getenv("local_server_ip") != null) {
			return System.getenv("local_server_ip");
		}
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e1) {
			try {
				Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
				while (enumeration.hasMoreElements()) {
					NetworkInterface face = enumeration.nextElement();
					Enumeration<InetAddress> i = face.getInetAddresses();
					while (i.hasMoreElements()) {
						InetAddress addr = i.nextElement();
						String ret = addr.getHostAddress();
						if (addr.isSiteLocalAddress()
								&& !addr.isLoopbackAddress()
								&& ret.indexOf(":") == -1
								&& ret.startsWith("10")) {//只获取10网段的
							return addr.getHostAddress();
						}
					}
				}
				throw new UnknownHostException();
			} catch (Exception e) {
				e.printStackTrace();
				throw Throwables.propagate(e);
			}
		}
	}

	public static long ipToLong(String ipAddress) {
		String[] ipAddressInArray = ipAddress.split("\\.");
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);
		}
		return result;
	}

}
