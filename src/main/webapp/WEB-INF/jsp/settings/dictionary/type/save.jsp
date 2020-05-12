<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">

	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script>
        $(function () {

            //为确保按钮绑定事件，执行字典类型的添加操作
            $("#saveTypeBtn").click(function () {
                var code = $.trim($("#code").val());

                if(code==""){
                    $("#msg").html("编码不能为空");
                    return false;
                }

                //发出ajax请求，验证字符编码是否重复
                $.ajax({
                    url:"settings/dictionary/type/checkCode.do",
                    data:{
                        "code":code
                    },
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        if(data.success){
                            //没有重复，可执行添加操作
                           // alert("执行添加操作")
                            //提交form表单，执行字典类型的添加操作
                            $("#saveTypeForm").submit();
                        }else {
                            //没有重复，做错误提示
                            $("#msg").html("编码已存在");
                        }
                    }
                })
            })
        })
    </script>
</head>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>新增字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveTypeBtn">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>

    <!--
      以传统形式提交表单，表单中的数据必须以name属性的形式才能够传递到后台
    -->
	<form action="settings/dictionary/type/saveType.do" method="post" class="form-horizontal" role="form" id="saveTypeForm">

        <div class="form-group">
            <label for="create-code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
            <div class="col-sm-10" style="width: 300px;">
                <input type="text" class="form-control" id="code" name="code" style="width: 200%;">
                <span id="msg"></span>
            </div>
        </div>

        <div class="form-group">
            <label for="create-name" class="col-sm-2 control-label">名称</label>
            <div class="col-sm-10" style="width: 300px;">
                <input type="text" class="form-control" id="name" name="name" style="width: 200%;">
            </div>
        </div>

        <div class="form-group">
            <label for="create-describe" class="col-sm-2 control-label">描述</label>
            <div class="col-sm-10" style="width: 300px;">
                <textarea class="form-control" rows="3" id="describe" name="description" style="width: 200%;"></textarea>
            </div>
        </div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>