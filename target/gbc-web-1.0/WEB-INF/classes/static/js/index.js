$(function() {

  $.extend($.jgrid.defaults, {
        datatype: 'json',
        jsonReader : {
          repeatitems:false,
          total: function(result) {
            //Total number of pages
            return Math.ceil(result.total / result.max);
          },
          records: function(result) {
            //Total number of records
            return result.total;
          }
        },
        prmNames: {
          page: "page.page",
          rows: "page.size",
          sort: "page.sort",
          order: "page.sort.dir"
        },
        sortname: 'title',
        sortorder: 'asc',
        height: 'auto',
        viewrecords: true,
        rowList: [10, 20, 50, 100],
        altRows: true,
        loadError: function(xhr, status, error) {
          alert(error);
        }
  });

  $.extend($.jgrid.edit, {
        closeAfterEdit: true,
        closeAfterAdd: true,
        ajaxEditOptions: { contentType: "application/json" },
        mtype: 'PUT',
        serializeEditData: function(data) {
          delete data.oper;
          return JSON.stringify(data);
        }
  });
  $.extend($.jgrid.del, {
        mtype: 'DELETE',
        serializeDelData: function() {
          return "";
        }
  });
  $( "#dlg" ).dialog({
      autoOpen: false,
      maxWidth:600,
      maxHeight: 700,
      width: 600,
      height: 500,
      modal: true,
      close: function(event, ui){ 
        location.reload(true);
      }
  });
  
  var MASTER_URL = '../rest';
  var DEPENDENT_URL = '../rest/dependents/';
  var TRANSACTION_URL = '../rest/transactions/';
  
  var addOptions = {width: "auto",mtype: "POST"};
  var editOptions = {
    width: "auto",
    onclickSubmit: function(params, postdata) {
      params.url = MASTER_URL + '/' + postdata.id;
    }
  };
  var delOptions = {
    onclickSubmit: function(params, postdata) {
      params.url = MASTER_URL + '/' + postdata;
    }
  };
  
  // Transaction detail grid
  var editTransOptions = {
      width: "auto",
      onclickSubmit: function(params, postdata) {
        params.url = TRANSACTION_URL + postdata.id;
      }
    };
  var delTransOptions = {
      onclickSubmit: function(params, postdata) {
        params.url = TRANSACTION_URL + postdata;
      }
    };
  
  //Dependent Inner Grid options
  var depEditOptions = {
    width: "auto",
    onclickSubmit: function(params, postdata) {
      params.url = DEPENDENT_URL + postdata.id;
    }
  };
  var depDelOptions = {
    onclickSubmit: function(params, postdata) {
      params.url = DEPENDENT_URL + postdata.replace('inner','');
    }
  };
  
  var options = {
    url: MASTER_URL,
    editurl: MASTER_URL,
    colModel:[
      {
        name:'id',
        label: 'Reg Num',
        index: 'id',
        formatter:'showlink',
        formatoptions : {
          baseLinkUrl : '../',
          idName : 'id',
          target : '_blank'
        },
        width: 90,
        editable: true,
        editoptions: {disabled: true, size:5}
      },
      {
        name:'name',
        label: 'Name',
        index: 'name',
        width: 200,
        editable: true,
        editrules: {required: true},
        formoptions: {rowpos: 2, colpos: 1}
      },
      { 
        label: 'Gender',
        name: 'gender',
        width: 60,
        align: 'center',
        editable: true,
        edittype: "select",
        editoptions: { value: "M:Male;F:Female" },
          formoptions: {rowpos: 2, colpos: 2}
      },
      {
        name:'address.street',
        label: 'Street',
        index: 'address.street',
        editable: true,
        hidden: true,
        editrules: {edithidden:true},
        formoptions: {rowpos: 3, colpos: 1}
      },
      {
        name:'address.postcode',
        label: 'Postcode',
        index: 'address.postcode',
        editable: true,
        hidden: true,
        editrules: {edithidden:true},
        formoptions: {rowpos: 3, colpos: 2}
      },
      {
        name:'address.city',
        label: 'City',
        index: 'address.city',
        editable: true,
        hidden: true,
        editrules: {edithidden:true},
        formoptions: {rowpos: 4, colpos: 1}
      },
      {
        name:'address.state',
        label: 'State',
        index: 'address.state',
        editable: true,
        hidden: true,
        formoptions: {rowpos: 4, colpos: 2}
      },
      {
        name:'email',
        label: 'Email',
        index: 'email',
        width: 200,
        editable: true,
        editrules: {required: true},
        formoptions: {rowpos: 5, colpos: 1}
      },
      {
        name:'phone',
        label: 'Phone',
        index: 'phone',
        width: 120,
        editable: true,
        editrules: {edithidden:true},
        formoptions: {rowpos: 5, colpos: 2}
      },
      {
          name:'dateOfBirth',
          label: 'Date of Birth',
          index: 'dateOfBirth',
          sorttype:'date',
          formatter: 'date',
          srcformat: 'd-m-Y',
          newformat: 'Y/j/n',
          editoptions: {
              dataInit: function (element) {
                $(element).datepicker({
                  autoclose: true,
                  dateFormat: 'yy-mm-dd',
                  orientation : 'auto bottom'
                });
              }
          },
          width: 100,
          align: 'center',
          editable: true,
          editrules: {required: true},
          formoptions: {srcformat: 'd/m/Y', rowpos: 6, colpos:1}
       },
       {
         name:'placeOfBirth',
         label: 'Place of Birth',
         index: 'placeOfBirth',
         editable: true,
         hidden: true,
         editrules: {edithidden:true},
         formoptions: {rowpos: 6, colpos:2}
       },
       {
         name:'bsnNum',
         label: 'Bsn Num',
         index: 'bsnNum',
         editable: true,
         editrules: {edithidden:true},
         formoptions: {rowpos: 7, colpos:1}
       },
      {
        name:'maritalStatus',
        label: 'Marital Status',
        index: 'maritalStatus',
        align: 'center',
        width: 110,
        editable: true,
        edittype: 'select',
        editrules: {edithidden:true},
        editoptions: {
          value: {'Single': 'Single', 'Married': 'Married', 'Divorced': 'Divorced', 'Widowed': 'Widowed'}
        },
        formoptions: {rowpos: 7, colpos:2}
      },
      {
        name:'startDate',
        label: 'Start Date',
        index: 'startDate',
        sorttype:'date',
        formatter: 'date',
        srcformat: 'd-m-Y',
        newformat: 'Y/j/n',
        searchoptions: {
          dataInit: function (element) {
            $(element).datepicker({
              autoclose: true,
              format: 'dd/mm/yyyy',
              orientation : 'bottom'
            });
          }
        },
        editoptions: {
            dataInit: function (element) {
              $(element).datepicker({
                autoclose: true,
                dateFormat: 'yy-mm-dd',
                orientation : 'auto bottom'
              });
            }
        },
        width: 100,
        align: 'center',
        editable: false,
        editrules: {required: true, integer: true}
      },
       {
        name:'initBalance',
        label: 'Initial balance',
        index: 'initBalance',
        align: 'right',
        width: 110,
        formatter:'currency',
        formatoptions:{
            thousandsSeparator: ",",
            decimalSeparator: ".",
            decimalPlaces : 2,
            prefix : "€ ",
            suffix : "",
            deaultValue: "€ 0.00"
        },
        editable: false,
        editrules: {edithidden:true}
      },
      {
        name: 'closeBalance',
        label: 'Closing balance',
        index: 'closeBalance',
        align: 'right',
        width: 120,
        formatter:'currency',
        formatoptions:{
            thousandsSeparator: ",",
            decimalSeparator: ".",
            decimalPlaces : 2,
            prefix : "€ ",
            suffix : "",
            deaultValue: "€ 0.00"
        },
        editable: false,
        editrules: {edithidden:true}
      },
      {
        label: 'Amount Due',
        index: 'amountDue',
        align: 'right',
        width: 100,
        sorttype: "currency",
        formatter: function (cellValue, option, rowObject) {
            return "€ " + (parseFloat(rowObject.initBalance) - parseFloat(rowObject.closeBalance)).toFixed(2);
        },
        editable: false,
        editrules: {edithidden:true}
      },
      {
        name:'wishes',
        label: 'Wishes',
        index: 'wishes',
        hidden: true,
        editable: true,
        edittype: 'textarea',
        editrules: {edithidden:true},
        formoptions: {rowpos: 8, colpos:1}
      },
      {
        name:'notes',
        label: 'Notes',
        index: 'notes',
        hidden: true,
        editable: true,
        edittype: 'textarea',
        editrules: {edithidden:true},
        formoptions: {rowpos: 8, colpos:2}
      },
      {
        name:'active',
        label: 'Active',
        index: 'active',
        formatter: 'checkbox',
        width: 50,
        align: 'center',
        editable: true,
        edittype: 'checkbox',
        editoptions: {value:"true:false"},
        formoptions: {rowpos: 9, colpos:1}
      },
      {
        name:'healthy',
        label: 'All family healthy',
        index: 'healthy',
        formatter: 'checkbox',
        width: 50,
        align: 'center',
        editable: true,
        hidden: true,
        edittype: 'checkbox',
        editrules: {edithidden:true},
        editoptions: {value:"true:false"},
        formoptions: {rowpos: 9, colpos:2}
      },
      {
        name:'modifiedBy',
        label: 'Modified By',
        index: 'modifiedBy',
        editable: true,
        hidden: true,
        editrules: {edithidden:true},
        editoptions: {disabled: true},
        formoptions: {rowpos: 10, colpos:1}
      },
      {
        name:'modifiedDate',
        label: 'Modified Date',
        index: 'modifiedDate',
        editable: true,
        hidden: true,
        editrules: {edithidden:true},
        editoptions: {disabled: true},
        formoptions: {rowpos: 10, colpos:2}
      }
    ],
    caption: "Applicants",
    pager : '#masterGridPager',
    height: 'auto',
    onSelectRow: function(rowid, selected) {
        if(rowid != null) {
          jQuery("#transactionGrid").jqGrid('setGridParam',{url: TRANSACTION_URL + rowid, editurl:TRANSACTION_URL + rowid, datatype: 'json'});
          jQuery("#transactionGrid").jqGrid('setCaption', 'Transactions ::'+rowid);
          jQuery("#transactionGrid").trigger("reloadGrid");
        }
      },
    ondblClickRow: function(id) {
      if (!id.includes('inner')) {
        $("#dlg").load("../?id="+id).dialog("open");
      }
    },
    
    // Inner grid
    jsonReader: {
        subgrid : { repeatitems: false}
    },
    subGrid: true,
    subgridtype: 'json',
    subGridRowExpanded: showChildGrid
    
  };

  function showChildGrid(parentRowID, parentRowKey) {
      var childGridID = parentRowID + "_table";
      var childGridPagerID = parentRowID + "_pager";
      var childGridURL = DEPENDENT_URL + parentRowKey;
      $('#' + parentRowID).append("<table id=" + childGridID + " border='1' cellpadding='0' cellspacing='0'></table><div id=" + childGridPagerID + "></div>");

      $("#" + childGridID).jqGrid({
          url: childGridURL,
          editurl: childGridURL,
          mtype: "GET",
          datatype: "json",
          idPrefix: "inner",
          page: 1,
          colModel: [
              { label: 'Reg Num', name:'id', index: 'id', formatter:'integer', width: 90, editable: true, editoptions: {disabled: true}},
              { label: 'Name', name: 'name', width: 200,editable: true,formoptions: {rowpos: 2, colpos: 1} },
              { label: 'Gender', name: 'gender', width: 60, align: 'center',editable: true, edittype: "select", editoptions: { value: "M:Male;F:Female" },formoptions: {rowpos: 2, colpos: 2}},
              { label: 'Date Of Birth', name: "dateOfBirth",index:"dateOfBirth",width:100,align:"center",sorttype:'date', formatter: 'date', formatoptions: {newformat: 'Y-m-d' }, formoptions: {rowpos: 3, colpos: 1},editable: true,
                editoptions: {
                  dataInit: function (element) {
                      $(element).datepicker({
                        autoclose: true,
                        format: 'yy-mm-dd',
                        dateFormat: 'yy-mm-dd',
                        orientation : 'auto bottom'
                      });
                    }
                }
              },
              { label: 'PlaceOfBirth', name: 'placeOfBirth', width: 120,hidden: true,editable: true,editrules: {edithidden:true}, formoptions: {rowpos: 3, colpos: 2} },
              { label: 'BsnNum', name: 'bsnNum', width: 150,editable: true, formoptions: {rowpos: 4, colpos: 1} },
              { label: 'Dependent Type', name: 'type', width: 125, editable: true, edittype: "select", editoptions: { value: "Spouse:Spouse;Child:Child;OtherContact:OtherContact" }, formoptions: {rowpos: 4, colpos: 2}},
              { label: 'Phone', name: 'phone', width: 120,editable: true, formoptions: {rowpos: 5, colpos: 2} },
              { label: 'Modified By', name: 'modifiedBy', width: 120 },
              { label: 'Modified Date', name: 'modifiedDate', width: 110 },
              { label: 'Active', name:"active",index:"active",formatter: 'checkbox', width: 50, align: 'center', editable: true, edittype: 'checkbox', editoptions: {value:"true:false"}, formoptions: {rowpos: 5, colpos: 1}}
          ],
          loadonce: true,
          height: '100%',
          ondblClickRow: depEditOptions,
          pager: "#" + childGridPagerID
      }).navGrid('#' + childGridPagerID,
            {}, //options
            depEditOptions,
            addOptions,
            depDelOptions,
            {} // search options
      );
  }

  $("#masterGrid")
      .jqGrid(options)
      .navGrid('#masterGridPager',
      {}, //options
      editOptions,
      addOptions,
      delOptions,
      {} // search options
  );

  var transOptions = {
      url: TRANSACTION_URL,
      editurl: TRANSACTION_URL,
      colModel:[
        {
          name:'id',
          label: 'Seq Num',
          index: 'id',
          formatter:'integer',
          width: 80,
          editable: true,
          hidden: true,
          editoptions: {disabled: true}
        },
        {
          name:'transactionId',
          label: 'Transaction Num',
          index: 'transactionId',
          editable: true,
          editrules: {edithidden:true}
          
        },
        {
          name:'transactionDate',
          label: 'Transaction Date',
          index: 'transactionDate',
          sorttype:'date',
          formatter: 'date',
          srcformat: 'd-m-Y',
          newformat: 'Y/j/n',
          formatoptions: { srcformat: 'Y-m-d', newformat: 'Y-m-d' },
          searchoptions: {
            dataInit: function (element) {
              $(element).datepicker({
                autoclose: true,
                format: 'dd/mm/yyyy',
                orientation : 'bottom'
              });
            }
          },
          editoptions: {
              dataInit: function (element) {
                $(element).datepicker({
                  autoclose: true,
                  dateFormat: 'yy-mm-dd',
                  orientation : 'auto bottom'
                });
              }
          },
          align: 'center',
          editable: true,
          editrules: {required: true}
        },
        {
          name:'amountPaid',
          label: 'Amount Paid',
          index: 'amountPaid',
          align: 'right',
          summaryType: 'sum',
          formatter:'currency',
          formatoptions:{
              thousandsSeparator: ",",
              decimalSeparator: ".",
              decimalPlaces : 2,
              prefix : "€ ",
              suffix : "",
              deaultValue: "$ 0.00"
          },
          editable: true,
          editrules: {required: true}
        }
      ],
      caption: "Transactions",
      pager : '#transactionGridPager',
      height: 'auto',
      rownumbers: true,
      rownumWidth: 35,
      footerrow: true,
      userDataOnFooter: true,

      onSelectRow: function(rowid, selected) {
        if(rowid != null) {

        }
      },
      ondblClickRow: function(id) {
        jQuery(this).jqGrid('editGridRow', id, editTransOptions);
      }
    };

    $("#transactionGrid")
        .jqGrid(transOptions)
        .navGrid('#transactionGridPager',
        {}, //options
        editTransOptions,
        addOptions,
        delTransOptions,
        {} // search options
    );
    
    $('#processTrans').click( function() {
        //$('#registration').attr('action', "admin/home").submit();
        $.ajax({
              url: "../rest/processTransactionFile",
              type: 'get'
        });
     });
});