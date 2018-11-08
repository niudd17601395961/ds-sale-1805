<%--
  Created by IntelliJ IDEA.
  User: lang
  Date: 2018/11/5
  Time: 18:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>注册页面</title>
    <link rel="stylesheet" href="css/sign.css"/>
    <script src="/js/jquery-1.7.2.min.js"></script>

    <script>
        // 1、获得焦点，内容为空，tip默认提示
        // 2、失去焦点，内容为空，tip为隐藏
        // 3、其他情况（按键抬起，失去焦点且内容不为空，或最后表单总验证）
        //    按键抬起为空，报错，不能为空
        //    内容匹配，成功
        //    内容不匹配，失败
        // 4、密码要进行安全等级检测，含数字、字母、特殊字符为强，两种为中，一种为弱
        // 5、确认密码失去焦点的时候就要验证是否一致
        // user
        var user_Boolean = false;
        var password_Boolean = false;
        var varconfirm_Boolean = false;
        var emaile_Boolean = false;
        var Mobile_Boolean = false;

        $(function () {
            $('#userName').blur(function(){
                var userName = $("#userName").val();
                if ((/^[a-z0-9_-]{6,16}$/).test($("#userName").val())){
                    $.ajax({
                        url:'/selectUserByName.do',
                        type:'post',
                        data:{
                            "userName":userName,
                        },
                        success:function (data) {
                            if(data == "yes"){
                                $('#userNameTip').html("当前账号可以使用").css("color","green");
                                user_Boolean = true;
                            }else{
                                $('#userNameTip').html("账号已存在").css("color","red");
                            }
                        }
                    })
                }else {
                    $('#userNameTip').html("当前账号不符合规范").css("color","red");
                    user_Boolean = false;
                }
            });
            // password
            $('#pwd').blur(function(){
                if ((/^[a-z0-9_-]{6,16}$/).test($("#pwd").val())){
                    $('#pwdTip').html("此密码可用").css("color","green");
                    password_Boolean = true;
                }else {
                    $('#pwdTip').html("请提高密码安全度").css("color","red");
                    password_Boolean = false;
                }
            });


            // password_confirm
            $('#pwd2').blur(function(){
                if (($("#pwd").val())==($("#pwd2").val())){
                    $('#pwd2Tip').html("两次密码输入一致").css("color","green");
                    varconfirm_Boolean = true;
                }else {
                    $('#pwd2Tip').html("两次密码输不一致").css("color","red");
                    varconfirm_Boolean = false;
                }
            });


            // Email
            $('#email').blur(function(){
                if ((/^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/).test($("#email").val())){
                    $('#emailTip').html("√").css("color","green");
                    emaile_Boolean = true;
                }else {
                    $('#emailTip').html("×").css("color","red");
                    emaile_Boolean = false;
                }
            });


            // Mobile
            $('#mobile').blur(function(){
                if ((/^1[34578]\d{9}$/).test($("#mobile").val())){
                    $('#mobileTip').html("√").css("color","green");
                    Mobile_Boolean = true;
                }else {
                    $('#mobileTip').html("请输入正确手机号").css("color","red");
                    Mobile_Boolean = false;
                }
            });

            // click
            $('#btn').click(function(){
                if(user_Boolean && password_Boolean && varconfirm_Boolean && emaile_Boolean && Mobile_Boolean == true){
                    if ($("#ck").prop("checked")){
                        $("#red_form").submit();
                    }else{
                        $('#ckTip').html("请认真阅读用户注册协议").css("color","red");
                    }
                }else {
                    alert("请完善信息");
                }
            });
        })

    </script>
</head>
<body>
<from action="saveUser.do" id="red_form">
<!--头部-->
<div class="header">
    <a class="logo" href="##"></a>
    <div class="desc">欢迎注册</div>
</div>
<!--版心-->
<div class="container">
    <!--京东注册模块-->
    <div class="register">
        <!--用户名-->
        <div class="register-box">
            <!--表单项-->
            <div class="box default">
                <label for="userName">用&nbsp;户&nbsp;名</label>
                <input type="text" id="userName" placeholder="您的账户名和登录名" name="yhMch"/>
                <i></i>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="userNameTip"></span>
            </div>
        </div>
        <!--设置密码-->
        <div class="register-box">
            <!--表单项-->
            <div class="box default">
                <label for="pwd">设 置 密 码</label>
                <input type="password" id="pwd" placeholder="建议至少两种字符组合" name="yhmm" />
                <i></i>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="pwdTip"></span>
            </div>
        </div>
        <!--确认密码-->
        <div class="register-box">
            <!--表单项-->
            <div class="box default">
                <label for="pwd2">确 认 密 码</label>
                <input type="password" id="pwd2" placeholder="请再次输入密码" />
                <i></i>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="pwd2Tip"></span>
            </div>
        </div>
        <!--设置密码-->
        <div class="register-box">
            <!--表单项-->
            <div class="box default">
                <label for="email">邮 箱 验 证</label>
                <input type="text" id="email" placeholder="请输入邮箱" name="yhYx"/>
                <i></i>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="emailTip"></span>
            </div>
        </div>
        <!--手机验证-->
        <div class="register-box">
            <!--表单项-->
            <div class="box default">
                <label for="mobile">手 机 验 证</label>
                <input type="text" id="mobile" placeholder="请输入手机号" name="yhShjh" />
                <i></i>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="mobileTip"></span>
            </div>
        </div>
        <!--注册协议-->
        <div class="register-box xieyi">
            <!--表单项-->
            <div class="box default">
                <input type="checkbox" id="ck" />
                <span>我已阅读并同意<a href="##">《京东用户注册协议》</a></span>
            </div>
            <!--提示信息-->
            <div class="tip">
                <i></i>
                <span id="ckTip"></span>
            </div>
        </div>
        <!--注册-->
        <button id="btn">注册</button>
    </div>
</div>
</from>
</body>
</html>
