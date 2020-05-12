<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});


		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为模态窗口中搜索市场活动的文本框绑定敲击键盘事件
		$("#searchName").keydown(function (event) {

			if(event.keyCode == 13){

				//取得市场活动的名称
				var searchName = $.trim($("#searchName").val());

				$.ajax({
					url : "workbench/clue/getActivityListByActivityName.do",
					data:{
						"searchName" : searchName
					},
					type : "get",
					dataType : "json",
					success : function (data) {
						/*

							data
								List<Activity> aList
								[{市场活动1},{2},{3}]

						 */

						var html = "";

						$.each(data,function (i,n) {
						    html += '<tr>;'
							html += '<td><input type="radio" name="xz" value="'+n.id+'" /></td>;'
							html += '<td id="n'+n.id+'">'+n.name+'</td>;'
							html += '<td>'+n.startDate+'</td>;'
							html += '<td>'+n.endDate+'</td>;'
							html += '<td>'+n.owner+'</td>;'
							html += '</tr>;'


						})

						$("#activitySearchBody").html(html);

					}
				})

				return false;
			}

		})

		//为提交按钮绑定事件
		$("#submitActivityBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择市场活动")

			}else {

				//alert($xz)

				var id = $xz.val();

				//为隐藏域中的市场活动id赋值
				$("#activityId").val(id);

				//取得选中的市场活动名称
				var name = $("#n"+id).html();

				//通过以上取得的活动名称，为文本框赋值
				$("#activityName").val(name);

				//关闭搜索市场活动的模态窗口
				$("#searchActivityModal").modal("hide");
			}


		})

        //为转换按钮绑定事件，执行线索的转换操作
        $("#convertBtn").click(function () {

            /*
               由于执行线索转换的操作，不需要在当前的线索转换页面中执行任何的局部刷新操作
               所以发出传统请求到后台，执行线索转换的操作即可

               传统请求的方式：
                  1.在浏览器地址栏输入地址，敲回车
                  2.点击<a href>超链接
                  3.以执行js代码的形式来执行超链接window.location.href=url....
                  4.提交form表单
                  ...

             */

            /*
               关于参数的传递：
                  1.线索的id，线索转换时使用
                  2.交易表单的参数，做添加交易用的，这些个参数有可能是没有的

                  注意：不论是否需要创建交易，线索的id都必须要传递到后台

             */

            //判断"为客户创建交易"的复选框
            //如果选中了，则需要创建交易，如果没有选中，就不需要创建交易
            if($("#isCreateTransaction").prop("checked")){

                //需要创建交易
                //最好不要使用window方式传递参数，因为麻烦并且不利于将来对于表单参数的扩展

                //使用提交表单的方式传递请求
                $("#tranForm").submit();



            }else {

                //不需要创建交易
                window.location.href = "workbench/clue/convert.do?clueId=${c.id}";

                alert("${c.id}")

            }
        })


	});
</script>

</head>
<body>

	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" autocomplete="off" id="searchName" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">提交</button>
			</div>
		  </div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${c.fullname} ${c.appellation}-${c.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;" >
		新建客户：${c.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${c.fullname} ${c.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form id="tranForm" action="workbench/clue/convert.do" method="post">
			<input type="hidden" name="clueId" value="${c.id}">

			<input type="hidden" name="flag" value="a">

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate" >预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" data-toggle="modal" data-target="#searchActivityModal" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
			  <input type="hidden" id="activityId" name="activityId">
		  </div>
		</form>

	</div>

	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${c.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>