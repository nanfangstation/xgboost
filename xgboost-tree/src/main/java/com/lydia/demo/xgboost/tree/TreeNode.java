/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.tree;

import lombok.Data;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 03:49
 */
@Data
public class TreeNode implements Cloneable {
    private Integer id;
    private TreeNode leftNode;
    private TreeNode rightNode;
    private String featureName;
    private Double modelValue;
    private Boolean missingFlag;

    protected TreeNode clone() throws CloneNotSupportedException {
        super.clone();
        TreeNode treeNode = new TreeNode();
        treeNode.setId(this.id);
        treeNode.setLeftNode(this.leftNode);
        treeNode.setRightNode(this.rightNode);
        treeNode.setFeatureName(this.featureName);
        treeNode.setModelValue(this.modelValue);
        treeNode.setMissingFlag(this.missingFlag);
        return treeNode;
    }

    @Override
    public String toString() {
        return "TreeNode{" + "id=" + id + ", leftNode=" + leftNode + ", rightNode=" + rightNode + ", featureName='"
            + featureName + '\'' + ", modelValue=" + modelValue + ", missingFlag=" + missingFlag + '}';
    }
}
