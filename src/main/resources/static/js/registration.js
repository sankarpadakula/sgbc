$(function() {

   /* $('.date').click( function() {
         $(this).datepicker({
          format : 'dd/mm/yyyy',
          autoclose : true
        });
      });*/
      
    var i = $('.dependent-group').size();

    $('#addDependent').click( function() {
       var newNameDiv = $('<div class="row"><div class="col-md-9"><div class="input-group"><span class="input-group-addon"><i class="fa fa-user-o fa-fw"></i></span>'
        + $('#name').clone().removeAttr("id").removeAttr("value").attr("name", 'childrens[' + i + '].name')[0].outerHTML
        + '</div></div><div class="col-md-3 row">'
        + '<label class="radio-inline">' + $('#male').clone().removeAttr("id").removeAttr("checked").attr("name", 'childrens[' + i + '].gender')[0].outerHTML + $('#male').parent().text() + '</label>'
        + '<label class="radio-inline">' + $('#female').clone().removeAttr("id").removeAttr("checked").attr("name", 'childrens[' + i + '].gender')[0].outerHTML + $('#female').parent().text() + '</label>'
        + '</div></div>');

       var newBirthDiv = $('<div class="row"><div class="col-md-6 input-group"><span class="input-group-addon"><i class="fa fa-calendar fa-lg"></i></span>'
          + $('#date').clone().removeAttr("id").removeAttr("value").attr("name",'childrens[' + i + '].dateOfBirth')[0].outerHTML
          + '</div><div class="col-md-6 input-group"><span class="input-group-addon" ><i class="fa fa-bed"></i></span>'
          + $('#pob').clone().removeAttr("id").removeAttr("value").attr("name",'childrens[' + i + '].placeOfBirth')[0].outerHTML
          + '</div></div>');
       var newBsnDiv = $('<div class="row"><div class="col-md-6 input-group"><span class="input-group-addon"><i class="fa fa-vcard-o fa-fw"></i></span>'
          + $('#bsn').clone().removeAttr("id").removeAttr("value").attr("name", 'childrens[' + i + '].bsnNum')[0].outerHTML
          + '</div><div class="col-md-5 input-group" >'
          + $('#fileupload').clone().removeAttr("value").attr("id", 'fileupload' + i).attr("name", 'childrens[' + i + '].docs').attr("selector", '#fileupload' + i)[0].outerHTML
          + '<label class="custom-file-label" for=fileupload'+i+' style="text-align:left;margin-left:15px;" title="Proof of Identity"></label>'
          + '</div>'
          + '<button id="remDependent" class="col-md-1 btn btn-default remDependent input-group-addon" onclick="$(this).closest(\'.dependent-group\').remove();" data-toggle="tooltip" data-placement="top" title="Remove kid"><i class="fa fa-trash-o fa-lg"></i></button>'
          + '</div><div class="mb-3"></div>');

      $(".dependents").append($('<div class="dependent-group" id="dependent_' + i +'" />').append(newNameDiv, newBirthDiv, newBsnDiv));
      i++;
      return false;
    });
    
    //Approve button action
    $('#approve').click( function() {
       $('#active').val(true);
       $('#token').val(getQueryString('token'));
       if (window.location.pathname.indexOf("admin/home") > -1)
         $('#registration').attr('action', "../admin/home").submit();
       else
         $('#registration').attr('action', "admin/home").submit();
       /*$.ajax({
             url: "",
             type: 'post',
             dataType: 'application/json',
             data: $("#registration").serialize()
       });*/
    });
    
    //Reject button action
    $('#reject').click( function() {
       $('#active').val(false);
       $('#token').val(getQueryString('token'));
       if (window.location.pathname.indexOf("admin/home") > -1)
         $('#registration').attr('action', "../admin/home").submit();
       else
         $('#registration').attr('action', "admin/home").submit();
    });
    var getQueryString = function ( field, url ) {
      var href = url ? url : window.location.href;
      var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
      var string = reg.exec(href);
      return string ? string[1] : null;
    };
    
    $('#registration').ready( function() {
      //var token = getQueryString('token');
      var id = getQueryString('id');
      //var confirmationMessage = $('#confirmationMessage').html();
      //var validationMessage = $('#validationMessage').html();
      if (id) {
        $('.fieldset').prop('disabled', true);
      }
      
    });
    
    $('#martial').change(function() {
      //console.log($(this).val());
        if ($(this).val() === 'Married') {
          $('#spouse').show();
          $('#kids').show();
        } else if ($(this).val() === 'Single' || $(this).val() === '') {
          $('#spouse').hide();
          $('#kids').hide();
        } else {
          $('#spouse').hide();
          $('#kids').show();
        }
    });
    $("#martial").change();
  });