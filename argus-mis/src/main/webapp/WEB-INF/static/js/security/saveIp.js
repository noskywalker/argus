/**
 * Created by xuefei on 7/14/16.
 */
function saveBlackIp() {
    $.get("/security/monitor/filterIplist/downloadIp", {
        "type": "1",
    }, function (result) {
        var host = result.host;
        window.location = host + "/downloadIp?type=1"
    }, "json")
}
