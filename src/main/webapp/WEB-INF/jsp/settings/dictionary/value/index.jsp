
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%><!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">

	<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function () {

			//为编辑按钮绑定事件，再跳转到类型修改页
			$("#updateValueBtn").click(function () {

				//取得所有的name=xz的input元素
				var $xz = $("input[name=xz]:checked");
				if($xz.length == 0){
					alert("请选择需要修改的记录")
				}else if($xz.length > 1){
					alert("一次只能选择一条记录进行修改操作")
				}else {
					//取得选中的记录的复选框的值
					//虽然是复选框，但是我们现在此时在else里面，能够确定只选择了一条记录
					//那么我们就可以直接通过val()方法获取唯一的选中的这个复选框的值
					var id = $xz.val();

					window.location.href = "settings/dictionary/value/toValueUpdate.do?id="+id;

				}
			})

			//为删除按钮绑定事件
			$("#deleteValueBtn").click(function () {
				var $xz = $("input[name=xz]:checked");
				if($xz.length == 0){
					alert("请选择需要被删除的记录");
				}else {
					//删除操作在任何系统中都是属于危险动作，需要为用户做出提示
					if(confirm("确定删除所选记录吗？")){

						var param="";
						for (var i = 0; i < $xz.length; i++) {
							param +="id="+$($xz[i]).val();
							//加上分隔符&
							if(i < $xz.length-1){
								param += "&";
							}
						}
						alert(param);
						window.location.href="settings/dictionary/value/deleteValue.do?"+param;
					}

				}
			})

		})
	</script>
</head>
<body>

	<div>
		<div style="position: relative; left: 30px; top: -10px;">
			<div class="page-header">
				<h3>字典值列表</h3>
			</div>
		</div>
	</div>
	<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
		<div class="btn-group" style="position: relative; top: 18%;">
		  <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/value/toValueSave.do'"><span class="glyphicon glyphicon-plus"></span> 创建 </button>
		  <button type="button" class="btn btn-default" id="updateValueBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
		  <button type="button" id="deleteValueBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	<div style="position: relative; left: 30px; top: 20px;">
		<table class="table table-hover">
			<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" /></td>
					<td>序号</td>
					<td>字典值</td>
					<td>文本</td>
					<td>排序号</td>
					<td>字典类型编码</td>
				</tr>
			</thead>
			<tbody>

			<c:forEach items="${dvList}" var="dv" varStatus="vs">
			<tr class="active">
				<td><input type="checkbox" name="xz" value="${dv.id}" /></td>
				<td>${vs.count}</td>
				<td>${dv.value}</td>
				<td>${dv.text}</td>
				<td>${dv.orderNo}</td>
				<td>${dv.typeCode}</td>
			</c:forEach>

			</tbody>
		</table>
	</div>
	
</body>
</html>