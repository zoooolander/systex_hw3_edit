<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>註冊</title>
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
        form {
            display: flex;
            flex-direction: column;
            width: 300px;
        }
        label {
            margin-bottom: 10px;
            color: #333;
        }
        input[type="text"], input[type="password"], input[type="email"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        input[type="submit"]:hover {
            background-color: #388E3C;
        }
        a {
            text-decoration: none;
            color: #007BFF;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<h1>註冊</h1>
<form action="registerAction.jsp" method="post">
    <label for="username">使用者名稱：</label>
    <input type="text" id="username" name="username" required>

    <label for="email">電子郵件：</label>
    <input type="email" id="email" name="email" required>

    <label for="password">密碼：</label>
    <input type="password" id="password" name="password" required>

    <input type="submit" value="註冊">
</form>
<p><a href="login.jsp">已有帳號？立即登入</a></p>
</body>
</html>
