$(function(){
	//var baseUrl = "http://10.141.4.56:28080/";
	var baseUrl = "http://argus-mistestmonitor.corp/";

	function totalBytesChart(){
		var myChart = echarts.init(document.getElementById('foot'));
		var num;
		var formerBytes = 0;
		var formerMonitors = 0;
		var formerAlarms = 0;
		var formerLogs = 0;
		var formerValue = 0;
		var no = new Date()
		var data = [];
		var po = new process();

		data.push({
			name: no.getTime(),
			value: [no.getTime(), formerBytes]
		});

		var option = {
			tooltip: {
				trigger: 'axis',
			},
			xAxis: {
				show: false,
				type: 'time',
				splitLine: {
					show: false
				},
			},
			yAxis: {
				show: true,
				type: 'value',
				boundaryGap: [0, '80%'],
				splitLine: {
					show: true,
					lineStyle: {
						color: ["#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2","#F2F2F2"],
						width: 1,
						type: "solid",
					}
				},
				// axisLine: {
				// 	show: true,
				// 	lineStyle: {
				// 		color: "#F2F2F2",
				// 	}
				// }
			},
			color: ["#4082FF"],
			series: [{
				type: 'line',
				showSymbol: true,
				symbol: "none",
				data: data,
			}]
		};

		myChart.setOption(option);

		function totalBytesSuccess(arr){
			po.stop();

			//no = new Date();
			formerValue = arr[0].currentValues;
			formerBytes = arr[0].currentValues - arr[0].formerValues;
			formerMonitors = arr[1].differenceValues;
			formerAlarms = arr[2].differenceValues;
			formerLogs = arr[3].differenceValues;

			$("#logsWord").html(formetLogsTotal(arr[0].currentValues));
			$("#logsNum").html(formetLogsNum(arr[1].currentValues));

			po.start();
		}

		function process(){
			var num;

			function getStart(){
				var temp = formerBytes / 10 + getRandom();
				temp = temp / 1024;

				no = new Date();

				if(data.length > 200){
					data.shift();
					data.push({
						name: no.getTime(),
						value: [no.getTime(), temp] 
					});
				}else{
					data.push({
						name: no.getTime(),
						value: [no.getTime(), temp] 
					});
				}

				if(data.length > 300){
					data.shift();
					data.push({
						name: no.getTime(),
						value: [no.getTime(), temp] 
					});
				}else{
					data.push({
						name: no.getTime(),
						value: [no.getTime(), temp] 
					});
				}


				myChart.setOption({
					series: [{
						data: data,
					}]
				});
			}

			function getRandom(){
				var p = Math.round(Math.random() * 2 + 1);
				var po = Math.pow(-1, p);
				var random = po * (formerBytes / 150) * Math.random();
				
				return random;
			}

			this.start = function(){
				num = window.setInterval(getStart, 1000);
			};

			this.stop = function(){
				window.clearInterval(num);
			};
		}

		function totalBytes(){
	
			$.ajax({
				url: baseUrl + 'statistics/totalBytes',
				type: 'GET',
				dataType: 'json',
				data: {formerBytes: formerValue, formerMonitors: formerMonitors, formerAlarms: formerAlarms, formerLogs: formerLogs}
			})
			.done(function(data) {
				if(data.success){
					totalBytesSuccess(data.obj);
				}else{
					console.log("一般不可能");
				}
			})
			.fail(function() {
				console.log("error");
			});
		}

		this.start = function(){
			totalBytes();

			num = setInterval(totalBytes, 1000 * 10);
		};

		this.stop = function(){
			clearInterval(num);
		};
	}

	function formetLogsTotal(value){
		var arr;
		var len;
		var temp = "";

		value = value / 1024 / 1024 / 1024;
		value = value + "";
		value = value.split(".");
		arr = value[0];
		arr = arr.split("").reverse();
		len = arr.length;
		while(len - 3 > 0){
			temp += arr.shift();
			temp += arr.shift();
			temp += arr.shift();
			temp += ",";
			len -= 3;
		}
		for(var i = 0;i < len;i++){
			temp += arr.shift();
		}
		temp = temp.split("").reverse().join("");
		
		if(value[1]){
			temp += "." + value[1].substring(0,3) + "GB";
		}else{
			temp += "GB";
		}
		

		return temp;
	}

	function formetLogsNum(value){
		var temp = "";

		value = value + "";
		value = value.split("").reverse().join("");

		if(value.length > 8){
			temp += value.substring(0, 4) + "万";
			temp += value.substring(4, 8) + "亿";

			for(var i = 8;i < value.length;i++){
				temp += value.substring(i, i+1);
			}
	
			return temp.split("").reverse().join("");
		}

		if(value.length > 4){
			temp += value.substring(0,4) + "万";

			for(var i = 4;i < value.length;i++){
				temp += value.substring(i, i+1);
			}
		}
		
		return temp.split("").reverse().join("");
	}

	function timeer(d,h){
		var num;

		function setTime(){
			var time = new Date().toLocaleString();
			var arr = time.split('/');
			var hour = arr[2].split(" ");
			var timeDate = arr[0] + "年" + arr[1] + "月" + hour[0] + "日";
	
			$("#" + d).html(timeDate);
			$("#" + h).html(hour[1]);
		}

		this.start = function(){
			num = setInterval(setTime, 1000);
		};

		this.stop = function(){
			clearInterval(num);
		};
	}

	///flow的代码
	function hanFlow(){
		var data1 = [];
		var data2 = [];
		var data3 = [];
		var num;
		var myChart = echarts.init(document.getElementById("flowFoot"));
		var option = {
			tooltip: {
				trigger: 'axis',
			},
			legend: {
				data: [{
					icon: 'bar'
				},'今日流量', '昨日流量'],
				right: '10%',
			},
			xAxis: {
				show: true,
				type: 'category',
				data: data3,
				boundaryGap: [0, '100%'],
				splitLine: {
					show: false
				}
			},
			yAxis: {
				show: true,
				type: 'value',
				boundaryGap: [0, '100%'],
				splitLine: {
					show: true,
					lineStyle: {
						color: ["#D6D6AD", "#D6D6AD", "#D6D6AD", "#D6D6AD", "#D6D6AD", "#D6D6AD", "#D6D6AD","#D6D6AD"],
						width: 1,
						type: "solid",
					}
				},
			},
			color: ["#008BFF", "#BB3D00"],
			series: [
				{
					name: "今日流量",
					type: 'line',
					data: data1,
					showSymbol: true,
					itemStyle : { normal: {label : {show: true}}}
				
				},
				{
					name: "昨日流量",
					type: "line",
					showSymbol: true,
					data: data2,
					itemStyle : { normal: {label : {show: true}}}
				}
			]
		};

		myChart.setOption(option);
		
		function getFlowData(){
			$.ajax({
				url: baseUrl + 'statisticsmonitor/dayinfo',
				type: 'GET',
				dataType: 'json',
			})
			.done(function(data) {
				if(data.success){
					processData(data.obj);
				}else{
					console.log("一般不肯能");
				}
			})
			.fail(function() {
				console.log("调用接口失败");
			});			
		}

		function processData(obj){
			data1 = [];
			data2 = [];
			data3 = [];


			for(var i = 0;i < obj.todaydata.length;i++){
				data1.push(obj.todaydata[i]);
				data2.push(obj.yesterdaydata[i]);
				data3.push(obj.todaytime[i]);
			}
			myChart.setOption({
				xAxis: {
					data: data3,
				},
				series: [
					{
						data: data1,
					},
					{
						data: data2,
					}
				]
			});
		}

		this.start = function(){
			getFlowData();
			num = setInterval(getFlowData, 1000 * 60 * 60);
		};

		this.stop = function(){
			clearInterval(num);
		};
	}

	//warns的代码
	function warnTends(){
		var data1 = [];
		var data2 = [];
		var num;
		var myChart = echarts.init(document.getElementById("warnFoot"));
		var option = {
			tooltip: {
				trigger: 'axis',
				axisPointer: {
					type: 'shadow'
				}
			},
			xAxis: {
				type: "category",
				data: data1,
				show: true,
				axisTick: {
					alignWithLabel: true
				},
				axisLine: {
					show: false
				},
				splitLine: {
					show: false
				},
				z: 10,
			},
			yAxis: {
				show: true,
				type: 'value',
				boundaryGap: [0, '100%'],
				splitLine: {
					show: true,
					lineStyle: {
						color: ["#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2","#F2F2F2"],
						width: 1,
						type: "solid",
					}
				},
			},
			color: ["#0067CC"],
			series: [
			{
				type: 'bar',
				showSymbol: true,
				barWidth: '30%',
				itemStyle : { normal: {label : {show: true, position: 'top',textStyle: {
					color: '#000000'
				} }}}
			}
			]
		};
		myChart.setOption(option);

		function getWarnTendsData(){
			$.ajax({
				url: baseUrl + '/statisticsmonitor/monitortimes',
				type: 'GET',
				dataType: 'json',
			})
			.done(function(data) {
				if(data.success){
					processWarnTendsData(data.obj);
				}else{
					console.log("基本不可能");
				}
			})
			.fail(function() {
				console.log("error");
			});
			
		}

		function processWarnTendsData(arr){
			for(var i = 0;i < arr.alarm.length;i++){
				data1.push(arr.time[i]);
				data2.push(arr.alarm[i]);
			}

			myChart.setOption({
				xAxis: {
					data: data1,
				},
				series: [
					{
						data: data2,
					}
				]
			})
		}

		this.start = function(){
			getWarnTendsData();

			num = window.setInterval(getWarnTendsData, 1000 * 3600 * 24);
		};

		this.stop = function(){
			window.clearInterval(num);
		};
	}

	//monitor的代码
	function appPayCenter(){
		var num1, num2;
		var dataApp1 = []; 
		var dataApp2 = [];
		var dataPay1 = [];
		var dataPay2 = [];
		var myChartApp = echarts.init(document.getElementById("pvFoot"));
		var myChartPay = echarts.init(document.getElementById("appFoot"));
		var optionApp = {
			title: {
				show: true,
				text: "理财APP访问频率",
				left: "40%",
				top: "10%",
			},
			tooltip: {
				trigger: 'axis',
			},
			xAxis: {
				data: dataApp1,
				show: true,
				type: 'category',
				splitLine: {
					show: false
				}
			},
			yAxis: {
				show: true,
				type: 'value',
				boundaryGap: [0, '100%'],
				splitLine: {
					show: true,
					lineStyle: {
						color: ["#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2","#F2F2F2"],
						width: 1,
						type: "solid",
					}
				},
			},
			color: ["#FF3368"],
			series: [{
				type: 'line',
				showSymbol: true,
				data: dataApp2,
				itemStyle : { normal: {label : {show: true}}}
			}]
		};
		myChartApp.setOption(optionApp);

		var optionPay = {
			title: {
				show: true,
				text: "支付页面访问频率",
				left: "40%",
				top: "10%",
			},
			tooltip: {
				trigger: 'axis',
			},
			xAxis: {
				data: dataPay1,
				show: true,
				type: 'category',
				splitLine: {
					show: false
				}
			},
			yAxis: {
				show: true,
				type: 'value',
				boundaryGap: [0, '100%'],
				splitLine: {
					show: true,
					lineStyle: {
						color: ["#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2", "#F2F2F2","#F2F2F2"],
						width: 1,
						type: "solid",
					}
				},
			},
			color: ["#FF790D"],
			series: [{
				type: 'line',
				showSymbol: true,
				data: dataPay2,
				itemStyle : { normal: {label : {show: true}}}
			}]
		};
		myChartPay.setOption(optionPay);

		function getAppData(){
			$.ajax({
				url: baseUrl + '/node/sumnode',
				type: 'GET',
				dataType: 'json',
				data: {nodeKey: 'c198d4e8c89748b987ce102feb149f75'},
			})
			.done(function(data) {
				if(data.success){
					processAppData(data.obj)
				}else{
					console.log("一般不肯能");
				}
			})
			.fail(function() {
				console.log("error");
			});
		}

		function getPayData(){
			$.ajax({
				url: baseUrl + 'node/sumnode',
				type: 'GET',
				dataType: 'json',
				data: {nodeKey: '79b12190311342edbdeda7d204b6bc81'},
			})
			.done(function(data) {
				if(data.success){
					processPayData(data.obj);
				}else{
					console.log("一般不肯能");
				}
			})
			.fail(function() {
				console.log("error");
			});
		}

		function processAppData(arr){
			dataApp1 = [];
			dataApp2 = [];

			for(var i = 0;i < arr.mtoday.length;i++){
				dataApp1.push(arr.mtime[i]);
				dataApp2.push(arr.mtoday[i]);
			}
			myChartApp.setOption({
				xAxis: {
					data: dataApp1
				},
				series: [{
					data: dataApp2,
				}]
			});
		}

		function processPayData(arr){
			dataPay1 = [];
			dataPay2 = [];

			for(var i = 0;i < arr.mtoday.length;i++){
				dataPay1.push(arr.mtime[i]);
				dataPay2.push(arr.mtoday[i]);
			}

			myChartPay.setOption({
				xAxis: {
					data: dataPay1,
				},
				series: [{
					data: dataPay2,
				}]
			});
		}

		this.start = function(){
			getAppData();
			getPayData();

			num1 = window.setInterval(getAppData, 1000 * 60 * 5);
			num2 = window.setInterval(getPayData, 1000 * 60 * 5);
		};

		this.stop = function(){
			window.clearInterval(num1);
			window.clearInterval(num2);
		};
	}

	//报警通知的代码
	function warnNotify(){
		var socket = null;
		var ft = new floatTimer();

		function floatTimer(){
			var num;

			function setFloatToUnsee(){
				$("#floated").css('display', 'none');
			}

			this.start = function(){
				$("#floated").css('display', 'block');
				num = setTimeout(setFloatToUnsee, 1000 * 60 * 5);
			};

			this.stop = function(){
				clearTimeout(num);
			};
		}

		function floatedUnusual(value){
			var inHtml = value + "(" + new Date().toTimeString().substring(0,8) + ")";

			ft.stop();
			$("#floatWord2").html(inHtml);
			ft.start();
		}

		this.start = function(){
			if(!window.WebSocket){
				window.WebSocket = window.MozWebSocket;
			}
			if(!window.WebSocket){
				console.log("浏览器不支持websocket");

				return;
			}

			socket = new WebSocket("ws://argus-mistestmonitor.corp/webSockets/alarm/alarmNotifySocket");

			while(socket != null && socket.readyState == WebSocket.OPEN){
				socket = new WebSocket("ws://argus-mistestmonitor.corp/webSockets/alarm/alarmNotifySocket");
			}

			socket.onopen = function(){

			};

			socket.onmessage = function(event){
				var msg = $.parseJSON(event.data);

				floatedUnusual(msg.monitorName);
			};

			socket.onerror = function(){
				while(socket != null && socket.readyState == WebSocket.OPEN){
					socket = new WebSocket("ws://argus-mistestmonitor.corp/webSockets/alarm/alarmNotifySocket");
				}
			};

			socket.onclose = function(){
				socket.close();
				socket = null;
			};
		};	

		this.stop = function(){
			socket.send("close");
			socket.close();
			socket = null;
		};
	}

	//轮播按钮的代码
	function takeTurns(){
		var num;
		var arr = ["logsAndFlow", "flowControll", "warnTends", "pvControll"];
		var i = 1;
		var totalBy = new totalBytesChart();
		var totalByTim = new timeer("timeDate", "timeHour");
		var hf = new hanFlow();
		var hfTime = new timeer("pvTimeDate", "pvTimeHour");
		var wt = new warnTends();
		var wtTim = new timeer("warnTimeDate", "warnTimeHour");
		var apc = new appPayCenter();
		var apcTime = new timeer("flowTimeDate", "flowTimeHour");
		var wn = new warnNotify();

		function turn(){
			var j = i == 0 ? 3 : i -1;

			$("#" + arr[i]).fadeIn();
			$("#" + arr[j]).fadeOut();
			i = ++i == 4 ? 0 : i;
		}

		this.start = function(){
			totalBy.start();
			hf.start();
			wt.start();
			apc.start();

			totalByTim.start();
			hfTime.start();
			wtTim.start();
			apcTime.start();

			wn.start();

			$("#" + arr[1]).css('display', 'none');
			$("#" + arr[2]).css('display', 'none');
			$("#" + arr[3]).css('display', 'none');

			num = window.setInterval(turn, 1000 * 30);
		};

		this.stop = function(){
			window.clearInterval(num);
		};
	}

	var tt = new takeTurns();

	tt.start();
});














