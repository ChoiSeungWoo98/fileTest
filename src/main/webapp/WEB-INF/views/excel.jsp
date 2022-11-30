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

            <li><input multiple="multiple" type="file" name="file" id="file_upload"></li>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
<%--            <li><input multiple="multiple" type="file" name="file"></li>--%>
        </ul>
    <input type="submit" value="제출" />
    </form>
</body>
<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
<script>
    $('#file_upload').change(function (){
        const files = $('#file_upload')[0];
        // 파일을 여러개 선택할 수 있으므로 files 라는 객체에 담긴다.
        console.log("input file: ", files.files)

        const formData = new FormData();
        formData.append("file", files.files[0]);


        $.ajax({
            type:"POST",
            url: "/excel/read",
            data: formData,
            processData: false,
            contentType: false,
            cashe: false,
            success: function(rtn){
                console.log(rtn);
            },
            error: function(err){
                console.log("err:", err)
            }
        });

    });
</script>
</html>