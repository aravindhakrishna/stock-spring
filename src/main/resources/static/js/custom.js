var update_req;
var selected_row;
var row_flag = false;
$(document).ready(function () {
	$("#btn-search").click(function () {
		console.log("Clicked")
		$(".stockRow").remove();
		fire_ajax_submit();
	});

	$('#editStock').click(function () {
		$(".stockForm").toggle();
	});
	$('#stocks').change(function () {
		var optionSelected = $("option:selected", this);
		var valueSelected = this.value;
		console.log(valueSelected)
		loadStocksGraph(valueSelected);
	});
	fire_ajax_submit();
	loadStocks();

	$('#stockrecords').on('click', 'tbody tr', function () {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		} else {
			var table = $('#stockrecords').DataTable();
			table.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
		console.log('API row values : ', this);
		var arrayItem = [];
		$('td', $(this)).each(function (index, item) {
			arrayItem.push($(item).html());
		});
		console.log(arrayItem);
		edit_row(arrayItem, this);
	})
	$(".stockForm").toggle();
	$("#addStock").click(function () {
		var sn = $("#stockName").val();
		var amt = $("#amount").val();
		var u = $("#unit").val();
		var ts = $("#meeting-time").val();
		console.log("time ", ts);
		console.log("time ", ts != '');
		console.log(row_flag);
		req = {}
		if (sn != '' && $.isNumeric(amt)) {
			req['name'] = sn;
			currentPrice = {}
			currentPrice['amount'] = amt;
			currentPrice['unit'] = u;
			currentPrice['currency'] = 'EUR';
			req['currentPrice'] = currentPrice;
			if (ts != '') {
				var d = new Date(ts);
				req['lastUpdate'] = moment(d).format('YYYY-MM-DDTHH:mm:ss') + '.998Z';
			}
			if (row_flag) {
				id = update_req['id'];
				lastUpdate = update_req['lastUpdate'];
				update_stock(req, "PUT", "/api/stocks/" + id, id, lastUpdate, selected_row, false);
				row_flag = false;
			} else {
				update_stock(req, "POST", "/api/stocks", null, null, true);
			}

		} else {
			$("#formError").html("<h4 style=color:red;>All inputs are mandatory</h4><pre>");
		}
	});

	$("#deleteStock").click(function () {
		if (selected_row) {
			$.ajax({
				type: "DELETE",
				contentType: "application/json",
				url: "/api/stocks/" + update_req['id'],
				cache: false,
				timeout: 600000,
				success: function (data) {

					console.log("SUCCESS : ", data);
					selected_row.remove();
				},
				error: function (e) {

					var json = "<h4>Ajax Response</h4><pre>" +
						e.responseText + "</pre>";
					$('#feedback').html(json);

					console.log("ERROR : ", e);

				}
			});
		}
	});
	$('td', 'table').each(function (i) {
		$(this).text(i + 1);
	});


});

function edit_row(records, row) {
	$('#formFeedback').hide();
	$("#stockName").val(records[1]);
	$("#amount").val(records[2].split(",")[0].replace('.', ''));
	$("#unit").val(records[3]);
	var d = new Date(records[4]);
	dt = moment(d).format('YYYY-MM-DDTHH:mm:ss')
	$("#meeting-time").val(dt);
	req = {}
	req['id'] = records[0];
	req['name'] = records[1];
	currentPrice = {}
	currentPrice['amount'] = records[2].split(",")[0].replace('.', '');
	currentPrice['unit'] = records[3];
	currentPrice['currency'] = 'EUR';
	req['currentPrice'] = currentPrice;
	req['lastUpdate'] = records[4];
	update_req = req;
	selected_row = row;
	row_flag = true;
}

function fire_ajax_submit() {


	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/stocks",
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function (data) {
			var dataSet = []
			$.each(data, function (i, item) {
				var dateTime = new Date(item.lastUpdate);
				dateTime = moment(dateTime).format("YYYY-MM-DD HH:mm:ss");
				dataSet.push([item.id, item.name, item.currentPrice.formatted, item.currentPrice.unit, dateTime]);
			});
			console.log(dataSet)
			$('#stockrecords').DataTable({
				data: dataSet,
				columns: [{
						title: "#"
					},
					{
						title: "Name"
					},
					{
						title: "Price"
					},
					{
						title: "Unit"
					},
					{
						title: "LastUpdated"
					}
				]
			});

			console.log("SUCCESS : ", data);
		},
		error: function (e) {

			var json = "<h4>Ajax Response</h4><pre>" +
				e.responseText + "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);
			$("#btn-search").prop("disabled", false);

		}
	});

}

function loadStocks() {

	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/stock/names",
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function (data) {
			$.each(data, function (i, item) {
				var date = new Date(item.lastUpdate);
				$('#stocks').append($("<option>").val(item).text(item));
			});

			console.log("SUCCESS : ", data);
			var firstData = $("#stocks option:first-child").val();
			console.log(firstData);
			loadStocksGraph(firstData);
		},
		error: function (e) {

			var json = "<h4>Ajax Response</h4><pre>" +
				e.responseText + "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);
			$("#btn-search").prop("disabled", false);

		}
	});

}

function update_stock(req, method, path, id, lastUpdate, row, flag) {

	$.ajax({
		type: method,
		contentType: "application/json",
		url: path,
		dataType: 'json',
		data: JSON.stringify(req),
		cache: false,
		timeout: 600000,
		success: function (data) {

			console.log("SUCCESS : ", data);
			$("#formFeedback").show();
			$("#formFeedback").html("<h3 style=color:green;>" + data.result + "</h3>");
			var dateTime = new Date(lastUpdate);
			dateTime = moment(dateTime).format("YYYY-MM-DD HH:mm:ss");
			var t = $('#stockrecords').DataTable();
			if (flag) {

				t.row.add([
					data.id,
					req['name'],
					req['amount'] + ' €',
					req['unit'],
					dateTime
				]).draw(false);
			} else {
				t.row(row).data([id, req['name'], req['currentPrice']['amount'] + ' €', req['unit'], dateTime]).draw();
			}

		},
		error: function (e) {

			var json = "<h4>Ajax Response</h4><pre>" +
				e.responseText + "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);

		}
	});
}

function loadStocksGraph(firstData) {
	time = ['x'];
	amount = ['amount'];

	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/stock/" + firstData,
		dataType: 'json',
		cache: false,
		timeout: 600000,
		success: function (data) {
			$.each(data, function (i, item) {
				time.push(item.lastUpdate);
				amount.push(item.currentPrice.amount);
			});

			console.log("SUCCESS : ", data);
			c3.generate({
				bindto: '#charts',
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

			var json = "<h4>Ajax Response</h4><pre>" +
				e.responseText + "</pre>";
			$('#feedback').html(json);

			console.log("ERROR : ", e);
			$("#btn-search").prop("disabled", false);

		}
	});

}