jQuery(document).ready(function($) {

	$('#saveUser').submit(function(e){
		e.preventDefault();

		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");

		const user = {};

		let elem = $(this).find('button.btn-success.save-button');

		user.name = $('#username').val();
		user.email = $('#email').val();
		user.password = $('#password').val();
		user.roles = [];
		$('#roles_chosen').find('.chosen-choices').find('.search-choice').each(function(){
				let role = {};
				role.id = $(this).find('.search-choice-close').data('option-array-index');
				role.name = $(this).find('span').html();
				user.roles.push(role);
		});



		$.ajax({
			type: $('#saveUser').attr('method'),
			url: $('#saveUser').attr('action'),
			contentType: "application/json",
			dataType: "json",
			data: JSON.stringify(user),
			beforeSend: function(request) {
				request.setRequestHeader(header, token);
				elem.find('i').addClass('rotating').addClass('fa-spinner');
			},
			success: function (data) {
				console.log('Submission was successful.');
				window.location.replace("/users");
			},
			error: function (data) {
				elem.find('i').removeClass('rotating').removeClass('fa-spinner');
				console.log('An error occurred.');
			},
		});
	});


 
});