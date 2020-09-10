$(function () {
    $.ajaxSetup({cache: false});
    var myChart = echarts.init(document.getElementById('flot-line-chart-moving-exception'));
    myChart.setOption({
        tooltip: {},
        legend: {
            data: ['IP']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: 'IP',
            type: 'line',
            data: []
        }]
    });

   /* var myChart_bar = echarts.init(document.getElementById('bar-chart-moving-exception'));
    myChart_bar.setOption({
        tooltip: {},
        legend: {
            data: ['IP']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: 'IP',
            type: 'bar',
            data: []
        }]
    });
*/

    var myChart1 = echarts.init(document.getElementById('flot-line-chart-moving-exception1'));
    myChart1.setOption({
        tooltip: {},
        legend: {
            data: ['UA']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [
            {
                name: 'UA',
                type: 'line',
                data: []
            }]
    });

   /* var myChart_bar1 = echarts.init(document.getElementById('bar-chart-moving-exception1'));
    myChart_bar1.setOption({
        tooltip: {},
        legend: {
            data: ['UA']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [
            {
                name: 'UA',
                type: 'bar',
                data: []
            }]
    });*/


    var myChart2 = echarts.init(document.getElementById('flot-line-chart-moving-exception2'));
    myChart2.setOption({
        tooltip: {},
        legend: {
            data: ['REFER']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [
            {
                name: 'REFER',
                type: 'line',
                data: []
            }]
    });

   /* var myChart_bar2 = echarts.init(document.getElementById('bar-chart-moving-exception2'));
    myChart_bar2.setOption({
        tooltip: {},
        legend: {
            data: ['REFER']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [
            {
                name: 'REFER',
                type: 'bar',
                data: []
            }]
    });*/
    trend();






    /*    setInterval(function () {
     trend()
     }, 600000);*/

    function trend() {
        $.get('/security/monitor/exception/trend?type=' + $("#time_frame").val() + "&domain=" + $("#domain_frame").val()).done(function (data) {
            // alert(data.obj.ip)
            // 填入数据
            myChart.setOption({
                xAxis: {
                    data: fmt(data.obj.ipTime, $("#time_frame").val()),
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: 'IP',
                    data: data.obj.ip,
                }]
            });

          /*  myChart_bar.setOption({
                xAxis: {
                    data: data.obj.time,
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: 'IP',
                    data: data.obj.ip,
                }]
            });*/

            myChart1.setOption({
                xAxis: {
                    data: fmt(data.obj.uaTime, $("#time_frame").val()),
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: 'UA',
                    data: data.obj.ua,
                }]
            });

          /*  myChart_bar1.setOption({
                xAxis: {
                    data: data.obj.time,
                },
                series: [ {
                    // 根据名字对应到相应的系列
                    name: 'UA',
                    data: data.obj.ua,
                }]
            });*/

            myChart2.setOption({
                xAxis: {
                    data: fmt(data.obj.referTime, $("#time_frame").val()),
                },
                series: [ {
                    // 根据名字对应到相应的系列
                    name: 'REFER',
                    data: data.obj.refer,
                }]
            });

         /*   myChart_bar2.setOption({
                xAxis: {
                    data: data.obj.time,
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: 'REFER',
                    data: data.obj.refer,
                }]
            });*/
        });

    }

    //格式化日期
    function fmt(time, type) {
        if (type != 'TODAY') {
            $.each(time, function (n, value) {
                time[n] = value.split(" ")[0];
            });
        }

        return time;
    }

    $("#time_frame").change(function () {
        trend();
    });
    $("#domain_frame").change(function () {
        trend();
    });

});