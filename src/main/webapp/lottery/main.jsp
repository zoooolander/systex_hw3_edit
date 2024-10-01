<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.util.LinkedList"%>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>樂透號碼產生器</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/style/myStyle.css" rel="stylesheet">
</head>
<body style="background-color: #f9f9f9;">

<div class="container d-flex justify-content-center align-items-center" style="height: 100vh;">
    <div class="text-center">
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

            <%-- Start error report --%>
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
            <%-- End error report --%>

            <button type="submit" class="btn btn-success btn-lg">產生號碼</button>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
