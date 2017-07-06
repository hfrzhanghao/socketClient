package org.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
/*import java.util.List;
 import java.util.Map;*/
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSONObject;

public class HttpRequest {
	public static final String ADD_URL = "http://114.112.74.78:8089/messagenotify";
	public static Logger logger = Logger.getLogger(HttpRequest.class);

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			/*
			 * for (String key : map.keySet()) { System.out.println(key + "--->"
			 * + map.get(key)); }
			 */
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送 GET 请求出现异常！" + e);
			String sr = HttpRequest.sendPost(ADD_URL, "{\"type\": 4,\"source\": \"103.25.23.85\",\"alarmcode\": -4001, \"content\": \"Stat Service connect exception\"}");
			logger.info(sr);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送 POST 请求出现异常！" + e);
			String sr = HttpRequest.sendPost(ADD_URL, "{\"type\": 4,\"source\": \"103.25.23.85\",\"alarmcode\": -4002, \"content\": \"receive data connect exception\"}");
			logger.info(sr);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 发送请求
		BasicConfigurator.configure();
		//用于本机
		//PropertyConfigurator.configure("C:/Users/Dell/log4j.properties");
		//用于服务器
		//PropertyConfigurator.configure("D:/msgWarnning/log4j.properties");
		final long timeInterval = 60000;
		
		// 定时发送请求，检测统计模块是否正常
		Runnable runnableStat = new Runnable() {
			public void run() {
				while (true) {
					// 组合get请求字符串
					String param = null;
					try {
						long end = System.currentTimeMillis();
						long start = end - 60000;
						param = URLEncoder.encode("{\"startTime\":" + start + ",\"endTime\":" + end + "}", "utf-8");
					} catch (UnsupportedEncodingException e) {
						logger.error("encode url时发生异常：" + e);
						e.printStackTrace();
					}
					// 发送get请求验证返回数据是否正常
					String s = HttpRequest.sendGet("http://103.25.23.85:8080/liveAnalysis/external/externalService", "service=userOnline&params=" + param);
					// 返回s为空或s中的result不为0则异常,发送短信
					if (s.equals(null)) {
						String sr = HttpRequest.sendPost(ADD_URL,
								"{\"type\": 4,\"source\": \"103.25.23.85\",\"alarmcode\": -4001, \"content\": \"exception:lost parameter!\"}");
						logger.info("统计服务异常！");
						logger.info(sr);
						System.out.println(sr);
						break;
					}
					logger.info("统计服务正常！");
					// 休眠1分钟
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		};
		
		// 定时发送数据，检测日志接收模块是否正常
		Runnable runnableReceive = new Runnable() {
			public void run() {
				while (true) {
					// 日志发送地址
					String url = "http://103.25.23.85:809/reportFlashMsg";
					
					//组装日志xml文件
					StringBuffer str = new StringBuffer();
					str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
					str.append("<flash>");
					str.append("<version>1</version>");
					str.append("<type>2</type>");
					str.append("<flashMsg>");
					str.append("[play][offset=0S logIndex=1 time=2016-12-29-14:45:27:762 startTime=0 subType=start operationGUID=E36B676E-8568-45B9-858A-E29439F5CEEF playTime=0 firstDataDuration=86 uid=D5A497D1-2A01-4724-853C-973D4D985C2B firstPicDuration=1424 url=http://112.114.112.3/vodtv.butel.com/463_20160704113144.m3u8?from=liebao&os=windows ctype=3]");
					str.append("</flashMsg>");
					str.append("</flash>");
					String data = str.toString();
					
					//发送文件
					String response = sendPost(url, data);
					
					//如果有返回且包含success字符串，则正常
					if(!response.equals(null) && response.contains("success")){
						logger.info("接收服务正常！");
					}else{ // 否则发送短信告警
						String sr = HttpRequest.sendPost(ADD_URL,
								"{\"type\": 4,\"source\": \"103.25.23.85\",\"alarmcode\": -4002, \"content\": \"exception:receive data connect exception\"}");
						logger.info("日志接收模块异常！");
						logger.info(sr);
						break;
					}
					// 休眠1分钟
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
				}
			}
		};
		Thread threadStat = new Thread(runnableStat);
		Thread threadReceive = new Thread(runnableReceive);
		threadStat.start();
		threadReceive.start();
	}
}
