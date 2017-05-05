package com.yuefor.customruleview.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CTWLPC on 2017/3/22.
 */

public class RuleShowLinearLayout extends LinearLayout {
    private TextView mRuleContentTv;
    private TextView mRuleTitleTextView;
    private Context mContext;

    public enum RuleShowType{
        RadioButton, //单选框
        CheckBox,    //多选框
        Spinner      //下拉框
    }
    public RuleShowLinearLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RuleShowLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RuleShowLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_show_rule, this);
        mRuleContentTv = (TextView) findViewById(R.id.tv_rule_content);
        mRuleTitleTextView = (TextView) findViewById(R.id.tv_rule_title);
    }


    /**
     *
     * @param parentGameRule
     */
    public void setData(GameRule parentGameRule, int costRatio){
        RuleShowLinearLayout.RuleShowType ruleShowType = RuleShowLinearLayout.RuleShowType.RadioButton;
        String showName = parentGameRule.getControlText();
        List<GameRule> childRuleList = parentGameRule.getChildren();

        if(parentGameRule.getControlType().equals("RadioButtonGroup") || parentGameRule.getControlType().equals("TabContainer")){
            ruleShowType = RuleShowLinearLayout.RuleShowType.RadioButton;
        }else if(parentGameRule.getControlType().equals("CheckBoxGroup")){
            ruleShowType = RuleShowLinearLayout.RuleShowType.CheckBox;
        }else if(parentGameRule.getControlType().equals("ControlContainer")){
            ruleShowType = RuleShowLinearLayout.RuleShowType.Spinner;
        }

        mRuleTitleTextView.setText(showName);

        if(ruleShowType == RuleShowType.RadioButton){
            List<GameRule> selectRuleList = new ArrayList<>();
            for(GameRule gameRule : childRuleList){
                if(gameRule.isSelect()){
                    selectRuleList.add(gameRule);
                }
            }

            for(int i = 0; i < selectRuleList.size(); i++){

                mRuleContentTv.append(selectRuleList.get(i).getControlText());
                if(selectRuleList.size() - 1 != i){
                    mRuleContentTv.append(",");
                }
            }
        }else if(ruleShowType == RuleShowType.CheckBox){
            List<GameRule> selectRuleList = new ArrayList<>();
            for(GameRule gameRule : childRuleList){
                if(gameRule.isSelect()){
                    selectRuleList.add(gameRule);
                }
            }

            for(int i = 0; i < selectRuleList.size(); i++){
                int cost = 0;
                try {
                    cost = Integer.valueOf(selectRuleList.get(i).getCost());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(cost != 0 && costRatio != 0){
                    mRuleContentTv.append(selectRuleList.get(i).getControlText());
                    float realCost = (float) cost / (float)costRatio;
                    mRuleContentTv.append("(");
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
                    ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mRuleContentTv.append(spanString);
                    mRuleContentTv.append("x"+ realCost + ")");

                }else {
                    mRuleContentTv.append(selectRuleList.get(i).getControlText());
                }
                if(selectRuleList.size() - 1 != i){
                    mRuleContentTv.append(",");
                }
            }
        }else if(ruleShowType == RuleShowType.Spinner){
            List<GameRule> selectRuleList = new ArrayList<>();
            for(GameRule gameRule : childRuleList){
                for(GameRule gameRuleChild : gameRule.getChildren()){
                    if(gameRuleChild.isSelect()){
                        selectRuleList.add(gameRuleChild);
                    }
                }
            }
            for(int i = 0; i < selectRuleList.size(); i++){
                int cost = 0;
                try {
                    cost = Integer.valueOf(selectRuleList.get(i).getCost());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(cost != 0 && costRatio != 0){
                    mRuleContentTv.append(selectRuleList.get(i).getControlText());
                    float realCost = (float) cost / (float)costRatio;
                    mRuleContentTv.append("(");
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
                    ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mRuleContentTv.append(spanString);
                    mRuleContentTv.append("x"+ realCost + ")");

                }else {
                    mRuleContentTv.append(selectRuleList.get(i).getControlText());
                }
                if(selectRuleList.size() - 1 != i){
                    mRuleContentTv.append(",");
                }
            }
        }
    }
}
