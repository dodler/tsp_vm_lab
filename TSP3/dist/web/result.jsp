<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="refresh" content="5;URL=http://localhost:8080/index.jsp" />
    <title>Результат вычислений</title>
</head>
<body>
    <p>Результат:  <%= request.getParameter("result") %></p>
</body>
</html>