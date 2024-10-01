package com.systex.hw3edit.controller;

import com.systex.hw3edit.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Controller
public class LotteryController {

    @Autowired
    private LotteryService lotteryService;

    @GetMapping("/main")
    public String showMainPage() {
        return "lottery/main"; // 返回 main.jsp
    }

    @PostMapping("/lottery")
    public String generateLotteryNumbers(
            @RequestParam("numberOfSets") String numberOfSetsStr,
            @RequestParam("excludeNumbers") String excludeNumbersStr,
            Model model) {

        LinkedList<String> errorMsgs = new LinkedList<>();
        model.addAttribute("errors", errorMsgs);

        // 1. 驗證輸入
        int numberOfSets = lotteryService.validateNumberOfSets(numberOfSetsStr, errorMsgs);
        Set<Integer> excludeNumbers = lotteryService.validateExcludeNumbers(excludeNumbersStr, errorMsgs);

        // 如果有錯誤，返回原頁面
        if (!errorMsgs.isEmpty()) {
            return "main";
        }

        // 2. 調用 Service 層產生樂透號碼
        List<Set<Integer>> lotteryNumbers = lotteryService.generateLotteryNumbers(excludeNumbers, numberOfSets * 6);

        // 3. 將結果添加到 model，並返回結果頁面
        model.addAttribute("lotteryNumbers", lotteryNumbers);
        model.addAttribute("numberOfSets", numberOfSets); // 修正變數名稱
        return "lottery/result";
    }
}
