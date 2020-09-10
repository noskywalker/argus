var myChart, myChart_count;
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
    var predate = new Date(nowdate.getTime() - 7*24*60*60*1000);
    var nowdateStr = getYYYYMMDD(nowdate);
    var predateStr = getYYYYMMDD(predate);
    $('#dtp_input1').val(predateStr);
    $('#dtp_input2').val(nowdateStr);
    var lineChart = echarts.init(document.getElementById('line-chart-collect'));
    var lineChartCount = echarts.init(document.getElementById('chart-collect-count'));
    var option = {
        tooltip: {},
        legend: {
            data: ['汇总数据']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '汇总数据',
                type: 'line',
                data: [],
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                }
            }]
    };
    lineChart.setOption(option);
    lineChartCount.setOption(option);
    myChart = lineChart;
    myChart_count = lineChartCount;
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
    forceParse: 0,
    endDate: new Date(),
    startDate: addDays(new Date(), -30)
});


function avgClickBtn() {
    beginTime = $('#dtp_input1').val();
    endTime = $('#dtp_input2').val();
    console.log(beginTime + "," + endTime);

// 异步加载数据
    $.get('/statisticsmonitor/intercollect', {
        nodeKey: $("#nodeKeySelect").val(),
        beginDate: beginTime,
        endDate: endTime,
        hanId : $("#hanIdV").val()
    }).done(function (data) {
        myChart.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '汇总数据',
                data: data.obj["data"]
            }]
        });

        myChart_count.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '汇总数据',
                data: data.obj["count"]
            }]
        });
    });
}

function addDays(nowDate, value) {
    nowDate.setDate(nowDate.getDate() + value);
    return nowDate;
}
