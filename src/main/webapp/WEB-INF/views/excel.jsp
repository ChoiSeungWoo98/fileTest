<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
      <!-- xmlns:th="http://www.thymeleaf.org" -->
<head>
    <meta charset="UTF-8">
    <title>엑셀 업로드</title>
</head>
<body>
    <form action="@{/excel/read}" method="POST" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="submit" value="제출" />
    </form>
</body>
</html>