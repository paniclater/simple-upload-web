<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload a File</title>
</head>
<body>
<h2>Upload a File</h2>
<form method="POST" action="upload" enctype="multipart/form-data">
		<input type="hidden" name="action" value="upload"/>
		<p>File Name</p>
		<input type="text" name="fileName"/>
		<p>File</p>
		<input type="file" name="file"/>
		<input type="submit" value="Submit"/>
</form>

</body>
</html>