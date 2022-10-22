jQuery(document).ready(function ($) {

    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    let i = 1;
    $('#registry-organization-table').find('td.serial').each(function () {
        $(this).html(i++);
    });

    $('body').on('click', '.small-image', function () {
        $('#big-image').attr('src',$(this).attr('src'));
        $('#smallmodal').show();
    });


    $('.delegate-select').chosen();

    $('.delegate-select').each(function () {

        let selectBox = $(this);
        addDelegateAutocomplete(selectBox);
    });

    $('.organization-select').chosen();

    $('.organization-select').each(function () {

        let selectBox = $(this);
        addOrganizationAutocomplete(selectBox);
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


    function addOrganizationAutocomplete(selectBox) {
        selectBox.closest('td').find('.chosen-search input').autocomplete({
            source: function (request, response) {

                let data = {};
                data.searchValue = request.term;

                $.ajax({
                    type: 'POST',
                    url: '/organizations/search',
                    data: data,
                    dataType: "json",
                    beforeSend: function (request) {
                        request.setRequestHeader(header, token);
                    },
                    success: function (data) {
                        selectBox.find('option:not(:checked)').remove();
                        response($.map(data, function (item) {
                            if (selectBox.find('option[value=' + item.id + ']').length == 0)
                                selectBox.append('<option value="' + item.id + '">' + item.shortName + '</option>');
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


    $('#registry-organization-form-button').on('click',function(e){
        e.preventDefault();
        if ($("#registry-organization-form").find('#organization-id').val().length == 0){
            $("#registry-organization-form").find('#organization-id').remove();
        }
        if ($("#registry-organization-form").find('#delegate-id').val().length == 0){
            $("#registry-organization-form").find('#delegate-id').remove();
        }
        if ($("#registry-organization-form").find('#document-date').val().length == 0){
            $("#registry-organization-form").find('#document-date').remove();
        }
        $("#registry-organization-form").submit();
    });


});

function uuidv4() {
    return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
        (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
    );
}

