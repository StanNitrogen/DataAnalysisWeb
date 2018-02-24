var statusEnum = {'0': '需求', '1': '筛查', '2': '订单', '3': '交付', '4': '起租'};

function consistency(attach_id) {
    // showMadal();
    $.ajax({
        type: "POST",
        url: "/servlet/DetailQueryServlet",
        data: {id: attach_id},
        dataType: "json",
        // contentType: 'application/json',
        success: function (data) {
            if (data) {
                console.log(data);
                var requireSum = 1;
                var str = "";
                for (var i = 0; i < data.length; i++) {

                    var obj = data[i];
                    if (i == 0){
                        requireSum = parseInt(obj.total);
                    }

                    str += statusEnum[obj.status] + '错误数:<font style="color: red">' + obj.consistence + '</font>,错误率:<font style="color: red">' + (parseInt(obj.consistence)/requireSum).toFixed(4) + '%</font>';
                    if (i < data.length - 1){
                        str += ',<br>';
                    }
                }
                console.log(str);
                $("#texta").html(str);
                showMadal();
            }
        },
        error: function (data) {
            alert("网络错误！");
        }

    });
}

function downloadIt(path) {
    window.open(path, "_blank");
}

function analysis(path) {
    // alert(path);
    // document.form1.action = path;
    // // form.action = path;
    // document.form1.submit();

    $('#myModal').modal('show');
    var w = $(document).width();
    var h = $(document).height() * 0.38;
}

//显示模态
function showMadal() {
    $('#ViewPanel').modal("show");
    var w = $(document).width();
    var h = $(document).height() * 0.38;
    //设置模态框的自适应大小
    // $("#texta").attr("width", w);
    if ($("#texta"))
        $("#texta").attr("style", "height:" + h + "px;overflow:auto;");
}

//关闭模态框
function closeMadal() {
    $("#dataFormModal")[0].reset(); // 清空表单
    $("#ViewPanel").modal('hide');
}

function query(path) {
    document.form1.action = path;
    document.form1.submit();
}

function gragh(attach_id) {
    window.location.href='/jsp/gragh.jsp?id='+attach_id;
}