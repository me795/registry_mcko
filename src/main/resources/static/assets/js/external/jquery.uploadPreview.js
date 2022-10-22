(function ($) {
  $.extend({
    uploadPreview : function (options) {

      // Options + Defaults
      var settings = $.extend({
        input_field: ".image-input",
        preview_box: ".image-preview",
        label_field: ".image-label",
        label_default: "Выберите изображение",
        label_selected: "Выберите изображение",
        no_label: false,
        success_callback : null,
      }, options);

      // Check if FileReader is available
      if (window.File && window.FileList && window.FileReader) {
        if (typeof($(settings.input_field)) !== 'undefined' && $(settings.input_field) !== null) {

          if ($(settings.preview_box).find('.form-upload-img').val().length > 0) {
            let url = $(settings.preview_box).find('.form-upload-img').val();
            // Image
            $(settings.preview_box).css("background-image", "url(" + url+ ")");
            $(settings.preview_box).css("background-size", "cover");
            $(settings.preview_box).css("background-position", "center center");
          }

          $(settings.input_field).change(function() {
            var files = this.files;

            if (files.length > 0) {
              let file = files[0];
              let reader = new FileReader();
              // let data = { 'title': 'Sample Photo Title', 'file': reader.result };
              let data = new FormData();
              data.append('file', files[0]);

              // Load file
              reader.addEventListener("load",function(event) {
                let loadedFile = event.target;

                // Check format
                if (file.type.match('image')) {

                  const token = $("meta[name='_csrf']").attr("content");
                  const header = $("meta[name='_csrf_header']").attr("content");

                  $.ajax({
                    url: "/file/upload",
                    type: "POST",
                    data:  data,
                    contentType: false,
                    cache: false,
                    processData:false,
                    beforeSend : function(request)
                    {
                      request.setRequestHeader(header, token);
                    },
                    success: function(data)
                    {
                      $(settings.preview_box).find('.form-upload-img').val(data.url);
                      // Image
                      $(settings.preview_box).css("background-image", "url("+loadedFile.result+")");
                      $(settings.preview_box).css("background-size", "cover");
                      $(settings.preview_box).css("background-position", "center center");
                    },
                    error: function(e)
                    {
                      $("#err").html(e).fadeIn();
                    }
                  });

                } else if (file.type.match('audio')) {
                  // Audio
                  $(settings.preview_box).html("<audio controls><source src='" + loadedFile.result + "' type='" + file.type + "' />Your browser does not support the audio element.</audio>");
                } else {
                  alert("This file type is not supported yet.");
                }
              });

              if (settings.no_label == false) {
                // Change label
                $(settings.label_field).html(settings.label_selected);
              }

              // Read the file
              reader.readAsDataURL(file);

              // Success callback function call
              if(settings.success_callback) {
                settings.success_callback();
              }
            } else {
              if (settings.no_label == false) {
                // Change label
                $(settings.label_field).html(settings.label_default);
              }

              // Clear background
              $(settings.preview_box).css("background-image", "none");

              // Remove Audio
              $(settings.preview_box + " audio").remove();
            }
          });
        }
      } else {
        alert("You need a browser with file reader support, to use this form properly.");
        return false;
      }
    }
  });
})(jQuery);
