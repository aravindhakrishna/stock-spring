$(document).ready(function () {

    $("#btn-search").click(function(){
                  console.log("Clicked")
                  $(".stockRow").remove();
                  fire_ajax_submit();
     });

    $('#stocks').change(function(){
                 var optionSelected = $("option:selected", this);
                     var valueSelected = this.value;
                     console.log(valueSelected)
                     loadStocksGraph(valueSelected);
            });
     fire_ajax_submit();
     loadStocks();
    $(".stockForm").toggle();
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
        success: function(data) {
        var dataSet=[]
            $.each(data,function(i,item){
                var dateTime = new Date(item.lastUpdate);
                dateTime = moment(dateTime).format("YYYY-MM-DD HH:mm:ss");
                dataSet.push([item.id,item.name,item.currentPrice.formatted,dateTime]);
            });
            console.log(dataSet)
             $('#stockrecords').DataTable( {
                    data: dataSet,
                    columns: [
                        { title: "#" },
                        { title: "Name" },
                        { title: "Price" },
                        { title: "LastUpdated"}
                    ]
                } );

            console.log("SUCCESS : ", data);
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
                 $('#stocks').append($("<option>").val(item).text(item));
            });

            console.log("SUCCESS : ", data);
            var firstData=$("#stocks option:first-child").val();
            console.log(firstData);
                loadStocksGraph(firstData);
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


function loadStocksGraph(firstData){
  time=['x'];
            amount=['amount'];

             $.ajax({
                    type: "GET",
                    contentType: "application/json",
                    url: "/api/stock/"+firstData,
                    dataType: 'json',
                    cache: false,
                    timeout: 600000,
                    success: function (data) {
                        $.each(data,function(i,item){
                           time.push(item.lastUpdate);
                           amount.push(item.currentPrice.amount);
                        });

                        console.log("SUCCESS : ", data);
                        c3.generate({
                            bindto:'#charts',
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
                                        format: '%Y-%m-%d %H:%M:%S'
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

}