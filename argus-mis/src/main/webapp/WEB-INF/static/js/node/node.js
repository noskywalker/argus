/**
 * Created by wangfeng on 7/14/16.
 */

$(document).ready(function () {
    var henable = $("#henable");
    var enable = henable.val() == 'false' ? false : true;
    $("[name='enable-checkbox']").bootstrapSwitch('state', enable, enable);

    // $("[name='enable-checkbox']").bootstrapSwitch();
    $.ajax({
        url: '/monitor/system/list',
        type: 'GET',
        dataType: 'json',
        data: {}
    })
        .done(function(message) {
            if(message.success){
                var sysid = $("#ylsystemId").val();
                var powerHtml;
                if (sysid) {
                    powerHtml += "<option value='' >" + '全部' + "</option>";
                } else {
                    powerHtml += "<option selected='selected' value='' >" + '全部' + "</option>";
                }

                for(var i = 0;i < message.obj.length;i++){
                    if (sysid && sysid == message.obj[i].id) {
                        powerHtml += "<option selected='selected' value='" + message.obj[i].id + "' >" + message.obj[i].systemName + "</option>";
                    } else {
                        powerHtml += "<option value='" + message.obj[i].id + "' >" + message.obj[i].systemName + "</option>";
                    }

                }
                $("#monitorSystem").append(powerHtml);
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
});

$("#addNodeForm").submit(function (event) {
    event.preventDefault();
    var $btn = $('#saveBtn');
    $btn.button('loading');
    var url = "/node/add";
    var data = $("#addNodeForm").serializeArray();
    var nodeEnable = $("[name='enable-checkbox']").is(':checked');
    var nodeSystemName = $('#systemList option:selected').text();
    data.push({name: 'enable', value: nodeEnable});
    data.push({name: 'nodeSystemName', value: nodeSystemName})
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: 'json',
        success: function (data) {
            console.log("success");
            if (data.success) {
                $(".modal-body").html(data.msg);
                $("#diaglogOK").click(function (event) {
                    location.reload(true);
                });
                location.href = "/node/getAllNode";
            } else {
                console.log("error!!")
                var msg = data.msg;
                $(".modal-body").html(msg);
            }
            var dialog = $('#dialog');
            if (dialog != null) {
                dialog.modal({
                    backdrop: 'static'
                });
            }
            $btn.button('reset');
        }, error: function () {
            // view("异常！");
            alert("异常！");
        }
    });
});

function onQueryClickSystem() {
    var bels = $("#monitorSystem").val();
    var isInt = $("#isInterface").val();
    // console.log(bels);
    window.location.href = "/node/getAllNodeBySysid?sysid=" + bels + "&isInt=" + isInt + "&hanId=" + document.getElementById('hanIdV').value;
}