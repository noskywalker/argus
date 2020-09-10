var chart1;

$(function () {
    $.ajaxSetup({cache: false});
    var bar = echarts.init(document.getElementById('bar-chart'));
    bar.setOption({
        tooltip: {},
        legend: {
            data: ['节点PV', '节点UV']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '节点PV',
                type: 'bar',
                data: [],
                barWidth: 14
            },
            {
                name: '节点UV',
                type: 'bar',
                data: [],
                barWidth: 14
            }
        ]
    });

    chart1 = bar;
    $.get('/statisticsmonitor/getpvuv', {nodeKey: $("#selectNodeKey").val()}).done(function (data) {

        // 填入数据
        bar.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '节点PV',
                data: data.obj["pv"]
            },
                {
                    // 根据名字对应到相应的系列
                    name: '节点UV',
                    data: data.obj["uv"]
                }
            ]
        });
    });

});


function onQueryClickNode() {
    // 异步加载数据
    $.get('/statisticsmonitor/getpvuv', {nodeKey: $("#selectNodeKey").val()}).done(function (data) {

        // 填入数据
        chart1.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '节点PV',
                data: data.obj["pv"]
            },
                {
                    // 根据名字对应到相应的系列
                    name: '节点UV',
                    data: data.obj["uv"]
                }]
        });
    });

}

