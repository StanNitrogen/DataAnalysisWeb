<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 33976
  Date: 2018/2/1
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    pageContext.setAttribute("basePath", basePath);

    List<Map<String, Object>> results = (List<Map<String, Object>>) request.getAttribute("result");
%>
<html>
<head>
    <title>数据分析</title>
    <script type="text/javascript" src="<%=basePath%>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/momentjs.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/moment-with-locales.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/file-input.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/fileinput_locale_zh.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/index.js"></script>
    <script type="text/javascript" src="<%=basePath%>/bootstrap/js/common.js"></script>

    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/bootstrap.min.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/bootstrap-datetimepicker.min.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/css/table-style.css" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath%>/bootstrap/css/fileinput.css" type="text/css"/>
    <%--<link rel="stylesheet" href="<%=basePath%>/bootstrap/css/bootstrap.min.css" type="text/css"/>--%>
    <style>
        h1 {
            text-align: center;
            font-family: 黑体;
        }

        .mar-5 {
            margin-left: 3rem;
        }

        .mar-right {
            margin-right: 5rem;
            float: right;
        }
    </style>
</head>
<body>
<h1>数据分析</h1>
<form id="form1" name="form1" class="form-inline mar-5" role="form" style="width: 93%">
    <div class="form-group">
        <label class="sr-only" for="excelName">Excel名称</label>
        <input type="text" class="form-control" id="excelName" name="excelName" placeholder="请输入名称">
    </div>
    <div class="form-group mar-5" id="but">
        <button type="button" onclick="query('<%=basePath%>servlet/QueryServlet?excelName='+$('#excelName').val())"
                class="btn btn-success">&nbsp;查&nbsp;&nbsp;&nbsp;&nbsp;询&nbsp;
        </button>
        <button type="button" onclick="analysis()" class="btn btn-warning">数据分析</button>
    </div>
    <div class="form-group mar-right" id="help">
        <a href="#" onclick="help()">操作帮助</a>
    </div>
</form>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">请选择Excel文件</h4>
            </div>
            <div class="modal-body">
                <%--<input type="hidden" name="fileIds" id="fileIds">--%>
                <div class='form-group' style='margin-bottom: 0;' id="file_container">
                    <%--<a href="~/Data/ExcelTemplate/Order.xlsx" class="form-control" style="border:none;">下载导入模板</a>--%>
                    <input type="file" name="inputfile" id="inputfile" class="file-loading"/>
                    <input type='hidden' name='suffix' value=''/>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="mar-5" style="width: 90%">
    <table class="table table-condensed table-bordered table-hover">
        <thead>
        <tr>
            <th>附件名</th>
            <th>非空错误总数</th>
            <th>非空错误率</th>
            <th>字典检索错误总数</th>
            <th>字典检索错误率</th>
            <th>一致性校验</th>
            <th>一致性错误率</th>
            <th>图标统计</th>
        </tr>
        </thead>
        <tbody>
        <% if (results != null) {
            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> map = results.get(i);
        %>

        <tr>
            <td><a href="#"
                   onclick="downloadIt('<%=basePath%>servlet/DownloadServlet?path=<%= (String) map.get("file_path")%>')"><%= (String) map.get("file_name")%>
            </a></td>
            <td><%= (String) map.get("is_not_null")%>
            </td>
            <td><%= (String) map.get("inn_rate")%> %</td>
            <td><%= (String) map.get("sql_error")%>
            </td>
            <td><%= (String) map.get("sql_rate")%> %</td>
            <td>
                <a href="#"
                   onclick="consistency('<%= (String) map.get("attach_id")%>')"><%= (String) map.get("consistence")%>
                </a>
            </td>
            <td><%= (String) map.get("consistence_rate")%> %</td>
            <td><a href="#" onclick="gragh('<%= (String) map.get("attach_id")%>')">查看</a></td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>

<div class="modal fade" id="helpPanel" tabindex="-1" role="dialog" aria-labelledby="ViewPanel" >
    <div class="modal-dialog" role="document" style="width: 768px!important;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="helpModalLabel">帮助</h4></div>
            <div id="_helpModalDialog_body" class="modal-body">
                <form class="form-horizontal" id="helpModal" role="form">
                    <div class="table-responsive">
                    <div id="myCarousel" class="carousel slide">
                        <ol class="carousel-indicators">
                            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                            <li data-target="#myCarousel" data-slide-to="1"></li>
                        </ol>
                        <!-- Carousel items -->
                        <div class="carousel-inner">
                            <div class="active item"><img id="h1" src="<%=basePath%>img/h1.jpg" width="100%" height="60%"/></div>
                            <div class="item"><img id="h2" src="<%=basePath%>img/h2.jpg" width="100%" height="60%"/></div>
                        </div>
                        <a href="#myCarousel" data-slide="prev" class="carousel-control left">
                            <span class="glyphicon glyphicon-chevron-left"></span>
                        </a>
                        <a href="#myCarousel" data-slide="next" class="carousel-control right">
                            <span class="glyphicon glyphicon-chevron-right"></span>
                        </a>
                    </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a class="btn" onClick="helpClose()">关闭</a>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="ViewPanel" tabindex="-1" role="dialog" aria-labelledby="ViewPanel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="modalLabel">详细描述</h4></div>
            <div id="_modalDialog_body" class="modal-body">
                <form class="form-horizontal" id="dataFormModal" role="form">
                    <!--  设置这个div的大小，超出部分不显示滚动条 -->
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th>一致性校验详情</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    <div id="texta" style="width: 100%"></div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <a class="btn" onClick="closeMadal()">关闭</a>
            </div>
        </div>
    </div>
</div>
</body>
<script>

    $("#inputfile").fileinput({
        language: 'zh', //设置语言
        title: "请上传附件",
        uploadUrl: "<%=basePath%>servlet/UploadServlet", //上传的地址
        // maxFileSize : 10240, //限制文件最大size
        maxFileCount: 10, //表示允许同时上传的最大文件个数
        browseClass: "btn btn-success", //按钮样式
        fileIdContainer: "[name='fileIds']",
        dropZoneEnabled: false,//是否显示拖拽区域
        msgFilesTooMany: "最多允许同时上传10个文件！",
        allowedFileExtensions: ['xls', 'xlsx', 'XLS', 'XLSX'],//接收的文件后缀
        enctype: 'multipart/form-data',
        showContainer: '#attachment',
        //显示文件类型 edit=可编辑 detail=明细 默认为明细
        showType: 'detail',
        showUpload: true, //是否显示上传按钮
        showCaption: true,//是否显示标题
        showRemove: true,
        showPreview: false,
        uploadTitle: '选择文件',
        //弹出窗口 执行上传附件后的回调函数(window:false不调用此方法)
        window: true,
//        uploadExtraData: function (previewId, index) {   //额外参数的关键点
//            return;
//        },
        callback: function (fileIds, oldfileIds) {
            //更新fileIds
            this.showFiles({
                fileIds: fileIds
            });
        }
    }).on("fileuploaded", function (event, data, previewId, index) {
//        文件上传完成后，通过返回值进行判断处理
        var res = data.response;
        if (res){
            alert("数据分析成功！");
        }else {
            alert("数据分析失败，请检查您的excel是否合乎规范！");
        }
        $('#inputfile').fileinput('clear');
        $('#inputfile').fileinput('unlock');
        $("#myModal").modal('hide');
        query('<%=basePath%>servlet/QueryServlet?excelName=' + $('#excelName').val());
    }).on("filebatchselected", function (event, files) {
        if (files && files[0]) {
            $('#inputfile').fileinput('upload');
        }
    });
</script>
</html>
