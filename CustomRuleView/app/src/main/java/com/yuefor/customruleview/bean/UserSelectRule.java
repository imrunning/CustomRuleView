package com.yuefor.customruleview.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by CTWLPC on 2017/4/1.
 */

public class UserSelectRule implements Serializable{
    @SerializedName("GameId")
    private String gameId;

    @SerializedName("AppCode")
    private String appCode;

    @SerializedName("Category")
    private String category;

    @SerializedName("CostRatio")
    private String costRatio;

    @SerializedName("TotalCost")
    private String totalCost;

    @SerializedName("UserSelection")
    private Map UserSelection;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCostRatio() {
        return costRatio;
    }

    public void setCostRatio(String costRatio) {
        this.costRatio = costRatio;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public Map getUserSelection() {
        return UserSelection;
    }

    public void setUserSelection(Map userSelection) {
        UserSelection = userSelection;
    }

}
