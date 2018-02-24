<%--
  Created by IntelliJ IDEA.
  User: 33976
  Date: 2018/2/8
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    pageContext.setAttribute("basePath", basePath);

%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>错误统计</title>
    <script type="text/javascript" src="<%=basePath%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/momentjs.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/file-input.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/fileinput_locale_zh.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/echarts.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/util.js"></script>

    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/bootstrap-datetimepicker.min.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/css/table-style.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/fileinput.css" type="text/css"/>
    <script>
        $(function () {
            init();
        })

        function init() {
            $.ajax({
                type: "POST",
                url: "/servlet/DetailQueryServlet",
                data: {id: '<%=request.getParameter("id")%>'},
                dataType: "json",
                // contentType: 'application/json',
                success: function (data) {
                    initGragh(data);
                },
                error: function (data) {
                    alert("网络错误！");
                }
            });
        }

        function initGragh(data) {

            //初始化echarts实例
            var myChart = echarts.init(document.getElementById('column'));

            if (!data) {
                return;
            }

            var rate;
            var con = new Array(),conRate = new Array();
            var inn = 0, sql = 0, total = 0, reqTotal = 0, innTotal = 0, sqlTotal = 0;
            debugger;
            for (var i=0;i<data.length;i++) {
                var obj = data[i];
                inn += nvl(obj.is_not_null);
                sql += nvl(obj.sql_error);
//                con += nvl(obj.consistence);
                con.push((obj.consistence)).toFixed(0);
                total += nvl(obj.total);
                innTotal += nvl(obj.inn_total);
                sqlTotal += nvl(obj.sql_total);
                if (i == 0) {
                    reqTotal = nvl(obj.total);
                }
                if (!reqTotal){
                    conRate.push(0);
                }else {
                    conRate.push((obj.consistence * 100 / reqTotal).toFixed(4));
                }


                option.series[parseInt(obj.status)].data = [nvl(obj.is_not_null), nvl(obj.sql_error)];
//                option.series[parseInt(obj.status)].data = [nvl(obj.is_not_null), nvl(obj.sql_error), nvl(obj.consistence)];
            }

//            rate = [(inn * 100 / innTotal).toFixed(4),
//                (sql * 100 / sqlTotal).toFixed(4),
//                (con * 100 / reqTotal).toFixed(4)];
            rate = [(inn * 100 / innTotal).toFixed(4),
                (sql * 100 / sqlTotal).toFixed(4)];

            option.series[5].data = rate;
            //使用制定的配置项和数据显示图表
            myChart.setOption(option);


            //初始化echarts实例
            var conChart = echarts.init(document.getElementById('con'));
            conOption.series[0].data = con;
            conOption.series[1].data = conRate;
            conChart.setOption(conOption);
        }

        var option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: ['需求阶段', '筛查阶段', '订单阶段', '交付阶段', '起租阶段', '错误率']
            },
            color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#ffff30'],
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                data: ['非空校验', '字典校验']
            },
            yAxis: [
                {
                    type: 'value',
                    name: '错误数',
                    min: 0,
                    axisLabel: {
                        formatter: '{value} 个'
                    }
                },
                {
                    type: 'value',
                    name: '错误率',
                    splitLine: false,
                    min: 0,
                    max: 100,
                    interval: 20,
                    axisLabel: {
                        formatter: '{value} %'
                    }
                }
            ],
            series: [
                {
                    name: '需求阶段',
                    type: 'bar',
                    barWidth: 50,
                    stack: '总量',
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [320, 302, 301]
                },
                {
                    name: '筛查阶段',
                    type: 'bar',
                    stack: '总量',
                    barWidth: 50,
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [120, 132, 101]
                },
                {
                    name: '订单阶段',
                    type: 'bar',
                    stack: '总量',
                    barWidth: 50,
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [220, 182, 191]
                },
                {
                    name: '交付阶段',
                    type: 'bar',
                    stack: '总量',
                    barWidth: 50,
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [150, 212, 201]
                },
                {
                    name: '起租阶段',
                    type: 'bar',
                    stack: '总量',
                    barWidth: 50,
                    label: {
                        normal: {
                            show: true,
                            position: 'insideRight'
                        }
                    },
                    data: [820, 832, 901]
                },
                {
                    name: '错误率',
//                    stack: '总量',
                    type: 'line',
                    yAxisIndex: 1,
                    data: [49, 23, 89],
                    itemStyle: {
                        normal: {
                            lineStyle: {
                                color: '#ffff30'
                            }
                        }
                    },
                    label: {
                        normal: {
                            show: true
                        }
                    }
                }
            ]
        };

        conOption = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['bar', 'line']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                data:['错误总数','错误率']
            },
            xAxis: [
                {
                    type: 'category',
                    data: ['需求阶段','筛查阶段','订单阶段','交付阶段','起租阶段'],
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '错误数',
                    min: 0,
                    axisLabel: {
                        formatter: '{value}'
                    }
                },
                {
                    type: 'value',
                    name: '错误率',
                    min: 0,
                    interval: 10,
                    axisLabel: {
                        formatter: '{value} %'
                    }
                }
            ],
            series: [
                {
                    name:'错误数',
                    type:'bar',
                    data:[2.0, 4.9, 7.0, 23.2, 25.6]
                },
                {
                    name:'错误率',
                    type:'line',
                    data:[2.6, 5.9, 9.0, 26.4, 28.7]
                }
            ]
        };
    </script>
</head>
<body>
<h1>错误统计</h1>
<div class="btn-toolbar">
    <button id="btn" class="btn btn-default" style="position: absolute;right: 10%" onclick="javascript:history.go(-1)">
        返回
    </button>
</div>
<div id="column" style="width:65%; height: 80%;padding-bottom: 5px;position: absolute;top:20%;left: 0%;"></div>
<div id="con" style="width:35%; height: 84%;padding-bottom: 5px;position: absolute;top:20%;left: 67%;"></div>
</body>
</html>
