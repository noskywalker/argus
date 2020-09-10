$(function () {
    $.ajaxSetup({cache:false});
    var myChart = echarts.init(document.getElementById('flot-line-chart-moving'));
    myChart.setOption({
        tooltip: {},
        legend: {
            data:['登陆PV','注册PV']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '登陆PV',
            type: 'line',
            data: []
        },
            {
                name: '注册PV',
                type: 'line',
                data: []
            }]
    });

    var myChart_bar = echarts.init(document.getElementById('bar-chart-moving'));
    myChart_bar.setOption({
        tooltip: {},
        legend: {
            data:['登陆PV','注册PV']
        },
        xAxis: {
            data: []
        },
        yAxis: {},
        series: [{
            name: '登陆PV',
            type: 'bar',
            data: []
        },
            {
                name: '注册PV',
                type: 'bar',
                data: []
            }]
    });

    // 异步加载数据
    $.get('/security/monitor/info?num=12').done(function (data) {

        // 填入数据
        myChart.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '登陆PV',
                data: data.obj["/auth/loginsubmit"]
            },{
                // 根据名字对应到相应的系列
                name: '注册PV',
                data: data.obj["/regist/submit"]
            }]
        });

        myChart_bar.setOption({
            xAxis: {
                data: data.obj.time
            },
            series: [{
                // 根据名字对应到相应的系列
                name: '登陆PV',
                data: data.obj["/auth/loginsubmit"]
            },{
                // 根据名字对应到相应的系列
                name: '注册PV',
                data: data.obj["/regist/submit"]
            }]
        });
    });


    setInterval(function () {
        $.get('/security/monitor/info?num=12').done(function (data) {
            // 填入数据
            myChart.setOption({
                xAxis: {
                    data: data.obj.time
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: '登陆PV',
                    data: data.obj["/auth/loginsubmit"]
                },
                    {
                        // 根据名字对应到相应的系列
                        name: '注册PV',
                        data: data.obj["/regist/submit"]
                    }]
            });

            myChart_bar.setOption({
                xAxis: {
                    data: data.obj.time
                },
                series: [{
                    // 根据名字对应到相应的系列
                    name: '登陆PV',
                    data: data.obj["/auth/loginsubmit"]
                },
                    {
                        // 根据名字对应到相应的系列
                        name: '注册PV',
                        data: data.obj["/regist/submit"]
                    }]
            });
        });
    }, 60000);

    $.get('/security/monitor/sum').done(function (data) {
        $("#huge1").text(data.obj["/auth/loginsubmit"]);
        $("#huge2").text(data.obj["/regist/submit"]);
    });

    setInterval(function () {
        $.get('/security/monitor/sum').done(function (data) {
            $("#huge1").text(data.obj["/auth/loginsubmit"]);
            $("#huge2").text(data.obj["/regist/submit"]);
        });
    }, 60000);



});