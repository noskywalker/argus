var hanMesg;
$(function () {
    fetchDataAndInsert('', '', '', 1, 10);

})

function insertIntoTable(array) {
    for (var i = 0; i < array.length && i < 10; i++) {
        insertOneRow(array[i], i + 1);
    }
}

function insertOneRow(row, i) {
    var name = "authName" + i;
    var type = "authType" + i;
    row.authType = row.authType == 0 ? "菜单" : "操作";
    var parentId = "parentId" + i;
    var parentName = "parentName" + i;
    var rowId = "row" + i;
    var status = "status" + i;
    var searchTag = "#" + i;
    var edit = "e" + i;
    var isUsed = row.enable == 0 ? "禁用" : "启用";
    row.enable = row.enable == 0 ? "可用" : "禁用";

    var trHtml = "<tr><td>" + i + "</td><td id=" + name + ">" + row.authName + "</td><td style=\"display:none;\" id=" + rowId + ">" + row.id + "</td><td id=" + type + ">" + row.authType + "</td><td style=\"display:none;\" id=" + parentId + ">" + row.parentId + "</td>" + "<td id=" + parentName + ">" + row.parentName + "</td><td>" + row.createTime + "</td><td>" + row.updateTime + "</td><td id=" + status + ">" + row.enable + "</td><td><a href='javascript:void(0);' id=" + edit + ">编辑权限</a>&nbsp;&nbsp;<button class='btn btn-info' id=" + i + ">" + isUsed + "</button></td></tr>"

    $("#dataTables-user").append(trHtml);
    $(searchTag).on('click', function () {
        event.preventDefault();
        onIsUsedClick(this.id);
    });
    $("#" + edit).on('click', function () {
        event.preventDefault();
        onEditClick(i);
    });
}

function onIsUsedClick(id) {
    var authName = $("#authName" + id).html();
    var authType = $("#authType" + id).html();
    var parentId = $("#parentId" + id).html();
    var parentName = $("#parentName" + id).html();
    var rowId = $("#row" + id).html();
    var enable = $("#" + id).html() == "禁用" ? 1 : 0;
    console.log("authName:" + authName + "  enable:" + enable + "   authType:" + authType + "  parentId:" + parentId + " id:" + rowId + " parentName:" + parentName);
    $.ajax({
        url: '/system/auth/edit',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        data: {authName: authName, enable: enable, authId: rowId}
    })
        .done(function (msg) {
            console.log(msg);
            if (msg.success) {
                var str = $("#" + id).html() == "禁用" ? "启用" : "禁用";
                var str1 = $("#status" + id).html() == "可用" ? "禁用" : "可用";

                $("#" + id).html(str);
                $("#status" + id).html(str1);
            } else {
                console.log("参数出错了");
            }
        })
        .fail(function () {
            console.log("接口调用出错");
        })
        .always(function () {
            console.log("complete");
        });

}

function check() {
    if ($("#paging").html() == 1) {
        $("#previous").css('visibility', 'hidden');
    } else {
        $("#previous").css('visibility', 'visible');
    }
}

function onPreviousClick() {
    var authName = $("#hanName").val();
    var authType = $("#hanAuthType").val();
    var status = $("#hanStatus").val();
    var page = $("#paging").html();

    page--;

    // if(status == "全部"){
    //     status = "";
    // }else if(status == "可用"){
    //     status = 0;
    // }else{
    //     status = 1;
    // }

    if (authName.length == 0) {
        fetchDataAndInsert('', '', status, page, 10);
    } else {
        fetchDataAndInsert(authName, authType, status, page, 10);
    }
}

function onNextClick() {
    var authName = $("#hanName").val();
    var authType = $("#hanAuthType").val();
    var status = $("#hanStatus").val();
    var page = $("#paging").html();

    page++;

    // if(status == "全部"){
    //     status = "";
    // }else if(status == "可用"){
    //     status = 0;
    // }else{
    //     status = 1;
    // }

    if (authName.length == 0) {
        fetchDataAndInsert('', '', status, page, 10);
    } else {
        fetchDataAndInsert(authName, authType, status, page, 10);
    }
}

function onQueryClick() {
    var authName = $("#hanName").val();
    var authType = $("#hanAuthType").val();
    var status = $("#hanStatus").val();
    fetchDataAndInsert(authName, authType, status, 1, 10);
}

function fetchDataAndInsert(authName, authType, status, page, rows) {
    console.log(page + "" + rows);
    $.ajax({
        url: '/system/auth/list',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        data: {authName: authName, authType: authType, enable: status, page: page, rows: rows}
    })
        .done(function (msg) {
            hanMesg = msg.obj;
            console.log(msg);
            if (msg.msg) {
                removeAll();
                insertIntoTable(msg.obj);
                $("#paging").html(page);
                check();
            } else {
                console.log("一般不可能");
            }
            console.log("success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
}
function removeAll() {
    $("#myTbody").empty();
}

function addPowerClick() {
    $.ajax({
        url: '/system/auth/list',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        data: {authType: 0, parentId: 0}
    })
        .done(function (msg) {
            console.log(msg);
            var dialogContent = '<div>权限名称&nbsp;&nbsp;<input id="dialogAuth" type="text"></input><br/><br/>权限类型&nbsp;&nbsp;<select id="dialogSelect"><option value="0">菜单权限</option><option value="1">操作权限</option></select><br/><br/>所属菜单&nbsp;&nbsp;<select id="dialogBelongs"><div>';
            for (var i = 0; i < msg.obj.length; i++) {
                dialogContent += "<option value=" + msg.obj[i].id + ">" + msg.obj[i].authName + "</option>"
            }
            dialogContent += "<option selected='selected' value='0'>" + '主菜单' + "</option></select></div>";

            var dia = art.dialog({
                id: '',
                title: '添加权限',
                content: dialogContent,
                resize: false,//可拉伸弹出框开关
                fixed: true,
                lock: true,//锁屏
                opacity: .7,//锁屏背景透明度
                background: '#fff',//锁屏背景颜色
                drag: false,//拖动开关
                width: 550,
                height: 250,
                okVal: '提交',
                ok: function () {
                    var authName = $("#dialogAuth").val();
                    var authType = $("#dialogSelect").val();
                    var belongs = $("#dialogBelongs option:selected").html();
                    var parentId = $("#dialogBelongs option:selected").val();
                    console.log("authName:" + authName.length + "authType:" + authType);
                    if (authName.length !== 0) {
                        $.ajax({
                            url: '/system/auth/add',
                            type: 'GET',
                            contentType: 'application/json',
                            dataType: 'json',
                            data: {authName: authName, authType: authType, parentId: parentId, parentName: belongs}
                        })
                            .done(function (message) {
                                if (message.success) {
                                    alert("添加成功");
                                } else {
                                    alert("添加失败");
                                }
                            })
                            .fail(function () {
                                console.log("error");
                            })
                            .always(function () {
                                console.log("complete");
                            });

                    } else {
                        alert("请输入权限名称");
                    }
                },
            });
            console.log("success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
}

function onEditClick(id) {
    var authName = $("#authName" + id).html();
    var authType = $("#authType" + id).html();
    var parentId = $("#parentId" + id).html();
    var parentName = $("#parentName" + id).html();
    var enable = $("#" + id).html();
    var rowId = $("#row" + id).html();

    enable = enable == "禁用" ? 0 : 1;

    $.ajax({
        url: '/system/auth/list',
        type: 'GET',
        contentType: 'application/json',
        dataType: 'json',
        data: {authType: 0, parentId: 0,},
    })
        .done(function (msg) {
            console.log(msg);
            var dialogContent = '<div>权限名称&nbsp;&nbsp;<input id="dialogAuth" type="text" value=' + authName + '></input><br/><br/>权限类型&nbsp;&nbsp;<select id="dialogSelect"><option value="0">菜单权限</option><option value="1">操作权限</option></select><br/><br/>所属菜单&nbsp;&nbsp;<select id="dialogBelongs">';
            for (var i = 0; i < msg.obj.length; i++) {
                if (parentName == msg.obj[i].authName) {
                    dialogContent += "<option selected='selected' value=" + msg.obj[i].id + " >" + msg.obj[i].authName + "</option>";
                } else {
                    dialogContent += "<option value=" + msg.obj[i].id + ">" + msg.obj[i].authName + "</option>";
                }

            }
            dialogContent += "<option selected='selected' value=0>" + '主菜单' + "</option></select></div>";

            var dia = art.dialog({
                id: '',
                title: '编辑权限',
                content: dialogContent,
                resize: false,//可拉伸弹出框开关
                fixed: true,
                lock: true,//锁屏
                opacity: .7,//锁屏背景透明度
                background: '#fff',//锁屏背景颜色
                drag: false,//拖动开关
                width: 550,
                height: 250,
                okVal: '提交',
                ok: function () {
                    var authNa = $("#dialogAuth").val();
                    var authTy = $("#dialogSelect").val();
                    var belo = $('#dialogBelongs option:selected').val();
                    var parentName = $('#dialogBelongs option:selected').html();

                    console.log("**edit**authName:" + authNa + "authType:" + authTy + "parentId:" + belo + "   enable:" + enable + "   id:" + rowId + "  parentName" + parentName);
                    if (authNa.length !== 0) {

                        $.ajax({
                            url: '/system/auth/edit',
                            type: 'GET',
                            contentType: 'application/json',
                            dataType: 'json',
                            data: {
                                authName: authNa,
                                authType: authTy,
                                parentId: belo,
                                authId: rowId,
                                enable: enable,
                                parentName: parentName
                            }
                        })
                            .done(function (message) {
                                console.log(message);
                                if (message.success) {
                                    alert("更新成功");
                                } else {
                                    alert("更新失败");
                                }
                            })
                            .fail(function () {
                                console.log("error");
                            })
                            .always(function () {
                                console.log("complete");
                            });

                    } else {
                        alert("请输入权限名称");
                    }
                },
            });
            console.log("success");
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
}