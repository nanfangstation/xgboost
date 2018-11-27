/*
 * Copyright (c) 2001-2018 GuaHao.com Corporation Limited. All rights reserved.
 * This software is the confidential and proprietary information of GuaHao Company.
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.lydia.demo.xgboost.tree;

/**
 * TODO
 *
 * @author Lydia
 * @version V1.0
 * @since 2018-11-28 04:46
 */
public enum FeatureEnum {
    sign_space_count(0, "sign_space_count");

    FeatureEnum(int index, String attribute) {
        this.index = index;
        this.attribute = attribute;
    }

    public static Integer getIndexByAttribute(String attribute) {
        for (FeatureEnum featureEnum : FeatureEnum.values()) {
            if (attribute.equals(featureEnum.getAttribute())) {
                return featureEnum.getIndex();
            }
        }
        return null;
    }

    public static String getAttributeByIndex(Integer index) {
        for (FeatureEnum featureEnum : FeatureEnum.values()) {
            if (index.equals(featureEnum.getIndex())) {
                return featureEnum.getAttribute();
            }
        }
        return null;
    }

    private int index;
    private String attribute;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
