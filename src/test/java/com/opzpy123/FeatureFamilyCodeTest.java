package com.opzpy123;

import cn.hutool.core.util.NumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class FeatureFamilyCodeTest {


    public static void main(String[] args) {
        FeatureFamilyCodeTest featureFamilyCodeTest = new FeatureFamilyCodeTest();
        System.out.println(featureFamilyCodeTest.tableName2EntityName("bommgmt.bm_part_assembly_mbom"));
    }

    private String tableName2EntityName(String tableName) {
        String lowerCase = tableName.split("\\.")[1].toLowerCase(Locale.ROOT);
        String[] s = lowerCase.split("_");
        StringBuilder enetityName = new StringBuilder();
        for (int i = 1; i < s.length; i++) {
            char[] arr = s[i].toCharArray();
            arr[0] = Character.toUpperCase(arr[0]);
            enetityName.append(new String(arr));
        }
        return enetityName.toString();
    }

    private static void numberUtils() {
        List<Double> doubles = List.of(-3.0, -2.0, -0.4, 0.5, 1.2, 4.0, 8.9);
        List<Double> collect = doubles.stream().filter(it -> NumberUtil.compare(it, -2.3) > 0 && NumberUtil.compare(1, it) > 0).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static void hashmaptest() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("a", "a");
        hashMap.put("b", "a");
        hashMap.put("c", "a");
        hashMap.put("d", "a");
        hashMap.put("e", "a");
    }

    private void featureFamilyCodeTEst() {
        long num = 99999;
        ArrayList<String> familyCodeList = new ArrayList<>();
        char[] alphabet = new char[26];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) ('A' + i);
        }
        //AA-ZZ;
        for (char a : alphabet) {
            for (char b : alphabet) {
                familyCodeList.add(a + String.valueOf(b));
            }
        }
        if (familyCodeList.size() > num) {
            List<String> collect = familyCodeList.stream().limit(num).collect(Collectors.toList());
        }
        //AAA-ZZZ;
        while (familyCodeList.size() < num) {
            ArrayList<String> familyCodeListCopy = new ArrayList<>(familyCodeList);
            for (String s : familyCodeListCopy) {
                for (char a : alphabet) {
                    familyCodeList.add(s + a);
                }
            }
        }
        List<String> collect = familyCodeList.stream().limit(num + 1).collect(Collectors.toList());
    }
}
