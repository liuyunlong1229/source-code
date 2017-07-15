function login(){
		
		$.ajax({
			   type: "POST",
			   url: BASE_CONTEXT_PATH+"/user/login",
			   data: "userName="+$("#userName").val(),
			   success: function(msg){
				   console.log(msg);
				   if (msg.ok) {
					   window.location.href=BASE_CONTEXT_PATH+"/user-list";
	                } else {
	                	 if (msg.code&&msg.message) {
		                        alert(msg.message);
		                    }
	                }
			   }
			});
		
	}
