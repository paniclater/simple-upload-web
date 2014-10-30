<%@page import="com.paniclater.UploadedFile"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="java.util.Map"%>
<%
	Map<Integer, UploadedFile> uploadedFileDatabase = (Map<Integer, UploadedFile>)request.getAttribute("uploadedFileDatabase");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Simple Upload Web</title>
</head>
<body>
<h1>Simple Upload Web</h1>
<p><a href="upload?action=upload">Upload a file</a></p>
<h2>Uploaded Files:</h2>
<%if (uploadedFileDatabase == null || uploadedFileDatabase.size() <= 0)  {%>
	<p>There have been no files uploaded</p>
<%
	} else { 
%>		
		<ul>
<%
		for(UploadedFile file : uploadedFileDatabase.values()) {
%>
			<li><%= file.getName() %> <a href="upload?action=download&id=<%=file.getId()%>">download</a></li>	
<%		
		}
%>
	</ul>
<% } %>
</body>
</html>