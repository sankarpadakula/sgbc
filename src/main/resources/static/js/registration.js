$(function() {

	 /* $('.date').click( function() {
		  alert("hi");
		     $(this).datepicker({
		      format : 'dd/mm/yyyy',
		      autoclose : true
		    });
		  });*/
		  
    var i = $('.dependent-group').size();

    $('#addDependent').click( function() {
      var newNameDiv = $('<div class="input-group has-feedback">'
          + '<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>'
          + '<div class="row">'
          + '<div class="col-md-8" style="padding-right: 0px;">'
          + $('#name').clone().removeAttr("id").attr("name", 'childrens['+i+'].name')[0].outerHTML
          + '<span class="glyphicon form-control-feedback" aria-hidden="true"></span>'
          + '</div>'
          + '<div class="col-md-3" style="padding: 0px;">'
          + '<div class="row" style="margin: 0px;">'
          + '<label class="radio-inline">' + $('#male').clone().removeAttr("id").attr("name", 'childrens['+i+'].gender')[0].outerHTML +  $('#male').parent().text() +'</label>'
          + '<label class="radio-inline">' + $('#female').clone().removeAttr("id").attr("name", 'childrens['+i+'].gender')[0].outerHTML +  $('#female').parent().text() + '</label>'
          + ' </div></div></div></div>');
      var newBirthDiv = $('<div class="input-group">'
          + '<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>'
          + '<div class="row">'
          + '<div class=" has-feedback col-md-6" style="margin-bottom: 0px;">'
          + $('#date').clone().removeAttr("id").attr("name", 'childrens['+i+'].dateOfBirth')[0].outerHTML
          + ' <span class="glyphicon form-control-feedback" aria-hidden="true"></span>'
          + '</div>'
          + '<div class="col-md-1" style="width:8%;padding:0px;">'
          + '<span class="input-group-addon form-control" ><span class="fa fa-bed"></span></span>'
          + '</div>'
          + '<div class=" has-feedback col-md-6" style="width:41.5%;padding:0px;margin-bottom: 0px;">'
          + $('#pob').clone().removeAttr("id").attr("name", 'childrens['+i+'].placeOfBirth')[0].outerHTML
          + '<span class="glyphicon form-control-feedback" aria-hidden="true"></span>'
          + '</div></div></div>');
      var newBsnDiv = $('<div class="input-group">'
          + '<span class="input-group-addon"><span class="glyphicon glyphicon-info-sign"></span></span>'
          + '<div class="row">'
          + '<div class=" has-feedback col-md-6" style="margin-bottom: 0px;">'
          + $('#bsn').clone().removeAttr("id").attr("name", 'childrens['+i+'].bsnNum')[0].outerHTML
          + ' <span class="glyphicon form-control-feedback" aria-hidden="true"></span>'
          + '</div>'
          + '<div class="checkbox has-feedback col-md-5" style="text-align: left;">'
         // + '<label><input type="checkbox" name="childrens['+i+'].healthy" th:checked="${healthy}" value="${true}" /> is healthy</label>'
         // + ' <span class="glyphicon form-control-feedback" aria-hidden="true"></span>'
          + '</div>'
          + '<button id="remDependent" class="col-md-1 btn btn-default remDependent" onclick="$(this).closest(\'.dependent-group\').remove();" data-toggle="tooltip" data-placement="top" title="Remove kid"><i class="glyphicon glyphicon-trash icon-white"></i></button>'
          + ' </div></div><legend></legend>');

      $(".dependents").append($('<div class="dependent-group" id="dependent_' + i +'" />').append(newNameDiv, newBirthDiv, newBsnDiv));
      i++;
      return false;
    });
    
    //Approve button action
    $('#approve').click( function() {
    	 $('#active').val(true);
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
    	 $('#registration').attr('action', "admin/home").submit();
    });
    
    var getQueryString = function ( field, url ) {
	    var href = url ? url : window.location.href;
	    var reg = new RegExp( '[?&]' + field + '=([^&#]*)', 'i' );
	    var string = reg.exec(href);
	    return string ? string[1] : null;
	  };
    
    $('#registration').ready( function() {
    	var token = getQueryString('token');
    	var id = getQueryString('id');
    	var confirmationMessage = $('#confirmationMessage').html();
    	var validationMessage = $('#validationMessage').html();
    	if ((token || id) && !validationMessage && !confirmationMessage) {
    		$('.fieldset').prop('disabled', true);
    	}
    });
    
    $('#martial').change(function() {
    	console.log($(this).val());
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