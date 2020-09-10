/**
 * Created by xuefei on 7/14/16.
 */

$(document).ready(function () {
    <!-- Page-Level Demo Scripts - Tables - Use for reference -->

    $('#dataTables-example').DataTable({
        serverSide: true,
        ajax: "/alarm/loadAlarmData",
        columns: [
            {"data": "ip"},
            {"data": "alarmType"},
            {"data": "triggerTime"},
            {"data": "triggerCount"},
            {"data": "alarmDetail"}
        ]
    });
});

$("#addAlarmStrategyForm").submit(function (event) {
    event.preventDefault();
    var $btn = $('#saveBtn');
    $btn.button('loading');
    var url = "/alarm/add";
    $.ajax({
        url: url,
        type: "POST",
        data: $("#addAlarmStrategyForm").serialize(),
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