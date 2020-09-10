$(function () {
    $.ajaxSetup({cache: false});
    var myChart_bar = echarts.init(document.getElementById('flot-bar-chart-moving'));
    myChart_bar.setOption({
        tooltip : {
            trigger: 'axis'
        },
        legend: {
            data:['响应时间']
        },
        calculable : true,
        xAxis : [],
        yAxis : [],
        series : []
    });

    // 异步加载数据
    $.get('/statisticsmonitor/interRankingMap').done(function (data) {
        myChart_bar.setOption({
            tooltip : {
                trigger: 'axis'
            },
            legend: {
                data:['响应时间(ms)']
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'value'
                }
            ],
            yAxis : [
                {
                    name : '接口名称',
                    type : 'category',
                    data : data.obj.name
                }
            ],
            series : [
                {
                    name:'响应时间(ms)',
                    type:'bar',
                    itemStyle : { normal: {color : '#7CCD7C', label : {show: true, position: 'insideRight'}}},
                    barWidth : 30,
                    data:data.obj.time
                }
            ]
        });
    });


});