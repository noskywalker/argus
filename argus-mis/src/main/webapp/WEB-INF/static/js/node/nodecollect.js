var parentJson;
var first;
var beginTime = $('#dtp_input1').val();
var endTime = $('#dtp_input2').val();

var dummyJsonData = function () {
    this.jsonFIFAHeaders = [{
        columns: [
            {dataIndex: 'date'},
            {dataIndex: 'nodeName'},
            {dataIndex: 'countpv'},
            {dataIndex: 'countuv'}
        ],
        dataObject: {date: '监控时间', nodeName: '节点名称', countpv: 'PV统计', countuv: 'UV统计'},
        trAttributeNames: ['classStyle', 'style'],
        trAttributeValueObject: {classStyle: 'headerbg', style: ''}
    }
    ];
    this.jsoninitNodes = getInfo();
};

function getInfo() {
    $.ajax({
        url: '/statisticsmonitor/getpvbydate',
        type: 'GET',
        dataType: 'json',
        data: {beginDate: beginTime, endDate: endTime},
        async: false
    }).done(function (message) {
        parentJson = message.obj;
    });
    return parentJson;

}

//ExpandNodeEvent
function fifaExpandNodeEvent(node, tree) {
    if (node.isLoad == false) {
        tree.startLoadingNode(node);
        //example for my dummy data logic
        var userObject = node.userObject;
        if (userObject != null) {
            var jsonName = userObject.jsonName;
            if (jsonName != null) {
                var jsonNodes = eval('new dummyJsonData().' + jsonName);
                if (jsonNodes != null) {
                    tree.loadingAddNodes(jsonNodes, node.id);
                }
            }
        }

        tree.endLoadingNode(node);
    }
}

//the flow of build tabletree

//create and config tabletree object
var fifaGirdTree = new Core4j.toolbox.TableTree4j({
    columns: [
        {isNodeClick: true, dataIndex: 'date', width: '25%'},
        {dataIndex: 'nodeName', width: '25%'},
        {dataIndex: 'countpv', width: '25%'},
        {dataIndex: 'countuv', width: '25%'},

    ],
    treeMode: 'gird',
    renderTo: 'pvCollect',
    useLine: true,
    useIcon: true,
    id: 'myworldcupgirdtree',
    useCookie: false,
    onExpandNodeEvents: [fifaExpandNodeEvent],
    headers: new dummyJsonData().jsonFIFAHeaders,
    //footers:jsonfooters,
    themeName: 'arrow',
    selectMode: 'single'
    //floatRight:true
});

//build tree by nodes
fifaGirdTree.build(new dummyJsonData().jsoninitNodes, true);


$('.form_date').datetimepicker({
    language: 'cn',
    weekStart: 0,
    todayBtn: true,
    autoclose: 1,
    todayHighlight: 0,
    startView: 2,
    minView: 2,
    maxView: 3,
    forceParse: 0,
    endDate: new Date(),
    startDate: addDays(new Date(), -30)
});

function onQueryClick() {
    beginTime = $('#dtp_input1').val();
    endTime = $('#dtp_input2').val();
    console.log(beginTime + "," + endTime);
    fifaGirdTree.rebuildTreeByNodes(new dummyJsonData().jsoninitNodes, true);
}

function addDays(nowDate, value) {
    nowDate.setDate(nowDate.getDate() + value);
    return nowDate;
}



