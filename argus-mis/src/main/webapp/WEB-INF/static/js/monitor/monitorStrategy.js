/**
 * Created by wangfeng on 7/14/16.
 */

$(document).ready(function () {
    // $("[name='enable-checkbox']").bootstrapSwitch();
    $.ajax({
        url: '/monitor/system/list',
        type: 'GET',
        dataType: 'json',
        data: {}
    })
        .done(function (message) {
            if (message.success) {
                var sysid = $("#ylsystemId").val();
                var powerHtml;
                if (sysid) {
                    powerHtml += "<option value='' >" + '全部' + "</option>";
                } else {
                    powerHtml += "<option selected='selected' value='' >" + '全部' + "</option>";
                }

                for (var i = 0; i < message.obj.length; i++) {
                    if (sysid && sysid == message.obj[i].id) {
                        powerHtml += "<option selected='selected' value='" + message.obj[i].id + "' >" + message.obj[i].systemName + "</option>";
                    } else {
                        powerHtml += "<option value='" + message.obj[i].id + "' >" + message.obj[i].systemName + "</option>";
                    }

                }
                $("#monitorSystem").append(powerHtml);
            } else {
                console.log("一般不可能");
            }
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            console.log("complete");
        });
});

function onQueryClickSystem() {
    var bels = $("#monitorSystem").val();
    console.log(bels);
    window.location.href = "/monitor/getAllMonitorStrategyBySysid?sysid=" + bels + "&hanId="+document.getElementById('hanIdV').value;
}