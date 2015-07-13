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
    
    
+    URL 中+号表示空格                      %2B     
空格 URL中的空格可以用+号或者编码           %20   
/   分隔目录和子目录                        %2F       
?    分隔实际的URL和参数                    %3F       
%    指定特殊字符                           %25       
#    表示书签                               %23       
&    URL 中指定的参数间的分隔符             %26       
=    URL 中指定参数的值                     %3D

jQuery ajax封装的get()和post()，已经对特殊字符"&"等做了处理：  
看看如下正则：  
rprotocol = /^\/\//,  
rquery = /\?/,  
rscript = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,  
rselectTextarea = /^(?:select|textarea)/i,  
rspacesAjax = /\s+/,  
rts = /([?&])_=[^&]*/,  
rurl = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/；  
  
// try replacing _= if it is there  
ret = s.url.replace(rts, "$1_=" + ts);  
// if nothing was replaced, add timestamp to the end  
s.url = ret + ((ret === s.url) ? (rquery.test(s.url) ? "&": "?") + "_=" + ts: "");  
最后附：  
javascript 编码和解码函数：  
  
1)encodeURI()：  
a>主要用于整个URI  
b>对空格进行编码  
c>不会对本身属于URI的特殊字符进行编码，例如":","/","?","#"  
  
2)encodeURIComponent():  
a>主要用于URI中的某一段  
b>会对发现的任何非标准字符进行编码  
  
3)escape():  
a>不会对 ASCII 字母和数字进行编码，  
b>不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . /   
c>其他所有的字符都会被转义序列替换。  
d>ECMAScript v3 反对使用该方法，应用使用 decodeURI() 和 decodeURIComponent() 替代它。  
<script type="text/javascript">  
//Visit%20W3@@@School%21  
console.log(escape("Visit W3@@@School!"));  
//%3F%21%3D%28%29%23%25%26  
console.log(escape("?!=()#%&"))  
</script>
    
