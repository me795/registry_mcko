(function ($) {

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");


    $('body').on('click', '.organization-delete-button', function() {

        if (confirm("Вы действительно хотите удалить запись об организации?")){
            let url = '/organization/' + $(this).data('organization-id');
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

    var dt = $('#organization-data-table').DataTable({
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
            url: '/organizations/data',
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
                class: 'organization-id',
                data: 'id',
                name: 'id'
            },
            {
                data: 'shortName',
                name: 'short_name'
            },
            {
                data: 'name',
                name: 'name'
            },
            {
                data: 'organizationOPF',
                name: 'organization_opf'
            },
            {
                data: 'organizationType',
                name: 'organization_type'
            },
            {
                data: 'ogrn',
                name: 'ogrn'
            },
            {
                data: 'inn',
                name: 'inn'
            },
            {
                data: 'activityStartedAt',
                name: 'activity_started_at'
            },
            {
                data: 'activityFinishedAt',
                name: 'activity_finished_at'
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
                class: 'organization-edit',
                orderable: false,
                data: null,
                defaultContent: ''
            },
            {
                class: 'organization-delete',
                orderable: false,
                data: null,
                defaultContent: ''
            }
        ]
    });

    dt.on('draw', function () {
        if ($('#organization-data-table tbody tr').size() > 0) {
            $('#organization-data-table tbody tr').each(function () {
                if ($(this).find('td').size() > 1) {

                    $(this).find('td.organization-edit').html('<a href="/organization/' + $(this).find('td.organization-id').html().trim() + '" >\n' +
                        '                                            <button type="button" class="btn btn-primary btn-sm"><i\n' +
                        '                                                    class="fa fa-pencil"></i>&nbsp;\n' +
                        '                                                Редактировать\n' +
                        '                                            </button>\n' +
                        '                                        </a>');

                    $(this).find('td.organization-delete').html('<button type="button" data-organization-id="' + $(this).find('td.organization-id').html().trim() + '" class="btn btn-danger btn-sm organization-delete-button"><i\n' +
                        '                                                    class="fa fa-exclamation"></i>&nbsp;\n' +
                        '                                                Удалить\n' +
                        '                                            </button>');

                }
            });
        }
    });


})(jQuery);