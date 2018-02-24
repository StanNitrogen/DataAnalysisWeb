/*
 * Jquery扩展函数 -- 公共附件
 *
 * @Author YINN
 * @Date 2017-12-04
 * @Version 1.0.3
 * @Summary 附件初始化方法，其中包含加载附件列表与附件上传按钮，附件上传模态的生成
 * @How to use 使用方法 : 在HTML中定义一个空的容器元素(最好是DIV),例如:<div id='test'></div>,则调用时，可在
 * <script>
 *      $(function () {
 *          var param = {
 *              biz_id : "xxxxxxx",
 *              biz_type : "xxxxxxx",
 *              ext1 : {value : "123456789", filter : false, isDeletable: true},
 *              ext2 : "ggggg",
 *              ext3 : 123,
 *              ....,
 *              isEditable : true,
 *              successCallback : function(a){
 *                  a = "1";
 *                  alertModal(a);
 *              }
 *          };
 *          $('#aaa').initAttach(param);
 *      })
 * </script>
 * 其中param为附件生成的参数.
 * @param 为JSON对象 抑或 严格JSON形式的字符串
 * 其中param包含以下各项配置参数:
 * {
 *      biz_id : "",  //(*必填参数)业务ID, 数据库为32位varchar字段,传入类型，可为数组 字符串 数字三种类型
 *      biz_type : "", //(*必填参数)业务类型, 当前业务数据表名
 *
 *      //(可选参数)扩展字段1-5,可接收JSON形式，number形式，string形式三种参数，标准形式为JSON格式,
 *      ext1: {
 *              value : "",  //为扩展字段值，String型
 *              filter : false,  //是否过滤，布尔类型，默认为false，当标记为true时，则当前附件显示列表添加过滤条件为 extend1 = #{value}
 *              isDeletable: true  //是否显示删除按钮，默认为true，当标记为false时，则附件列表中ext1 = 当前value的所有行,不显示删除按钮
 *              },
 *      ext2: {value : "", filter : false, isDeletable: true},
 *      ext3: {value : "", filter : false, isDeletable: true},
 *      ext4: {value : "", filter : false, isDeletable: true},
 *      ext5: {value : "", filter : false, isDeletable: true},
 *      isEditable: true,  //(可选参数)全局是否可编辑，布尔类型，默认为true，当标记为false时，所有行不显示删除按钮，同时上传附件按钮不显示。
 *      isFilter: true,  //(可选参数)全局是否启用过滤，布尔类型，默认为true，当标记为false时，
 *      filterConfig: "&&",  //(可选参数)全局过滤条件的集合操作配置参数,String类型，默认为"&&"，意为并且，当标记为"||"时，全部filter过滤条件取并集操作（或）
 *      deletableConfig: "||",  //(可选参数)全局删除条件的集合操作配置参数,String类型，默认为"||"，意为并且，当标记为"&&"时，全部isDeletable删除条件取并集操作（交）
 *      successCallback: function () {  //(可选参数)附件上传成功回调函数， 在关闭附件模态框与刷新附件列表之后调用，其中若传入参数不为function对象则不执行
 *      },
 *      errorCallback: function () {   //(可选参数)附件上传失败回调函数， 在关闭附件模态框与刷新附件列表之后调用，其中若传入参数不为function对象则不执行
 *      }
 *  }
 *
 */

(function ($) {
    $.fn.initAttach = function (param) {
    	
    	if(!$.fn.fileinput) {
    		// 引入js
            var css = "<link type='text/css' rel='stylesheet' href='" + projectName + "/asserts/bootstrap/css/fileinput.css'/>";
            var js =  "<script type='text/javascript' src='" + projectName + "/asserts/bootstrap/js/file-input.js'></script>" +
                      "<script type='text/javascript' src='" + projectName + "/asserts/bootstrap/js/fileinput_locale_zh.js'></script>";
            $('head').append(css);
            $('head').append(js);
    	}

        //参数转化
        var options = transParams(param);

        //验证参数
        validParams(param);

        //参数合并
        var params = $.extend(true, {}, $.fn.initAttach.defaults, options || {});

        /**
         * 如果id为以逗号间隔的多id字符串时，则不显示以往的上传附件的历史
         */
        if(params.biz_id.split(',').length == 1){
	        //加载附件上传按钮与模态框
	        createAttachUploadModal(this,params);
	
	        //获取附件列表
	        getAttachList(this,params);
        }
    }

    //默认参数
    $.fn.initAttach.defaults = {
        biz_id : "",
        biz_type : "",
        ext1: {value : "", filter : false, isDeletable: true},
        ext2: {value : "", filter : false, isDeletable: true},
        ext3: {value : "", filter : false, isDeletable: true},
        ext4: {value : "", filter : false, isDeletable: true},
        ext5: {value : "", filter : false, isDeletable: true},
        isEditable: true,
        isFilter: true,
        filterConfig: "&&", //'&&'  or  '||'
        deletableConfig: "||",
        successCallback: function () {
        },
        errorCallback: function () {
        }
    }

    //获取附件列表
    function getAttachList(that,param){
        var that = $(that);
        var $tb = that.find("table");
        if($tb){
            $tb.remove();
        }
        var table = "";
            table += "<table class='table table-condensed table-striped table-responsive' id='attachTable'>\n" +
                     "    <thead>\n" +
                     "       <th>文件名</th>\n" +
//                     "       <th>文件大小</th>\n" +
                     "       <th>上传时间</th>\n" +
                     "       <th>上传人</th>\n" +
                     "       <th>操作</th>\n" +
                     "    </thead>\n" +
                     "    <tbody id='attachTbody" + getId(param) + "'>";
            table += "    </tbody>\n" +
                     "</table>";
        that.append(table);
        /**
         * 查询附件列表，并根据权限配置删除按钮
         */
        $.ajax({
            type: "POST",
            url: projectName+"/asserts/tpl/attach/getAttachList",
            dataType: 'json',
            data: param,
            async: false,
            // contentType: "application/json;charset=UTF-8",
            success: function (value) {
                var html = "";
                if (value != null && value.success == "1") {
                    var data = value.obj;
                    if(data && data.length > 0){
                        for (var i=0;i<data.length;i++){
                            /**
                             * 当ajax查询出来的扩展字段 与 param中的扩展字段值相等，并且查询出来的扩展字段不为空时
                             */
                            if(data[i].extend_field1 == param.ext1.value && data[i].extend_field1 !=""){
                                data[i]["ext1_isDeletable"] = param.ext1.isDeletable;
                            }else {
                                data[i]["ext1_isDeletable"] = !(param.ext1.isDeletable);
                            }
                            if(data[i].extend_field2 == param.ext2.value && data[i].extend_field2 !=""){
                                data[i]["ext2_isDeletable"] = param.ext2.isDeletable;
                            }else {
                                data[i]["ext2_isDeletable"] = !(param.ext2.isDeletable);
                            }
                            if(data[i].extend_field3 == param.ext3.value && data[i].extend_field3 !=""){
                                data[i]["ext3_isDeletable"] = param.ext3.isDeletable;
                            }else {
                                data[i]["ext3_isDeletable"] = !(param.ext3.isDeletable);
                            }
                            if(data[i].extend_field4 == param.ext4.value && data[i].extend_field4 !=""){
                                data[i]["ext4_isDeletable"] = param.ext4.isDeletable;
                            }else {
                                data[i]["ext4_isDeletable"] = !(param.ext4.isDeletable);
                            }
                            if(data[i].extend_field5 == param.ext5.value && data[i].extend_field5 !=""){
                                data[i]["ext5_isDeletable"] = param.ext5.isDeletable;
                            }else {
                                data[i]["ext5_isDeletable"] = !(param.ext5.isDeletable);
                            }
                            html += "<tr>\n";
                            html += "<td>\n" +
                                    "     <a name='paths' href='#'>" + data[i].file_name + "</a>\n" +
                                    "     <input type='hidden' name='file_path' value='" + data[i].file_path + "'/>\n" +
                                    "     <input type='hidden' name='isExternal' value='" + data[i].isExternal + "'/>\n" +
                                    "     <input type='hidden' name='file_type' value='" + data[i].file_type + "'/>\n" +
                                    "     <input type='hidden' name='attach_id' value='" + data[i].attach_id + "'/>\n" +
                                    "</td>\n";
//                            html += "<td>" + (data[i].file_size? data[i].file_size + "KB": "-") + "</td>\n";
                            html += "<td>" + data[i].create_time + "</td>\n";
                            html += "<td>" + data[i].create_user + "</td>\n";
                            if(param.isEditable == false){
                                html += "<td></td>";
                            }else {
                                    /**
                                     * 当原始参数中，biz_id 与 biz_type同时不为空串时，并且
                                     * eval计算每个扩展字段中isDeletabale与deletableConfig(交并关系)
                                     * 的值同时为true（即：设置过扩展字段，并且扩展字段中isDeletabale
                                     * 为true时）才展示,
                                     * 或者 param中五个ex字段与查询返回的五个ex字段同时不存在时（即：
                                     * 初始化init附件时，只设置biz_id与biz_type）才显示。
                                     */
                                if (
                                    (param.biz_id!= "" && param.biz_type!=""
                                    && eval(data[i].ext1_isDeletable + param.deletableConfig +
                                            data[i].ext2_isDeletable + param.deletableConfig +
                                            data[i].ext3_isDeletable + param.deletableConfig +
                                            data[i].ext4_isDeletable + param.deletableConfig +
                                            data[i].ext5_isDeletable)) ||
                                    (!data[i].extend_field1 && !data[i].extend_field2 && !data[i].extend_field3
                                        && !data[i].extend_field4 && !data[i].extend_field5 && !param.ext1.value
                                        && !param.ext2.value && !param.ext3.value && !param.ext4.value && !param.ext5.value)
                                        ){
                                    html += "<td>" +
                                            "    <input type='hidden' name='attach_id' value='" + data[i].attach_id + "'/>\n" +
                                            "    <button type='button' class='btn btn-default btn-sm' name='btnRemove'>\n" +
                                            "        <span class='glyphicon glyphicon-remove-sign' aria-hidden='true'></span>\n" +
                                            "    </button>\n"+
                                            "</td>";
                                }else {
                                    html += "<td></td>";
                                }
                            }
                            html += "</tr>";
                        }
                    }else {
                        html += "<td colspan='5'>沒有匹配的记录</td>";
                    }
                    $('#attachTbody' + getId(param)).append(html);
                } else {
                    var html = "<tr><td colspan='5'>沒有匹配的记录</td></tr>";
                    $('#attachTbody' + getId(param)).append(html);
                    alertModel('获取附件列表失败，请联系管理员!');
                }
                $("a[name='paths']").on('click',function(){
                    var ie = $(this).siblings("input[name='isExternal']").val();
                    var downLoadPath = $(this).siblings("input[name='file_path']").val();
                    var attach_id = $(this).siblings("input[name='attach_id']").val();
                    if(ie.toString() == "1"){
                        window.open(downLoadPath, "_blank");
                    }else {
                        window.open(projectName+"/asserts/tpl/attach/downloadAttach?attach_id=" + attach_id , "_blank");
                    }
                });
                //绑定删除方法
                $("button[name='btnRemove']").on('click',function(){
                    $.ajax({
                        type: "POST",
                        url: projectName+"/asserts/tpl/attach/deleteAttachById",
                        dataType: 'json',
                        data: {
                                attach_id : $(this).siblings("input[name='attach_id']").val()
                              },
                        async: false,
                        success: function (value) {
                            if (value != null && value.success == "1") {
                                getAttachList(that,param);
                                alertModel(value.msg);
                            } else {
                                getAttachList(that,param);
                                alertModel(value.msg);
                            }
                        },
                        error: function (data) {
                            getAttachList(that,param);
                            alertModel(data.msg);
                        }
                    });
                })
            },
            error: function (data) {
                var html = "<td colspan='5'>沒有匹配的记录</td>";
                $('#attachTbody' + getId(param)).append(html);
                alertModel('获取附件列表失败!');
            }
        });
    }
    
    function getId(params) {
    	return params.biz_type + params.biz_id;
    }

    function createAttachUploadModal(that,params) {
        var $that = $(that);
        var modal =
            "<form>\n" +
            "    <div id='attachModal" + getId(params) + "' class='modal hide fade' tabindex='-1'>\n" +
            "        <div class='modal-dialog' role='document'>\n" +
            "            <div class='modal-content'>\n" +
            "                <div class='modal-header'>\n" +
            "                    <button class='close' type='button' data-dismiss='modal'>×</button>\n" +
            "                    <h4 id='myModalLabel'>附件上传</h4>\n" +
            "                </div>\n" +
            "                <div class='modal-body'>\n" +
            "                    <form class='form-horizontal' id='attachForm" + getId(params) + "' method='post' enctype='multipart/form-data' role='form'>\n" +
            "                        <div class='form-group'>\n" +
            "                            <label>文件名称</label>\n" +
            "                            <input type='text' name='file_name' id='file_name" + getId(params) + "' class='form-control' required='required'/>\n" +
            "                        </div>\n" +
//            "                        <div class='form-group' style='margin-bottom: 0;'>\n" +
//            "                            <input type='file' name='base_attach' id='base_attach' class='file-loading'/>\n" +
//            "                            <input type='hidden' name='suffix' value=''/>\n" +
//            "                        </div>\n" +
            "                    </form>\n" +
            "                </div>\n" +
            "                <div class='modal-footer'>\n" +
            "                    <button class='btn btn-success' data-dismiss='modal'>提交</button>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</form>\n";
        var modalButton =
            "<div class='btn-toolbar' style='margin-top:10px'>\n" +
            "	<div class='form-group' style='margin-bottom: 0;'>\n" +
            "   	<input type='file' name='base_attach' id='base_attach" + getId(params) + "' class='file-loading'/>\n" +
            "   	<input type='hidden' name='suffix' value=''/>\n" +
            "	</div>\n" +
//            "    <button class='btn btn-success' id='showAttachModal'>\n" +
//            "        上传附件\n" +
//            "    </button>\n" +
            "</div>\n";
        $that.append(modal);
        //根据全局可编辑变量判断是否有上传按钮
        if(params.isEditable && params.isEditable == true){
            $that.append(modalButton);
        }
        //对附件上传按钮 绑定点击事件，显示模态，同时设置附件上传input的各项参数
//        $('#showAttachModal').click(function () {
//            $('#attachModal').modal('show');
//        });
        $('#attachModal' + getId(params) + ' .modal-footer > button').click(function() {
        	$("#base_attach" + getId(params)).fileinput('upload');
        });
        $("#base_attach" + getId(params)).fileinput({
            language : 'zh', //设置语言
            title : "请上传附件",
            uploadUrl : projectName+"/asserts/tpl/attach/uploadAttach", //上传的地址
            // maxFileSize : 10240, //限制文件最大size
            maxFileCount : 10, //表示允许同时上传的最大文件个数
            browseClass : "btn btn-success", //按钮样式
            fileIdContainer : "[name='fileIds']",
            dropZoneEnabled : false,//是否显示拖拽区域
            msgFilesTooMany : "最多允许同时上传10个文件！",
            enctype : 'multipart/form-data',
            showContainer : '#attachment',
            //显示文件类型 edit=可编辑 detail=明细 默认为明细
            showType : 'detail',
            showUpload: false, //是否显示上传按钮
            showCaption: false,//是否显示标题
            showRemove: false,
            showPreview: false,
            uploadTitle: '选择文件',
            //弹出窗口 执行上传附件后的回调函数(window:false不调用此方法)
            window : true,
            uploadExtraData : function(previewId, index) {   //额外参数的关键点
                var obj = {};
                obj["biz_id"] = params.biz_id;
                obj["biz_type"] = params.biz_type;
                obj["file_name"] = $('#file_name' + getId(params)).val();
                obj["extend_field1"] = params.ext1.value;
                obj["extend_field2"] = params.ext2.value;
                obj["extend_field3"] = params.ext3.value;
                obj["extend_field4"] = params.ext4.value;
                obj["extend_field5"] = params.ext5.value;
                return obj;
            },
            callback:function(fileIds,oldfileIds){
                //更新fileIds
                this.showFiles({
                    fileIds:fileIds
                });
            }
        }).on("fileuploaded", function(event, data, previewId, index) {
            //文件上传完成后，通过返回值进行判断处理
            var res = data.response;
            if(res.success){
//                alertModel(res.msg);
                closeAttachModal(params);
                getAttachList($that,params);
                if($.isFunction(params.successCallback())){
                    params.successCallback();
                }
            }else {
                alertModel(res.msg);
                // closeAttachModal();
                $('#base_attach' + getId(params)).fileinput('clear');
                $('#base_attach' + getId(params)).fileinput('unlock');
                getAttachList($that,params);
                if($.isFunction(params.errorCallback())){
                    params.errorCallback();
                }
            }
        }).on("filebatchselected", function(event, files) {
        	if(files && files[0]) {
        		$('#file_name' + getId(params)).val(files[0].name);
        		$('#attachModal' + getId(params)).modal('show');
        	}
        });
    }

    //参数校验
    function validParams(param){
        if(!param.biz_id) throw "参数错误,biz_id为undefined";
        if(!param.biz_type) throw "参数错误,biz_type为undefined";
        if(param.biz_id == "" || param.biz_id == null || param.biz_id == []) throw "参数错误,biz_id为NULL或\"\"或空数组[]";
        if(param.biz_type == "" || param.biz_type == null) throw "参数错误,biz_type为NULL或\"\"";
    }

    //参数转化
    function transParams(param){
        var jsonObj = {};
        //判断传入参数是否为Json对象，如果为Json字符串则转化为json对象，都不满足则抛错
        if (!isJSON(param)){
            try {
                jsonObj = JSON.parse(param);
            }catch (e){
                throw "传入参数有误,请传入JSON对象或者标准JSON形式的字符串";
            }
        }else {
            jsonObj = param;
        }

        //业务id转换
        var idCon = jsonObj.biz_id;
        if(!isNaN(idCon)) idCon = String(idCon);
        if ($.isArray(idCon)){
            var idStr = "";
            $.each(idCon,function (i,v) {
                idStr += v;
                if (i < idCon.length-1) idStr += ",";
            })
            jsonObj["biz_id"] = idStr;
        }else if(typeof idCon=='string' && idCon.constructor==String){
        }else {
            throw "传入参数有误,biz_id只能为数组，数字，字符串类型";
        }

        //扩展字段转化
        extTransfer(jsonObj,jsonObj.ext1,"ext1");
        extTransfer(jsonObj,jsonObj.ext2,"ext2");
        extTransfer(jsonObj,jsonObj.ext3,"ext3");
        extTransfer(jsonObj,jsonObj.ext4,"ext4");
        extTransfer(jsonObj,jsonObj.ext5,"ext5");
        return jsonObj;
    }

    //关闭模态框并清空表单值
    function closeAttachModal(params){
        //清空值

        // $("#file_name").fileinput('clear');
        $('#base_attach' + getId(params)).fileinput('clear');
        $('#base_attach' + getId(params)).fileinput('unlock');
        $("#file_name" + getId(params)).val('');
        $("#attachModal" + getId(params)).modal('hide');
    }

    //判断参数对象是否为JSON对象
    function isJSON(obj){
        var isjson = typeof(obj) == "object" && Object.prototype.toString.call(obj).toLowerCase() == "[object object]" && !obj.length;
        return isjson;
    }

    /*
     * 延展字段转化函数
     * param : tranJsonObj 需转换的Json对象
     * param : extendObj 延展字段对象
     * param : extName 返回时
     *
     */
    function extTransfer(tranJsonObj,extendObj,extName){
        if(extendObj){
            //数字字符串转换
            if(!isNaN(extendObj)) extendObj = String(extendObj);
            if(typeof extendObj=='string' && extendObj.constructor==String){
                if(tranJsonObj.isEditable == false){
                    tranJsonObj[extName] = {
                        value : extendObj,
                        filter : true,
                        isDeletable: false
                    }
                }else {
                    tranJsonObj[extName] = {
                        value : extendObj,
                        filter : false,
                        isDeletable: true
                    }
                }
            }else if(!isJSON(extendObj)){
                //如果不是json，不是数字，字符串
                if(tranJsonObj.isEditable == false){
                    tranJsonObj[extName] = {
                        value : "",
                        filter : true,
                        isDeletable: false
                    }
                }else {
                    tranJsonObj[extName] = {
                        value : "",
                        filter : false,
                        isDeletable: true
                    }
                }
            }
        }
    }
})(jQuery);


