<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
      <!-- xmlns:th="http://www.thymeleaf.org" -->
<head>
    <meta charset="UTF-8">
    <title>엑셀 업로드</title>
</head>
<body>
    <form name="fileForm" action="/excel/read" method="POST" enctype="multipart/form-data">
        <ul>

            <li><input multiple="multiple" type="file" name="file"></li>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
        </ul>
    <input type="submit" value="제출" />
    </form>
</body>
</html>