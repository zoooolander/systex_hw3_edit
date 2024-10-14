<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>註冊</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f4f4;
        }
        /* 自定義註冊按鈕顏色 */
        .btn-custom {
            background-color: #4CAF50;
            color: white;
        }
        .btn-custom:hover {
            background-color: #388E3C;
        }
        /* 灰字樣式 */
        .top-left-text {
            position: absolute;
            top: 10px;
            left: 10px;
            color: gray;
            font-size: 14px;
        }
    </style>
</head>
<body>
<!-- 左上角灰字 -->
<div class="top-left-text">
    這是原本的註冊頁面
<%--    <a href="<%= request.getContextPath() %>/ajaxLogin.jsp"  style="margin-left: 5px;">切換到 AJAX 登入頁面</a>--%>
</div>
<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
    <div class="card p-4" style="width: 300px;">
        <h1 class="text-center mb-4">註冊</h1>

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

        <form action="register" method="post">
            <div class="mb-3">
                <label for="username" class="form-label">使用者名稱：</label>
                <input type="text" id="username" name="username" required class="form-control" placeholder="輸入使用者名稱">
            </div>

            <div class="mb-3">
                <label for="email" class="form-label">電子郵件：</label>
                <input type="email" id="email" name="email" required class="form-control" placeholder="輸入電子郵件">
            </div>

            <div class="mb-3">
                <label for="password" class="form-label">密碼：</label>
                <input type="password" id="password" name="password" required class="form-control" placeholder="輸入密碼">
            </div>

            <button type="submit" class="btn btn-custom w-100">註冊</button>
        </form>

        <p class="mt-3 text-center">
            <a href="login.jsp">已有帳號？立即登入</a>
        </p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
