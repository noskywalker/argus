$(function(){
    fetchDataAndInsert('','','',1,10);

})

function fetchDataAndInsert(email,enable,userName,page,rows){
    $.ajax({
        url: '/system/user/list',
        type: 'GET',
        dataType: 'json',
        data: {email:email, enable:enable, userName:userName, page: page, rows: rows}
    })
        .done(function(msg) {
            console.log(msg);
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

function removeAll(){
    $("#myTbody").empty();
}

function insertIntoTable(array){
    for(var i = 0;i < array.length && i < 10;i++){
        insertOneRow(array[i], i + 1);
    }
}

function insertOneRow(row, i){
    var name = "name" + i;
    var email = "email" + i;
    var phone = "phone" + i;
    var status = "status" + i;
    var userName = "userName" + i;
    var hanId = "id" + i;
    var searchTag = "#" + i;
    var edit = "e" + i;
    var enableStatus = row.enable;
    var isUsed = enableStatus == 0 ? "禁用" : "启用";
    row.enable = enableStatus == 0 ? "可用" : "禁用";

    console.log("enableStatus:"+enableStatus);
    var trHtml = "<tr><td>" + i + "</td><td id="+userName+">" + row.userName + "</td><td style=\"display:none;\"  id="+hanId+">" + row.id + "</td><td id="+email+">" + row.email + "</td><td id="+phone+">" + row.phone + "</td><td>" + row.lastLoginTime + "</td><td id="+status+">" + row.enable + "</td><td><a href='javascript:void(0);' id=" + edit + " name="+row.id+">编辑权限</a>&nbsp;&nbsp;<button class='btn btn-info' id=" + i +">" + isUsed + "</button></td></tr>"

    $("#dataTables-user").append(trHtml);
    $(searchTag).on('click', function() {
        event.preventDefault();
        onIsUsedClick(this.id);
    });
    $("#" + edit).on('click', function() {
        event.preventDefault();
        onEditClick(i);
    });
}

function check(){
    if($("#paging").html() == 1){
        $("#previous").css('visibility', 'hidden');
    }else{
        $("#previous").css('visibility', 'visible');
    }
}

function onIsUsedClick(id){
    var userName = $("#userName" + id).html();
    var email = $("#email" +id).html();
    var phone = $("#phone" + id).html();
    var myId = $("#id" + id).html();
    var enable = $("#" + id).html() == "禁用" ? 1 : 0;
    console.log("userName:" + userName + "  email:" + email + "  phone:" + phone + "  enable:" + enable + "  myId:" + id);
    $.ajax({
        url: '/system/user/edit',
        type: 'GET',
        dataType: 'json',
        data: {userName: userName, email: email,enable:enable, phone: phone, id: myId},
    })
        .done(function(msg) {
            if(msg){
                var str = $("#" + id).html() == "禁用" ? "启用" : "禁用";
                var str1 = $("#status" + id).html() == "可用" ? "禁用" : "可用";

                $("#" + id).html(str);
                $("#status" + id).html(str1);
            }else{
                console.log("参数出错了");
            }
        })
        .fail(function() {
            console.log("接口调用出错");
        })
        .always(function() {
            console.log("complete");
        });

}

function onQueryClick(){
    var email = $("#hanEmail").val();
    var userName = $("#hanName").val();
    var status = $("#hanStatus").val();

    fetchDataAndInsert(email,status,userName,1,10);
}

function onPreviousClick(){
    var email = $("#hanEmail").val();
    var userName = $("#hanName").val();
    var status = $("#hanStatus").val();
    var page = $("#paging").html();

    page--;

    if(userName.length == 0 && email.length == 0){
        fetchDataAndInsert('',status,'',page,10);
    }else{
        fetchDataAndInsert(email,status,userName,page,10);
    }
}

function onNextClick(){
    var email = $("#hanEmail").val();
    var userName = $("#hanName").val();
    var status = $("#hanStatus").val();
    var page = $("#paging").html();

    page++;

    if(userName.length == 0 && email.length == 0){
        fetchDataAndInsert('',status,'',page,10);
    }else{
        fetchDataAndInsert(email,status,userName,page,10);
    }
}

function addUserClick(){
    var dialogContent = "<div>邮箱&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='dialogEmail' type='text'></input><br/><br/>用户名称&nbsp;&nbsp;<input id='dialogName' type='text'></input><br/><br/>手机&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='dialogPhone' type='text'></input><br/><br/><span style='color:red'>tips:初始密码为888888</span>"
    var dia = art.dialog({
        id: '',
        title: '添加用户',
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
            var email = $("#dialogEmail").val();
            var userName = $("#dialogName").val();
            var phone = $("#dialogPhone").val();

            if(email.length !== 0 && userName.length !== 0 && phone.length !== 0){
                $.ajax({
                    url: '/system/user/add',
                    type: 'GET',
                    dataType: 'json',
                    data: {email:email, userName:userName, phone:phone}
                })
                    .done(function(message) {
                        if(message.success){
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

            }else{
                alert("请您填全信息.");
            }
        },
    });
}

function onEditClick(id){
    var userId = $("#e" + id).attr("name");
    
    $.ajax({
        url: '/system/auth/list',
        type: 'GET',
        dataType: 'json',
        data: {userId: userId,flag:1,enable:0}
    })
        .done(function(msg) {
            
            if(msg.success){
                var dialogContent = "<div id='myDialog'>";
                for(var i = 0;i < msg.obj.length;i++){
                    dialogContent += "<B>"+msg.obj[i].authName+"</B>" + "&nbsp;&nbsp;&nbsp;&nbsp;<br/>";
                    var kidAuth =  msg.obj[i].kidAuth;
                    if(kidAuth!= null) {
                        for (var j = 0; j < kidAuth.length; j++) {
                            if(kidAuth[j].checked){
                                dialogContent += "<input type='checkbox' value=" + kidAuth[j].id + " checked='true' >" + kidAuth[j].authName + "</input>";
                            }else{
                                dialogContent += "<input type='checkbox' value=" + kidAuth[j].id + ">" + kidAuth[j].authName + "</input>";
                            }
                        }
                    }
                    dialogContent += "<hr style=\"height:1px;border:none;border-top:1px dashed #0066CC;\"/>"
                }
                dialogContent += "</div>";
                var dia = art.dialog({
                    id: '',
                    title: '编辑用户权限',
                    content: dialogContent,
                    resize:true,//可拉伸弹出框开关
                    fixed:false,
                    lock:true,//锁屏
                    opacity:.7,//锁屏背景透明度
                    background:'#fff',//锁屏背景颜色
                    drag:true,//拖动开关
                    width:550,
                    height:250,
                    okVal:'提交',
                    ok: function(){
                        var list = document.getElementById("myDialog");
                        var list = list.childNodes;
                        var a = '';
                        for(var i = 0;i < list.length;i++){
                            if(list[i].checked){
                                a += list[i].value + ',';
                            }
                        }
                        a = a.substring(0, a.length - 1);
                        $.ajax({
                            url: '/system/user/edituserauth',
                            type: 'GET',
                            dataType: 'json',
                            data: {authIds: a,userId: userId,},
                        })
                            .done(function(m) {
                                if(m.success){
                                    alert("编辑成功");
                                }else{
                                    alert("编辑失败");
                                }
                            })
                            .fail(function() {
                                console.log("error");
                            })
                            .always(function() {
                                console.log("complete");
                            });

                    },
                });

            }else{
                console.log("接口出错了");
            }
        })
        .fail(function() {
            console.log("error");
        })
        .always(function() {
            console.log("complete");
        });

}