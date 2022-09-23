<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Multipart 파일업로드 샘플</title>
</head>
<body>
	<form action="Multipart" enctype="multipart/form-data" method="post">
		<input type="file" name="realFile" id="realFile" size="100%" multiple/> <br>
		폼데이터1 : <input type="text" name="formData1" id="formData1" /> <br>
		폼데이터2 : <input type="text" name="formData2" id="formData2" /> <br>
		<input type="submit" value="전송" />
	</form>
</body>
</html>