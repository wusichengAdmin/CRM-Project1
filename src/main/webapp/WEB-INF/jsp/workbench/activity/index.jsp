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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
	<script type="text/javascript">

	$(function(){


		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为创建按钮绑定事件，打开添加市场活动的模态窗口
		$("#toActivitySaveBtn").click(function () {

			$.ajax({
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function(data){
					//解析json数组，我们最常使用的就是$.each(json数组，回调函数)
					//每一个n，就是遍历出来的json对象
					var html = "<option></option>";
					$.each(data,function (i,n) {
						html += "<option value='"+n.id+ "'>"+n.name+"</option>";
					});

					//以上option拼接完毕后，将所有的option填充到所有者的下拉框中
					$("#create-owner").html(html);
					var id = "${user.id}";
					$("#create-owner").val(id);

					//打开模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})


        //为保存按钮绑定事件，执行市场活动的添加操作
		$("#saveActivityBtn").click(function () {

			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					//数据从下面的模态窗口来
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
				},
				type:"post",
				dataType : "json",
				success:function (data) {

					if(data.success){
						//添加成功
						//清空模态窗口中的表单元素值

						//刷新列表
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//重置Form表单（清空表单元素值）
						/*
                            jquery有subimt方法做表单的提交，但是jquery并没有reset方法做表单的重置(idea工具乱提示)
                            虽然jquery没有，但是原生js的dom对象有reset方法
                            我们应该使用dom.reset()
                         */
						$("#activitySaveFrom")[0].reset();


						//关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else {
						alert("添加活动失败");
					}
				}
			})
		})

		//页面加载完毕后，发出ajax请求，取得市场活动信息列表
		//执行该方法
		pageList(1,2);

		//为查询按钮绑定事件，执行条件查询市场活动的操作
		$("#searchActivityBtn").click(function () {

			//每次点击查询按钮后，将搜索框中的信息保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startTime").val()));
			$("#hidden-endDate").val($.trim($("#search-endTime").val()));

			pageList(1,2);

		})


		//为全选复选框绑定事件，执行全选反选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);
		})

		//动态元素需要用on方法来进行事件的绑定
		//语法： $(需要绑定的元素的有效的父级元素).on(绑定事件的方式，需要绑定的元素，回调函数)
		/*
		    我们需要绑定的元素是所有name=xz的input元素
			$("input[name=xz]")
			我去查找这类元素的父级元素
			找到td，但是这个td标签是无效的父级元素，因为td元素也是我动态拼接生成的
			所以我继续上上一级扩展，找更上一层的元素，直到找到有效的父级元素为止
			最终找到了tBody元素，这个元素是有效的，在页面中已经存在的元素，不是我拼接生成的
			*/

		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);

		})




		//为删除按钮绑定事件，执行市场活动的删除操作
		$("#deleteActivityBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if($xz.length==0){
				alert("请选择需要删除的记录");
			}else {
				if(confirm("确定删除所选记录吗？")) {

					/*
					* 我们需要做批量删除操作，需要拼接参数
					* ids=xxx&ids=xxx&ids=xxx....
					*
					* */
					var param = "";

					for (var i = 0; i < $xz.length; i++) {

						param += "ids=" + $($xz[i]).val();


						if (i < $xz.length) {

							param += "&";

						}
					}

					$.ajax({
						url: "workbench/activity/deleteActivity.do",
						data: param,
						type: "post",
						dataType: "json",
						success: function (data) {

							/*

							data
								{success:true/false}

						   */

							if (data.success) {
								//删除成功
								//刷新市场活动信息列表
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							} else {

								alert("删除市场活动失败")
							}
						}
					})
				}
			}
		})

		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#toActivityUpdateBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if($xz.length==0){
				alert("请选择需要修改的记录");
			}else if ($xz.length > 1){
				alert("一次只能选择一条执行修改操作");
			}else {
				var id = $xz.val();
				$.ajax({
					url:"workbench/activity/getUserListAndActivityById.do",
					data:{
						"id":id
					},
					type:"post",
					dataType:"json",
					success:function (data) {

						/*
						    data
                                List<User> uList
                                Activity a
                                {"uList":[{用户1},{2},{3}],"a":{市场活动}}
					    */

						//填充所有者下拉列表
						var html = "<option></option>";

						$.each(data.uList,function (i,n) {

							html += "<option value='"+n.id+"'>"+n.name+"</option>";

						})

						$("#edit-owner").html(html);

						//填充修改操作模态窗口表单中的信息
						$("#edit-id").val(data.a.id);
						$("#edit-owner").val(data.a.owner);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);

						//打开修改操作的模态窗口
						$("#editActivityModal").modal("show");

					}
				})


			}

		})

		//为全部导出按钮绑定事件，实现全部市场活动记录生成excel的操作
		$("#exportActivityAllBtn").click(function () {

			if(confirm("确定要导出所有数据吗？")){

				//必须是传统请求
				window.location.href = "workbench/activity/exportActivityAll.do"
			}

		})

		//为选择导出按钮绑定事件，实现选择市场活动记录生成excel
		$("#exportActivityXzBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("请选择需要导出的市场活动");

			}else {

				if(confirm("确定要导出所选数据吗？")){
					var param = "";

					for (var i = 0; i < $xz.length; i++) {

						param += "ids="+$($xz[i]).val();

						if(i < $xz.length-1){
							param += "&";

						}

					}

					//必须是传统请求
					window.location.href = "workbench/activity/exportActivityXz.do?"+param;

				}
			}

		})

		//为导入按钮绑定事件，执行市场活动的添加操作
		$("#importActivityBtn").click(function () {


			//取得的是一个虚拟的路径+文件的名字
			var filePathName = $("#activityFile").val();

			//取得文件的后缀名
			var suffix = filePathName.substr(filePathName.lastIndexOf(".")+1);

			//判断后缀名是否符合要求 （xls，xlsx）
			if(!(suffix=="xls")||(suffix=="xlsx")){

				alert("仅支持xls，xlsx文件")

				//终止操作
				return false;
			}

			//取得文件对象
			//语法：dom.files[i]
			var myFile = $("#activityFile")[0].files[0];

			//判断文件大小不能超过5M
			if(myFile.size > 1024*1024*5){

				alert("文件大小超过5M");

				return false;
			}

			//本次上传的是二进制文件，必须使用FormData类型的对象做载体，进行文件的传递

			var formData = new FormData();

			//调用append方法，将需要上传的文件添加到FormData载体中，同时为需要上传的文件起一个key
			formData.append("myFile",myFile);

			$.ajax({
				url:"workbench/activity/importActivity.do",
				data:formData,
				type:"post",//上传文件操作必须使用post方式的请求
				dataType:"json",
				/*

					我们以前传递的参数普遍都是字符串参数，ajax默认传递参数的方式就是传递字符串参数

					但是我们今天要传递二进制的文件

					所以我们应该将默认的传递字符串的方式的设置给禁用掉

				 */

				processData:false,
				contentType:false,
				success:function (data) {

					/*

						data
							{"success":true/false,"count":?}

					 */

					if(data.success){

						//导入成功

						//刷新列表
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//将文件上传的区域中的信息出掉
						$("#activityFile").val("");

						//提示导入了几条记录
						alert("成功导入"+data.count+"条记录");

						//关闭模态窗口
						$("#importActivityModal").modal("hide");

					}else {

						alert(("导入文件失败"));
					}
				}
			})

		})

		//为更新按钮绑定事件，执行市场活动的修改操作
		$("#updateActivityBtn").click(function () {

			$.ajax({

				url:"workbench/activity/updateActivity.do",
				data:{

					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())
				},
				type:"post",
				dataType:"json",
				success:function (data) {

					/*

					   data
					     {success:true/false}

				    */

					if(data.success){
						//修改成功

						//刷新列表

						/*

                            $("#activityPage").bs_pagination('getOption', 'currentPage')
                                返回的是当前页的页码

                             $("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
                                 返回的是维持当前的每页展现的记录数


                             执行修改操作完毕后，我们应该维持在当前页，应该维持每页展现的记录数


                         */

						pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭模态窗口
						$("#editActivityModal").modal("hide");
					}else {

						alert("添加市场活动失败");
					}

				}
			})
		})


	});

	function pageList(pageNo,pageSize) {

		//将全选复选框重置
		$("#qx").prop("checked",false);

		//每次刷新活动列表之前，需要将隐藏域中的查询条件取出来，重新复制到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startTime").val($.trim($("#hidden-startDate").val()));
		$("#search-endTime").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{

				"pageNoStr":pageNo,
				"pageSizeStr" : pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startTime":$.trim($("#search-startTime").val()),
				"endTime":$.trim($("#search-endTime").val())
			},
			type:"get",
			dataType:"json",
			success:function (data) {
				/*

                    data

                        List<Activity> dataList...  市场活动列表

                        int total... 查询总条数

                        {"total":100,"dataList":[{市场活动1},{2},{3}]}

                 */

				var html = "";
				$.each(data.dataList,function (i,n) {

					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/toActivityDetail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';

				})

				//以上tr和td的部分拼接完成后，将所有的tr，td标签填充到tBody中
				$("#activityBody").html(html);


				//====================================================================
				//开始分页操作

				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}

		})

	}
	
</script>
</head>
<body>

    <%--

       使用隐藏域保存有效的查询地址

    --%>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activitySaveFrom" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">

								<!--

                                    所有的表单元素，例如input、select元素，我们处理这些表单元素的values值，都是使用val方法

                                    其中textarea是文本域，文本域比较特殊的地方在于它并没有value属性，它有的是标签对
                                    以前我们总结过，对于标签对中的信息做存取值的操作，我们使用html方法
                                    但是textarea仍然是属于表单元素范畴，虽然没有value属性，我们也要使用val方法来对其元素值进行操作

                                -->
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>

                <%--
                    data-dismiss="modal" ：关闭模态窗口
                --%>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">


					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>

						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls或.xlsx格式]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS/XLSX的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchActivityBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="toActivitySaveBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="toActivityUpdateBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（全部导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>