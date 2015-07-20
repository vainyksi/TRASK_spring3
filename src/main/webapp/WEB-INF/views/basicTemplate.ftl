<#macro basic_structure>
<#-- add parameters: "...macro structure user, afsdvc..."-->

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.main_content {
	margin-left: auto;
	margin-right: auto;
}

.top_menu {
	height: 30px;
	background: aqua;
	margin-bottom: 20px;
	padding: 10px 5px 5px 5px;
}

#login_button {
	float: right;
}
#footer {
	margin-top: 50px;
	text-align: center;
}
</style>
</head>
<body>
	<div class="top_menu">
		{user.name} | Registrations | Users | Branches <span id="login_button">{Login btn}
			| {Logout btn}</span>
	</div>
	<div class="main_content">
	
		<#nested>
	
	</div>
	<div id="footer">
		<hr>
		<i> Registrations test project - TRASK SOLUTIONS SLOVAKIA </i>
	</div>
</body>
</html>
</#macro>


<@basic_structure>
	ahoj
</@basic_structure>