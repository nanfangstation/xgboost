/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 03:49
 */
public class XGBoostPredictor {

    private List<TreeNode> treeNodeList = Lists.newArrayList();

    public XGBoostPredictor(String modelPath) {
        loadModel(modelPath);
    }

    public List<TreeNode> getTreeNodeList() {
        return treeNodeList;
    }

    private void loadModel(String modelPath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(modelPath)));
            String line;
            Map<Integer, String> treeNodeMap = Maps.newHashMap();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("booster")) {
                    if (!line.contains("[0]")) {
                        parseTree(treeNodeMap);
                        treeNodeMap.clear();
                    }
                } else {
                    String[] parts = line.trim().split(":");
                    Integer id = Integer.parseInt(parts[0]);
                    treeNodeMap.put(id, parts[1]);
                }
            }
            parseTree(treeNodeMap);
            bufferedReader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseTree(Map<Integer, String> treeNodeMap) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(0);
        parseLine(treeNode, 0, treeNodeMap);
        treeNodeList.add(treeNode);
    }

    private void parseLine(TreeNode treeNode, Integer id, Map<Integer, String> treeNodeMap) {
        String line = treeNodeMap.get(id);
        if (line.contains("leaf")) { // 叶子节点
            String[] parts = line.split(",");
            String[] tuple = parts[0].split("=");
            treeNode.setId(id);
            treeNode.setFeatureName(tuple[0]);
            treeNode.setModelValue(Double.parseDouble(tuple[1]));
        } else { //分裂节点
            String[] parts = line.split(" ");
            String splitText = parts[0].substring(1, parts[0].length() - 1);
            String[] splitTuple = splitText.split("<");
            treeNode.setFeatureName(splitTuple[0]); // 分隔特征
            treeNode.setModelValue(Double.parseDouble(splitTuple[1])); // 分隔数值
            String[] dataArray = parts[1].split(",");
            Integer yes = Integer.parseInt(dataArray[0].split("=")[1]); // 左节点
            Integer no = Integer.parseInt(dataArray[1].split("=")[1]); // 右节点
            Integer missing = Integer.parseInt(dataArray[0].split("=")[1]); // 缺失
            treeNode.setMissingFlag(yes.equals(missing));
            treeNode.setId(id);
            TreeNode leftNode = new TreeNode();
            parseLine(leftNode, yes, treeNodeMap);
            treeNode.setLeftNode(leftNode);
            TreeNode rightNode = new TreeNode();
            parseLine(rightNode, no, treeNodeMap);
            treeNode.setRightNode(rightNode);
        }
    }

    public PredictResult predictResult(Map<String, Double> featureMap) {
        PredictResult predictResult = new PredictResult();
        try {
            Double predictScore = 0.0;
            List<TreeNode> nodePath = Lists.newArrayList();
            for (int i = 0; i < treeNodeList.size(); i++) {
                TreeNode treeNode = treeNodeList.get(i);
                while (treeNode.getLeftNode() != null) {
                    if (featureMap.containsKey(treeNode.getFeatureName())) {
                        if (featureMap.get(treeNode.getFeatureName()) < treeNode.getModelValue()) {
                            treeNode = treeNode.getLeftNode();
                        } else {
                            treeNode = treeNode.getRightNode();
                        }
                    } else {
                        if (treeNode.getMissingFlag()) {
                            treeNode = treeNode.getLeftNode();
                        } else {
                            treeNode = treeNode.getRightNode();
                        }
                    }
                }
                predictScore += treeNode.getModelValue();
                nodePath.add(treeNode.clone());
            }
            predictResult.setPredictScore(predictScore + 0.5);
            predictResult.setNodePath(nodePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return predictResult;
    }

}
