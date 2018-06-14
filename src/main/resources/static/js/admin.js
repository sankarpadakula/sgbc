$(document).ready(function() {
  
  //tab click
  $(' .tablink a ').click(function(e) {
    var currentAttrValue= $(this).attr('href');
    $('.tabs ' + currentAttrValue).show().siblings().hide();
    $(this).parent('li').addClass('active').siblings().removeClass('active');
    e.preventDefault();
  });
  
  
  
  
});
