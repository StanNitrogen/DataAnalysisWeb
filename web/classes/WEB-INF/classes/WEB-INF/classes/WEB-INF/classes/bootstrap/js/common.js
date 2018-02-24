/**---------------
 * 对IE8和9的placeholder兼容
*/
(function(){
	if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE8.0")
	{
		$('input, textarea').placeholder();
	}
	else if(navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion .split(";")[1].replace(/[ ]/g,"")=="MSIE9.0")
	{
		$('input, textarea').placeholder();
	}
})()
/**---------------
 * 点击菜单以外影藏
 *
 */

// $(document).on('click',function(e){
// 		if($(document).find('body').hasClass('index')==false){
// 		 $('.menu-item',parent.document).removeClass('active');
// 	      $('.menu-item-child',parent.document).removeClass('menu-open').css('display','none');
// 	      var otherImg=$('.menu-item',parent.document);
// 	      for(var i=0;i<otherImg.length;i++){
// 	          var curImg=$(otherImg.eq(i).find('img').get(0)).attr('src').split("image/")[1];
// 	          if(curImg.indexOf("-active")==-1){
// 	              var newImg=curImg.split('.')[0];
// 	          }else{
// 	              var newImg=curImg.split('-active.')[0];
// 	          }
// 	          $(otherImg.eq(i).find('img').get(0)).attr('src',""+projectName+"/asserts/image/"+newImg+".png");
// 	          otherImg.eq(i).removeClass('active');
// 	          $(otherImg.eq(i).find('.menu-item-child').get(0)).removeClass(' menu-open').hide();
// 	      }
// 		}
//
// 	});
/**---------------
 * ztree树：class=ztree
 * 样式
 */
$(document).ready(function(){
	var winHeight=0,winWidth=0;
	// 获取窗口宽度
	if (window.innerWidth)
		winWidth = window.innerWidth;
	else if ((document.body) && (document.body.clientWidth))
		winWidth = document.body.clientWidth;
	// 获取窗口高度
	if (window.innerHeight)
		winHeight = window.innerHeight;
	else if ((document.body) && (document.body.clientHeight))
		winHeight = document.body.clientHeight;
	// 通过深入 Document 内部对 body 进行检测，获取窗口大小
	if (document.documentElement && document.documentElement.clientHeight && document.documentElement.clientWidth)
	{
		winHeight = document.documentElement.clientHeight;
		winWidth = document.documentElement.clientWidth;
	}
	$('ul.ztree').height(winHeight-20+"px");
	$('ul.ztree').css("overflow-y","scroll");


	/**
	 * 设置未来(全局)的AJAX请求默认选项
	 * 主要设置了AJAX请求遇到Session过期的情况
	 */
//	alert();
//	$.ajaxSetup({
//	    complete: function(xhr,status) {
//	        var sessionStatus = xhr.getResponseHeader('sessionstatus');
//	        alert(sessionStatus);
//	        if(sessionStatus == 'timeout') {
//	            var top = getTopWinow();
//	            var yes = confirm('由于您长时间没有操作, session已过期, 请重新登录.');
//	            if (yes) {
//	                top.location.href = '/skynk/index.html';           
//	            }
//	        }
//	    }
//	});
//	/**
//	 * 在页面中任何嵌套层次的窗口中获取顶层窗口
//	 * @return 当前页面的顶层窗口对象
//	 */
//	function getTopWinow(){
//	    var p = window;
//	    while(p != p.parent){
//	        p = p.parent;
//	    }
//	    return p;
//	}
});

// //获取当前路路径
// var pathName = window.document.location.pathname;
// //获取带"/"的项目名，如：/NCMS
// var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

//获取当前网址，如： http://localhost:8083/myproj/view/my.jsp
var curWwwPath=window.document.location.href;

//获取主机地址之后的目录，如： myproj/view/my.jsp
var pathName=window.document.location.pathname;
var pos=curWwwPath.indexOf(pathName);

//获取主机地址，如： http://localhost:8083
var localhostPaht=curWwwPath.substring(0,pos);

//获取带"/"的项目名，如：/myproj
var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);

//得到了 http://localhost:8083/myproj
var realPath=localhostPaht+projectName;
/**---------------
 * 分页
 */

var curPageNum = 1;//当前页码：1
var ipageCount = 10;//每页条数


/**---------------
 * 全局函数：
 */
/*设置cookie*/
function setCookie(name, value, days) {
	if (days == null || days == '' || days == undefined) {
		days = 300;
	}
	var exp = new Date();
	exp.setTime(exp.getTime() + days * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + "; path=/;expires="
			+ exp.toGMTString();
}

/* 获取cookie */
function getCookie(name){
	var str = "(^| )"+name+"=([^;]*)(;|$)";
	var arr,reg=new RegExp(str);
    return (arr=document.cookie.match(reg))?unescape(arr[2]).substring(1,unescape(arr[2]).length - 1):null;
}
/* 根据key排序json */
function sortByKey(array, key) {
	return array.sort(function(a, b) {
		var x = a[key];
		var y = b[key];
		return ((x < y) ? -1 : ((x > y) ? 1 : 0));
	});
}

/* 判断元素stringToSearch是否在array中 */
function in_array(stringToSearch, arrayToSearch) {
	for (s = 0; s < arrayToSearch.length; s++) {
		thisEntry = arrayToSearch[s].toString();
		if (thisEntry == stringToSearch) {
			return true;
		}
	}
	return false;
}

//js取出url中的参数
function getParameter(sProp) {
    var re = new RegExp(sProp + "=([^\&]*)", "i");
    var a = re.exec(unescape(document.location.search));
    if (a == null)
        return null;
    return decodeURI(a[1]);
};

/**
 * 获取点击checkbox个数
 */
var rowschecked = new Array();
function isCheckedLessOne(){
	var checkNum = 0;
	rowschecked = new Array();
	var checklist = document.getElementsByName("checkbox");
	for(var i=0;i<checklist.length;i++)
    {
		// 已选中可操作行
	    if(checklist[i].checked == 1){
	    	checkNum ++;
	    	rowschecked.push(showTableList[i]);
	    }
    }
	return checkNum;
}
function isChecked(){
	var checkNum = 0;
	rowschecked = new Array();
	var checklist = $("#tb tbody input[type='checkbox']");
	for(var i=0;i<checklist.length;i++)
    {
		// 已选中可操作行
	    if(checklist[i].checked == 1){
	    	checkNum ++;
	    	rowschecked.push(showTableList[i]);
	    }
    }
	return checkNum;
}

/**---------------*/
//操作提示框
/**
 * 用法：alertModel('你需要的文本写这里');
 * */
var idn = 0;
function alertModel(message,func){
	idn = idn+1;
	var html='';
	html+='<div class="modal fade" id="alertModel'+ idn +'" tabindex="-1" role="dialog">';
	html+='<div class="modal-dialog" role="document">';
	html+='<div class="modal-content">';
	html+='<div class="modal-header">';
	html+='<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
	html+='<h4>系统提示</h4>';
	html+='</div>';
	html+='<div class="modal-body">';
	html+='<p>'+ message +'</p >';
	html+='</div>';
	html+='<div class="modal-footer">';
	html+='<button type="button" id="closeModel'+idn+'" data-dismiss="modal" class="btn btn-default">关闭</button>';
	html+='</div>';
	html+='</div>';
	html+='</div>';
	html+='</div>';
	$('body').append(html);
	$("#closeModel"+idn).bind("click",function(){
		$('#alertModel'+ idn ).modal('hide');
		if(func){
			 return func();
		}
	});
	$('#alertModel'+ idn ).modal('show');
}
/**---------------*/
//确认提示框
/**
* 用法：confirmModel('您要删除吗','confirmOk');
* function confirmOk(){};
* */
var ids = 0;
function confirmModel(message, func){
	ids = ids+1;
	var str='';
	str+='<div class="modal fade delModel" id="delModel'+ ids +'">';
	str+='<div class="modal-dialog">';
	str+='<div class="modal-content message_align">';
	str+='<div class="modal-header">';
	str+='<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>';
	str+='<h4 class="modal-title">提示信息</h4>';
	str+='</div>';
	str+='<div class="modal-body">';
	str+='<p>'+message+'</p>';
	str+='</div>';
	str+='<div class="modal-footer">';
	str+='<input type="hidden" id="url" value="true"/>';
	str+='<button type="button" class="btn btn-default" data-dismiss="modal" style="margin-top:4px;">取消</button>';
	str+='<button onclick="'+func+'()" class="btn btn-success" data-dismiss="modal">确定</button>';
	str+='</div>';
	str+='</div>';
	str+='</div>';
	str+='</div>';
	$('body').append(str);
	$('#delModel'+ ids).modal('show');
}
/**
 * 导入弹出框
 * @param title 弹出框名称()
 * @param suffix 模块标记
 * @param func 保存调用的方法名称
 * @param isExportTemple 是否导出模板 false:不显示（默认），true：显示
 * @returns 用法：importModel("塔维侧账单导入","formSubmit")
 *
 * @author zhujj
 */
function importModel(title,suffix,func,isExportTemple){
	
	if($('#importModel').length) {
		$('#importModel').remove();
	}
	//导入弹出页面
	var str='';
	str+='<div id="importModel" class="modal hide fade importModel" tabindex="-1">';
		str+='<div class="modal-dialog" role="document">';
			str+='<div class="modal-content">';
				str+='<div class="modal-header">';
					str+='<button class="close" type="button" data-dismiss="modal">×</button>';
					str+='<h4 id="myModalLabel">'+title+'</h4>';
				str+='</div>';
				str+='<div class="modal-body">';
					str+='<form class="form-horizontal" id="dataForm" action="importFile" method="post" enctype="multipart/form-data">';
						str+='<div class="form-group">';
							str+='<input type="file" multiple="multiple" accept=".XLS,.xlsx" id="file" name="file" />';
							str+='<input type="hidden" name="suffix" value="'+suffix+'"/>';
						str+='</div>';
						if(isExportTemple){//判断是否显示模板
							str+='<div class="form-group">';
								str+='<a href="exportTemplate">下载导入模板</a>';
							str+='</div>';
						}
					str+='</form>';
				str+='</div>';
				str+='<div class="modal-footer">';
					str+='<a href="#" class="btn" data-dismiss="modal">关闭</a>';
					str+='<a href="#" class="btn btn-primary" onclick="'+func+'(\''+suffix+'\');">保存</a>';
				str+='</div>';
			str+='</div>';
		str+='</div>';
	str+='</div>';
	$('body').append(str);
	$('#importModel').modal('show');
}
/**
 * 进度条遮盖
 *
 *	$('.progress-width').css('width',importProcess+'%');//设置进度条数量
 *	$(".progress-num").text(importProcess + "%");//设置显示的百分比
 *	$(".modal_message").html(importProcessMsg);//设置进度提示信息
 * @returns
 */
function processModel(){
//	<!-- 进度条模态框 -->
	var str='';
	str+='<div id="processModal" class="modal fade processModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">';
		str+='<div class="modal-dialog">';
			str+='<div class="modal-content">';
				str+='<div class="modal-body">';
					str+='<div>';
						str+='<h5 class="modal_message" style="padding-left:25px;height:18px;background:url(../../../image/loading-s.gif) no-repeat;">正在初始化...</h5>';
					str+='</div>';
					str+='<div class="progress">';
						str+='<div class="progress-bar progress-width" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width:3%;"><span class="progress-num">3%</span></div>';
					str+='</div>';
				str+='</div>';
			str+='</div>';
		str+='</div>';
	str+='</div>';

	$('body').append(str);
	$('#processModal').modal('show');
}
//封装ajax
/**
 * 	用法：
 * 	myajax.path({url:"",type:"post",data:{},success:function(data){写你的执行代码}});//post请求
 * 	myajax.path({url:"",data:{},success:function(data){写你的执行代码}});//get请求
 * */
var myajax = myajax || (function(){
    function Pack(){}
    Pack.prototype.json_ajax=function(obj){
        var me=this;
        $.ajax({
            url:obj.url,
            type:obj.type,
            data:obj.data,
            async:true,//异步
            success:obj.success,
            statusCode: {
            	404: obj.error,
            }
        })
    };
    Pack.prototype.jsonp_ajax=function(obj){
        var me=this;
        $.ajax({
            url:obj.url,
            type:"get",
            async:true,//异步
            crossDomain:true,//是否跨域
            dataType:"jsonp",
            jsonp:"callback",//这里定义了callback的参数名称，以便服务获取callback的函数名即getMessage
            jsonpCallback:"flightHandler",//这里定义了jsonp的回调函数
            success:obj.success,
            statusCode: {
            	404: obj.error,
            }
        })
    };
    Pack.prototype.path=function(obj){
        var me=this;
        if(obj.jsonp)me.jsonp_ajax(obj);
        else me.json_ajax(obj);
    };
    return new Pack();
})();

/**
 * 时间对象的格式化;
 * 调用方法：new Date(value).format("yyyy-MM-dd hh:mm:ss");
 */
Date.prototype.format = function(format){
    /*
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */

    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
    // millisecond
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
                .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                    : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}


/**
 * 获取用户所有权限 的地市 区县信息
 * @param title 名称 例如：供应商地市，传入title为供应商
 * @param async 同步异步设置,默认：false-异步;true-同步。不传则默认异步
 * @param CitySelect 选中地市 例如：回显选中，传入地市ID，不传则不选中
 * @param VillageSelect 选中县区 例如：回显选中，传入区县ID，不传则不选中
 */
function getAddress(title,async,citySelect,villageSelect){
	$.ajax({
		type : "post",
		url :projectName+"/rent/common/getAddressRent",
		dataType : 'json',
		contentType : "application/json;charset=UTF-8",
		async:(async?true:false),
		success : function(value) {
			if(value != null){
				sysReguins = value.obj;
				if(sysReguins!=null){
					$('#city').empty();
					$('#village').empty();
					var strCity = "<option value=''>--选择"+(title?title:"")+"地市--</option>";
					$.each(sysReguins, function (i, item){
						strCity += "<option value='" +item.regId+"' "+(citySelect&&citySelect==item.regId?"selected='true'":"")+">"+item.regName+ "</option>";
					});

					$("#city").append(strCity);

					var strReg = "<option value=''>--选择"+(title?title:"")+"区县--</option>";
					$("#village").append(strReg);
					//绑定联动事件 修改人zhujj
					$("#city").change(function(){
						$("#village").empty();
						strReg = "<option value=''>--选择"+(title?title:"")+"区县--</option>";
						if($("#city").val()!=""){
							$.each(sysReguins, function (i, item){
								if(item.regId==$("#city").val()){
									$.each(item.child, function (j, itemchild){
										strReg += "<option value='" + itemchild.regId+"' "+(villageSelect&&villageSelect==itemchild.regId?"selected='true'":"")+">"+itemchild.regName+ "</option>";
									});
								}
							});
						}
						$("#village").append(strReg);
					});

				}
			}
		},
		error : function(data) {
			alertModel('获取用户地区信息失败!');
		}
	});
}
//getAddress();

function getProvince(){
	$.ajax({
		type : "post",
		url :projectName+"/asserts/tpl/system/parameter/queryAllProvince",
		dataType : 'json',
		data : {
			count:10
		},
		contentType : "application/json;charset=UTF-8",
		success : function(back) {
			if (back != null) {
 				if(back.success=="1"){
 					var province = back.Obj;
 					list = province;
					if(list.length==1){
						$("#provinceChoice").append("<option value=" + list[0].prvId + ">" + list[0].prvName + "</option>");
						$("#provinceChoicePop").append("<option value=" + list[0].prvId + ">" + list[0].prvName + "</option>");
					}else{
						$("#provinceChoice").append("<option value=''>" +'--请选择--'+ "</option>");
						$("#provinceChoicePop").append("<option value=''>" +'--请选择--'+ "</option>");
						for (var i = 0; i < list.length; i++) {
							$("#provinceChoice").append("<option value=" + list[i].prvId + ">" + list[i].prvName + "</option>");
							$("#provinceChoicePop").append("<option value=''>" +'--请选择--'+ "</option>");
						}
					}
 				}else{
					alertModel(back.msg);
 				}
 			}
		},
		error : function(data) {
			alertModel('获取用户地区信息失败!');
		}
	});
}


/**
 * 查询数据字典
 * @param dictionaryCode 为查询编码
 * return dictionaryObject 查询对象
 */
function  queryDictType(dictionaryCode){
	var dictionaryObject=null;
	$.ajax({
		url : projectName+'/rent/common/queryDictionaryByCode',
		data : {
			dictgroup_code : dictionaryCode
		},
		type : 'post',
		dataType : 'json',
		async:false,
		success : function(back) {
			if (back != null) {
				if(back.success=="1"){
					dictionaryObject = back.Obj;
				}
			}
		},
		error : function() {
			alert("请求异常");
		}
	})
	return dictionaryObject;
}

/**
 * 动态设置 select的内容
 * 调用方法 appendSelect("paymentMethod",paymenyMethod,"dict_id","dict_name","","付款方式");
 * @param selectId select标签ID（必填）
 * @param item 数据集合
 * @param valueCol 取值字段
 * @param textCol 取显示字段
 * @param id 默认选中id
 * @param title 默认显示名称 请选中+title
 * @returns
 */
function appendSelect(selectId,item,valueCol,textCol,id,title){
	$('#'+selectId).empty();//删除之前的数据
	var s = '';
	if(title!=null&&title!=""){
		$("#"+selectId).append("<option value=''>" +'--请选择'+title+'--'+ "</option>");
	}
	$.each(item,function(i,v){

		$("#"+selectId).append("<option value='"+v[valueCol]+"' "+(id==v[valueCol]?"selected='selected'":"")+">" +v[textCol]+ "</option>");
	});
}


/**************************进度条后台控制开始*******************************/


//启动轮询，初始化进度条
/**
 * suffix 标识模块，防止Session名重复
 */

function startTimeJob(suffix){

	//**每次上传重新设置
	importProcess="3";
	importProcessMsg="正在初始化...";

	$('.progress-width').css('width',importProcess+'%');//设置进度条数量
	$(".progress-num").text(importProcess + "%");//设置显示的百分比
	$(".modal_message").html(importProcessMsg);//设置进度提示信息
	processModel();//显示进度条
	$('#importModel').modal("hide");
	getProcess(true,suffix);
}
//停止定时器
function stopTimeJob(){
	clearTimeout(t);
	$("#processModal").modal("hide");
}

var importProcess="";
var importProcessMsg="";
var t;
//获取进度
function getProcess(isStart,suffix){
	$.ajax({
		url:'/asserts/tpl/queryProcessInfo',
		type : 'post',
		data:{"isStart":isStart,"suffix":suffix},//标记是那个模块的进度条
		dataType:"json",
	    async: true,
     	cache: false,
 		success : function(result){
 			if(result != null&&result.success=="1"){

 				importProcess=result.obj.importProcess;
 				importProcessMsg=result.obj.importProcessMsg;
 				if(importProcess!=null&&importProcessMsg!=null){
 					updateProcess();//更新进度
 				}else{

 				}
 				if(!importProcess||importProcess<100){//没有完成就轮询
 					t=setTimeout("getProcess(false,'"+suffix+"')",2000);//停两秒在调用
 				}
 			}
 		},
 		error : function() {
 			//stopTimeJob();//停止定时器
			console.log("导入获取进度失败，请重试！");
 		}
	});

}
//更新进度条
function updateProcess(){
	$('.progress-width').css('width',importProcess+'%');//设置进度条数量
	$(".progress-num").text(importProcess + "%");//设置显示的百分比
	$(".modal_message").html(importProcessMsg);//设置进度提示信息

}

/**************************进度条后台控制结束*******************************/

/**************************文件上传开始***********************************/
/**
 * 导入弹出框
 * @param ID 上传按钮主键，为了区分一个页面多个方式的上传
 * @param suffix 模块标记
 * @param func 上传成功回调方法
 * @param url 后台方法，默认使用公用上传方法
 * @param func 回调方法
 * @returns 用法：webUploadInit("数据收集","successBack",projcetName+"asserts/tpl/common/webupload/uploadFile")
 * @author zhujj
 */

function webUploadInit(id,suffix,url,func){
	var str='<div id="'+id+'" class="hide">文件上传</div>';
	$('body').append(str);
	url=url?url:projectName+"/asserts/tpl/common/webupload/uploadFile";//默认调用公用方法
	var uploader = WebUploader.create({
	    swf:projectName+"/asserts/webuploader/Uploader.swf",
	    server:url,
	    pick:"#"+id,
	    auto:true,
	    duplicate :true
   });
	// 文件上传过程中显示加载中的。
   /*uploader.on( 'uploadProgress', function( file, percentage) {

   });*/
   //上传成功
   uploader.on('uploadSuccess', function(file, json) {
	   func(file, json);//上传成共回调
   });
   //销毁重启
   /*uploader.on("uploadFinished", function () {
       uploader.destroy();
       webUploadInit(id,suffix,url,func);
   });*/
   return uploader;
}
/**
 * id 控件id
 * webUploader 上传控件对象
 * json 需要上传同时传递的参数
 * 触发
 */
function webUploadClick(id,webUploader,json){
	/**
	 * 设置上传需要的参数
	 */
	if(webUploader&&json){//限制不传递参数
		webUploader.options.formData = json;
//		webUploader.on('uploadBeforeSend',function(object,data,header){
//			alert(data)
//			data=$.extend(data,json);
//		});
	}
	$("#"+id+" .webuploader-element-invisible").click();
}
/**************************文件上传结束***********************************/

function getSelectItem(type){
	var selectItems = $('#tb').bootstrapTable('getSelections');
	if(type == 1){
		if(selectItems.length != 1){
			alertModel("请选择一条数据");
			return null;
		}
	}else{
		if(selectItems.length < 1){
			alertModel("请选择至少一条数据");
			return null;
		}
	}
	return selectItems;
}

//记录数据字典数据Map
var dictionaryMap  = null;
/**
 * 加载需要的数据字典类型的数据
 * @returns
 */
function loadDictionary(codes,callback){
	var param ={
		codes:codes
	}
	$.ajax({
 		url : projectName+'/asserts/tpl/system/dictionary/queryDictionarysByCodes',
 		data : param,
 		type : 'get',
		cache : false,
 		dataType : 'json',
		contentType: "application/json;charset=utf-8",
 		success : function(res) {
 			if (res != null) {
 				dictionaryMap = res.obj;
 				callback(res.obj);
 			}
 		},
 		error : function() {
			alertModel("加载需要的数据字典请求失败！");
 		}
 	});
}
/**
 * 单个下拉选项 设置数据字典内容
 * @param id
 * @param code
 * @returns
 */
function dictionaryOption(id,code){
	var opts="";
	for(var key in dictionaryMap[code]){
		var f_unit_num  = key;
		opts+='<option value='+ f_unit_num +'>'+ dictionaryMap[code][key] +'</option>';
	}
	$("#"+id).append(opts);
}

/**
 * [dictionaryOptions 多组动态添加下拉选项]
 * @param  {[type]} paramsArr 参数形如[{key:value},{id:name},……]
 * @return {[type]}           
 */
function dictionaryOptions(paramsArr){
	$.each(paramsArr, function(index, el) {
		for ( k in el ) {
			var opts = "";
			for(var key in dictionaryMap[el[k]]){
				var f_unit_num = key;
				opts+='<option value='+ f_unit_num +'>'+ dictionaryMap[el[k]][key] +'</option>';
			}
			$("#"+k).append(opts);
		}
	});
}
/**
 * [dictionaryOptions 多组动态添加下拉选项]
 * @param  {[type]} paramsArr 参数形如[{key:value},{id:name},……]
 * @param  {[type]} processState 阶段状态，需求阶段>=100 <200，站址筛查>=200 < 300，订单确认>=300 < 400，验收确认>=500 < 600，起租确认>=600 < 700
 * @return {[type]}           
 */
function dictionaryOptionsByProcessState(paramsArr, processState){
	var process = parseInt(processState / 100);
	$.each(paramsArr, function(index, el) {
		for ( k in el ) {
			var opts = "";
			for(var key in dictionaryMap[el[k]]){
				// 已归档订单状态要加载，不与阶段进行对比
				if(key == 1000 || parseInt(parseInt(key) / 100) == process){
					var f_unit_num = key;
					opts += '<option value=' + f_unit_num + '>' + dictionaryMap[el[k]][key] + '</option>';
				}
			}
			$("#" + k).append(opts);
		}
	});
}
/**
 * 单列数据字典获取 对应名字
 * @param code
 * @param value
 * @param defaultName 
 * @returns
 */
function dictionarySingleColumn(code,value,defaultName){
	var name = defaultName;
	for(var key in dictionaryMap[code]){
		if(key == value){
			name = dictionaryMap[code][key];
			return name;
		}
	}
	return name;
}
//省份、地市、区县集合
var prvRegCoun = null;
function loadPrvRegCoun(callback) {
		$.ajax({
			url: projectName + '/asserts/tpl/propose/tower/queryRegion',
			dataType: 'json',
			success : function(res) {
	 			if (res != null) {
	 				callback(res.obj);
	 			}
	 		},
	 		error : function() {
				alertModel("加载省份、地市、区县失败！");
	 		}
		});
}

function getPrvRegCounById(id,defaultName){
	var name = defaultName;
	for(var key in prvRegCoun){
		if(key == id){
			name = prvRegCoun[key];
			return name;
		}
	}
	return name;
}

commonPBatchMap = {};
commonPBatchList = null;
function getPBantchByYear(year,callback,id,status){//id为加载批次时select元素的id（配合函数createBantchByYear使用）
	$.ajax({
	    url:projectName+'/asserts/tpl/songtech/yearplan/getBatch',
	    data: {
	    	year:year,
	    	isValid:1
	    },
 		type : 'post',
	    cache:false,
	    success:function(result){
	        //请求成功时
	    	if(result!=null && result.success=="1"){
	    		commonPBatchList = result.Obj;
	    		if(commonPBatchList) {
	    			for(var i in commonPBatchList) {
	    				var row = commonPBatchList[i];
	    				commonPBatchMap[row.batch_id] = row.batch_name
	    					+ (row.is_valid? '': ' (已停用)');
		    		}
	    		}
	    		
	    		if(!year) {
	    			commonPBatchList = [];
	    		}
	    		
	    		if(id == undefined || id == null || id == ""){
	    			callback && callback(commonPBatchMap);
	    		}else{
	    			callback && callback(id,status);
	    		}
	    	}
	    },
	    error:function(){
	    	alertModel("获取"+year+"年度项目批次计划失败！");
	    }
	});
}

/*  Abel
 	PBatchPlan.js 中有相同功能且实际使用的函数 但由于在公共js中 暂不做处理
 */
//加载批次
function createBantchByYear(id,status){
	$("#"+id).html('');
	var opts = "";
    if(status == true) opts += "<option id='chooseAllOption' value=''>-项目批次-</option>";
	for(var i in commonPBatchList){
		var o = commonPBatchList[i];
		opts+='<option value='+ o.batch_id +'>'+ o.batch_name + (o.is_valid? '': ' (已停用)') +'</option>';
	}
	$("#"+id).append(opts);
	//如果该年批次为空，就提示用户。
	if($("#"+id).val() == null){
//		alertModel('该年份下没有计划批次！'); // 弹出表单
	}

	if(status == true){
	    if($("#"+id).find('option').length < 2){
            var $y = $('.input-group-addon');
            if ($.trim($y.text().toString()) == '年' && isBlank(!$y.siblings().val()) ) {
        		console.log(2);
                alertModel('该年份下没有计划批次！');
                $y.siblings().val(undefined);
            }
        }

		var vl = "";
		$("#"+id).find('option').each(function (i,el) {
			if ($(el).val() && $(el).val() != ""){
				vl += $(el).val();
				if(i < $("#"+id+"").find('option').length - 1){
					vl += ',';
				}
			}
		})
		$("#"+id+" option[value='']").val(vl);
    }else {
        if($("#"+id).find('option').length == 0){
            var $y = $('.input-group-addon');
            if ($.trim($y.text().toString()) == '年' && isBlank(!$y.siblings().val())) {
                alertModel('该年份下没有计划批次！');
                $y.siblings().val(undefined);
            }
        }
    }
    // $("#"+id+" option:first").prop('selected', 'selected');
	$("#"+id).blur();
}

// bootstrap按钮式下拉菜单互动js
$(function() {
	$('.input-group-btn > .dropdown-menu > li').click(function() {
		var menu = $(this).parent(),
			component = menu.parent(),
			btn = menu.prev(),
			option = $(this).find('a'),
			bindInput = component.next();
		
		btn.html(option.text() + ' <span class="caret"></span>');
		
		if(!bindInput.length) {
			bindInput = component.prev();
		}
		bindInput.attr('name', option.attr('name'));
	});
	$('.input-group-btn > .dropdown-menu > li:first').click();
});

function isBlank(obj){
    return(!obj || $.trim(obj) === "");
};

/**
 * 年限范围1970~当前年份20年后(含上下限)
 * @author Abel
 * @param year 	传入需验证的年份
 * @returns		正常范围内的年份
 */
function ruleYear(year) {
	var year = +year;
	if ( !year || !(year+1 > 0) ) return;
	var currentYear = +new Date().getFullYear();
	if (year-1700 < 0 || year-currentYear-20 > 0 ) {
		year = currentYear;
	}
	return year;
}
