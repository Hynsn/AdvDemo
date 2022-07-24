package com.hynson.chart;

public enum BPQueryTimeType {
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    ALL("all");

    String queryType;

    BPQueryTimeType(String queryType) {
        this.queryType = queryType;
    }

}
