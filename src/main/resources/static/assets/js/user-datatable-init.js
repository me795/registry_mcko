(function ($) {
    //    "use strict";


    /*  Data Table
    -------------*/




    $('#bootstrap-data-table').DataTable({
		oLanguage: {
			"sLengthMenu": "Отображено _MENU_ записей на страницу",
			"sSearch": "Поиск:",
			"sZeroRecords": "Ничего не найдено - извините",
			"sInfo": "Показано с _START_ по _END_ из _TOTAL_ записей",
			"sInfoEmpty": "Показано с 0 по 0 из 0 записей",
			"sInfoFiltered": "(filtered from _MAX_ total records)",
			"oPaginate": {
				"sFirst": "Первая",
				"sLast": "Посл.",
				"sNext": "След.",
				"sPrevious": "Пред."
			}
		}
    });



    $('#bootstrap-data-table-export').DataTable({
        dom: 'lBfrtip',
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        buttons: [
            'copy', 'csv', 'excel', 'pdf', 'print'
        ]
    });
	
	$('#row-select').DataTable( {
			initComplete: function () {
				this.api().columns().every( function () {
					var column = this;
					var select = $('<select class="form-control"><option value=""></option></select>')
						.appendTo( $(column.footer()).empty() )
						.on( 'change', function () {
							var val = $.fn.dataTable.util.escapeRegex(
								$(this).val()
							);
	 
							column
								.search( val ? '^'+val+'$' : '', true, false )
								.draw();
						} );
	 
					column.data().unique().sort().each( function ( d, j ) {
						select.append( '<option value="'+d+'">'+d+'</option>' )
					} );
				} );
			}
		} );


	$('.change-user-status').click(function(e){

		if (confirm("Вы действительно хотите заблокировать/разблокировать пользователя?")) {

			const token = $("meta[name='_csrf']").attr("content");
			const header = $("meta[name='_csrf_header']").attr("content");
			const url = '/user/' + $(this).data('user-id') + '/status_change';
			const elem = $(this);

			$.ajax({
				type: 'PUT',
				url: url,
				contentType: "application/json",
				dataType: "json",
				beforeSend: function (request) {
					request.setRequestHeader(header, token);
					elem.find('i').removeClass('fa-lock').removeClass('fa-unlock').addClass('rotating').addClass('fa-spinner');
				},
				success: function (data) {
					elem.find('i').removeClass('rotating').removeClass('fa-spinner');
					if (elem.hasClass('btn-success')) {
						elem.removeClass('btn-success').addClass('btn-danger');
						elem.find('i').removeClass('fa-unlock').addClass('fa-lock');
						elem.find('span').html('Заблокировать');
					} else {
						elem.removeClass('btn-danger').addClass('btn-success');
						elem.find('i').removeClass('fa-lock').addClass('fa-unlock');
						elem.find('span').html('Разблокировать');
					}
				},
				error: function (data) {
					elem.find('i').removeClass('rotating').removeClass('fa-spinner');
					if (elem.hasClass('btn-success')) {
						elem.find('i').addClass('fa-unlock');
					}else{
						elem.find('i').addClass('fa-lock');
					}
					console.log('An error occurred.');
				},
			});
		}
	});






})(jQuery);