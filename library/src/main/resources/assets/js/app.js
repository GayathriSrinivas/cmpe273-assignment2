$(":button").click(function() {
    var isbn = this.id;
    alert('About to report lost on ISBN ' + isbn);
    $.ajax({
    	  url: 'v1/books/'+isbn+'?status=lost',
    	  type: 'PUT',
    	  dataType: "json",
          contentType: "application/json;charset=utf-8",
    	  success: function(data) {
    	    $('#' + isbn).attr("disabled",true);
    	    $('#s' + isbn).html("lost");
    	  }
    	});
    
});