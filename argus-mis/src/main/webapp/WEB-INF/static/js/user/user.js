/**
 * Created by xuefei on 7/14/16.
 */

$(document).ready(function () {
    var url = "/group/getGroup";
    var select_group = $("#groupList");

    $('#groupList').selectpicker({
        noneSelectedText: '请选择组名',
        actionsBox: true
    });
    $('#openIdList').selectpicker({
        noneSelectedText: '请选择微信昵称',
        actionsBox: true
    });
});

$("#addUserForm").submit(function (event) {
    event.preventDefault();
    var $btn = $('#saveBtn');
    $btn.button('loading');
    var url = "/user/add";
    $.ajax({
        url: url,
        type: "POST",
        data: $("#addUserForm").serialize(),
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