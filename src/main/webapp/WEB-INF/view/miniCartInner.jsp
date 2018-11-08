<%--
  Created by IntelliJ IDEA.
  User: niudd
  Date: 2018/11/7
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:forEach var="cart" items="${cartList}">
    <div class="one">
        <img src="images/lec1.png"/>
        <span class="one_name">
                ${cart.skuMch}
        </span>
        <span class="one_prece">
            <b>￥${cart.skuJg}*${cart.tjshl}</b><br />
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;删除
        </span>
    </div>
</c:forEach>
<div class="gobottom">
    共<span>${countNum}</span>件商品&nbsp;&nbsp;&nbsp;&nbsp;
    共计￥<span>${hjSum}</span>
    <button class="goprice">去购物车</button>
</div>
</body>
</html>
