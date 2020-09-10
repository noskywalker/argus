/**
 * Created by xuefei on 7/14/16.
 */

$(document).ready(function () {

    var henable = $("#henable");
    var enable = henable.val() == 'false' ? false : true;

    $("[name='enable-checkbox']").bootstrapSwitch('state', enable, enable);
    // $("[name='enable-checkbox']").bootstrapSwitch();
});

$("#addAlarmRuleForm").submit(function (event) {
    event.preventDefault();
    var $btn = $('#saveBtn');
    $btn.button('loading');
    var url = "/security/monitor/alarm/rule/add";
    var data = $("#addAlarmRuleForm").serializeArray();
    $.ajax({
        url: url,
        type: "POST",
        data: data,
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                $(".modal-body").html(data.msg);
                $("#diaglogOK").click(function (event) {
                    location.reload(true);
                });
            } else {
                var msg = data.errorMessages.join('\n');
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