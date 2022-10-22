jQuery(document).ready(function ($) {

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    $('#saveOrganization').submit(function(e){
        e.preventDefault();

        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        const organization = {};

        let elem = $(this).find('button.btn-success.save-button');

        organization.shortName = $('#organization-short-name').val();
        organization.name = $('#organization-name').val();
        organization.organizationOPF = $('#organization-opf').val();
        organization.organizationType = $('#organization-type').val();
        organization.ogrn = $('#organization-ogrn').val();
        organization.inn = $('#organization-inn').val();
        if ($('#organization-activity-started-at').val().length > 0)
            organization.activityStartedAt = $('#organization-activity-started-at').val();
        if ($('#organization-activity-finished-at').val().length > 0)
            organization.activityFinishedAt = $('#organization-activity-finished-at').val();
        organization.stampList = [];
        $('.stamp-row').each(function(){
            let stamp = {};
            if ($(this).find('.stamp-id').length > 0){
                stamp.id = $(this).find('.stamp-id').val();
            }
            stamp.link = $(this).find('.stamp-link').val();
            if ($(this).find('.stamp-activity-started-at').val().length > 0)
                stamp.activityStartedAt = $(this).find('.stamp-activity-started-at').val();
            if ($(this).find('.stamp-activity-finished-at').val().length > 0)
                stamp.activityFinishedAt = $(this).find('.stamp-activity-finished-at').val();
            organization.stampList.push(stamp);
        });
        organization.organizationDelegateList = [];
        $('.od-row').each(function(){
            let od = {};
            if ($(this).find('.od-id').length > 0){
                od.id = $(this).find('.od-id').val();
            }
            od.delegate = {};
            od.delegate.id = $(this).find('.od-delegate-id').val();
            od.position = $(this).find('.od-position').val();
            od.orderDocumentType = $(this).find('.od-order-document-type').val();
            od.orderDocumentNum = $(this).find('.od-order-document-num').val();
            if ($(this).find('.od-order-document-date').val().length > 0)
                od.orderDocumentDate = $(this).find('.od-order-document-date').val();
            if ($(this).find('.od-activity-started-at').val().length > 0)
                od.activityStartedAt = $(this).find('.od-activity-started-at').val();
            if ($(this).find('.od-activity-finished-at').val().length > 0)
                od.activityFinishedAt = $(this).find('.od-activity-finished-at').val();
            od.documentTypeList = [];
            $(this).find('.od-document-ids .document-select-item').map(function(){
                let docType = {}
                docType.id = $(this).data('doc-id');
                od.documentTypeList.push(docType);
            });
            organization.organizationDelegateList.push(od);
        });


        $.ajax({
            type: $('#saveOrganization').attr('method'),
            url: $('#saveOrganization').attr('action'),
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(organization),
            beforeSend: function(request) {
                request.setRequestHeader(header, token);
                elem.find('i').addClass('rotating').addClass('fa-spinner');
            },
            success: function (data) {
                console.log('Submission was successful.');
                window.location.replace("/organizations");
            },
            error: function (data) {
                elem.find('i').removeClass('rotating').removeClass('fa-spinner');
                console.log('An error occurred.');
            },
        });
    });

    $('.image-preview').each(function () {

        if ($(this).attr('id') != null) {
            let input_field = '#' + $(this).find(".image-upload").attr('id');
            let preview_box = '#' + $(this).attr('id');
            let label_field = '#' + $(this).find(".image-label").attr('id');
            $.uploadPreview({
                input_field: input_field,
                preview_box: preview_box,
                label_field: label_field
            });
        }

    });

    $('.delegate-select').chosen();

    $('.delegate-select').each(function () {

        let selectBox = $(this);
        addDelegateAutocomplete(selectBox);
    });


    // $('.document-select').chosen();
    //
    // $('.document-select').each(function () {
    //     let selectBox = $(this);
    //     addDocumentAutocomplete(selectBox);
    // });

    $('body').on('click', '.row-delete', function () {
        if (confirm('Вы действительно хотите удалить данную запись?')) {
            let table = $(this).closest('table');
            $(this).closest('tr').remove();
            renewNumberList(table);
        }

    });


    $('body').on('click', '.od-document-ids', function() {

        $('.od-document-ids').removeClass('active-document-list');
        $(this).addClass('active-document-list');
        $('#scrollmodal-doctype').find('.doc-type-check').prop('checked',false);

        $(this).find('.document-select-item').each(function(){
            console.log($(this).data('doc-id'));
            $('#scrollmodal-doctype').find('.doc-type-check[data-doc-id='+$(this).data('doc-id')+']').prop('checked',true);
        });

        $('#scrollmodal-doctype').show();
    });

    $('.save-doc-list').on('click',function(){

        $('.od-document-ids.active-document-list ul').empty();

        $('#scrollmodal-doctype').find('.doc-type-check:checked').each(function(){
            $('.od-document-ids.active-document-list ul')
                .append('<li class="document-select-item" data-doc-id="' +$(this).data("doc-id")+ '">'+$(this).data("doc-name")+'</li>');
        });

        $('#scrollmodal-doctype').find('button.close').click();
    });

    $('.stamp-add').on('click', function () {
        let template = $(this).closest('table').find('tr.row-template');
        template.before('<tr>' + template.html() + '</tr>');
        let newLine = template.prev('tr');
        let newId = uuidv4();
        newLine.addClass('stamp-row');
        newLine.find('.image-preview').attr('id', 'image-preview' + newId);
        newLine.find('.image-label').attr('id', 'image-label' + newId);
        newLine.find('.image-label').attr('for', 'image-upload' + newId);
        newLine.find('.image-upload').attr('id', 'image-upload' + newId);

        let input_field = '#' + newLine.find(".image-upload").attr('id');
        let preview_box = '#' + newLine.find(".image-preview").attr('id');
        let label_field = '#' + newLine.find(".image-label").attr('id');
        $.uploadPreview({
            input_field: input_field,
            preview_box: preview_box,
            label_field: label_field
        });

        renewNumberList($(this).closest('table'));

    });

    $('.od-add').on('click', function () {
        let template = $(this).closest('table').find('tr.row-template');
        template.before('<tr>' + template.html() + '</tr>');
        let newLine = template.prev('tr');

        let oldLine = newLine.prev('tr');
        if (oldLine.length > 0){
            newLine.find('.od-document-ids ul').html(oldLine.find('.od-document-ids ul').html());
        }

        newLine.addClass('od-row');
        newLine.find('.od-delegate-id').chosen();
        addDelegateAutocomplete(newLine.find('.od-delegate-id'));

        renewNumberList($(this).closest('table'));
    });


    function addDelegateAutocomplete(selectBox) {
        selectBox.closest('td').find('.chosen-search input').autocomplete({
            source: function (request, response) {

                let data = {};
                data.searchValue = request.term;

                $.ajax({
                    type: 'POST',
                    url: '/delegates/search',
                    data: data,
                    dataType: "json",
                    beforeSend: function (request) {
                        request.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        selectBox.find('option:not(:checked)').remove();
                        // selectBox.empty();
                        response($.map(data, function (item) {
                            if (selectBox.find('option[value=' + item.id + ']').length == 0)
                                selectBox.append('<option value="' + item.id + '">' + item.surname + ' ' + item.name + ' ' + item.middleName + '</option>');
                        }));
                        if (selectBox.find('option').length > 0) {
                            selectBox.append('<option value=""></option>');
                        }
                        let searchVal = selectBox.closest('td').find('.chosen-search-input').val();
                        selectBox.trigger("chosen:updated");
                        selectBox.closest('td').find('.chosen-search-input').val(searchVal);

                    }
                });
            }
        });
    }


    // function addDocumentAutocomplete(selectBox) {
    //     selectBox.closest('td').find('.chosen-search-input').autocomplete({
    //         source: function (request, response) {
    //
    //             let data = {};
    //             data.searchValue = request.term;
    //
    //             $.ajax({
    //                 type: 'POST',
    //                 url: '/documents/search',
    //                 data: data,
    //                 dataType: "json",
    //                 beforeSend: function (rq) {
    //                     rq.setRequestHeader(header, token);
    //                 },
    //                 success: function (data) {
    //                     selectBox.find('option:not(:checked)').remove();
    //                     response($.map(data, function (item) {
    //                         // if (selectBox.find('option[value=]' + item.id).length == 0)
    //                         selectBox.append('<option value="' + item.id + '">' + item.name + '</option>');
    //                     }));
    //                     if (selectBox.find('option').length > 0) {
    //                         selectBox.append('<option value=""></option>');
    //                     }
    //                     let searchVal = selectBox.closest('td').find('.chosen-search-input').val();
    //                     selectBox.trigger("chosen:updated");
    //                     selectBox.closest('td').find('.chosen-search-input').val(searchVal);
    //                 }
    //             });
    //         }
    //     });
    // }


    function renewNumberList(table) {
        let i = 1;
        table.find('td.serial').each(function () {
            $(this).html(i++);
        });
    }

});

function uuidv4() {
    return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    );
}

