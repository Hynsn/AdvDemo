package com.hynson.chart;


public enum BPUnitType {
    MMHG("mmHg"),
    KPA("Kpa");

    String queryType;

    BPUnitType(String queryType) {
        this.queryType = queryType;
    }
}
