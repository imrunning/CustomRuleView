package com.yuefor.customruleview.util;


import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.bean.UserSelectRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by CTWLPC on 2017/4/1.
 */

public class RuleUtil {
    public static Map<String, String> getShareRuleList(GameRule gameRule, UserSelectRule userRule) {
        if(gameRule == null || userRule == null){
            return new HashMap<>();
        }
        Map<String, String> shareRuleMap = new LinkedHashMap<>();
        Map<String, String> ruleKeyMap = new HashMap<>();
        insertRuleKeyToMap(ruleKeyMap, gameRule);
        Map userRuleJSONObject = userRule.getUserSelection();
        Set entries = userRuleJSONObject.entrySet();
        if (entries != null) {
            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String ruleText = ruleKeyMap.get(entry.getKey());
                shareRuleMap.put(entry.getKey().toString(), ruleText);
            }
        }
        return shareRuleMap;
    }

    private static void insertRuleKeyToMap(Map ruleKeyMap,GameRule gameRule){
        if(gameRule == null || ruleKeyMap == null){
            return ;
        }
        ruleKeyMap.put(gameRule.getControlKey(), gameRule.getControlText());
        if(CollectionUtils.isNotEmpty(gameRule.getChildren())){
            for(GameRule gameRuleTemp : gameRule.getChildren()){
                ruleKeyMap.put(gameRuleTemp.getControlKey(), gameRuleTemp.getControlText());
                insertRuleKeyToMap(ruleKeyMap, gameRuleTemp);
            }
        }
    }

    /**
     * 获取已选择规则的list
     * @param gameRule
     * @return
     */
    public static List<GameRule> getSelectRuleList(GameRule gameRule){
        List<GameRule> gameRuleList = new ArrayList<>();
        insertSelectRuleToList(gameRule, gameRuleList);
        return gameRuleList;
    }

    private static void insertSelectRuleToList(GameRule gameRule, List<GameRule> selectRuleList){
        if(gameRule != null && selectRuleList != null){
            if(gameRule.isSelect()){
                selectRuleList.add(gameRule);
            }
            if(CollectionUtils.isNotEmpty(gameRule.getChildren())){
                for(GameRule gameRuleTemp : gameRule.getChildren()){
                    insertSelectRuleToList(gameRuleTemp, selectRuleList);
                }
            }
        }

    }

    public static int getTotalCost(List<GameRule> gameRuleList){
        int totalCost = 0;
        if(CollectionUtils.isNotEmpty(gameRuleList)){
            for(GameRule gameRule : gameRuleList){
                try {
                    totalCost = totalCost + Integer.valueOf(gameRule.getCost());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return totalCost;
    }

    public static Map getUserSelection(GameRule gameRule){
        if(gameRule == null){
            return new HashMap();
        }
        Map jsonObject = new HashMap();
        List<GameRule> gameRuleList = getSelectRuleList(gameRule);
        for(GameRule gameRuleTemp : gameRuleList){
            jsonObject.put(gameRuleTemp.getControlKey(), "1");
        }
        return jsonObject;
    }

    public static List<String> getSelectRuleKeyList(Map jsonObject){
        if(jsonObject == null){
            return new ArrayList<>();
        }
        List<String> gameRuleKeyList = new ArrayList<>();
//        for(Map.Entry entry : jsonObject.entrySet()){
//             gameRuleKeyList.add(entry.getKey().toString());
//        }
        Set entries = jsonObject.entrySet();
        if (entries != null) {
            Iterator iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                gameRuleKeyList.add(entry.getKey().toString());
            }
        }
        return gameRuleKeyList;
    }

}
