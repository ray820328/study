$("#form").attr("action","deployResult.jsp");
    $("#form").attr("target","_blank");
    $("#form").attr("method","GET");
    $("#form").attr("enctype","application/x-www-form-urlencoded");
    $("#form").attr("encoding","application/x-www-form-urlencoded");
    $("#form").submit();

var ajaxRequest = function(formId, url, callback){
    	var formObj = jQuery("form#" + formId);//$(formId)
    	$.ajax({      
    		 type: "POST",
    		 dataType: 'json',
    		 url: !url ? formObj.attr("action") : url,     
    		 data: formObj.serialize(),
    		 timeout: 5000,
    		 success: function(response){
    		 	 if(callback){
    		    	 callback(response, 'success'); 
    		     }
    		 },
    		 error: function(xhr, textStatus, errorThrown){
    			 alert("failed: " + textStatus);
    		 }
    	});
    };
