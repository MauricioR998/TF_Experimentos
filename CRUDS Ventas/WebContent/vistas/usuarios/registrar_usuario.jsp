<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registrar Usuario</title>
</head>
<body>
	<h2>Registrar Usuario</h2>
	<table>
		<tr>
			<td><a href="adminController?action=index" >Regresar</a> </td>
		</tr>
	</table>
	<form action="adminController?action=registrar_usuario" method="post">
		<table align="center" class="table">
		<tr>
			<th scope="row"><a>Username:</a></th>		
			<td><input type="text" name="username"/></td>	
		</tr>
		<tr>
			<th scope="row"><a>Password:</a></th>		
			<td><input type="text" name="password"/></td>	
		</tr>		
		</table>
	<br>
	<table border="0" align="center">
		<tr>
		<td><button type="submit" name="registrar" class="btn btn-secondary">Registrar</button></td>	
		</tr>
		</table>
	</form>
</body>
</html>