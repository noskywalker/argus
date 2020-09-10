var chart4;

$(function () {
    $.ajaxSetup({cache: false});
    var uv_bar = echarts.init(document.getElementById('uvbar-chart'));
    uv_bar.setOption({
        tooltip: {},
        legend: {
            data: ['14日节点UV']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '14日节点UV',
                type: 'bar',
                data: [],
                barWidth: 14
            }]
    });

    chart4 = uv_bar;
    $.get('/statisticsmonitor/getuvbynode', {nodeKey: $("#selectNodeKey").val()}).done(function (data) {

        // 填入数据
        uv_bar.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '14日节点UV',
                data: data.obj["uv"]
            }]
        });
    });

});


function onQueryClickNodeUV() {
    // 异步加载数据
    $.get('/statisticsmonitor/getuvbynode', {nodeKey: $("#selectNodeKey").val()}).done(function (data) {

        // 填入数据
        chart4.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '14日节点UV',
                data: data.obj["uv"]
            }]
        });
    });

}

