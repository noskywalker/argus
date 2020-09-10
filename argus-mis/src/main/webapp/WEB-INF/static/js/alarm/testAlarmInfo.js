function onSubmitClickAlarm(){
    var alid = $("#alid").val();
    var mesg = $("#mesg").val();
    var sysname = $("#sysname").val();

    if(alid.length !== 0 && mesg.length !== 0 && sysname.length !== 0){
        $.ajax({
            url: '/alarm/sendTestAlarm',
            type: 'GET',
            dataType: 'json',
            data: {alid:alid, mesg:mesg, sysname:sysname}
        })
            .done(function(mes) {
                if(mes.success){
                    alert(mes.msg);
                }else{
                    alert(mes.msg);
                }
            })
            .fail(function() {
                console.log("error");
            })
            .always(function() {
                // console.log("complete");
            });

    }else{
        alert("输入信息不对");
    }
}