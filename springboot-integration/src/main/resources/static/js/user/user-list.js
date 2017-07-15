$.ajax({
   type: "GET",
   url: BASE_CONTEXT_PATH+"/user/list",
 //  data: "name=John&location=Boston",
 //  dataType:"json",
   success: function(msg){
	   var usersHTML="<tr><th>用户名</th><th>出生日期</th></tr>";
	   console.log(msg);
	  if(msg.ok && msg.content){
		   for(var i=0;i<msg.content.length;i++){
			   var user=msg.content[i];
			   usersHTML+="<tr><td>"+user.userName+"</td><td>"+getSmpFormatDateByLong(user.birthDay,true)+"</td></tr>"; 
		   }
	   }
	   $("#userTab").append(usersHTML);
   }
});