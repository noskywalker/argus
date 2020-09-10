var chart1, chart2, chart3;

$(function () {
    $.ajaxSetup({cache: false});
    var myChart1 = echarts.init(document.getElementById('line-chart-moving1'));
    myChart1.setOption({
        tooltip: {},
        legend: {
            data: ['今日PV', '昨日PV']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '今日PV',
            type: 'line',
            data: []
        }, {
            name: '昨日PV',
            type: 'line',
            data: []
        }]
    });
    var myChart2 = echarts.init(document.getElementById('line-chart-moving2'));
    myChart2.setOption({
        tooltip: {},
        legend: {
            data: ['今日PV', '昨日PV']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '今日PV',
            type: 'line',
            data: []
        }, {
            name: '昨日PV',
            type: 'line',
            data: []
        }]
    });

    var myChart_bar = echarts.init(document.getElementById('bar-chart'));
    myChart_bar.setOption({
        tooltip: {},
        legend: {
            data: ['14日节点pv']
        },
        xAxis: {
            data: []
        }
        ,
        yAxis: {}
        ,
        series: [
            {
                name: '14日节点pv',
                type: 'bar',
                data: [],
                barWidth: 14
            }]
    });

    chart1 = myChart1;
    chart2 = myChart2;
    chart3 = myChart_bar;
    // 异步加载数据
    $.get('/node/sumnode', {nodeKey: $("#nodeKey").val()}).done(function (data) {

        // 填入数据
        myChart1.setOption({
            xAxis: {
                data: data.obj.mtime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '今日PV',
                data: data.obj["mtoday"]
            }, {
                name: '昨日PV',
                data: data.obj["myesterday"]
            }]
        });


        // 填入数据
        myChart2.setOption({
            xAxis: {
                data: data.obj.htime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '今日PV',
                data: data.obj["htoday"]
            }, {
                // 根据名字对应到相应的系列
                name: '昨日PV',
                data: data.obj["hyesterday"]
            }]
        });

        myChart_bar.setOption({
            xAxis: {
                data: data.obj.dtime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '14日数据',
                data: data.obj["ddata"]
            }]
        });

    });

});

function onQueryClickNode() {
    // 异步加载数据
    $.get('/node/sumnode', {nodeKey: $("#nodeKey").val()}).done(function (data) {

        // 填入数据
        chart1.setOption({
            xAxis: {
                data: data.obj.mtime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '今日PV',
                data: data.obj["mtoday"]
            }, {
                name: '昨日PV',
                data: data.obj["myesterday"]
            }]
        });


        // 填入数据
        chart2.setOption({
            xAxis: {
                data: data.obj.htime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '今日PV',
                data: data.obj["htoday"]
            }, {
                // 根据名字对应到相应的系列
                name: '昨日PV',
                data: data.obj["hyesterday"]
            }]
        });

        chart3.setOption({
            xAxis: {
                data: data.obj.dtime
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '14日数据',
                data: data.obj["ddata"]
            }]
        });


    });
}

