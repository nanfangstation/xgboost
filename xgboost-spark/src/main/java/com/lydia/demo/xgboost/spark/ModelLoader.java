/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.spark;

import java.util.List;

import org.apache.spark.ml.tuning.TrainValidationSplitModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 03:10
 */
public class ModelLoader {
    public static void main(String[] args) {
        SparkSession sparkSession = new SparkSession.Builder().master("local").appName("xgboost").getOrCreate();
        TrainValidationSplitModel model = TrainValidationSplitModel.load("");
        SQLContext sqlContext = sparkSession.sqlContext();
        Dataset<Row> feature = sqlContext.read().json("");
        Dataset<Row> result = model.transform(feature);
        List<Row> rowList = result.toJavaRDD().collect();
        for (Row row : rowList) {
            System.out.println("分数: " + row.getFloat(row.fieldIndex("prediction")));
        }
    }
}
