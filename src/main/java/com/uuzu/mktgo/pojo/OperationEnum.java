/*
 * Copyright 2015-2020 msun.com All right reserved.
 */
package com.uuzu.mktgo.pojo;

/**
 * @author zhoujin
 */
public enum OperationEnum {

    EQ("="), GT(">"), LT("<"), GTL(">="), LTL("<="), BET("<>");

    String operation;

    OperationEnum(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
