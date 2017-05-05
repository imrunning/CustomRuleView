package com.yuefor.customruleview.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CTWLPC on 2017/3/22.
 */

public class GameRule implements Serializable{
    @SerializedName("ControlKey")
    private String controlKey;

    @SerializedName("ControlText")
    private String controlText;

    @SerializedName("ControlType")
    private String controlType; //控件类型

    @SerializedName("DefaultKey")
    private String defaultKey; //默认选中的key

    @SerializedName("Cost")
    private String cost;

    @SerializedName("CostRatio")
    private String costRatio; //cost显示倍率，实际值为1.3, costRatio为10, 则界面显示为13

    @SerializedName("SelectionMin")
    private String selectionMin; //子控件最少选中个数

    @SerializedName("SelectionMax")
    private String selectionMax; //子控件最多选中个数

    @SerializedName("IsPlayerNumberRequired")
    private String isPlayerNumberRequired; //是否有玩家人数限制

    @SerializedName("PlayerNumberMin")
    private String playerNumberMin; //最少玩家人数

    @SerializedName("PlayerNumberMax")
    private String playerNumberMax; //最多玩家人数

    @SerializedName("Category")
    private String category; //规则大类

    @SerializedName("ControlStyle")
    private GameRuleStyle controlStyle; //控制控件显示个数和样式

    @SerializedName("Children")
    private List<GameRule> children;

    private boolean isSelect;

    public String getDefaultKey() {
        return defaultKey != null ? defaultKey : "";
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey ;
    }

    public String getCost() {
        return cost != null ? cost : "0";
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCostRatio() {
        return costRatio != null ? costRatio : "1";
    }

    public void setCostRatio(String costRatio) {
        this.costRatio = costRatio;
    }

    public String getSelectionMin() {
        return selectionMin != null ? selectionMin : "0";
    }

    public void setSelectionMin(String selectionMin) {
        this.selectionMin = selectionMin;
    }

    public String getSelectionMax() {
        return selectionMax != null ? selectionMax : "0";
    }

    public void setSelectionMax(String selectionMax) {
        this.selectionMax = selectionMax;
    }

    public String getIsPlayerNumberRequired() {
        return isPlayerNumberRequired != null ? isPlayerNumberRequired : "0";
    }

    public void setIsPlayerNumberRequired(String isPlayerNumberRequired) {
        this.isPlayerNumberRequired = isPlayerNumberRequired;
    }

    public String getPlayerNumberMin() {
        return playerNumberMin != null ? playerNumberMin : "0";
    }

    public void setPlayerNumberMin(String playerNumberMin) {
        this.playerNumberMin = playerNumberMin;
    }

    public String getPlayerNumberMax() {
        return playerNumberMax != null ? playerNumberMax : "0";
    }

    public void setPlayerNumberMax(String playerNumberMax) {
        this.playerNumberMax = playerNumberMax;
    }

    public String getCategory() {
        return category != null ? category : "";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getControlKey() {
        return controlKey != null ? controlKey : "";
    }

    public void setControlKey(String controlKey) {
        this.controlKey = controlKey;
    }

    public String getControlText() {
        return controlText != null ? controlText : "";
    }

    public void setControlText(String controlText) {
        this.controlText = controlText;
    }

    public String getControlType() {
        return controlType != null ? controlType : "";
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public GameRuleStyle getControlStyle() {
        return controlStyle;
    }

    public void setControlStyle(GameRuleStyle controlStyle) {
        this.controlStyle = controlStyle;
    }

    public List<GameRule> getChildren() {
        return children;
    }

    public void setChildren(List<GameRule> children) {
        this.children = children;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
