$(document).ready(function () {

    $("#btn-search").click(function(){
                  console.log("Clicked")
                  $(".stockRow").remove();
                  fire_ajax_submit();
     });

     fire_ajax_submit();
     loadStocks();

     $("#addStock").click(function(){
           var sn= $("#stockName").val();
           var amt= $("#amount").val();
           var u=$("#unit").val();
           var ts= $("#meeting-time").val();
           req={}
           if(sn !='' && $.isNumeric(amt)){
             req['name']=sn;
             currentPrice={}
             currentPrice['amount']=amt;
             currentPrice['unit']=u;
             currentPrice['currency']='EUR';
             req['currentPrice']=currentPrice;
              if(ts !='')
                 var d =new Date(ts);
                 req['lastUpdate']=d.valueOf();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/api/stocks",
        dataType: 'json',
        data:JSON.stringify(req),
        cache: false,
        timeout: 600000,
        success: function (data) {

            console.log("SUCCESS : ", data);
            $("#formFeedback").html("<h3 style=color:green;>Created</h3>");

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });
           }
           else{
             $("#formError").html("<h4 style=color:red;>All inputs are mandatory</h4><pre>");
           }
      });
      $('td', 'table').each(function(i) {
        $(this).text(i+1);
         });


});

function edit_row(no){
}
function fire_ajax_submit() {

    $("#btn-search").prop("disabled", true);

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/stocks",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $.each(data,function(i,item){
                 var date = new Date(item.lastUpdate);
                 var $tr = $('<tr class="stockRow">').append(
                            $('<td>').text(item.id),
                            $('<td>').text(item.name),
                            $('<td>').text(item.currentPrice.amount),
                            $('<td>').text(item.currentPrice.unit),
                            $('<td>').text(item.currentPrice.currency),

                            $('<td>').text(date.toString("MM-DD-YYYY HH:MM")),
                        );
                    $('#stockrecords').append($tr);
            });

            console.log("SUCCESS : ", data);
              $('#stockrecords').each(function() {
                      var currentPage = 0;
                      var numPerPage = 2;
                      var $table = $(this);
                      $table.bind('repaginate', function() {
                          $table.find('tbody tr').hide().slice(currentPage * numPerPage, (currentPage + 1) * numPerPage).show();
                      });
                      $table.trigger('repaginate');
                      var numRows = $table.find('tbody tr').length;
                      var numPages = Math.ceil(numRows / numPerPage);
                      console.log(numPages);
                      var $pager = $('<div class="pager"></div>');
                      for (var page = 0; page < numPages; page++) {
                          $('<span class="page-number"></span>').text(page + 1).bind('click', {
                              newPage: page
                          }, function(event) {
                              currentPage = event.data['newPage'];
                              $table.trigger('repaginate');
                              $(this).addClass('active').siblings().removeClass('active');
                          }).appendTo($pager).addClass('clickable');
                      }
                      $pager.insertBefore($table).find('span.page-number:first').addClass('active');
                  });

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}

function loadStocks(){

 $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/stock/names",
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            $.each(data,function(i,item){
                 var date = new Date(item.lastUpdate);
                 $('#stocks').append($("<option>").val(item).text(item););
            });

            console.log("SUCCESS : ", data);
            var firstData=$("#stocks").val($("#stocks option:first").val());
            time=['x'];
            amount=['amount'];

             $.ajax({
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/stocks/"+firstData,
                    dataType: 'json',
                    cache: false,
                    timeout: 600000,
                    success: function (data) {
                        $.each(data,function(i,item){
                           time.append(item.lastUpdate);
                           amount.append(item.currentPrice.amount);
                        });

                        console.log("SUCCESS : ", data);
                        c3.generate({
                            bindTo:'#charts',
                            data: {
                                x: 'x',
                                columns: [
                                    time,
                                    amount
                                ]
                            },
                            axis: {
                                x: {
                                    type: 'timeseries',
                                    tick: {
                                        format: '%Y-%m-%d hh:mm'
                                    }
                                }
                            }
                        });


                    },
                    error: function (e) {

                        var json = "<h4>Ajax Response</h4><pre>"
                            + e.responseText + "</pre>";
                        $('#feedback').html(json);

                        console.log("ERROR : ", e);
                        $("#btn-search").prop("disabled", false);

                    }
                });

        },
        error: function (e) {

            var json = "<h4>Ajax Response</h4><pre>"
                + e.responseText + "</pre>";
            $('#feedback').html(json);

            console.log("ERROR : ", e);
            $("#btn-search").prop("disabled", false);

        }
    });

}