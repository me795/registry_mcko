(function ($) {

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");


    $('body').on('click', '.delegate-delete-button', function() {

        if (confirm("Вы действительно хотите удалить запись о должностном лице?")){
            let url = '/delegate/' + $(this).data('delegate-id');
            let elem = $(this);
            $.ajax({
                type: 'DELETE',
                url: url,
                contentType: "application/json",
                dataType: "json",
                beforeSend: function(request) {
                    request.setRequestHeader(header, token);
                    elem.find('i').addClass('rotating').addClass('fa-spinner');
                },
                success: function () {
                    elem.closest('tr').remove();
                },
                error: function () {
                    elem.find('i').removeClass('rotating').removeClass('fa-spinner');
                    console.log('An error occurred.');
                },
            });
        }
    });

    var dt = $('#delegate-data-table').DataTable({
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
        },
        processing: true,
        serverSide: true,
        serverMethod: 'post',
        ajax: {
            contentType: 'application/json',
            url: '/delegates/data',
            type: 'POST',
            beforeSend: function (request) {
                request.setRequestHeader(header, token);
            },
            data: function (d) {
                return JSON.stringify(d);
            }
        },
        columns: [
            {
                class: 'delegate-id',
                data: 'id',
                name: 'id'
            },
            {
                data: 'surname',
                name: 'surname'
            },
            {
                data: 'name',
                name: 'name'
            },
            {
                data: 'middleName',
                name: 'middle_name'
            },
            {
                class: 'signature-link',
                orderable: false,
                data: 'signatureLink',
                name: 'signature_link'
            },
            {
                data: 'createdAt',
                name: 'created_at'
            },
            {
                data: 'updatedAt',
                name: 'updated_at'
            },
            {
                class: 'delegate-edit',
                orderable: false,
                data: null,
                defaultContent: ''
            },
            {
                class: 'delegate-delete',
                orderable: false,
                data: null,
                defaultContent: ''
            }
        ]
    });

    dt.on('draw', function () {
        if ($('#delegate-data-table tbody tr').size() > 0) {
            $('#delegate-data-table tbody tr').each(function () {
                if ($(this).find('td').size() > 1) {
                    let link = $(this).find('td.signature-link').html();
                    $(this).find('td.signature-link').html('<img src="' + link + '" style="width: 150px;">');

                    $(this).find('td.delegate-edit').html('<a href="/delegate/' + $(this).find('td.delegate-id').html().trim() + '" >\n' +
                        '                                            <button type="button" class="btn btn-primary btn-sm"><i\n' +
                        '                                                    class="fa fa-pencil"></i>&nbsp;\n' +
                        '                                                Редактировать\n' +
                        '                                            </button>\n' +
                        '                                        </a>');

                    $(this).find('td.delegate-delete').html('<button type="button" data-delegate-id="' + $(this).find('td.delegate-id').html().trim() + '" class="btn btn-danger btn-sm delegate-delete-button"><i\n' +
                        '                                                    class="fa fa-exclamation"></i>&nbsp;\n' +
                        '                                                Удалить\n' +
                        '                                            </button>');

                }
            });
        }
    });


})(jQuery);