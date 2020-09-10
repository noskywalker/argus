/**
 * Created by xuefei on 7/14/16.
 */
$(document).ready(function () {

    var hostcount = 0;
    $(document).on('click', '.btn-add', function (e) {
        e.preventDefault();

        var controlForm = $('.controls'),
            currentEntry = $(this).parents('.entry:first'),
            newEntry = $(currentEntry.clone()).appendTo(controlForm);

        newEntry.find('input').val('');

        var input_name = newEntry.find('input')[0].name;
        var reg = /(\d+)/i;
        var entry_count = parseInt(reg.exec(input_name)[0]);
        hostcount = entry_count > hostcount ? entry_count : hostcount;

        var inputLength = newEntry.find('input').length;
        for(var i = 0;i<inputLength;i++) {
            newEntry.find('input')[i].name = newEntry.find('input')[i].name.replace(hostcount, hostcount + 1);
            newEntry.find('input')[i].id = newEntry.find('input')[i].id.replace(hostcount, hostcount + 1);
        }

        hostcount++;
        controlForm.find('.entry:not(:last) .btn-add')
            .removeClass('btn-add').addClass('btn-remove')
            .removeClass('btn-success').addClass('btn-danger')
            .html('<span class="glyphicon glyphicon-minus"></span>');
    }).on('click', '.btn-remove', function (e) {
        $(this).parents('.entry:first').remove();

        var name = $(this).parents('.entry:first').find('input')[0].name;
        var reg = /(\d+)/i;
        var index = parseInt(reg.exec(name)[0]);
        var entry = $('.entry');
        for (var i = index; i < entry.length; i++) {
            var origin = parseInt(reg.exec($(entry[i]).find('input')[0].name)[0]);
            var inputLength = $(entry[i]).find('input').length;
            for(var j = 0;j < inputLength;j++) {
                $(entry[i]).find('input')[j].name = $(entry[i]).find('input')[j].name.replace(origin, origin - 1);
                $(entry[i]).find('input')[j].id = $(entry[i]).find('input')[j].id.replace(origin, origin - 1);
            }
        }
        hostcount--;
        e.preventDefault();
        return false;
    });
});

var $template = $(".template");

var hash = 1;

$(".btn-add-panel").on("click", function () {
    event.preventDefault();
    var $newPanel = $($template[$template.length-1]).clone();
    $newPanel.find('input').val('');
    $newPanel.find('button').remove();
    var panelText = $newPanel.find(".accordion-toggle").text();
    var reg = /\d+/i;
    var index = parseInt(reg.exec(panelText)[0]);
    hash = index>hash ? index : hash;
    $newPanel.find(".collapse").removeClass("in");
    $newPanel.find(".accordion-toggle").attr("href", "#collapse" + (++hash))
        .text("监控策略 #" + hash);
    $newPanel.find(".panel-collapse").attr("id", "collapse" + hash).addClass("collapse").removeClass("in");

    var inputArray = $newPanel.find('input');
    var selectArray = $newPanel.find('select');

    for(var i = 0;i < inputArray.length;i++) {
        inputArray[i].name = inputArray[i].name.replace(hash-2, hash-1);
        inputArray[i].name = inputArray[i].name.replace(hash-2, hash-1);
        inputArray[i].name = inputArray[i].name.replace(hash-2, hash-1);
        inputArray[i].name = inputArray[i].name.replace(hash-2, hash-1);

        inputArray[i].id = inputArray[i].id.replace(hash-2, hash-1);
        inputArray[i].id = inputArray[i].id.replace(hash-2, hash-1);
        inputArray[i].id = inputArray[i].id.replace(hash-2, hash-1);
        inputArray[i].id = inputArray[i].id.replace(hash-2, hash-1);
    }


    for (var i = 0; i < selectArray.length; i++) {
        selectArray[i].name = selectArray[i].name.replace(hash - 2, hash - 1);
        selectArray[i].name = selectArray[i].name.replace(hash - 2, hash - 1);
        selectArray[i].name = selectArray[i].name.replace(hash - 2, hash - 1);
        selectArray[i].name = selectArray[i].name.replace(hash - 2, hash - 1);

        selectArray[i].id = selectArray[i].id.replace(hash - 2, hash - 1);
        selectArray[i].id = selectArray[i].id.replace(hash - 2, hash - 1);
        selectArray[i].id = selectArray[i].id.replace(hash - 2, hash - 1);
        selectArray[i].id = selectArray[i].id.replace(hash - 2, hash - 1);
    }


    $("#accordion1").append($newPanel.fadeIn());

    var template = $("#accordion1").find('.template');
    if(template.length >= 1) {
        $newPanel.find('.panel-body').append("<button class='btn btn-lg btn-primary btn-danger btn-remove-panel' id='removePanel"+ hash + "'\>" + "<i class='glyphicon glyphicon-minus'></i>" + " 删除该监控项 </button>");
    }

    $("#removePanel" + hash).on("click", function () {
        event.preventDefault();
        var removePanel = $(this.parentElement.parentElement.parentElement);
        removePannelAction(removePanel);
    });

    $template = $(".template");

});


$(".btn-remove-panel").on("click", function () {
    event.preventDefault();
    var removePanel = $(this.parentElement.parentElement.parentElement);
    removePannelAction(removePanel);
});

$("#addMonitorForm").submit(function (event) {
    event.preventDefault();
    var $btn = $('#saveBtn');
    $btn.button('loading');
    var url = "/monitor/addnew";
    $.ajax({
        url: url,
        type: "POST",
        data: $("#addMonitorForm").serialize(),
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                $(".modal-body").html(data.msg);
                $("#diaglogOK").click(function (event) {
                    location.reload(true);
                });
            } else {
                var msg = data.errorMessages.join('\n');
                $(".modal-body").html(msg);
            }
            var dialog = $('#dialog');
            if (dialog != null) {
                dialog.modal({
                    backdrop: 'static'
                });
            }
            $btn.button('reset');
        }, error: function (jqXHR, textStatus, errorThrown) {
            alert("异常！");
        }
    });
});

function removePannelAction(removePanel) {
    var length = $("#accordion1").find('.panel').length;
    console.log("目前总共数量:" + length);
    var removedHeader = removePanel.find(".accordion-toggle").text();
    var index = getNumberFromStrategyHeader(removedHeader);
    console.log("删掉第:" + index + "个");
    for (var i = index; i < length; i++) {
        var panel = $("#accordion1").find('.panel')[i];
        var toggle = $(panel).find(".accordion-toggle");
        var needChangeIndex = getNumberFromStrategyHeader(toggle.text());
        toggle.text(toggle.text().replace(needChangeIndex, needChangeIndex - 1));
        console.log("后面的第" + needChangeIndex + "个,需要变为第" + (needChangeIndex - 1) + "个");

        // change collapse
        toggle.attr("href", toggle.attr("href").replace(needChangeIndex, (needChangeIndex - 1)));
        var panelCollapse = $("#collapse" + needChangeIndex);
        panelCollapse.attr("id", panelCollapse.attr("id").replace(needChangeIndex, (needChangeIndex - 1)));

        // change remove button id
        var removeButton = $("#removePanel" + needChangeIndex);
        removeButton.attr("id", removeButton.attr("id").replace(needChangeIndex, (needChangeIndex - 1)));


        var inputArray = $(panel).find('input');
        var selectArray = $(panel).find('select');

        for (var j = 0; j < inputArray.length; j++) {
            inputArray[j].name = inputArray[j].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].name = inputArray[j].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].name = inputArray[j].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].name = inputArray[j].name.replace(needChangeIndex - 1, (needChangeIndex - 2));

            inputArray[j].id = inputArray[j].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].id = inputArray[j].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].id = inputArray[j].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            inputArray[j].id = inputArray[j].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
        }

        for (var k = 0; k < selectArray.length; k++) {
            selectArray[k].name = selectArray[k].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].name = selectArray[k].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].name = selectArray[k].name.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].name = selectArray[k].name.replace(needChangeIndex - 1, (needChangeIndex - 2));

            selectArray[k].id = selectArray[k].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].id = selectArray[k].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].id = selectArray[k].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
            selectArray[k].id = selectArray[k].id.replace(needChangeIndex - 1, (needChangeIndex - 2));
        }

    }
    hash = length - 1;
    console.log("hash:" + hash);

    // unbind remove button event
    // btnRemove.unbind("click");
    removePanel.remove();

    // check the panel count, if equal 1, remove "删除监控项"
    length = $("#accordion1").find('.panel').length;
    if(length == 1) {
        var firstPanel = $("#accordion1").find('.panel')[0];
        var btn_remove_panel = $(firstPanel).find('.btn-remove-panel');
        btn_remove_panel.remove();
    }
}

function getNumberFromStrategyHeader(name) {
    var reg = /\d+/i;
    var index = parseInt(reg.exec(name)[0]);
    return index;
}