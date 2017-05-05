package com.yuefor.customruleview.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by CTWLPC on 2017/3/22.
 */

public class GameRuleStyle {
    @SerializedName("DisplayAs")
    private String displayAs;

    @SerializedName("ColumnCount")
    private String columnCount;

    public String getDisplayAs() {
        return displayAs != null ? displayAs : "";
    }

    public void setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }

    public String getColumnCount() {
        return columnCount != null ? columnCount : "2";
    }

    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
    }
}
