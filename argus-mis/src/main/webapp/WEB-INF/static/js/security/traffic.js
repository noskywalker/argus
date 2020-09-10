$(function () {
    $.ajaxSetup({cache: false});
    var myChart = echarts.init(document.getElementById('flot-line-chart-moving'));
    myChart.setOption({
        tooltip: {},
        legend: {
            data: ['今日流量', '昨日流量']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '今日流量',
            type: 'line',
            data: []
        }, {
            name: '昨日流量',
            type: 'line',
            data: []
        }]
    });

    // 异步加载数据
    $.get('/statisticsmonitor/dayinfo').done(function (data) {

        // 填入数据
        myChart.setOption({
            xAxis: {
                data: data.obj.todaytime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '今日流量',
                data: data.obj["todaydata"]
            }, {
                name: '昨日流量',
                data: data.obj["yesterdaydata"]
            }]
        });
    });


    var myChart_bar = echarts.init(document.getElementById('flot-bar-chart-moving'));
    myChart_bar.setOption({
        tooltip: {},
        legend: {
            data: ['14日数据流量']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '14日数据流量',
                type: 'bar',
                data: [],
                barWidth: 14
            }]
    })
    ;
    // 异步加载数据
    $.get('/statisticsmonitor/sumdayinfo').done(function (data) {
        myChart_bar.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '14日数据流量',
                data: data.obj["diffBytes"]
            }]
        });
    });


});