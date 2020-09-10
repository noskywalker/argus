/**
 * Created by xuefei on 7/14/16.
 */
$(document).ready(function () {
    // 基于准备好的dom，初始化echarts实例
    var demoMap = echarts.init(document.getElementById('demo-map'));
    var dataObj;
    var dataObj2 = [];
    var url = "/statisticsmonitor/getipaddress";
    var jishu = 0;
    $.ajax({
        url: url,
        type: "GET",
        data: {},
        dataType: 'json',
        async: false,
        success: function (data) {
            dataObj = data.obj;
        }, error: function (jqXHR, textStatus, errorThrown) {
            dataObj = "";
        }
    });
    var geoCoordMap = {
        '海门': [121.15, 31.89],
        '鄂尔多斯': [109.781327, 39.608266],
        '招远': [120.38, 37.35],
        '舟山': [122.207216, 29.985295],
        '齐齐哈尔': [123.97, 47.33],
        '盐城': [120.13, 33.38],
        '赤峰': [118.87, 42.28],
        '青岛': [120.33, 36.07],
        '乳山': [121.52, 36.89],
        '金昌': [102.188043, 38.520089],
        '泉州': [118.58, 24.93],
        '莱西': [120.53, 36.86],
        '日照': [119.46, 35.42],
        '胶南': [119.97, 35.88],
        '南通': [121.05, 32.08],
        '拉萨': [91.11, 29.97],
        '云浮': [112.02, 22.93],
        '梅州': [116.1, 24.55],
        '文登': [122.05, 37.2],
        '上海': [121.48, 31.22],
        '攀枝花': [101.718637, 26.582347],
        '威海': [122.1, 37.5],
        '承德': [117.93, 40.97],
        '厦门': [118.1, 24.46],
        '汕尾': [115.375279, 22.786211],
        '潮州': [116.63, 23.68],
        '丹东': [124.37, 40.13],
        '太仓': [121.1, 31.45],
        '曲靖': [103.79, 25.51],
        '烟台': [121.39, 37.52],
        '福州': [119.3, 26.08],
        '瓦房店': [121.979603, 39.627114],
        '即墨': [120.45, 36.38],
        '抚顺': [123.97, 41.97],
        '玉溪': [102.52, 24.35],
        '张家口': [114.87, 40.82],
        '阳泉': [113.57, 37.85],
        '莱州': [119.942327, 37.177017],
        '湖州': [120.1, 30.86],
        '汕头': [116.69, 23.39],
        '昆山': [120.95, 31.39],
        '宁波': [121.56, 29.86],
        '湛江': [110.359377, 21.270708],
        '揭阳': [116.35, 23.55],
        '荣成': [122.41, 37.16],
        '连云港': [119.16, 34.59],
        '葫芦岛': [120.836932, 40.711052],
        '常熟': [120.74, 31.64],
        '东莞': [113.75, 23.04],
        '河源': [114.68, 23.73],
        '淮安': [119.15, 33.5],
        '泰州': [119.9, 32.49],
        '南宁': [108.33, 22.84],
        '营口': [122.18, 40.65],
        '惠州': [114.4, 23.09],
        '江阴': [120.26, 31.91],
        '蓬莱': [120.75, 37.8],
        '韶关': [113.62, 24.84],
        '嘉峪关': [98.289152, 39.77313],
        '广州': [113.23, 23.16],
        '延安': [109.47, 36.6],
        '太原': [112.53, 37.87],
        '清远': [113.01, 23.7],
        '中山': [113.38, 22.52],
        '昆明': [102.73, 25.04],
        '寿光': [118.73, 36.86],
        '盘锦': [122.070714, 41.119997],
        '长治': [113.08, 36.18],
        '深圳': [114.07, 22.62],
        '珠海': [113.52, 22.3],
        '宿迁': [118.3, 33.96],
        '咸阳': [108.72, 34.36],
        '铜川': [109.11, 35.09],
        '平度': [119.97, 36.77],
        '佛山': [113.11, 23.05],
        '海口': [110.35, 20.02],
        '江门': [113.06, 22.61],
        '章丘': [117.53, 36.72],
        '肇庆': [112.44, 23.05],
        '大连': [121.62, 38.92],
        '临汾': [111.5, 36.08],
        '吴江': [120.63, 31.16],
        '石嘴山': [106.39, 39.04],
        '沈阳': [123.38, 41.8],
        '苏州': [120.62, 31.32],
        '茂名': [110.88, 21.68],
        '嘉兴': [120.76, 30.77],
        '长春': [125.35, 43.88],
        '胶州': [120.03336, 36.264622],
        '银川': [106.27, 38.47],
        '张家港': [120.555821, 31.875428],
        '三门峡': [111.19, 34.76],
        '锦州': [121.15, 41.13],
        '南昌': [115.89, 28.68],
        '柳州': [109.4, 24.33],
        '三亚': [109.511909, 18.252847],
        '自贡': [104.778442, 29.33903],
        '吉林': [126.57, 43.87],
        '阳江': [111.95, 21.85],
        '泸州': [105.39, 28.91],
        '西宁': [101.74, 36.56],
        '宜宾': [104.56, 29.77],
        '呼和浩特': [111.65, 40.82],
        '成都': [104.06, 30.67],
        '大同': [113.3, 40.12],
        '镇江': [119.44, 32.2],
        '桂林': [110.28, 25.29],
        '张家界': [110.479191, 29.117096],
        '宜兴': [119.82, 31.36],
        '北海': [109.12, 21.49],
        '西安': [108.95, 34.27],
        '金坛': [119.56, 31.74],
        '东营': [118.49, 37.46],
        '牡丹江': [129.58, 44.6],
        '遵义': [106.9, 27.7],
        '绍兴': [120.58, 30.01],
        '扬州': [119.42, 32.39],
        '常州': [119.95, 31.79],
        '潍坊': [119.1, 36.62],
        '重庆': [106.54, 29.59],
        '台州': [121.420757, 28.656386],
        '南京': [118.78, 32.04],
        '滨州': [118.03, 37.36],
        '贵阳': [106.71, 26.57],
        '无锡': [120.29, 31.59],
        '本溪': [123.73, 41.3],
        '克拉玛依': [84.77, 45.59],
        '渭南': [109.5, 34.52],
        '马鞍山': [118.48, 31.56],
        '宝鸡': [107.15, 34.38],
        '焦作': [113.21, 35.24],
        '句容': [119.16, 31.95],
        '北京': [116.46, 39.92],
        '徐州': [117.2, 34.26],
        '衡水': [115.72, 37.72],
        '包头': [110, 40.58],
        '绵阳': [104.73, 31.48],
        '乌鲁木齐': [87.68, 43.77],
        '枣庄': [117.57, 34.86],
        '杭州': [120.19, 30.26],
        '淄博': [118.05, 36.78],
        '鞍山': [122.85, 41.12],
        '溧阳': [119.48, 31.43],
        '库尔勒': [86.06, 41.68],
        '安阳': [114.35, 36.1],
        '开封': [114.35, 34.79],
        '济南': [117, 36.65],
        '德阳': [104.37, 31.13],
        '温州': [120.65, 28.01],
        '九江': [115.97, 29.71],
        '邯郸': [114.47, 36.6],
        '临安': [119.72, 30.23],
        '兰州': [103.73, 36.03],
        '沧州': [116.83, 38.33],
        '临沂': [118.35, 35.05],
        '南充': [106.110698, 30.837793],
        '天津': [117.2, 39.13],
        '富阳': [119.95, 30.07],
        '泰安': [117.13, 36.18],
        '诸暨': [120.23, 29.71],
        '郑州': [113.65, 34.76],
        '哈尔滨': [126.63, 45.75],
        '聊城': [115.97, 36.45],
        '芜湖': [118.38, 31.33],
        '唐山': [118.02, 39.63],
        '平顶山': [113.29, 33.75],
        '邢台': [114.48, 37.05],
        '德州': [116.29, 37.45],
        '济宁': [116.59, 35.38],
        '荆州': [112.239741, 30.335165],
        '宜昌': [111.3, 30.7],
        '义乌': [120.06, 29.32],
        '丽水': [119.92, 28.45],
        '洛阳': [112.44, 34.7],
        '秦皇岛': [119.57, 39.95],
        '株洲': [113.16, 27.83],
        '石家庄': [114.48, 38.03],
        '莱芜': [117.67, 36.19],
        '常德': [111.69, 29.05],
        '保定': [115.48, 38.85],
        '湘潭': [112.91, 27.87],
        '金华': [119.64, 29.12],
        '岳阳': [113.09, 29.37],
        '长沙': [113, 28.21],
        '衢州': [118.88, 28.97],
        '廊坊': [116.7, 39.53],
        '菏泽': [115.480656, 35.23375],
        '合肥': [117.27, 31.86],
        '武汉': [114.31, 30.52],
        '大庆': [125.03, 46.58]
    };

    var convertData = function (data) {
        console.log(jishu);
        jishu++;
        var res = [];
        for (var i = 0; i < data.length; i++) {
            for (var key in geoCoordMap) {
                if (data[i].cityName.indexOf(key) > -1) {
                    var geoCoord = geoCoordMap[key];
                    var val = data[i].percent;
                    res.push({
                        name: key,
                        value: geoCoord.concat(val.substring(0, val.length - 2))
                    });
                    dataObj2.push(data[i]);
                }
            }

        }
        return res;
    };

    option = {
        backgroundColor: '#404a59',
        title: {
            text: '全国城市流量占比',
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            y: 'bottom',
            x: 'right',
            data: ['流量'],
            textStyle: {
                color: '#fff'
            }
        },
        geo: {
            map: 'china',
            label: {
                emphasis: {
                    show: false
                }
            },
            roam: true,
            itemStyle: {
                normal: {
                    areaColor: '#323c48',
                    borderColor: '#111'
                },
                emphasis: {
                    areaColor: '#2a333d'
                }
            }
        },
        series: [
            {
                name: '流量',
                type: 'scatter',
                coordinateSystem: 'geo',
                data: convertData(dataObj),
                symbolSize: function (val) {
                    return getNum(val[2]);
                },
                label: {
                    normal: {
                        formatter: '{b}',
                        position: 'right',
                        show: false
                    },
                    emphasis: {
                        show: true
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#ddb926'
                    }
                }
            },
            {
                name: 'Top 20',
                type: 'effectScatter',
                coordinateSystem: 'geo',
                data: convertData(dataObj2.slice(0, 20)),
                symbolSize: function (val) {
                    return getNum(val[2]);
                },
                showEffectOn: 'render',
                rippleEffect: {
                    brushType: 'stroke'
                },
                hoverAnimation: true,
                label: {
                    normal: {
                        formatter: '{b}',
                        position: 'right',
                        show: true
                    }
                },
                itemStyle: {
                    normal: {
                        color: '#f4e925',
                        shadowBlur: 10,
                        shadowColor: '#333'
                    }
                },
                zlevel: 1
            }
        ]
    };

    demoMap.setOption(option);

    // 柱状图
    var demoBar = echarts.init(document.getElementById('demo-bar'));
    demoBar_option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '直接访问',
                type: 'bar',
                barWidth: '60%',
                data: [10, 52, 200, 334, 390, 330, 220]
            }
        ]
    };
    demoBar.setOption(demoBar_option);

    fetchStaticsData();
    setInterval("fetchStaticsData()", 10000);
    // setInterval("splitStaticsDataIntoParts(10)", 1000);


    var offset = 0;
    plot();

    function plot() {
        var sin = [],
            cos = [];
        for (var i = 0; i < 12; i += 0.2) {
            sin.push([i, Math.sin(i + offset)]);
            cos.push([i, Math.cos(i + offset)]);
        }

        var options = {
            series: {
                lines: {
                    show: true
                },
                points: {
                    show: true
                }
            },
            grid: {
                hoverable: true //IMPORTANT! this is needed for tooltip to work
            },
            yaxis: {
                min: -1.2,
                max: 1.2
            },
            tooltip: true,
            tooltipOpts: {
                content: "'%s' of %x.1 is %y.4",
                shifts: {
                    x: -60,
                    y: 25
                }
            }
        };
    }

    fetchSystemFlow();
    setInterval("fetchSystemFlow()", 10000);

});
function getNum(param) {
    if (param >= 1) {
        return param * 2;
    } else if (param >= 0.1) {
        return param * 4;
    } else if (param >= 0.01) {
        return param * 8;
    } else if (param < 0.01) {
        return param * 16;
    }
}

//Flot Moving Line Chart
$(function () {
    var container = $("#flot-line-chart-moving");
    // Determine how many data points to keep based on the placeholder's initial size;
    // this gives us a nice high-res plot while avoiding more than one point per pixel.
    var maximum = container.outerWidth() / 2 || 300;

    var data = [];
    var realtimeTrafficList = [];

    // Update the random dataset at 25FPS for a smoothly-animating chart
    var updateRandomTimer = setInterval(function updateRandom() {
        if (typeof tHuge1 != "undefined") {
            series[0].data = getRandomData(10, 1);
            plot.setData(series);
            plot.draw();
        }
    }, 500);

    function getRandomData(iPartition, flag) {
        if (typeof tHuge1 != "undefined" && flag == 1) {
            var chaBytes = tHuge1;
            var oldBytes = findtempa;
            var chaBytesByS = 0;
            if (chaBytes > 0 && chaBytes < oldBytes) {

                if (chaBytes > 1024 * 1024) {
                    // 线上
                    chaBytes = chaBytes / 1024;
                } else {
                    // 测试
                    chaBytes = chaBytes * 10;
                }


                chaBytesByS = chaBytes / iPartition;
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
                // console.log(datas.length);
                // console.log(datas);
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
            max: 16000
        },
        legend: {
            show: true
        }
    });


});

var tHuge1, tHuge2, tHuge3, tHuge4;
var huge1 = $("#huge1");
var huge2 = $("#huge2");
var huge3 = $("#huge3");
var huge4 = $("#huge4");


var findtempa = 0;
var findtempb = 0;
var findtempc = 0;
var findtempd = 0;


function removeAll() {
    $("#myTbody").empty();
}

function fetchStaticsData() {
    var url = "/statistics/totalBytes";
    $.ajax({
        url: url,
        type: "GET",
        data: {
            "formerBytes": findtempa,
            "formerLogs": findtempb,
            "formerMonitors": findtempc,
            "formerAlarms": findtempd,
        },
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                tHuge1 = data.obj[0].differenceValues;
                tHuge2 = data.obj[1].differenceValues;
                tHuge3 = data.obj[2].differenceValues;
                tHuge4 = data.obj[3].differenceValues;

                var iHuge1 = parseInt(findtempa) + parseInt(tHuge1);
                var iHuge2 = parseInt(findtempb) + parseInt(tHuge2);
                var iHuge3 = parseInt(findtempc) + parseInt(tHuge3);
                var iHuge4 = parseInt(findtempd) + parseInt(tHuge4);

                if (iHuge1 < 0) {
                    findtempa = 0;
                    huge1.text(0);
                } else {
                    findtempa = iHuge1;
                    if (iHuge1 > 100000) {
                        huge1.text((iHuge1 / 1024 / 1024 / 1024).toFixed(2));
                    } else {
                        huge1.text(iHuge1);
                    }
                }
                if (iHuge2 < 0) {
                    huge2.text(0);
                    findtempb = 0;
                } else {
                    findtempb = iHuge2;
                    if (iHuge2 > 100000) {
                        huge2.text(iHuge2.toString().substring(0, 5) + "..");
                    } else {
                        huge2.text(iHuge2);
                    }
                }
                if (iHuge3 < 0) {
                    huge3.text(0);
                    findtempc = 0;
                } else {
                    findtempc = iHuge3;
                    if (iHuge3 > 100000) {
                        huge3.text(iHuge3.toString().substring(0, 5) + "..");
                    } else {
                        huge3.text(iHuge3);
                    }
                }
                if (iHuge4 < 0) {
                    huge4.text(0);
                    findtempd = 0;
                } else {
                    findtempd = iHuge4;
                    if (iHuge4 > 100000) {
                        huge4.text(iHuge4.toString().substring(0, 5) + "..");
                    } else {
                        huge4.text(iHuge4);
                    }
                }

            } else {
            }
        }, error: function (jqXHR, textStatus, errorThrown) {
            // console.log("fail.......");
            tHuge1 = undefined;
        }
    });
}

function splitStaticsDataIntoParts(iPatition) {
    if (typeof tHuge1 != "undefined") {
        var iHuge1 = parseInt(huge1.text()) + parseInt(tHuge1 / iPatition);
        if (iHuge1 < 0) huge1.text(0);
        else huge1.text(iHuge1);

    }
    if (typeof tHuge2 != "undefined") {
        var iHuge2 = parseInt(huge2.text()) + parseInt(tHuge2 / iPatition);
        if (iHuge2 < 0) huge2.text(0);
        else huge2.text(iHuge2);
    }
    if (typeof tHuge3 != "undefined") {
        var iHuge3 = parseInt(huge3.text()) + parseInt(tHuge3 / iPatition);
        if (iHuge3 < 0) huge3.text(0);
        else huge3.text(iHuge3);
    }
    if (typeof tHuge4 != "undefined") {
        var iHuge4 = parseInt(huge4.text()) + parseInt(tHuge4 / iPatition);
        if (iHuge4 < 0) huge4.text(0);
        else huge4.text(iHuge4);
    }
}
function onmouse1() {
    var inHtml = "<span>" + findtempa + "&nbsp;B</span><br/><span>" + (findtempa / 1024).toFixed(2) + "&nbsp;KB</span></br><span>" + (findtempa / 1024 / 1024).toFixed(2) + "&nbsp;MB</span></br><span>" + (findtempa / 1024 / 1024 / 1024).toFixed(2) + "&nbsp;GB</span></br><span>" + (findtempa / 1024 / 1024 / 1024 / 1024).toFixed(2) + "&nbsp;TB</span>"

    $("#han1").append(inHtml);
}
function outmouse1() {
    $("#han1").empty();
}
function onmouse2() {
    var inHtml = "<span>" + findtempb + "&nbsp;条</span>"

    $("#han2").append(inHtml);
}
function outmouse2() {
    $("#han2").empty();
}
function onmouse3() {
    var inHtml = "<span>" + findtempc + "&nbsp;次</span>"

    $("#han3").append(inHtml);
}
function outmouse3() {
    $("#han3").empty();
}
function onmouse4() {
    var inHtml = "<span>" + findtempd + "&nbsp;次</span>"

    $("#han4").append(inHtml);
}
function outmouse4() {
    $("#han4").empty();
}

function onmousewt() {
    $("#websocketContent").show();
}
function outmousewt() {
    $("#websocketContent").hide();
}

var list = new Array();
//调用接口查询系统日志数据
function fetchSystemFlow() {
    $.ajax({
        url: '/statisticsmonitor/systemflow',
        type: 'GET',
        dataType: 'json',
        data: {}
    })
        .done(function (message) {
            if (message.success) {
                removeAll();
                var powerHtml;
                var i = 0;
                if (list.length == 0) {
                    var a = 0;
                    for (var prop in message.obj) {
                        if (message.obj.hasOwnProperty(prop)) {
                            list[a] = message.obj[prop];
                            a++;
                        }
                    }
                } else {
                    var a = 0;
                    for (var prop in message.obj) {
                        if (message.obj.hasOwnProperty(prop)) {
                            if (list[a] == message.obj[prop]) {
                                message.obj[prop] = "<font color='#7fff00'>" + message.obj[prop] + "</font>";
                                a++;
                            } else {
                                list[a] = message.obj[prop];
                                message.obj[prop] = "<font color='red'>" + message.obj[prop] + "</font>";
                                a++;
                            }
                        }
                    }
                }
                // console.log(message.obj);
                for (var prop in message.obj) {
                    if (message.obj.hasOwnProperty(prop)) {
                        i++;
                        powerHtml += "<tr><td>" + prop + "</td><td>" + message.obj[prop] + "</td></tr>";
                    }
                }
                $("#dataTables-flow").append(powerHtml);
            } else {
                console.log("一般不可能");
            }
        })
        .fail(function () {
            console.log("error");
        })
        .always(function () {
            // console.log("complete");
        });

    // console.log(list);
}

var websocket = null;
createWebSocket();
initWebSocket();
function createWebSocket() {
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        // websocket = new WebSocket("ws://localhost:8080/webSockets/alarm/alarmNotifySocket");
        // 上线修改
        websocket = new WebSocket("ws://argus-mis.monitor.corp/webSockets/alarm/alarmNotifySocket");
    }
}

function initWebSocket() {
    if (websocket) {
        //连接发生错误的回调方法
        websocket.onerror = function () {
            setMessageInnerHTML("websocket:error");
        };
        //连接成功建立的回调方法
        websocket.onopen = function (event) {
            setMessageInnerHTML("连接成功，等待报警通知");
        }
        //接收到消息的回调方法
        websocket.onmessage = function (event) {
            setMessageInnerHTML(event.data);
        }
        //连接关闭的回调方法
        websocket.onclose = function () {
            setMessageInnerHTML("连接失败，服务器断开");
            setTimeout("restartWebSocket()", 10000);
        }
    }
}

function restartWebSocket() {
    setMessageInnerHTML("重启连接。。。。");
    createWebSocket();
    if (websocket) {
        initWebSocket();
    }
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    $("#websocketTitle")[0].innerHTML = "最新报警提醒(" + getNowDate() + ")";
    $("#websocketMessage")[0].innerHTML = innerHTML + '<br/>';
}
//关闭连接
function closeWebSocket() {
    websocket.close();
}
//发送消息
function send() {
    websocket.send("connect====");
}
function getNowDate() {
    var myDate = new Date();
    var mytime = myDate.toLocaleString();
    return mytime;
}
//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    websocket.close();
}