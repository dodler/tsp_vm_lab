<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <p> Форма ввода данных test </p>
        <div id="data">
        <form action="mainServlet" method=post id="inp">
            <input type=hidden>
                <p>Вычитаемое</p><input type=text name="num1" form="inp"/>
                <p>Вычитатель</p><input type=text name="num2" form="inp"/>
                <input type="submit"> <input type=reset>
            </input>
        </form>
        </div>
    </body>
</html>