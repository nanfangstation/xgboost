/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.tree;

import java.util.List;

import lombok.Data;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 03:53
 */
@Data
public class PredictResult {
    private Integer predictLabel;
    private Double predictScore;
    private List<TreeNode> nodePath;

    @Override
    public String toString() {
        return "PredictResult{" + "predictLabel=" + predictLabel + ", predictScore=" + predictScore + ", nodePath="
            + nodePath + '}';
    }
}
