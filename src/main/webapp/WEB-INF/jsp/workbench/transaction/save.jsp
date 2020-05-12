<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    //取得阶段与可能性之间的对应关系
    Map<String, String> pMap = (Map<String, String>) application.getAttribute("pMap");

    Set<String> set = pMap.keySet();
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">

    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

    <script>

        var json = {

            <%
               for (String key : set) {

                   String value = pMap.get(key);

            %>

            "<%=key%>": <%=value%>,

            <%
               }
            %>

            //[object Object]:说明这是一个有效的json对象



        }


        $(function () {

            $(".time1").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            $(".time2").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "top-left"
            });

            //为模态窗口中搜索市场活动的文本框绑定敲击键盘事件
            $("#searchName").keydown(function (event) {

                if (event.keyCode == 13) {

                    //取得市场活动的名称
                    var searchName = $.trim($("#searchName").val());
                    $.ajax({
                        url: "workbench/transaction/getActivityListByActivityName.do",
                        data: {
                            "searchName": searchName
                        },
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            /*

                            data
                                List<Activity> aList
                                [{市场活动1},{2},{3}]

                         */

                            var html = "";

                            $.each(data, function (i, n) {
                                html += '<tr>';
                                html += '<td><input type="radio" name="xz" value="' + n.id + '"/></td>';
                                html += '<td id="n' + n.id + '">' + n.name + '</td>';
                                html += '<td>' + n.startDate + '</td>';
                                html += '<td>' + n.endDate + '</td>';
                                html += '<td>' + n.owner + '</td>';
                                html += '</tr>';

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


                if ($xz.length == 0) {
                    q
                    alert("请选择市场活动");
                } else {

                    var id = $xz.val();

                    //为隐藏域中的市场活动id赋值
                    $("#create-activityId").val(id);

                    //取得选中的市场活动名称
                    var name = $("#n" + id).html();

                    //通过以上取得的活动名称，为文本框赋值
                    $("#create-activityName").val(name);

                    //关闭搜索市场活动的模态窗口
                    $("#findMarketActivity").modal("hide");

                }
            })


            //为查找联系人绑定键盘敲击事件
            $("#searchContacts").keydown(function (event) {

                if (event.keyCode == 13) {

                    //取得联系人名称
                    var name = $.trim($("#searchContacts").val());

                    $.ajax({

                        url: "workbench/transaction/getActivityListByContactsName.do",
                        data: {

                            "searchContacts": $.trim($("#searchContacts").val())

                        },
                        type: "get",
                        dataType: "json",
                        success: function (data) {
                            /*
                                data:
                                   [{联系人1}，{2}，{3}]

                             */

                            var html = "";

                            $.each(data, function (i, n) {

                                html += '<tr>;'
                                html += '<td><input type="radio" name="xz" value="' + n.id + '"/></td>;'
                                html += '<td id="' + n.id + '">' + n.fullname + '</td>;'
                                html += '<td>' + n.email + '</td>;'
                                html += '<td>' + n.mphone + '</td>;'
                                html += '</tr>;'

                            })

                            $("#contactsSearchBody").html(html)

                        }


                    })

                    return false;
                }
            })

            //为搜索联系人模态窗口中的提交按钮绑定事件
            $("#submitContactsBtn").click(function () {


                //取得选中的记录的id
                var $xz = $("input[name=xz]:checked");

                var id = $xz.val();

              //  alert(id)

                //将选中的id赋值到联系人下方的隐藏域中
                $("#create-contactsId").val(id);

                //取得所选记录中的联系人的名字

                var fullname = $("#" + id).html();

               // alert(fullname)

                //赋值
                $("#create-contactsName").val(fullname);

                //关闭模态窗口
                $("#findContacts").modal("hide");

            })

            /*

                触发的是自动补全事件：是bs_tyepahead插件为我们提供的绑定事件的方式

                为客户名称的文本框，绑定自动补全事件

             */

            $("#create-customerName").typeahead({
                //该函数，是在我们在文本框中输入了信息之后触发的
                /*

                    参数1：我们在文本框中输入的信息
                    参数2：为我们展现信息列表的动画的方法

                 */
                source: function (query, process) {

                    /*

                        自动补全事件的触发，我们根据输入的关键字查询列表的过程一定是发出ajax请求
                        因为展现数据的格式，必须是json格式的数据

                     */

                    $.get(
                        "workbench/transaction/getCustomerNamesByName.do",
                        {"name": query},
                        function (data) {
                            //alert(data);

                            /*

                                data
                                    List<Customer> cList
                                    [{客户名称1},{2},{3}]

                                    注意：以下使用的process方法，只识别字符串的json数组

                             */
                            process(data);
                        },
                        "json"
                    );
                },
                delay: 1000	//延迟加载的时间，单位是毫秒
            });
            /*

                关于数据的保存
                将来的实际项目开发，有可能会遇到这样一种情况
                数据量比较少，数据一般情况下不会更新，保存的数据是以键值对的形式来呈现的
                这种数据，一般我们都是需要保存到properties属性文件中

                我们要以阶段为key，可能性为value，来保存信息

                创建Stage2Possibility.properties

                stage阶段：key
                possibility可能性：value

                key=value
                01资质审查=10
                02需求分析=25
                03价值建议=40
                ...
                ...
                07成交=100
                08..=0
                09..=0

                注意：
                    properties文件，我们能想到的就是不支持中文

                我们现在可以在服务器启动的时候，就将以上的Stage2Possibility.properties中的数据解析出来
                保存到服务器内存中，以服务器缓存的形式存在

             */

            //为阶段的下拉框提供下拉框选中事件，通过选中的阶段，自动填写可能性
            $("#create-stage").change(function () {

                //取得选中的阶段名称
                var stage = $("#create-stage").val();


                //方式一：发出ajax请求到后台，通过后台来取得可能性
                //发出ajax请求到后台，通过后台来取得可能性
                /*$.ajax({

                    url : "workbench/transaction/getPossibilityByStage.do",
                    data : {

                        "stage" : stage

                    },
                    type : "get",
                    dataType : "json",
                    success : function (data) {
                        /!*

                            data
                                {possibility:?}

                         *!/

                        //通过从后台取得的可能性，为表单中的可能性文本框赋值
                        $("#create-possibility").val(data.possibility);


                    }

                })*/

                //方式二：不过后台，直接取得可能性
                /*
                   阶段：stage变量
                   阶段和可能性之间的对应关系:pMap

                   现在还不能取得可能性，因为pMap是java变量
                   如果java变量是一个普通的字符串或是普通的数值，那么我们是可以转换成js变量来使用
                   我们现在需要手动的将map中的键值对，转换成js中的键值对(json)来使用

                   pMap ---> json


                 */

                /*
                   我们以前一直以json.key的形式来取得value值
                   （var possibility = json.stage;）

                   但是今天不行，因为今天json的key是一个变量
                   应该以这种方式来取得
                   var possibility = json[stage];
                 */

                var possibility = json[stage];

                $("#create-possibility").val(possibility);

            })

            //为保存按钮绑定事件，执行交易的添加操作
            $("#saveTranBtn").click(function () {

                //发送传统请求，提交表单
                $("#tranForm").submit();

            })


        })
    </script>

</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询"
                                   id="searchName">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
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

<!-- 查找联系人 -->
<div class="modal fade" id="findContacts" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <!--

                  我们使用自己的自动补全事件
                  需要将浏览器默认为我们提供的自动补全去除掉
                  使用：autocomplete="off"

              -->
                            <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询"
                                   autocomplete="off" id="searchContacts">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="contactsSearchBody">
                    <%--<tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>李四</td>
                        <td>lisi@wkcto.com</td>
                        <td>12345678901</td>
                    </tr>
                    <tr>
                        <td><input type="radio" name="activity"/></td>
                        <td>李四</td>
                        <td>lisi@wkcto.com</td>
                        <td>12345678901</td>
                    </tr>--%>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="submitContactsBtn">提交</button>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveTranBtn">保存</button>
        <button type="button" class="btn btn-default">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form  id="tranForm" action="workbench/transaction/saveTran.do"  class="form-horizontal" role="form" style="position: relative; top: -30px;" >
    <div class="form-group">
        <label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionOwner" name="owner">
                <option></option>
                <c:forEach items="${uList}" var="u">
                    <option value="${u.id}" ${u.id eq user.id ?"selected":""}>${u.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-amountOfMoney" name="money">
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-transactionName" name="name">
        </div>
        <label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time1" id="create-expectedClosingDate" name="expectedDate">
        </div>
    </div>

    <div class="form-group">
        <label for="create-customerName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-customerName" autocomplete="off" name="customerName"
                   placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="create-transactionStage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select name="stage" class="form-control" id="create-stage">
                <option></option>
                <c:forEach items="${stage}" var="s">
                    <option value="${s.value}">${s.text}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionType" name="type">
                <option></option>
                <c:forEach items="${transactionType}" var="t">
                    <option value="${t.value}">${t.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-possibility">
        </div>
    </div>

    <div class="form-group">
        <label for="create-clueSource" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" name="source" id="create-clueSource">
                <option></option>
                <c:forEach items="${source}" var="s">
                    <option value="${s.value}">${s.text}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                           data-toggle="modal"
                                                                                           data-target="#findMarketActivity"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-activityName">
            <input type="hidden" id="create-activityId" name="activityId">
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);"
                                                                                            data-toggle="modal"
                                                                                            data-target="#findContacts"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-contactsName">
            <input type="hidden" id="create-contactsId" name="contactsId">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control time2" id="create-nextContactTime" name="nextContactTime">
        </div>
    </div>

</form>
</body>
</html>