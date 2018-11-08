<%--
  Created by IntelliJ IDEA.
  User: lang
  Date: 2018/11/5
  Time: 15:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.2.min.js"></script>
<script>
    function findMiniCart(){
       $.post("findMiniCart.do",function (data) {
           $("#miniCartInnerDiv").html(data);
       })
        $("#miniCartInnerDiv").show();
    }
    function outMiniCart(){
        $("#miniCartInnerDiv").hide();
    }
</script>
<body>
    <div class="search">
        <div class="logo"><img src="./images/logo.jpg" alt=""></div>
        <div class="search_on">
            <div class="se">
                <input type="text" name="search" class="lf">
                <input type="submit" class="clik" value="搜索">
            </div>
            <div class="se">
                <a href="">取暖神奇</a>
                <a href="">1元秒杀</a>
                <a href="">吹风机</a>
                <a href="">玉兰油</a>
            </div>
        </div>
        <div class="card" onmouseover="findMiniCart()" onmouseout="outMiniCart()">
            <a href="">购物车<div class="num">0</div></a>
            <!--购物车商品-->
            <div class="cart_pro">
               <!-- <h6>最新加入的商品</h6>-->
               <div id="miniCartInnerDiv" style="display:none"></div>
               <%--<div class="gobottom">
                    共<span>${countNum}</span>件商品&nbsp;&nbsp;&nbsp;&nbsp;
                    共计￥<span>${hjSum}</span>
                    <button class="goprice">去购物车</button>
                </div>--%>
            </div>
        </div>
    </div>
</body>
</html>
