<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
		<script>
		    function loginForm() {
		        document.myform.action = "${login_url}";
		        document.myform.submit();
		    }
		</script>
    </head>
	<body onLoad="loginForm()">
	    <form name="myform" method="POST">
	        <input type="hidden" name="${loginname_name}" value="${loginname}"/>
	        <input type="hidden" name="${loginpassword_name}" value="${loginpassword}"/>
	    </form>
	</body>
</html>
