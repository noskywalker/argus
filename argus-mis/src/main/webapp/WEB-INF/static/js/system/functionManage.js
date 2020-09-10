var allList = '';
$(function(){
    fetchDataAndInsert('','',1,10);

    $.ajax({
        url: '/system/auth/list',
        type: 'GET',
        dataType: 'json',
        data: {}
    })
        .done(function(message) {
            if(message.success){
                var powerHtml;
                allList = message.obj;
                powerHtml += "<option selected='selected'>" + '全部' + "</option>";
                for(var i = 0;i < message.obj.length;i++){
                    powerHtml += "<option>" + message.obj[i].authName + "</option>";
                }
                $("#hanStatus").append(powerHtml);
            }else{
                console.log("一般不可能");
            }
        })
        .fail(function() {
            console.log("error");
        })
        .always(function() {
            console.log("complete");
        });


})

function fetchDataAndInsert(authId,funcUri,page,rows){
    $.ajax({
        url: '/system/func/list',
        type: 'GET',
        dataType: 'json',
        data: {authId:authId, funcUri:funcUri, page: page, rows: rows},
    })
        .done(function(msg) {
            if(msg.msg){
                removeAll();
                insertIntoTable(msg.obj);
                $("#paging").html(page);
                check();
            }else{
                console.log("一般不可能");
            }
            console.log("success");
        })
        .fail(function() {
            console.log("error");
        })
        .always(function() {
            console.log("complete");
        });
}

function insertIntoTable(array){
    for(var i = 0;i < array.length && i < 10;i++){
        insertOneRow(array[i], i + 1);
    }
}

function insertOneRow(row, i){
    var authId = "authId" + i;
    var funcUri = "funcUri" + i;
    var belongs = "belongs" + i;
    var trId = "tr" + i;

    var trHtml = "<tr id="+trId+"><td>" + i + "</td><td style=\"display:none;\" id="+authId+">" + row.id + "</td><td id="+funcUri+">" + row.funcUri + "</td><td id="+belongs+">" + row.authName + "</td><td>" + row.createTime + "</td><td><button class='btn btn-info' id="+i+">删除</td></tr>"

    $("#dataTables-user").append(trHtml);

    $("#" + i).on('click', function() {
        event.preventDefault();
        onDeleteClick(i);
    });
}

function check(){
    if($("#paging").html() == 1){
        $("#previous").css('visibility', 'hidden');
    }else{
        $("#previous").css('visibility', 'visible');
    }
}

function removeAll(){
    $("#myTbody").empty();
}

function onDeleteClick(i){
    var confirm = window.confirm("您确定要删除此项系统功能吗？");

    if(confirm){
        var id = $("#authId" + i).html();
        console.log(id);
        $.ajax({
            url: '/system/func/delete',
            type: 'GET',
            dataType: 'json',
            data: {funcId:id}
        })
            .done(function(msg) {
                console.log(msg);
                if(msg.success){
                    $("#tr" + i).remove();
                }else{
                    alert("删除失败");
                }
            })
            .fail(function() {
                console.log("error");
            })
            .always(function() {
                console.log("complete");
            });

    }
}

function onQueryClick() {
    var functionUri = $("#hanName").val();
    var bels = $("#hanStatus").val();
    var id;

    if (bels == '全部') {
        id = -1;
    } else {
    for (var i = 0; i < allList.length; i++) {
        if (bels == allList[i].authName) {
            id = allList[i].id;
        }
    }
}
    fetchDataAndInsert(id,functionUri,1,10);
}

function onPreviousClick(){
    var functionUri = $("#hanName").val();
    var bels = $("#hanStatus").val();
    var id;
    var page = $("#paging").html();
    if (bels == '全部') {
        id = -1;
    } else {
        for(var i = 0;i < allList.length;i++){
         if(bels == allList[i].authName){
             id = allList[i].id;
         }
        }
    }
    page--;

    if(functionUri.length == 0){
        fetchDataAndInsert('','',page,10);
    }else{
        fetchDataAndInsert(id,functionUri,page,10);
    }
}

function onNextClick(){
    var functionUri = $("#hanName").val();
    var bels = $("#hanStatus").val();
    var id;
    var page = $("#paging").html();
    if (bels == '全部') {
        id = -1;
    } else {
        for(var i = 0;i < allList.length;i++){
            if(bels == allList[i].authName){
                id = allList[i].id;
            }
        }
    }
    page++;

    if(functionUri.length == 0){
        fetchDataAndInsert('','',page,10);
    }else{
        fetchDataAndInsert(id,functionUri,page,10);
    }
}

function addUserClick(){
    var dialogContent = "<div>功能URI&nbsp;&nbsp;<input id='dialogUri' type='text'></input><br/><br/>所属权限&nbsp;&nbsp;<select id='dialogPow'>"

    for(var i = 0;i < allList.length;i++){
        dialogContent += "<option>" + allList[i].authName + "</option>";
    }
    dialogContent += "</select></div>";

    var dia = art.dialog({
        id: '',
        title: '添加功能',
        content: dialogContent,
        resize:false,//可拉伸弹出框开关
        fixed:true,
        lock:true,//锁屏
        opacity:.7,//锁屏背景透明度
        background:'#fff',//锁屏背景颜色
        drag:false,//拖动开关
        width:550,
        height:250,
        okVal:'提交',
        ok: function(){
            var funcUri = $("#dialogUri").val();
            var belongs = $("#dialogPow").val();
            var id;

            for(var i = 0;i < allList.length;i++){
                if(belongs == allList[i].authName){
                    id = allList[i].id;
                }
            }
            console.log("funcUri:" + funcUri + "  id:" + id + "  belongs:" + belongs);
            if(funcUri.length !== 0){
                $.ajax({
                    url: '/system/func/add',
                    type: 'GET',
                    dataType: 'json',
                    data: {authId:id, funcUri:funcUri, authName:belongs}
                })
                    .done(function(mes) {
                        if(mes.success){
                            alert("添加成功");
                        }else{
                            alert("添加失败");
                        }
                    })
                    .fail(function() {
                        console.log("error");
                    })
                    .always(function() {
                        console.log("complete");
                    });

            }
        },
    });
}