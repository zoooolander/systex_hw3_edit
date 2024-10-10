package com.systex.hw3edit.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LotteryService {

    /**
     * @Description 驗證組數
     * @param numberOfSetsStr
     * @param errorMsgs
     * @return
     */
    public int validateNumberOfSets(String numberOfSetsStr, LinkedList<String> errorMsgs) {
        try {
            int numberOfSets = Integer.parseInt(numberOfSetsStr);
            if (numberOfSets <= 0) {
                errorMsgs.add("組數必須大於 0！");
                return 0;
            }
            return numberOfSets;
        } catch (NumberFormatException e) {
            errorMsgs.add("請輸入有效的組數！");
            return 0;
        }
    }

    /**
     * @Descripiton 驗證排除的數字
     * @param excludeNumbersStr
     * @param errorMsgs
     * @return
     */
    public Set<Integer> validateExcludeNumbers(String excludeNumbersStr, LinkedList<String> errorMsgs) {
        Set<Integer> excludeNumbers = new HashSet<>();
        String[] inputNumbers = excludeNumbersStr.split(" ");

        // todo 修改架構，太多層了！可參考 guard clause
        if (inputNumbers.length != 5) {
            errorMsgs.add("請輸入正好 5 個數字！");
        } else {
            for (String numStr : inputNumbers) {
                try {
                    int num = Integer.parseInt(numStr);
                    if (num < 1 || num > 49 || excludeNumbers.contains(num)) {
                        errorMsgs.add("數字需介於 1~49 且不能重複！");
                    } else {
                        excludeNumbers.add(num);
                    }
                } catch (NumberFormatException e) {
                    errorMsgs.add("請輸入有效的整數！");
                }
            }
        }
        return excludeNumbers;
    }

    /**
     * @Description 產生樂透號碼的方法
     * @param excludeNumbers
     * @param totalNumbers
     * @return
     */
    public List<Set<Integer>> generateLotteryNumbers(Set<Integer> excludeNumbers, int totalNumbers) {
        List<Set<Integer>> result = new ArrayList<>();
        Random random = new Random();
        Set<Integer> numbers;

        while (result.size() * 6 < totalNumbers) {
            numbers = new HashSet<>();
            while (numbers.size() < 6) {
                int randomNumber = random.nextInt(49) + 1;
                if (!excludeNumbers.contains(randomNumber)) {
                    numbers.add(randomNumber);
                }
            }
            result.add(numbers);
        }
        return result;
    }

}
