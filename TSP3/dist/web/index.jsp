<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*,java.util.*, lian.Task, javax.xml.bind.JAXBContext,javax.xml.bind.JAXBException,javax.xml.bind.Marshaller"%>
<html>

<%
    Task task = new Task();
    String num1 = request.getParameter("num1"),
    num2 = request.getParameter("num2");
    task.num1=num1;
    task.num2=num2;
    task.res=null;
    if (num1 != null && !"".equals(num1) && num2 != null && !"".equals(num2)){
        session.setAttribute("task", task);
        String site = new String("http://localhost:8080/mainServlet");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", site);
    }

%>

    <head>
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body>
        <p> Форма ввода данных </p>
        <div id="data">
        <form action="index.jsp" method=post id="inp">
            <input type=hidden>
                <p>Вычитаемое</p><input type=text name="num1" form="inp" pattern="^-{0,1}\d,{0,1}\d{0,10}$"/>
                <p>Вычитатель</p><input type=text name="num2" form="inp" pattern="^-{0,1}\d,{0,1}\d{0,10}$"/>
                <input type="submit"> <input type=reset>
            </input>
        </form>
        </div>

        <%
            String res = "";
            task = (Task)session.getAttribute("task");

                StringWriter sw = null;
                    JAXBContext context = null;
                    if (task != null){
                    try
                    {
                        sw = new StringWriter();
                        context = JAXBContext.newInstance(task.getClass());
                        Marshaller marshaller = context.createMarshaller();
                        marshaller.marshal(task, sw);
                    } catch (JAXBException e)
                    {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    }

            if (task != null){
                res = task.res;
            }

        %>
        <p><%=res%></p>
        <%
        if (res != null && !"".equals(res))
            session.setAttribute("task", null);
        %>

<%String out2="";
if (sw!=null)out2=sw.toString();
%>
        <!--
        <%=out2%>
        -->
    </body>
</html>