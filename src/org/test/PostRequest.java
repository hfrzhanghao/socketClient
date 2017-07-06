package org.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostRequest {
	public static String go(String urls, String postData) {
		try {
			byte[] bt = postData.getBytes("UTF-8");
			URL url = new URL(urls);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			OutputStream out = (OutputStream) connection.getOutputStream();
			out.write(bt);
			out.close();
			InputStream content = (InputStream) connection.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(content, "UTF-8"));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			return sb.toString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String url = "http://103.25.23.85:809/reportFlashMsg";
		StringBuffer str = new StringBuffer();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		str.append("<flash>");
		str.append("<version>1</version>");
		str.append("<type>2</type>");
		str.append("<flashMsg>");
		//str.append("[play][sub_type=c_s uid=5094264a-4321-4692-b12c-a4c234f58e96 operationGUID=fdb3b3b2-6b71-4fff-9ad3-938910aa93f7 url=http://gslb.butel.com/416e80d9-3298-438a-8c82/3732360b6481.mp4");
		//str.append(" cip=221.226.190.134 sip=undefined lockCountInner=1 start_time=20160913112515.190 lockDuration=95 duration=0 seekCount=0 lockCount=1 avg_bitrate=0 max_bitrate=0 min_bitrate=0 audioByteCount= audioBytesPerSecond=0 audioRate=0 audioLossRate=0 byteCount= currentBytesPerSecond=0 dataBufferLength=0 droppedFrames=0 maxBytesPerSecond=0 playbackBytesPerSecond=0 videoBufferByteLength=0 videoRate=0 videoByteCount= videoBytesPerSecond=0 logIndex=0 time=2016-12-26-18:24:01:821]");
		//str.append("[LoadPlayer][loadDuration=616 playerSDKVersion=7.2.17.1 operationGUID=149456028407088625 userName=undefined time=2017-05-12-11:38:04:071 logIndex=0 OSVersion=windows_nt_10.0 OSType=windows browserVersion=Chrome_57.0.2987.98 flashVersion=WIN_25,0,0,171 startTime=20170512113804 networkType=unknown playerSDKType=Flash openType=browser uid=149456017767664910 businessID=undefined subType=c_s deviceTypeName=unknown]");
		str.append("[play][offset=0S logIndex=1 time=2016-12-29-14:45:27:762 startTime=0 subType=start operationGUID=E36B676E-8568-45B9-858A-E29439F5CEEF playTime=0 firstDataDuration=86 uid=D5A497D1-2A01-4724-853C-973D4D985C2B firstPicDuration=1424 url=http://112.114.112.3/vodtv.butel.com/463_20160704113144.m3u8?from=liebao&os=windows ctype=3]");
		//str.append(" clientip=221.226.190.134 sip=undefined result=200 firstPicDuration=1200 logIndex=0 time=2016-12-28-08:24:01:821]");
		//str.append("[LoadPlayer][time=2017-05-12-12:22:21:692 subType=c_s flashVersion=WIN_23,0,0,162 playerSDKType=Flash playerSDKVersion=7.2.10.1 uid=147908986162329797 deviceTypeName=unknown OSType=windows operationGUID=148706816169196120 startTime=20170214182921 OSVersion=windows_nt_6.1 logIndex=0 businessID=undefined browserVersion=Chrome_53.0.2785.116 userName=undefined networkType=unknown loadDuration=1722 openType=browser]");
		//str.append("[LoadPlayer][subType=c_s uid=D5A497D1-2A01-4724-853C-973D4D985C2B operationGUID=E36B676E-8568-45B9-858A-E29439F5CEEF businessID=undefined userName=undefined openType=app startTime=20170210154423 loadDuration=9000.000000 OSType=IOS OSVersion=IOS10.2.1 playerSDKType=iOSNative playerSDKVersion=2.1.42.20170207_beta logIndex=0 deviceTypeName=iPhone6 networkType=Wifi time=2017-02-10-15:44:32:399]");
		//str.append("[playlock][droppedFrames=4598 lockInfo= time=2017-02-16-16:26:20:543 url=http://vodtv.butel.com/463_20160704113144.m3u8?from=liebao&os=windows videoBufferByteLength=3704391 avgBitrate=87673.44207727043 maxBitrate=113122.27761742324 duration=409.788 dataBufferLength=69.386 subType=c_s minBitrate=23962.10744874124 audioRate=3113.621298361766 videoLossRate=0 videoRate=11184.606196463195 ctype=3 uid=D5A497D1-2A01-4724-853C-973D4D985C2B currentBytesPerSecond=0 byteCount=38386672 operationGUID=E36B676E-8568-45B9-858A-E29439F5CEEF maxBytesPerSecond=0 logIndex=11 audioBytesPerSecond=0 audioByteCount=0 videoBytesPerSecond=0 lockCount=3 videoByteCount=0 lockCountInner=0 seekCount=0 playbackBytesPerSecond=59131.33940182054 audioLossRate=0]");
		//str.append("[play][subType=start uid=B2978C70-9F91-4149-8599-6E913FECA310 operationGUID=E5D686DB-35C2-4648-9BEE-F1E75C89364B ctype=3 url=http://vodtv.butel.com/a85b5af637df4465a2a41786a8dc1ca1.mp4 sip=undefined startTime=0 playTime=0 fisrtDataDuration=2269 firstPicDuration=2290 businessID=undefined result=0 logIndex=0 time=2017-02-20-14:40:49:504]");
		str.append("</flashMsg>");
		str.append("</flash>");

		String data = str.toString();
		String response = go(url, data);
		if(response == null){
			
		}else if(!response.contains("success")){
			
		}else{
			
		}
		System.out.println(go(url, data));
	}

}
