$(document).ready(function() {

	$('#publishSubmitButton').click(function(e) {
		e.preventDefault();
		publishNews();
	});

	function publishNews() {

$.ajax({
	url: 'newsServlet',
	type: "GET",
	timeout: 5000,
	data: {
		'newsHeader':  $('#newsHeaderField').val(),
		'newsContent': $('#newsContentField').val(),
		'action': 'publishNews'
	}
}).success(function(result) {
$('#publishStatusMessage').text('Successfully posted news');
console.log('successfully posted news');
}).fail(function() {
$('#publishStatusMessage').text('Error, couldn\'t post news');
});
}


});
