<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2020/2/20
  Time: 21:56
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="<%=basePath%>">

    <title>title</title>

    <script src="jquery/jquery-1.11.1-min.js"></script>
    <script src="ECharts/echarts.min.js"></script>

    <script>
        $(function () {

            getCharts();

        })

        /*function getCharts() {

            $.ajax({

                url : "workbench/chart/transaction/getChart.do",
                type : "get",
                dataType : "json",
                success : function (data) {

                    /!*

                        data
                            List<String> nameList ...  统计项列表
                            int max ...  统计数量中的最大值
                            List<Map<String,Object>> dataList..  统计数据

                            {

                                "nameList" : ["01资质审查","02需求分析"..],

                                "max":100,

                                "dataList":[
                                    {"value":20,"name":"01资质审查"},
                                    {"value":60,"name":"02x需求分析"},
                                    {}
                                  ]

                             }


                     *!/

                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: data.nameList//['展现','点击','访问','咨询','订单']
                        },

                        series: [
                            {
                                name:'漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: data.min,
                                max: data.max,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList
                                    /!*[
                                    {value: 60, name: '访问'},
                                    {value: 40, name: '咨询'},
                                    {value: 20, name: '订单'},
                                    {value: 80, name: '点击'},
                                    {value: 100, name: '展现'}
                                ]*!/
                            }
                        ]
                    };




                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                }

            })*/

        //}
        function getCharts() {

            $.ajax({

                url : "workbench/chart/transaction/getChart.do",
                type : "get",
                dataType : "json",
                success : function (data) {

                    /*

                        data
                            List<String> nameList ...  统计项列表
                            int max ...  统计数量中的最大值
                            List<Map<String,Object>> dataList..  统计数据

                            {

                                "nameList" : ["01资质审查","02需求分析"..],

                                "max":100,

                                "dataList":[
                                    {"value":20,"name":"01资质审查"},
                                    {"value":60,"name":"02x需求分析"},
                                    {}
                                  ]

                             }


                     */

                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 指定图表的配置项和数据
                    var option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易阶段数量的漏斗图'
                        },

                        //统计项
                        legend: {
                            data: data.nameList//['展现','点击','访问','咨询','订单']
                        },

                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: data.min,   //找到统计项的最小值
                                max: data.max,//100, //找到统计项最大值
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: data.dataList/*[
                                    {value: 60, name: '01资质审查'},
                                    {value: 40, name: '02需求分析'},
                                    {value: 20, name: '03价值建议'},
                                    {value: 80, name: '04..'},
                                    {value: 100, name: '05..'}
                                ]*/ //统计图表所需数据
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);


                }

            })



        }


    </script>
</head>
<body>

<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div id="main" style="width: 800px;height:400px;"></div>

</body>
</html>
