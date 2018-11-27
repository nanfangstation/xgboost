/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.model;

import java.util.Arrays;
import java.util.List;

import ml.dmlc.xgboost4j.java.XGBoostError;
import ml.dmlc.xgboost4j.scala.Booster;
import ml.dmlc.xgboost4j.scala.DMatrix;
import ml.dmlc.xgboost4j.scala.XGBoost;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 05:00
 */
public class ModelLoadPredict {
    public static void main(String[] args) {
        try {
            Booster booster = XGBoost.loadModel("");
            DMatrix feature = new DMatrix("");
            float[][] predicts = booster.predict(feature, false, 1000);
            List predictList = Arrays.asList(predicts);
            Double result = predictList.parallelStream().mapToDouble(a -> {
                float[] b = (float[]) a;
                return b[0];
            }).sum();
            result = result < 0 ? 0 : result / 7;
        } catch (XGBoostError xgBoostError) {
            xgBoostError.printStackTrace();
        }
    }
}
