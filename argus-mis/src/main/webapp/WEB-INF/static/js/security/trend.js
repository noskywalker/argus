$(function () {
    $.ajaxSetup({cache: false});
    var myChart_bar = echarts.init(document.getElementById('bar-chart-moving'));
    myChart_bar.setOption({
        tooltip: {},
        legend: {
            data: ['监控次数', '报警次数']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '监控次数',
                type: 'bar',
                data: [],
                barWidth: 15
            },
            {
                name: '报警次数',
                type: 'bar',
                barWidth: 15,
                data: []
            }]
    })
    ;

    // 异步加载数据
    $.get('/statisticsmonitor/monitortimes').done(function (data) {
        myChart_bar.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '监控次数',
                data: data.obj["monitor"]
            }, {
                // 根据名字对应到相应的系列
                name: '报警次数',
                data: data.obj["alarm"]
            }]
        });
    });


    var myChart1 = echarts.init(document.getElementById('line-chart-moving1'));
    myChart1.setOption({
        tooltip: {},
        legend: {
            data: ['报警趋势']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '报警趋势',
            type: 'line',
            data: []
        }]
    });
    var myChart2 = echarts.init(document.getElementById('line-chart-moving2'));
    myChart2.setOption({
        tooltip: {},
        legend: {
            data: ['监控趋势']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '监控趋势',
            type: 'line',
            data: []
        }]
    });

    // 异步加载数据
    $.get('/statisticsmonitor/summonitor').done(function (data) {

        // 填入数据
        myChart1.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '报警趋势',
                data: data.obj["alarm"]
            }]
        });


        // 填入数据
        myChart2.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '监控趋势',
                data: data.obj["monitor"]
            }]
        });


    });


});