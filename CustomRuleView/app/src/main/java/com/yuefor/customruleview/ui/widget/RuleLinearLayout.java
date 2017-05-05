package com.yuefor.customruleview.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.ui.adapter.RuleCheckBoxAdapter;
import com.yuefor.customruleview.ui.adapter.RuleRadioButtonAdapter;
import com.yuefor.customruleview.ui.adapter.RuleSpinnerAdapter;
import com.yuefor.customruleview.ui.fragment.RuleSelectFragment;

import java.util.List;

/**
 * Created by CTWLPC on 2017/3/22.
 */

public class RuleLinearLayout extends LinearLayout {
    private RecyclerView mRuleRecyclerView;
    private TextView mRuleTitleTextView;
    private Activity mContext;
//    private List<RuleLinearLayout> childViewList = new ArrayList<>();
//    private RuleLinearLayout mChildView;
    private RuleRadioButtonAdapter mRuleRadioButtonAdapter;
    private RuleCheckBoxAdapter mRuleCheckBoxAdapter;
    private RuleSpinnerAdapter mRuleSpinnerAdapter;
    private RuleSelectFragment.UpdateUI mUpdateUIListener;

    public enum RuleShowType{
        RadioButton, //单选框
        CheckBox,    //多选框
        Spinner      //下拉框
    }
    public RuleLinearLayout(Activity context) {
        super(context);
        mContext = context;
        init();
    }

    public RuleLinearLayout(Activity context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RuleLinearLayout(Activity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void setUiUpdateListener(RuleSelectFragment.UpdateUI updateUIListener){
        mUpdateUIListener = updateUIListener;
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.layout_rule, this);
        mRuleRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_rule);
        mRuleTitleTextView = (TextView) findViewById(R.id.tv_rule_title);
    }


    /**
     *
     * @param parentGameRule
     * @param playerNum 当前房间的人数
     */
    public void setData(GameRule parentGameRule, int playerNum, int costRatio){
        RuleLinearLayout.RuleShowType ruleShowType = RuleLinearLayout.RuleShowType.RadioButton;
        String showName = parentGameRule.getControlText();
        List<GameRule> childRuleList = parentGameRule.getChildren();
        int columnCount = 2; //默认两列
        String displayAs;
        int maxSelect = 0;
        int minSelect = 0;

        try {
            maxSelect = Integer.valueOf(parentGameRule.getSelectionMax());
            minSelect = Integer.valueOf(parentGameRule.getSelectionMin());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(parentGameRule.getControlStyle() != null){
            columnCount = Integer.valueOf(parentGameRule.getControlStyle().getColumnCount());
//            displayAs = parentGameRule.getControlStyle().getDisplayAs();
        }

        if(parentGameRule.getControlType().equals("RadioButtonGroup") || parentGameRule.getControlType().equals("TabContainer")){
            ruleShowType = RuleLinearLayout.RuleShowType.RadioButton;
        }else if(parentGameRule.getControlType().equals("CheckBoxGroup")){
            ruleShowType = RuleLinearLayout.RuleShowType.CheckBox;
        }else if(parentGameRule.getControlType().equals("ControlContainer")){
            ruleShowType = RuleLinearLayout.RuleShowType.Spinner;
        }

        GridLayoutManager ruleManager = new GridLayoutManager(mContext, columnCount){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRuleTitleTextView.setText(showName);
        mRuleRecyclerView.setLayoutManager(ruleManager);
//        mRuleRecyclerView.addItemDecoration(new RuleSpaceItemDecoration(CommonUtil.dip2px(5)));
        if(ruleShowType == RuleShowType.RadioButton){
            mRuleRadioButtonAdapter = new RuleRadioButtonAdapter(mContext, playerNum, costRatio);
            mRuleRadioButtonAdapter.setDatas(childRuleList);
            mRuleRadioButtonAdapter.setUiUpdateListener(mUpdateUIListener);
            mRuleRecyclerView.setAdapter(mRuleRadioButtonAdapter);
        }else if(ruleShowType == RuleShowType.CheckBox){
            mRuleCheckBoxAdapter  = new RuleCheckBoxAdapter(mContext, playerNum, maxSelect, minSelect , costRatio);
            mRuleCheckBoxAdapter.setDatas(childRuleList);
            mRuleCheckBoxAdapter.setUiUpdateListener(mUpdateUIListener);
            mRuleRecyclerView.setAdapter(mRuleCheckBoxAdapter);
        }else if(ruleShowType == RuleShowType.Spinner){
            mRuleSpinnerAdapter = new RuleSpinnerAdapter(mContext, playerNum, costRatio);
            mRuleSpinnerAdapter.setDatas(childRuleList);
            mRuleSpinnerAdapter.setUiUpdateListener(mUpdateUIListener);
            mRuleRecyclerView.setAdapter(mRuleSpinnerAdapter);
        }

    }

    public void addChildView(RuleLinearLayout ruleLinearLayout){
        if(mRuleRadioButtonAdapter != null){
            mRuleRadioButtonAdapter.addChildView(ruleLinearLayout);
        }
    }
}
