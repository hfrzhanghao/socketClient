package org.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginClient {
	public static void main(String[] args) {
		try {
			// 1.建立客户端socket连接，指定服务器位置及端口
			Socket socket = new Socket("localhost", 60530);
			// 2.得到socket读写流
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			// 输入流
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			// 3.利用流按照一定的操作，对socket进行读写操作
			String info = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><flash><version>1</version><type>2</type><flashMsg>[LoadPlayer][subType=c_s uid=a9c24ad8-0d3e-4952-9f1a-63629857e0e5 operationGUID=7837b4ec-8e8a-4f86-a586-152007f6e689 openType=app startTime=20161118095444.412 loadDuration=244 OSType=Android OSVersion=Android_4.2.1 playerSDKType=AndroidNative playerSDKVersion=1.5.2.99 browserVersion=undefined flashVersion=undefined logIndex=0 deviceTypeName=google Galaxy Nexus networkType=Wifi time=2016-11-18-09:54:44:770 clientip=221.226.190.134]|#live#|</flashMsg></flash>";
			pw.write(info);
			pw.flush();
			socket.shutdownOutput();
			// 接收服务器的相应
			String reply = null;
			while (!((reply = br.readLine()) == null)) {
				System.out.println("接收服务器的信息：" + reply);
			}
			// 4.关闭资源
			br.close();
			is.close();
			pw.close();
			os.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
