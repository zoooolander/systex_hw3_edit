<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="java.util.List, java.util.Set, java.util.ArrayList, java.util.Collections" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
    <meta charset="UTF-8">
    <title>樂透號碼結果</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center mb-4">樂透號碼結果</h1>
    <%
        List<Set<Integer>> lotteryNumbers = (List<Set<Integer>>) request.getAttribute("lotteryNumbers");
        Integer numberOfSets = (Integer) request.getAttribute("numberOfSets"); // 使用 Integer 而不是 int

        if (lotteryNumbers != null && numberOfSets != null) { // 檢查 lotteryNumbers 和 numberOfSets 是否為 null
            for (int i = 0; i < numberOfSets; i++) {
                List<Integer> sortedNumbers = new ArrayList<>(lotteryNumbers.get(i));
                Collections.sort(sortedNumbers);
                String numbersString = String.join(", ", sortedNumbers.stream().map(String::valueOf).toArray(String[]::new));
    %>
    <div class="alert alert-light border" role="alert">
        第 <strong><%= (i + 1) %></strong> 組號碼：<span class="badge bg-success"><%= numbersString %></span>
    </div>
    <%
        }
    } else {
    %>
    <div class="alert alert-warning" role="alert">無結果。</div>
    <%
        }
    %>

    <div class="mt-4 text-center">
        <a href="main.jsp" class="btn btn-primary">重新產生號碼</a>
        <a href="../index.jsp" class="btn btn-secondary">返回首頁</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
