<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.LinkedList"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>樂透號碼產生器</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f4f4;
        }
        .btn-custom {
            background-color: #4CAF50;
            color: white;
        }
        .btn-custom:hover {
            background-color: #388E3C;
        }
        .welcome-message {
            font-size: 14px;
            margin-bottom: 10px;
            text-align: center;
        }
        /* 右上角登出連結 */
        .logout {
            position: absolute;
            top: 20px;
            right: 20px;
        }
        /* 登出連結顏色設置為藍色 */
        .logout a {
            color: blue;
            text-decoration: none;
        }
        .logout a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">

    <%-- 右上角的登出連結 --%>
    <div class="logout">
        <a href="#" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">登出</a>
        <form id="logout-form" action="<%= request.getContextPath() %>/logout" method="post" style="display: none;">
            <!-- 隱藏表單，使用JavaScript觸發POST請求 -->
        </form>
    </div>

    <div class="text-center">
        <%-- 顯示使用者名稱 --%>
        <%
            String username = (String) session.getAttribute("username");
            if (username != null) {
        %>
        <div class="welcome-message">
            歡迎, <%= username %>!
        </div>
        <%
            }
        %>

        <h1 class="mb-4">樂透號碼產生器</h1>

        <form action="${pageContext.request.contextPath}/lottery" method="post" class="bg-light p-4 border rounded">
            <div class="mb-3">
                <label for="numberOfSets" class="form-label">請輸入需要產生幾組號碼：</label>
                <input type="number" id="numberOfSets" name="numberOfSets" required class="form-control" placeholder="輸入數字">
            </div>

            <div class="mb-3">
                <label for="excludeNumbers" class="form-label">請輸入5個需要排除的數字 (範圍 1~49，以空格分隔)：</label>
                <input type="text" id="excludeNumbers" name="excludeNumbers" required class="form-control" placeholder="例如：1 2 3 4 5">
            </div>

            <%-- 顯示錯誤訊息 --%>
            <%
                LinkedList<String> errors = (LinkedList<String>) request.getAttribute("errors");
                if (errors != null) {
            %>
            <div class="alert alert-danger" role="alert">
                <ul class="list-unstyled mb-0">
                    <%
                        for (String error : errors) {
                    %>
                    <li><%= error %></li>
                    <%
                        }
                    %>
                </ul>
            </div>
            <%
                }
            %>

            <button type="submit" class="btn btn-custom btn-lg">產生號碼</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>