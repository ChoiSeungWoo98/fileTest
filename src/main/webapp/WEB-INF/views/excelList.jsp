<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
</head>
<body>
<table class="table table-striped">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">이름</th>
        <th scope="col">이메일</th>
    </tr>
    </thead>
    <c:forEach items="${datas}" var="datas" varStatus="data">
    <tbody>
            <td>${datas.excelfilePhoneNum}</td>
            <td>${datas.excelfileName}</td>
            <td>${datas.excelfileEmail}</td>
    </tbody>
    </c:forEach>
</table>
</body>
</html>