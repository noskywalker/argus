$(function(){
$.ajax({
    url: '/system/user/usermenu',
    type: 'POST',
    dataType: 'json',
    data: {},
})
    .done(function(messa) {
        if(messa.success){
            var powerHtml = "<li><a href='/dashboard/index' th:href='@{/dashboard/index}'><i class='fa fa-dashboard fa-fw'></i>仪表盘</a>";

            for(var i = 0;i < messa.obj.length;i++){
                powerHtml += "<li name='1' id=" + "hanli" + i + "><a href='#'><i class='fa fa-users fa-fw'></i>" + messa.obj[i].authName + "<span class='fa arrow'></span></a>"

                if(messa.obj[i].kidList.length !== 0){
                    powerHtml += "<ul id=" + "hanul" + i + " class='nav nav-second-level collapse'>";
                }
                for(var j = 0;j < messa.obj[i].kidList.length;j++){
                    powerHtml += "<li><a href=" + messa.obj[i].kidList[j].funcUri + "?hanId=" + i + ">" + messa.obj[i].kidList[j].authName + "</a></li>";
                }
                if(messa.obj[i].kidList.length !== 0){
                    powerHtml += "</ul>";
                }
                powerHtml += "</li>";
            }
            $("#side-menu").append(powerHtml);

            for(var i = 0;i < messa.obj.length;i++){
                $("#hanli" + i).on("click", function(){
                    var id = this.id.substring(5,this.id.length);
                    var name = $("#hanli" + id).attr('name');
                    //console.log("===" + id);
                    if(name == '1'){

                        $("#hanul" + id).removeClass('collapse');
                        $("#hanli" + id).attr('name','2');
                    }else{
                        $("#hanul" + id).addClass('collapse');
                        $("#hanli" + id).attr('name','1');
                    }
                    clearOther(messa,id);
                });
            }
        }else{
            console.log("一般不可能！");
        }

        var hanId = getData("hanId");
        $("#hanul" + hanId).removeClass('collapse');
        $("#hanli" + hanId).attr('name','2');
    })
    .fail(function() {
        console.log("error");
    })
    .always(function() {
        // console.log("complete");
    });
})

function clearOther(mes,id){
    for(var i = 0;i < mes.obj.length;i++){
        if(i != id){
            $("#hanul" + i).addClass('collapse');
            $("#hanli" + i).attr('name','1');
        }
    }
}

function getData(name){
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
    var r = window.location.search.substr(1).match(reg);

    if (r !== null)
        return (r[2]);
    return null;
}