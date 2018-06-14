$(document).ready(function () {
        $('#applicantsTable').jtable({
            title: 'Table of Applicants',
            actions: {
                listAction: '/gbc/applicants',
                updateAction: '/applicants/approve',
                deleteAction: '/applicants/reject'
            },
            fields: {
                id: {
                	title:'S.NO',
                    key: true,
                    list: true,
                    create:true
                },
                name: {
                    title: 'Name',
                    width: '30%',
                    edit:false
                },
                gender: {
                    title: 'Gender',
                    width: '30%',
                    edit:true
                },
                email: {
                    title: 'Email',
                    width: '20%',
                    edit: true
                },
                active: {
                    title: 'Status',
                    width: '20%',
                    edit: true
                }
            }
        });
        $('#applicantsTable').jtable('load');
    });