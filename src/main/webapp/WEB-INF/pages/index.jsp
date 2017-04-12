<%@ taglib prefix="spring" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
  Created by IntelliJ IDEA.
  User: Anton Rynkovoy
  Date: 04/12/2017
  Time: 21:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <spring:url value="/resources/css/styles.css" var="indexCss" />
    <link href="${indexCss}" rel="stylesheet">
    <link href="/resources/css/styles.css" rel="stylesheet">
    <title>Yaber Taxi :)</title>
    <%--<link href="<c:url value="/resources/css/styles.css" />" rel="stylesheet"/>--%>
</head>
<body>
    <marquee><h1>Hello from Yanber Taxi :)</h1></marquee>
</body>
</html>
