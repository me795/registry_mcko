jQuery(document).ready(function($) {

	$('#saveDelegate').submit(function(e){
		e.preventDefault();

		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");

		const delegate = {};

		let elem = $(this).find('button.btn-success.save-button');

		delegate.surname = $('#delegate-surname').val();
		delegate.name = $('#delegate-name').val();
		delegate.middleName = $('#delegate-middle-name').val();
		delegate.signatureLink = $('#delegate-signature-link').val();



		$.ajax({
			type: $('#saveDelegate').attr('method'),
			url: $('#saveDelegate').attr('action'),
			contentType: "application/json",
			dataType: "json",
			data: JSON.stringify(delegate),
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
				elem.find('i').addClass('rotating').addClass('fa-spinner');
			},
			success: function (data) {
				console.log('Submission was successful.');
				window.location.replace("/delegates");
			},
			error: function (data) {
				elem.find('i').removeClass('rotating').removeClass('fa-spinner');
				console.log('An error occurred.');
			},
		});
	});

	$('.image-preview').each(function(){
		console.log($(this).find(".image-upload").attr('id'));
		console.log($(this).attr('id'));
		console.log($(this).find(".image-label").attr('id'));
		let input_field = '#' + $(this).find(".image-upload").attr('id');
		let preview_box = '#' + $(this).attr('id');
		let label_field = '#' + $(this).find(".image-label").attr('id');
		$.uploadPreview({
			input_field: input_field,
			preview_box: preview_box,
			label_field: label_field
		});

	});

 
});