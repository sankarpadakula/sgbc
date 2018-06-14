$(document).ready( function () {
	 var table = $('#applicantsTable').DataTable({
			"sAjaxSource": "/gbc/applicants",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			      { "mData": "id"},
		          { "mData": "name" },
				  { "mData": "gender" },
				  { "mData": "email" },
				  { "mData": "phone" },
				  { "mData": "martialStatus" },
				  { "mData": "healthyFamily" },
				  { "mData": "childrens" },
				  { "mData": "createdDate" },
				  { "mData": "enabled" }
			],
	 		"aocolumnDefs": [
	                { "data": null, "targets": -1, "defaultContent":"<h4><a class='dt-view'></a><a class='dt-edit'></a><a class='dt-delete'></a></h4>" }
	            ]	
	 });
	 
	 
	 $('#tt').datagrid({
	        title:'Editable DataGrid',
	        iconCls:'icon-edit',
	        width:660,
	        height:250,
	        singleSelect:true,
	        idField:'id',
	        url:'/gbc/applicants',
	        columns:[
	            {field:'id',title:'Reg ID',width:60},
	            {field:'name',title:'Name',width:100},
	            {field:'email',title:'Email',width:80,align:'right',editor:{type:'numberbox',options:{precision:1}}},
	            {field:'unitcost',title:'Unit Cost',width:80,align:'right',editor:'numberbox'},
	            {field:'attr1',title:'Attribute',width:180,editor:'text'},
	            {field:'enabled',title:'enabled',width:50,align:'center',
	                editor:{
	                    type:'checkbox',
	                    options:{
	                        on: 'P',
	                        off: ''
	                    }
	                }
	            },
	            {field:'action',title:'Action',width:80,align:'center',
	                formatter:function(value,row,index){
	                    if (row.editing){
	                        var s = '<a href="javascript:void(0)" onclick="saverow(this)">Save</a> ';
	                        var c = '<a href="javascript:void(0)" onclick="cancelrow(this)">Cancel</a>';
	                        return s+c;
	                    } else {
	                        var e = '<a href="javascript:void(0)" onclick="editrow(this)">Edit</a> ';
	                        var d = '<a href="javascript:void(0)" onclick="deleterow(this)">Delete</a>';
	                        return e+d;
	                    }
	                }
	            }
	        ],
	        onBeforeEdit:function(index,row){
	            row.editing = true;
	            $(this).datagrid('refreshRow', index);
	        },
	        onAfterEdit:function(index,row){
	            row.editing = false;
	            $(this).datagrid('refreshRow', index);
	        },
	        onCancelEdit:function(index,row){
	            row.editing = false;
	            $(this).datagrid('refreshRow', index);
	        }
	    });
});