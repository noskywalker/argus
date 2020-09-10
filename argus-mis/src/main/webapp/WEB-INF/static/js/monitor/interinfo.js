$(function () {
    $.ajaxSetup({cache: false});
    var myChart = echarts.init(document.getElementById('flot-line-chart'));
    var myChart_count = echarts.init(document.getElementById("line-chart-count"));
    var option = {
        tooltip: {},
        legend: {
            data: ['昨日', '今日']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '昨日',
                type: 'line',
                data: [],
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
            },
            {
                name: '今日',
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
    myChart.setOption(option);
    myChart_count.setOption(option);

    // 异步加载数据
    $.get('/statisticsmonitor/intercompare', {nodeKey: $("#nodeKeySelect").val()}).done(function (data) {
        myChart.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '昨日',
                data: data.obj["yesterday"]
            }, {
                // 根据名字对应到相应的系列
                name: '今日',
                data: data.obj["today"]
            }]
        });

        myChart_count.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '昨日',
                data: data.obj["yesterdaycount"]
            }, {
                // 根据名字对应到相应的系列
                name: '今日',
                data: data.obj["todaycount"]
            }]
        });
    });
});

