package com.test;

import java.io.*;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.text.SimpleDateFormat;

public class SendFileMultiPart {
    
	public static void main(String[] args) throws IOException {
		long initTime = startTime();
		
	    String path = "C:\\Users\\User\\Desktop\\send\\";
	    File dir = new File(path);	    
		File[] fileList = dir.listFiles();	// path에 있는 파일 리스트 불러옴		
		String[] fileNameList = new String[dir.listFiles().length];

		for (int i = 0; i < fileList.length; i++) {
			System.out.println("fileList[" + i + "] : " + fileList[i].getName());
			fileNameList[i] = fileList[i].getName();
		}
		
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost uploadFile = new HttpPost("http://localhost:8080/FileSendChunked/MultipartToJSON");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			Charset charset = Charset.forName("UTF-8");
			builder.setCharset(charset);
			builder.addTextBody("formData1", "테스트1", ContentType.TEXT_PLAIN.withCharset("UTF-8"));
			builder.addTextBody("formData2", "테스트2", ContentType.TEXT_PLAIN.withCharset("UTF-8"));
			
			// This attaches the file to the POST:
			for(File file : fileList) {
				builder.addBinaryBody("realfile", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, file.getName());
			}			
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			// HttpEntity responseEntity = response.getEntity();
			System.out.println("response : " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		endTime(initTime);
		
	}
    
	public static long startTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		String format_time1 = format.format(System.currentTimeMillis());
		long time = System.currentTimeMillis();
		System.out.println("시작 : " + format_time1);
		return time;
	}
	
	public static void endTime(long startTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format_time2 = format.format(System.currentTimeMillis());
		System.out.println("종료 : " + format_time2);
		long endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		System.out.println("전송 소요시간 : " + elapsed + "ms");
	}
}
