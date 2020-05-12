<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#loginAct").focus();
            //为登录按钮绑定事件
            $("#btn").click(function () {

                login();
            });
            //为当前的窗口window绑定事件,绑定敲键盘时间，通过判断敲的键盘的键位，来识别回车键
            //根据键位的码值来识别13是回车键
            $(window).keydown(function (event) {


                if (event.keyCode == 13) {

                    //alert("回车操作");
                    login()
                }
            })

        });

        //验证登录方法
        function login() {


            //验证登录账号和密码不能为空
            var loginAct = $.trim($("#loginAct").val());
            var loginPwd = $.trim($("#loginPwd").val());

            if (loginAct == "" || loginPwd == "") {
                $("#msg").html("密码不能为空");
                return false;
            }
            var flag = "";

            if ($("#flag").prop("checked")) {

                flag = "a";
            }

            $.ajax({
                url: "settings/user/login.do",
                data: {
                    "loginAct": loginAct,
                    "loginPwd": loginPwd,
                    "flag": flag
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        //登录成功
                        window.location.href = "workbench/toWorkbenchIndex.do";
                    } else {
                        //登录失败
                        $("#msg").html(data.msg);
                    }
                }
            })
        }


    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 50%; position: relative; top:50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">物电学院&nbsp吴思呈</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control" type="text" placeholder="用户名" id="loginAct">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control" type="password" placeholder="密码" id="loginPwd">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">
                    <label>
                        <input type="checkbox" id="flag"> 十天内免登录
                    </label>
                    &nbsp;&nbsp;
                    <span id="msg" style="color: red"></span>
                </div>
                <button type="button" class="btn btn-primary btn-lg btn-block" id="btn"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>