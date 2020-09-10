function getYYYYMMDD(nowdate) {
    var seperator1 = "-";
    var month = nowdate.getMonth() + 1;
    var strDate = nowdate.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = nowdate.getFullYear() + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
$(function () {
    var nowdate = new Date();
    var nowdateStr = getYYYYMMDD(nowdate);
    if ($('#searchTime').val()) {
        $('#dtp_input1').val($('#searchTime').val());
    } else {
        $('#dtp_input1').val(nowdateStr);
    }
});

$('.form_date').datetimepicker({
    language: 'cn',
    weekStart: 0,
    todayBtn: true,
    autoclose: 1,
    todayHighlight: 0,
    startView: 2,
    minView: 2,
    maxView: 3,
    forceParse: 0
});

function onQueryUsertraceHz() {
    window.location.href = "/usertrace/usertraceHzByDay?beginDate=" + document.getElementById('dtp_input1').value + "&hanId="+document.getElementById('hanIdV').value;
}
function exportUsertraceHz() {
    window.location.href = "/usertrace/exportUsertraceHzByDay?beginDate=" + document.getElementById('dtp_input1').value;
}
function clearUsertraceHz() {
    window.location.href = "/usertrace/clearUsertraceHzByDay?hanId="+document.getElementById('hanIdV').value;
}
