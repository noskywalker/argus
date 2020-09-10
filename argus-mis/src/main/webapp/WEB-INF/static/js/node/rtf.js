var newBytes, oldBytes, chaBytes, ynum;

$(document).ready(function () {
    fetchStaticsData();
    setInterval("fetchStaticsData()", 10000);

});


//Flot Moving Line Chart
$(function () {
    var container = $("#flot-line-chart-moving");
// Determine how many data points to keep based on the placeholder's initial size;
// this gives us a nice high-res plot while avoiding more than one point per pixel.
    var maximum = container.outerWidth() / 2 || 300;
    ynum = document.getElementById("maxNum").value;
    if (ynum == "undefined" || ynum == 0) {
        ynum = 20;
    }
    var data = [];
    var realtimeTrafficList = [];

    var updateRandomTimer = setInterval(function updateRandom() {
        if (typeof newBytes != "undefined") {
            series[0].data = getRandomData(10, 1);
            plot.setData(series);
            plot.draw();
        }
    }, 500);

    function getRandomData(iPartition, flag) {
        if (typeof newBytes != "undefined" && flag == 1) {
            var chaBytesByS = 0;
            if (chaBytes > 0 && chaBytes < oldBytes) {
                var newcha = chaBytes;
                if (newcha > 1024 * 1024) {
// 线上
                    newcha = newcha / 1024;
                } else {
// 测试
                    newcha = newcha * 10;
                }
                chaBytesByS = newcha / iPartition;
                for (var im = 0; im < iPartition; im++) {
// 随机正负1%-11%的浮动
                    var halfPercent = Math.round(Math.random() * 2 + 1);
                    var positiveAndNegative = Math.pow(-1, halfPercent);
                    var floatingPercent = (Math.random() + 0.1) * 0.1;
                    var floatingNum = chaBytesByS * floatingPercent;
                    var chaBytesBySf = chaBytesByS + floatingNum * positiveAndNegative;
                    realtimeTrafficList.push(chaBytesBySf);
                }
                if (realtimeTrafficList.length > maximum) {
                    var chai = realtimeTrafficList.length - maximum;
                    for (var jm = 0; jm < chai; jm++) {
                        realtimeTrafficList.shift();
                    }
                }
            } else {
                for (var ih = 0; ih < iPartition; ih++) {
                    realtimeTrafficList.push(0);
                }
                if (realtimeTrafficList.length > maximum) {
                    var chai2 = realtimeTrafficList.length - maximum;
                    for (var jh = 0; jh < chai2; jh++) {
                        realtimeTrafficList.shift();
                    }
                }
            }
            if (realtimeTrafficList && realtimeTrafficList.length && realtimeTrafficList.length > 0) {
                var datas = [];
                for (var ik = 0; ik < realtimeTrafficList.length; ++ik) {
                    datas.push(realtimeTrafficList[ik])
                }

                while (datas && datas.length && datas.length < maximum) {
                    var chaleng = maximum - datas.length;
                    for (var k = 0; k < chaleng; ++k) {
                        datas.unshift(0);
                    }
                }
                var ress = [];
                for (var i = 0; i < datas.length; ++i) {
                    ress.push([i, datas[i]])
                }
                return ress;
            }
        }

        if (flag == 0) {
// 加载初始化为0
            if (data.length) {
                data = data.slice(1);
            }
            while (data.length < maximum) {
                data.push(0);
            }
            var res = [];
            for (var i = 0; i < data.length; ++i) {
                res.push([i, data[i]])
            }
            return res;
        }
    }

    series = [{
        data: getRandomData(10, 0),
        lines: {
            fill: true
        }
    }];

    var plot = $.plot(container, series, {
            grid: {
                borderWidth: 1,
                minBorderMargin: 20,
                labelMargin: 10,
                backgroundColor: {
                    colors: ["#fff", "#e4f4f4"]
                },
                margin: {
                    top: 8,
                    bottom: 20,
                    left: 20
                },
                markings: function (axes) {
                    var markings = [];
                    var xaxis = axes.xaxis;
                    for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                        markings.push({
                            xaxis: {
                                from: x,
                                to: x + xaxis.tickSize
                            },
                            color: "rgba(232, 232, 255, 0.2)"
                        });
                    }
                    return markings;
                }
            },
            xaxis: {
                tickFormatter: function () {
                    return "";
                }
            },
            yaxis: {
                min: 0,
                max: ynum,
                type: 'value'

            },
            legend: {
                show: true
            }
        })
        ;

    $("#ClickA").on('click', function (event) {
        onQueryClickNode();
    });


    function onQueryClickNode() {
        var nodeKey = $("#nodeKey").val();
        window.location.href = "/statisticsmonitor/rtf?nodeKey=" + nodeKey + "&hanId="+document.getElementById('hanIdV').value;
    }
});


function fetchStaticsData() {
    var url = "/statisticsmonitor/bytesBykey";
    var nodeKey = $("#nodeKey").val();
    $.ajax({
        url: url,
        type: "GET",
        data: {
            "nodeKey": nodeKey
        },
        dataType: 'json',
        success: function (data) {
            newBytes = data.obj;
            chaBytes = newBytes - oldBytes;
            oldBytes = newBytes;
        }, error: function (jqXHR, textStatus, errorThrown) {
            newBytes = undefined;
        }
    })
}







