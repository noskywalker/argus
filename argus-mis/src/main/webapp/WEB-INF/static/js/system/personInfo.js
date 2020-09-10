var message;
$(function(){
    $.ajax({
        url: '/system/user/userinfo',
        type: 'POST',
        dataType: 'json',
        data: {},
    })
        .done(function(msg) {
            if(msg.success){
                message = msg.obj;
                $("#email").val(msg.obj.email).attr('disabled', 'disabled');
                $("#userName").val(msg.obj.userName);
                $("#phone").val(msg.obj.phone);
            }else{
                alert("一般不可能");
            }
        })
        .fail(function() {
            console.log("error");
        })
        .always(function() {
            console.log("complete");
        });

})
function onChangeClick(){
    var dialogContent = "<div>原密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='dialogPass' type='text'></input><br/><br/>新密码&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='dialogNew' type='text'></input><br/><br/>确认密码&nbsp;&nbsp;<input id='dialogConfirm' type='text'></input></div>"
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
            var pass = $("#dialogPass").val();
            var newI = $("#dialogNew").val();
            var confim = $("#dialogConfirm").val();

            if(pass.length !== 0 && newI.length !== 0 && newI === confim){
                $.ajax({
                    url: '/system/user/updateUserPassword',
                    type: 'GET',
                    dataType: 'json',
                    data: {oldPass:pass, newPass:newI, userId:message.id}
                })
                    .done(function(mess) {
                        if(mess.success){
                            alert("修改成功");
                        }else{
                            alert("修改失败");
                        }
                    })
                    .fail(function() {
                        console.log("error");
                    })
                    .always(function() {
                        console.log("complete");
                    });

            }else{
                alert("输入信息不对");
            }
        },
    });
}

function onSubmitClick(){
    var userName = $("#userName").val();
    var phone = $("#phone").val();

    if(userName.length !== 0 && phone.length !== 0 && (userName != message.userName || phone != message.phone)){
        $.ajax({
            url: '/system/user/edit',
            type: 'GET',
            dataType: 'json',
            data: {email:message.email, enable:message.enable, id:message.id,phone:phone, userName:userName}
        })
            .done(function(mes) {
                if(mes.success){
                    alert("修改成功");
                }else{
                    alert("修改失败");
                }
            })
            .fail(function() {
                console.log("error");
            })
            .always(function() {
                console.log("complete");
            });

    }else{
        alert("输入信息不对");
    }
}