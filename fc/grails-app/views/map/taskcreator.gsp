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
	<body">
	    <form name="myform" method="POST">
	    </form>
        <script>
            loginForm();
        </script>
	</body>
</html>
