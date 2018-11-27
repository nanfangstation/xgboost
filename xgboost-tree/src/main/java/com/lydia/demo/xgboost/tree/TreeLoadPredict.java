/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.tree;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 04:30
 */
public class TreeLoadPredict {

    public static void main(String[] args) {
        String dataPath = "";
        String modelPathOutOrder = "";
        String modelPathInOrder = "";
        String modelPathBubble = "";
        XGBoostPredictor xgBoostPredictorOutOrder = new XGBoostPredictor(modelPathOutOrder);
        XGBoostPredictor xgBoostPredictorInOrder = new XGBoostPredictor(modelPathInOrder);
        XGBoostPredictor xgBoostPredictorBubble = new XGBoostPredictor(modelPathBubble);
        List<Map<String, Double>> feature = readFature(dataPath);
        double resultOutOrder = feature.stream().mapToDouble(f -> {
            PredictResult predictResult = xgBoostPredictorOutOrder.predictResult(f);
            return predictResult.getPredictScore();
        }).sum();
        System.out.println("出站平均分: " + resultOutOrder / 7);
        double resultInOrder = feature.stream().mapToDouble(f -> {
            PredictResult predictResult = xgBoostPredictorInOrder.predictResult(f);
            return predictResult.getPredictScore();
        }).sum();
        System.out.println("入站平均分: " + resultInOrder / 7);
        double resultBubble = feature.stream().mapToDouble(f -> {
            PredictResult predictResult = xgBoostPredictorBubble.predictResult(f);
            return predictResult.getPredictScore();
        }).sum();
        System.out.println("冒泡平均分: " + resultBubble / 7);
    }

    private static List<Map<String, Double>> readFature(String path) {
        List<String> dataList = read(path);
        List<Feature> featureList = Lists.newArrayList();
        featureList = JSON.parseArray(dataList.get(0), Feature.class);
        List<Map<String, Double>> featureMapList = Lists.newArrayList();
        featureList.forEach(feature -> {
            Map<String, Double> treeMap = new TreeMap<>();
            Class cl = feature.getClass();
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods) {
                String methodName = method.getName();
                String fieldName = methodName.substring(3);
                char[] fieldChar = fieldName.toCharArray();
                fieldChar[0] += 32;
                String lowerFieldName = String.valueOf(fieldChar);
                if (methodName.startsWith("get") && FeatureEnum.getIndexByAttribute(lowerFieldName) != null) {
                    try {
                        treeMap.put("f" + FeatureEnum.getIndexByAttribute(lowerFieldName),
                            (double) (int) method.invoke(feature));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            featureMapList.add(treeMap);
        });
        return featureMapList;
    }

    private static List<String> read(String filePath) {
        List<String> list = Lists.newArrayList();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineText;
                while ((lineText = bufferedReader.readLine()) != null) {
                    list.add(lineText);
                }
                bufferedReader.close();
                read.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
