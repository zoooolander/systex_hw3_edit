<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Welcome</title>
	<style>
		body {
			font-family: Arial, sans-serif;
			background-color: #f4f4f4;
			margin: 0;
			padding: 20px;
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			height: 100vh;
		}
		h1 {
			color: #333;
			margin-bottom: 20px;
		}
		p {
			margin: 20px 0; /* 增加間隔 */
		}
		a {
			text-decoration: none;
			color: white;
			background-color: #4CAF50;
			padding: 10px 20px;
			border-radius: 5px;
			transition: background-color 0.3s;
		}
		a:hover {
			background-color: #388E3C;
		}
	</style>
</head>
<body>
<h1>Homework3</h1>
<p>
	<a href="lottery/main.jsp">Lottery</a>
</p>
<p>
	<a href="game/guess.jsp">Guess Number</a>
</p>
</body>
</html>
