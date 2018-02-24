var operate_type = 1;// 1 新增，2 修改
var curPageNum = 0;
var cityList = null;
var regionList = null;
var iGroupPageCount = 50;
var date = new Date();
var yearNow = date.getFullYear();
var lev;

//初始化
$(function () {
    getBatch(function() {
    	getRegionByProvince(initCurrentPage);
    });
});

/**
 * 初始化加页面
 */
function initCurrentPage() {
    //getIGroupPageCount();
    curPageNum = 1;
    // 查询默认条件表单数据
    getLev(loadTableData);
    // loadTableData();
    // mer();
    // 获取省份列表
    // getProvince();
}

//年计划
function loadTableData() {
    // 先销毁表格
    $('#tb').bootstrapTable('destroy');
    // 初始化表格,动态从服务器加载数据
    $("#tb").bootstrapTable({
        method: "post",
        contentType: "application/x-www-form-urlencoded",
        url: "retrieveRegionNormAllotInfo", // 获取数据的地址
        ajaxOptions: {async: false},
        editable: true,//开启编辑模式
        striped: true, // 表格显示条纹
        pagination: true, // 启动分页
        // pageSize : 50, // 每页显示的记录数
        pageSize: iGroupPageCount, // 每页显示的记录数
        pageNumber: curPageNum, // 当前第几页
        minimumCountColumns: 1,  //最少允许的列数
        clickToSelect: true,  //是否启用点击选中行
        pageList : [iGroupPageCount], // 记录数可选列表
        // pageList: [10,25,50], // 记录数可选列表
		smartDisplay:false,
        // showColumns: true, // 开启自定义列显示功能
        search: false, // 是否启用查询
        sidePagination: "server", // 表示服务端请求
        // 设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder
        // 设置为limit可以获取limit, offset, search, sort, order
        queryParamsType: "undefined",
        queryParams: function queryParams(params) { // 设置查询参数
            var param = {
                pageNumber: params.pageNumber,
                pageSize: params.pageSize,
                year: $('#year').val(),
                batch_id: $('#batch').val(),
                region: $("#region").val(),
                status_issue: $("#allot").val()
            };
            return param;
        },
        columns: [{
            checkbox: true
            // rowspan:5
        }, {
            field: 'effective_id',
            title: 'id标识(唯一id标识)',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || !value) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='effective_id' value='" + value + "'>";
            }
        }, {
            field: 'e_id',
            title: 'e_id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || !value) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='e_id' value='" + value + "'>";
            }
        }, {
            field: 'p_year',
            title: '年度',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='p_year' value='" + value + "'>";
            }
        }, {
            field: 'num_order',
            title: '序号',width:'50'
            // formatter: function (value, row, index) {
            //     if (index == null || index == undefined) {
            //         index = 0;
            //     }
            //     return "<input type='text' style='width: 100%' class='form-control' name='num_order' value='" + index + "'>";
            // }
        },
        //     {
        //     field: 'order',
        //     title: '序号',
        //     formatter: function (value, row, index) {
        //         if (index == null || index == undefined) {
        //             index = 0;
        //         }
        //         return index + 1;
        //     }
        // },
            {
            field: 'reg_name',
            title: '地市名称',width:'80'
        }, {
            field: 'reg_id',
            title: '区县id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='reg_id' value='" + value + "'>";
            }
        }, {
            field: 'prv_id',
            title: '省id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='prv_id' value='" + value + "'>";
            }
        },{
            field: 'batch_id',
            title: '批次id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='batch_ids' value='" + value + "'>";
            }
        }, {
            field: 'site_type',
            title: '基站类型id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='site_type' value='" + value + "'>";
            }
        }, {
            field: 'site_name',
            title: '基站类型',
            visible: false
        }, {
            field: 'build_type',
            title: '建设类型id',
            visible: false,
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control' name='build_type' value='" + value + "'>";
            }
        }, {
            field: 'build_name',
            title: '建设类型',
            visible: false
        }, {
            field: 'qty',
            title: '最近分配规模量',
            width: '120',
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<a href='#' onclick='getHistory("+row.e_id+")'>"+value+"</a>";
            }
        }, {
            field: 'qty',
            title: '分配规模量',
            width: '100',
            formatter: function (value, row, index) {
                if (value == null || value == undefined) {
                    value = "";
                }
                return "<input type='text' style='width: 100%' class='form-control'  ori-value='" + value + "' name='qty_uses' value='" + value + "'>";
            }
            // edit: true
        },
        //     {
        //     field: 'p_share_rate',
        //     title: '共享率',
        //     width: '10%',
        //     visible: false
        // }, {
        //     field: 'share_rate',
        //     title: '编辑共享率',
        //     width: '10%',
        //     visible: false,
        //     formatter: function (value, row, index) {
        //         if (value == null || value == undefined) {
        //             value = "";
        //         }
        //         return "<input type='text' style='width: 100%' onblur='changeItValue();' class='form-control' name='share_rates' value='" + value + "'>";
        //     }
        //     // edit: true
        // },
            {
            field: 'status_issue',
            title: '分配状态',
            formatter: function (value, row, index) {
                if (value && (value == 1 || value == '1')){
                    return '已分配';
                }else {
                    return '未分配';
                }
                return value;
            },width:'80'
        }, {
            field: 'final_approve_time',
            title: '最近分配时间',
            formatter:function (value,row,index) {
                if(value){
                    return new Date(value).format('yyyy-MM-dd hh:mm:ss');
                }else {
                    return '';
                }
            },width:'170'
        }, {
            field: 'opertion',
            title: '操作',
            visible: false,
            // rowspan:6,
            formatter: function (value, row, index) {
                // console.log("value="+value)
                return "<button class='btn btn-success'>提交审批并下发</button>";
            },
            events: {
                'click .btn': function (e, value, row, index) {
                    alert('暂未实现');
                    //showExamineModal(row);
                    e.stopPropagation();
                    return false;
                }
            }
        }],
        onLoadSuccess: function (d) { // 加载失败时执行
            //合并单元格
//            mer();
            showPlanInfo();
            initInput();
        },
        onLoadError: function () { // 加载失败时执行
            alertModel("请求失败！");
        },
        responseHandler: function (res) {
        	thWidth($('#tb'));
            if (res != null) {//报错反馈
                if (res.success != "1") {
                    alertModel(res.msg);
                }
                // showTableList = res.obj.result;
            }
            return {
                "total": res.obj.total,//总页数
                "rows": res.obj.result //数据
            };
        }
    });
    // mer();
    showPlanInfo();
    // initInput();
}




function getIGroupPageCount() {
    $.ajax({
        type: "POST",
        url: "getRegionPageCount",
        dataType: 'json',
        data:{reg_id : $('#region').val()},
        async: false,
        contentType: "application/json;charset=UTF-8",
        success: function (value) {
            if (value != null && value.success == '1') {
                iGroupPageCount = value.obj;
            } else {
                iGroupPageCount = 50;
                alertModel(value.msg);
            }
        },
        error: function (data) {
            iGroupPageCount = 50;
            alertModel(data.msg);
        }
    });
}

function showExamineModal() {
    $("#ViewPanel").modal("show");
}

//合并单元格
function mer() {
    // var data = $('#tb').bootstrapTable('getData', true);
    // mergeColumn('#tb', data, 0, 'prv_name');
    // mergeColumn('#tb', data, 'prv_order', 'prv_name');
    // mergeColumn('#tb', data, 'prv_name');
    // mergeColumn('#tb', data, 'site_name', ['prv_name', 'site_name']);
    // mergeColumn('#tb', data, 'build_name', ['prv_name', 'site_name', 'build_name']);
    // mergeColumn('#tb', data, 'p_share_rate', 'prv_name');
    // mergeColumn('#tb', data, 'share_rate', 'prv_name');
    // mergeColumn('#tb', data, 'examine_type', 'prv_name');
    // mergeColumn('#tb', data, 'issued_type', 'prv_name');
    // mergeColumn('#tb', data, 'final_approve_time', 'prv_name');
    // mergeColumn('#tb', data, 'opertion', 'prv_name');
}

function submitAll() {
    var selectContent = new Array();
    var dataArray = new Array();
    var paramArray = new Array();

    selectContent = $('#tb').bootstrapTable('getSelections');
    if(selectContent.length == 0 || !selectContent){
        alertModel("请至少选择一条数据进行操作");
        return false;
    }
    var judgeFlag = false;
    for (var i = 0; i < selectContent.length; i++) {
        var dataJson = {};
        var paramJSON = {};
        //防止取值错乱
        var flagNum =  Number(selectContent[i].num_order) - 1;
        // var claz = $("input[name=qty_uses]:eq(" + flagNum + ")").attr('class');
        // if(claz && claz.toString() == 'form-control valflag'){
            dataJson["effective_id"] = !selectContent[i].effective_id ? "" : selectContent[i].effective_id;
            dataJson["e_id"] = !selectContent[i].e_id ? "" : selectContent[i].e_id;
            dataJson["p_year"] = !selectContent[i].p_year ? "" : selectContent[i].p_year;
            dataJson["reg_id"] = !selectContent[i].reg_id ? "" : selectContent[i].reg_id;
            dataJson["batch_id"] = !selectContent[i].batch_id ? $("#batch").val() : selectContent[i].batch_id;
            dataJson["qty_use"] = !$("input[name=qty_uses]:eq(" + flagNum + ")").val() ? 0 : $("input[name=qty_uses]:eq(" + flagNum + ")").val();
            dataJson["prv_id"] = !selectContent[i].prv_id ? "" : selectContent[i].prv_id;
            dataJson["site_type"] = !selectContent[i].site_type ? "" : selectContent[i].site_type;
            dataJson["build_type"] = !selectContent[i].build_type ? "" : selectContent[i].build_type;

            //后端验证 变化值 zhenTM exin
            var changedVal = !$("input[name=qty_uses]:eq(" + flagNum + ")").val()?0:$("input[name=qty_uses]:eq(" + flagNum + ")").val();
            var originalVal = !$("input[name=qty_uses]:eq(" + flagNum + ")").attr('ori-value')?0:$("input[name=qty_uses]:eq(" + flagNum + ")").attr('ori-value');
            if(!isNaN(Number(changedVal)) && !isNaN(Number(originalVal))){
                paramJSON.changeValue = Number(changedVal) - Number(originalVal);
                paramJSON.batch_id = !selectContent[i].batch_id ? $("#batch").val() : selectContent[i].batch_id;
                paramArray.push(paramJSON);
            }

            //更改提交状态为可提交
            // judgeFlag = true;
            dataArray.push(dataJson);
        // }
    }
    var chooseData = JSON.stringify(dataArray);
    console.log(JSON.stringify(paramArray));
    debugger;
    // if (judgeFlag == true){
        serverValid(JSON.stringify(paramArray),submitData,chooseData,loadTableData,showPlanInfo);
        // submitData(chooseData);
        // loadTableData();
        // showPlanInfo();
    // }
}

//提交数据
function submitData(d) {
    $.ajax({
        type: "POST",
        url: "submitDataForRegionNormAllot",
        data: d,
        dataType: 'json',
        async: false,
        contentType: "application/json;charset=UTF-8",
        success: function (value) {
            if (value != null) {
                alertModel("数据保存成功！");
            } else {
                alertModel('审批出错，请联系系统管理员!');
            }
        },
        error: function (data) {
            alertModel('审批出错，请联系系统管理员!');
        }
    });
}

//

//导出
function exportModel() {
    confirmModel("您确定要导出吗？", "exportModelInfo");
}

function exportModelInfo() {
    // var para = "&year=" + $("#year").val();
    //  para += "&batch_id=" + $("#batch").val();
    // window.open("regionNormAllotExport?" + para, "_blank");
    var param = {
        year: $('#year').val(),
        batch_id: $('#batch').val(),
        region: $("#region").val(),
        status_issue: $("#allot").val()
    }
    window.open("exportModel?" + $.param(param), "_blank");
}


//导出
function exportResourceInfo() {
    confirmModel("您确定要导出吗？", "exportInfo");
}

function exportInfo() {
    // var para = "&year=" + $("#year").val();
    //  para += "&batch_id=" + $("#batch").val();
    // window.open("regionNormAllotExport?" + para, "_blank");
    var param = {
        year: $('#year').val(),
        batch_id: $('#batch').val(),
        region: $("#region").val(),
        status_issue: $("#allot").val()
    }
    window.open("regionNormAllotExport?" + $.param(param), "_blank");
}


/**
 * 导入
 */

function importResourceInfo() {
    //"地市指标分配" 弹出框名称
    //"_regionNormAllotExport" 功能标识
    //"formSubmit" 回调方法
    importModel("地市指标分配", "_regionNormAllotExport", "formSubmit");//弹出导入框
}

/**
 * 上传文件并保存到数据库
 * @param suffix 模块标识
 */
function formSubmit(suffix) {
    $("#dataForm").append('<input type="hidden" name="total" id="total" value="' + Number($('#total').text())+'">');
    var formData = new FormData($("#dataForm")[0]);
    console.log(formData);
    $.ajax({
        url: 'regionNormAllotImport',
        type: 'post',
        data: formData,
        async: true,
        cache: false,
        contentType: false,
        processData: false,
        beforeSend: function () {//启动a
            startTimeJob(suffix);
        },
        success: function (result) {
            stopTimeJob();//停止进度条
            if (result != null && result.success == "1") {
                loadTableData();
                alertModel(result.msg);
            }else {
                alertModel(result.msg);
            }
        },
        complete: function () {//ajax请求完成时执行
            stopTimeJob();//停止进度条
        },
        error: function () {
            stopTimeJob();//停止进度条
            alertModel("请求失败！");
        }
    });
}

//校验
function validNum(that) {
    var v = $(that).val();
    if (isNaN(v)) {
        alertModel("只能填写数字");
        $(that).val("");
    }
    if (v.length > 8) {
        alertModel("填写长度必须小于八位");
        $(that).val("");
    }

    if( Number($('#total').text()) <  Number(countUsed())) {
    	alertModel("待分配指标数量超过了计划的总量！");
        $(that).val("");
    }
}

function showPlanInfo() {
	// var data = {
	// 	orderBatch: $('#batch').val(),
	// 	pageNumber: 1,
	// 	pageSize: 50
	// };
    $.ajax({
        type: "POST",
        url: "getQtySurplusUsed",
        data: {batch_id : $('#batch').val()},
        dataType: 'json',
        async: false,
        // contentType: "application/json;charset=UTF-8",
        success: function (value) {
            if (value != null && value.success == '1') {
                var plan = value.obj;

                // console.log(plan);
                if(plan && plan.qty) {
                    var planSurplus = !plan.surplus?0:plan.surplus;
                        $('#total').text(plan.qty);
                        $('#remaining').text(planSurplus);
                        if(plan.effective_time_end){
                            $('#effectTimeTd').show();
                            $('#effectTime').text(new Date(plan.effective_time_end).format('yyyy-MM-dd')).parent().show();
                        }else {
                            $('#effectTime').text('').parent().hide();
                            $('#effectTimeTd').hide();
                        }
                    } else {
                        $('#total').text(0);
                        $('#remaining').text(0);
                        $('#effectTime').text('').parent().hide();
                        $('#effectTimeTd').hide();
                    }
            } else {
                $('#total').text(0);
                $('#remaining').text(0);
                $('#effectTime').text('').parent().hide();
                $('#effectTimeTd').hide();
            }
        },
        error: function (data) {
            alertModel('数据计算出错，请联系系统管理员!');
        }
    });
	// $.post('queryEffectInfo', data, function(res) {
	// 	if(res && res.data && res.data.length) {
	// 		var plan = res.data[0];
     //        if (plan.p_inital_year_scale && plan.p_inital_year_scale != null && plan.p_inital_year_scale != ""){
     //            $('#total_hide').val(plan.p_inital_year_scale);
     //        }else {
     //            $('#total_hide').val('0');
     //        }
	// 		if(plan.p_inital_year_scale) {
     //            $('#total').text(plan.p_inital_year_scale);
     //            //获取缓存剩余
     //            var remaining = localStorage.iys;
     //            if(!remaining) remaining = plan.p_inital_year_scale - countUsed();
     //            $('#remaining').text(plan.p_inital_year_scale - countUsed());
	// 			$('#effectTime').text(new Date(plan.effective_time_end).format('yyyy-MM-dd')).parent().show();
	// 			//设置缓存 防止刷新丢失
	// 			localStorage.iys = plan.p_inital_year_scale - countUsed();
	// 		} else {
	// 			$('#total').text(0);
	// 			$('#remaining').text(0);
	// 			$('#effectTime').text('').parent().hide();
	// 		}
	// 	}
	// });
}

function countUsed() {
	var sum = 0;
	$('input[name="qty_uses"]').each(function(i,e) {
		var val = parseInt($(e).val());
		if(!isNaN(val)) {
			sum += val;
		}
	});
    // debugger;
    // var tbArr =  $('#tb').bootstrapTable('getData');
    // for (var i in tbArr){
     //    var val = Number(tbArr[i].qty);
     //    if (!isNaN(val)){
     //        sum += val;
     //    }
    // }
	return sum;
}

function getRegionByProvince(fun) {
    var r = $("#region").val();
    // console.log(r);
    $.ajax({
        type: "POST",
        url: "getRegionByProvince",
        // data: {prv_group: r},
        dataType: 'json',
        // async: false,
        // contentType: "application/json;charset=UTF-8",
        success: function (value) {
            if (value != null && value.success == "1") {
                var html = "";
                $("#region").html(html);
                if(value.obj.length > 1){
                    html += "<option value=''>-全部-</option>";
                }
                for (var i in value.obj) {
                    html += "<option value='" + value.obj[i].reg_id + "'>" + value.obj[i].reg_name + "</option>";
                }
                $("#region").append(html);
                fun && fun();
            } else {
                alertModel('获取查询条件错误，请联系系统管理员!');
            }
        },
        error: function (data) {
            alertModel('获取查询条件错误，请联系系统管理员!');
        }
    });
}

//查询批次菜单
function getBatch(fun) {
    var d = {year: $('#year').val()};
    $.ajax({
        type: "POST",
        url: "getBatchForAllot",
        dataType: 'json',
        data: d,
        // async: false,
        // contentType: "application/json;charset=UTF-8",
        success: function (value) {
            if (value != null && value.success == "1") {
                var html = "";
                //清空
                $("#batch").html(html);
                for (var i in value.obj) {
                    html += "<option value='" + value.obj[i].batch_id + "'>" + value.obj[i].batch_name + "</option>";
                }
                $("#batch").append(html);
                fun && fun();
            } else {
                alertModel('获取查询条件错误，请联系系统管理员!');
            }
        },
        error: function (data) {
            alertModel('获取查询条件错误，请联系系统管理员!');
        }
    });
}

function getHistory(h_id){
    // console.log("hid:"+h_id);
    $.ajax({
        type: "POST",
        url: "getHistory",
        dataType: 'json',
        data: {h_id:h_id},
        success: function (value) {
            if (value != null && value.success == "1") {
                var $tbody = $("#historyTbody");
                var data = value.obj;
                
                $tbody.html('');
                for(var i=0;data && i<data.length;i++){
                    var html = "";
                    var qtyValue = !data[i].qty?0:data[i].qty;
                    html = "<tr>" +
                                "<td>"+data[i].user_name+"</td>" +
                                "<td>"+data[i].create_time+"</td>" +
                                "<td>"+ qtyValue +"</td>" +
                            "</tr>";
                    $tbody.append(html);
                }
                showMadal();
            } else {
                alertModel('获取查询条件错误，请联系系统管理员!');
                closeMadal();
            }
        },
        error: function (data) {
            alertModel('获取查询条件错误，请联系系统管理员!');
            closeMadal();
        }
    });
}

//显示模态
function showMadal() {
    $('#ViewPanel').modal("show");
    var w = $(document).width() * 0.5;
    var h = $(document).height() * 0.38;
    //设置模态框的自适应大小
    // $(".modal-dialog").attr("width", w);
    // $("#textareaDiv").attr("style", "height:" + h + "px;overflow:auto;");
}

//关闭模态框
function closeMadal() {
    $("#dataFormModal")[0].reset(); // 清空表单
    $("#ViewPanel").modal('hide');
}

function getLev(func){
    $.ajax({
        type: "POST",
        url: "getScaleLevel",
        dataType: 'json',
        success: function (value) {
            if (value != null && value.success == "1") {
                lev = value.obj.lev;
                if(lev && !isNaN(Number(lev)) && Number(lev) == 2){
                    $('.btn-toolbar').show();
                    $('#queryIt').show();
                    func();
                }else {
                    $('.btn-toolbar').hide();
                    $('#queryIt').hide();
                }
            } else {
            }
        },
        error: function (data) {
            alertModel(data.msg);
        }
    });
}
// //绑定onchange事件
// $('#region').change(function(){
//     getProvinceByGroup();
// })

function initInput() {
    $('input[name="qty_uses"]').click(function(e) {
        e.stopPropagation();
        return false;
    }).on('blur',function() {
        validNum(this);
        redthis(this);
    });
}

//校验
function redthis(that) {
    var v = !$(that).val()?0:$(that).val();
    var ori = !$(that).attr('ori-value')?0:$(that).attr('ori-value');
    // 当值变化后红显
    if(ori === null || ori === '' || ori === undefined) {
        ori = '0';
    }
    if(ori - v) {
        $(that).addClass('valflag');
    } else {
        $(that).removeClass('valflag');
    }
}

function serverValid(param,fun1,submitJSON,fun2,fun3) {
    $.ajax({
        type: "POST",
        url: "serverValid",
        data: param,
        contentType : 'application/json;charset=utf-8',
        dataType: 'json',
        success: function (value) {
            if (value != null && value.success == "1") {
                fun1(submitJSON);
                fun2();
                fun3();
            } else {
                alertModel(value.msg);
            }
        },
        error: function (data) {
            alertModel(data.msg);
        }
    });
}