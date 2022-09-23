package com.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// import org.apache.commons.io.output.DeferredFileOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/FileSend")
public class UploadActionMultiPart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BYTE = 1024;
	private static final int KILOBYTE = 1024;
	private static final int MEMORY_THRESHOLD = BYTE * KILOBYTE * 1; // 1MB memory buffer
	private static final long MAX_FILE_SIZE = BYTE * KILOBYTE * 2047; // 2GB 가장 큰 파일 용량
	private static final long MAX_REQUEST_SIZE = BYTE * KILOBYTE * 4095; // client에서 server로 전송 되는 request의 총 용량 4GB
	// private static final String TEMP_STROAGE = "C:\\Users\\User\\Desktop\\recv\\";
	private static final String FILE_STROAGE = "C:\\Users\\jaewan\\Desktop\\multipart_test\\rcv";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Multipart Recevie Process...");
		long startTime = System.currentTimeMillis();
		System.out.println("content-type : " + request.getContentType());
		
		if( ServletFileUpload.isMultipartContent(request) ) {
			// System.out.println("isMultiPartContent : true");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(MEMORY_THRESHOLD);
			
			// server에 파일이 업로드 되는 동안 임시로 저장 될 경로
			factory.setRepository(new File(FILE_STROAGE));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE); // 가장 큰 파일 사이즈 설정
			upload.setSizeMax(MAX_REQUEST_SIZE); // 총 request의 사이즈 설정
			upload.setHeaderEncoding("utf-8");
			// 서버에 파일 저장 경로
			// String uploadPath = getServletContext().getRealPath("") + File.separator + FILE_STROAGE;
			String uploadPath = FILE_STROAGE;
			System.out.println("uploadPath => " + uploadPath);
			
			// 서버에 저장소 만들기
			File uploadStorage = new File(uploadPath);
			if(!uploadStorage.isDirectory()) {
				uploadStorage.mkdir(); 
			}
			try {
				 List<FileItem> formItems = upload.parseRequest(request);
				 System.out.println("!! formItems => " + formItems.toString());
				 if (formItems != null && formItems.size() > 0) {
					 
					 // form data들을 가져 온다.					 
					 for (FileItem item : formItems) {
						 //form의 file type만 가져온다.
						 if (!item.isFormField()) {							 
							 System.out.println("multipart form data name => " + item.getName());
							 String fileName = new File(item.getName()).getName();
							 String filePath = uploadPath + File.separator + fileName;
							 File storeFile = new File(filePath);
							 
							 // file 저장
							 item.write(storeFile);							 							 
						 } else {
							 // System.out.println("normal form data name => " + item.getString());
							 System.out.println("item Content Type => " + item.getContentType());
							 String name = item.getString();
							 if(item.getContentType() == null) {
								 name = new String(name.getBytes("8859_1"),"utf-8");	// ContentType에 charset 지정 안되어 있을 경우, utf-8 설정	 
							 }
							 System.out.println("normal form data name => " + item.getFieldName() + ", data => " + name);
						 }
					 }
					 request.setAttribute("result", "업로드완료");
				 }
			} catch (Exception ex) {
				request.setAttribute("result", "업로드에러 : " + ex.getMessage());
			}
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - startTime;
			System.out.println("전송 소요시간 : " + (double)elapsed/1000 + " s");
			
			// CORS Error 처리
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
			
			request.setAttribute("elapsedTime", (double)elapsed/1000 + " s");
			getServletContext().getRequestDispatcher("/fileResult.jsp").forward(request, response);
		}
	}
	
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setAccessControlHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK  );
	}
   
	private void setAccessControlHeaders(HttpServletResponse resp) {
		// CORS Error 처리
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        
   }
}
