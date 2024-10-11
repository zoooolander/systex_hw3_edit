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
        /* 灰字提示 */
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

<!-- 顯示 AJAX 提示訊息 -->
<div class="top-left-text">
    這是 AJAX 登入畫面 |
    <a href="<%= request.getContextPath() %>/login.jsp"  style="margin-left: 5px;">切換到原本登入頁面</a>
</div>

<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
    <div class="card p-4" style="width: 300px;">
        <h1 class="text-center mb-4">登入</h1>

        <!-- 錯誤訊息顯示區域 -->
        <div id="error-message" class="alert alert-danger" style="display: none;" role="alert"></div>

        <form id="login-form">
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<script>
    // 處理登入表單提交事件
    $('#login-form').on('submit', function(event) {
        event.preventDefault(); // 阻止表單的默認提交

        // 獲取表單數據
        const formData = {
            email: $('#email').val(),
            password: $('#password').val()
        };

        // 發送 AJAX 請求
        $.ajax({
            url: '/eBiz/ajaxLogin',  // AJAX 登入請求的 URL
            method: 'POST',
            data: JSON.stringify(formData),
            contentType: 'application/json',
            success: function(data) {
                if (data.status === 'success') {
                    // 登入成功，轉向主頁
                    window.location.href = '/eBiz/lottery/main.jsp';
                } else {
                    // 顯示錯誤訊息
                    $('#error-message').text(data.message).show();
                }
            },
            error: function(xhr, status, error) {
                // 顯示錯誤訊息
                $('#error-message').text("發生錯誤，請重試。").show();
                console.error(error);
            }
        });
    });
</script>

</body>
</html>
