$(document).ready(function() {

	$('#newsPublishForm').submit(function(e) {

		$.ajax({
			url : 'newsServlet',
			type : "POST",
			timeout : 5000,
			data : new FormData(this),
			processData : false,
			contentType : false

		}).success(function(result) {
			$('#publishStatusMessage').text('Successfully posted news');
			console.log('successfully posted news');
		}).fail(function() {
			$('#publishStatusMessage').text('Error, couldn\'t post news');
		});

		e.preventDefault();
	});	
});
