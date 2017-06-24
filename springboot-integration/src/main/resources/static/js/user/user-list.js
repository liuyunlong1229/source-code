$.ajax({
   type: "GET",
   url: "/user/list",
 //  data: "name=John&location=Boston",
 //  dataType:"json",
   success: function(msg){
	   var usersHTML="<tr><th>用户名</th><th>出生日期</th></tr>";
	   for(var i=0;i<msg.length;i++){
		   var user=msg[i];
		   usersHTML+="<tr><td>"+user.userName+"</td><td>"+getSmpFormatDateByLong(user.birthDay,true)+"</td></tr>"; 
	   }
	   $("#userTab").append(usersHTML);
   }
});