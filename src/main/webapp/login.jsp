<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登入</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f4f4;
        }
        /* 自定義登入按鈕顏色 */
        .btn-custom {
            background-color: #4CAF50;
            color: white;
        }
        .btn-custom:hover {
            background-color: #388E3C;
        }
    </style>
</head>
<body>
<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
    <div class="card p-4" style="width: 300px;">
        <h1 class="text-center mb-4">登入</h1>

        <%-- 錯誤訊息顯示區域 --%>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <div class="alert alert-danger" role="alert">
            <%= error %>
        </div>
        <%
            }
        %>

        <form action="login" method="post">
            <div class="mb-3">
                <label for="email" class="form-label">電子信箱：</label>
                <input type="text" id="email" name="email" required class="form-control" placeholder="輸入電子信箱">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">密碼：</label>
                <input type="password" id="password" name="password" required class="form-control" placeholder="輸入密碼">
            </div>

            <button type="submit" class="btn btn-custom w-100">登入</button>
        </form>
        <p class="mt-3 text-center">
            <a href="<%= request.getContextPath() %>/register" onclick="event.preventDefault();
                    window.location.href='<%= request.getContextPath() %>/register.jsp';">還沒有帳號？立即註冊</a>
        </p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
