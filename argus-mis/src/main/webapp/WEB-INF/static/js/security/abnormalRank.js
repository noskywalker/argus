/**
 * @author guoshouqing
 * @date 2016-09-28
 */
(function($){
    $(".form-control").change(function () {
        var params=new Object();
        params.interval =$("#time_frame option:selected").val();
        params.domain= $("#domain_frame option:selected").val();
        params.rankType =  $("#rankType_frame option:selected").val();
        loadData(params);
    });

    function loadData(params){
        $.ajax({
            type : "get",
            url : "/security/monitor/abnormalrank/ajaxRank",
            data : {domain:params.domain,interval:params.interval,rankType:params.rankType},
            dataType : "json",
            success : function(data) {
                var aaData=new Array();
                for(i=0 ;i<data.length;i++){
                    var row = new Array();
                    row[0]=data[i].name;
                    row[1]=data[i].freq;
                    aaData[i] =row;
                }
                rebuildDataTable(aaData,params);
            }
            ,
            error:function () {
                alert("异常!");
            }
        })
    };
    function rebuildDataTable(aaData,params) {
        $('#dataTables-group').DataTable( {
            destroy:true,
            aaData: aaData,
            aoColumns:[
                {"sTitle":params.rankType,
                    "mRender": function(data) {
                        var queryString="name="+data+"&interval="+params.interval+"&rankType="+params.rankType;
                        return "<a href=/security/monitor/abnormalrank/detail?"+queryString+">"+data+"</a>";
                    }
                },
                {
                    "sTitle":"频率"
                }
            ]
        } );
    }
}(jQuery));