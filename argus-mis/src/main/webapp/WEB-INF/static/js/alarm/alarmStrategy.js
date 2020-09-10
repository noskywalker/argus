// var alarmMsg;
// $(function () {
//     fetchDataAndInsert('', '', 1, 10);
//
// })
//
// function insertIntoTable(array) {
//     for (var i = 0; i < array.length && i < 10; i++) {
//         insertOneRow(array[i], i + 1);
//     }
// }
//
// function insertOneRow(row, i) {
//     var name = "alarmName" + i;
//     var type = "alarmType" + i;
//     var rowId = "row" + i;
//     var alarmStrategy = "alarmStrategy" + i;
//     var edit = "e" + i;
//
//     var trHtml = "<tr><td>" + i + "</td><td id=" + name + ">" + row.alarmName + "</td><td style=\"display:none;\" id=" + rowId + ">" + row.id + "</td><td id=" + type + ">" + row.type + "</td><td id=" + alarmStrategy + ">" + row.alarmStrategy + "</td><td>" + row.createDate + "</td><td><a href=@{'/alarm/edit/'" + row.id + "}> 编辑</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class='btn btn-info' id=" + i + ">删除</button></td></tr>"
//
//     $("#dataTables-user").append(trHtml);
//     $("#" + i).on('click', function () {
//         event.preventDefault();
//         onDeleteClick(i);
//     });
//
//     $("#e" + i).on('click', function (event) {
//         var myi = $("#row" + i).html();
//
//         console.log("id: " + myi);
//         window.location.href = "/alarm/add?id=" + myi;
//     });
// }
//
// function check() {
//     if ($("#paging").html() == 1) {
//         $("#previous").css('visibility', 'hidden');
//     } else {
//         $("#previous").css('visibility', 'visible');
//     }
// }
//
// function onPreviousClick() {
//     var alarmName = $("#alarmName").val();
//     var alarmType = $("#alarmType").val();
//     var page = $("#paging").html();
//
//     page--;
//
//     if (alarmName.length == 0) {
//         fetchDataAndInsert('', '', page, 10);
//     } else {
//         fetchDataAndInsert(alarmName, alarmType, page, 10);
//     }
// }
//
// function onNextClick() {
//     var alarmName = $("#alarmName").val();
//     var alarmType = $("#alarmType").val();
//     var page = $("#paging").html();
//
//     page++;
//
//     if (alarmName.length == 0) {
//         fetchDataAndInsert('', '', page, 10);
//     } else {
//         fetchDataAndInsert(alarmName, alarmType, page, 10);
//     }
// }
//
// function onQueryClick() {
//     var alarmName = $("#alarmName").val();
//     var alarmType = $("#alarmType").val();
//     fetchDataAndInsert(alarmName, alarmType, 1, 10);
// }
//
// function fetchDataAndInsert(alarmName, alarmType, page, rows) {
//     console.log(page + "" + rows);
//     $.ajax({
//         url: '/alarm/getAlarmStrategy',
//         type: 'GET',
//         contentType: 'application/json',
//         dataType: 'json',
//         data: {alarmName: alarmName, alarmType: alarmType, page: page, rows: rows}
//     })
//         .done(function (msg) {
//             alarmMsg = msg.obj;
//             console.log(msg);
//             if (msg.msg) {
//                 removeAll();
//                 insertIntoTable(msg.obj);
//                 $("#paging").html(page);
//                 check();
//             } else {
//                 console.log("一般不可能");
//             }
//             console.log("success");
//         })
//         .fail(function () {
//             console.log("error");
//         })
//         .always(function () {
//             console.log("complete");
//         });
// }
// function removeAll() {
//     $("#myTbody").empty();
// }
// function onDeleteClick(i) {
//     var confirm = window.confirm("您确定要删除此项系统功能吗？");
//
//     if (confirm) {
//         var id = $("#authId" + i).html();
//         console.log(id);
//         $.ajax({
//             url: '/system/func/delete',
//             type: 'GET',
//             dataType: 'json',
//             data: {funcId: id}
//         })
//             .done(function (msg) {
//                 console.log(msg);
//                 if (msg.success) {
//                     $("#tr" + i).remove();
//                 } else {
//                     alert("删除失败");
//                 }
//             })
//             .fail(function () {
//                 console.log("error");
//             })
//             .always(function () {
//                 console.log("complete");
//             });
//
//     }
// }
//
//
