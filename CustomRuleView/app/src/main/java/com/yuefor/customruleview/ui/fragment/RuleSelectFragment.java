package com.yuefor.customruleview.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.bean.UserSelectRule;
import com.yuefor.customruleview.ui.activity.RuleSelectActivity;
import com.yuefor.customruleview.ui.widget.RuleLinearLayout;
import com.yuefor.customruleview.util.CollectionUtils;
import com.yuefor.customruleview.util.RuleUtil;

import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_BACK;

public class RuleSelectFragment extends Fragment{
    private final int REQUEST_PAY_HAPPY_COIN = 1001;
    private LinearLayout contentLL;
    private List<GameRule> mGameRuleList;
    private int mPlayerNum = 1; //当前房间的人数
    private int mCostRatio = 1; //倍率
    private float mRealCost; //总花费
    private GameRule mGameRule;
    private UserSelectRule mUserSelectRule;
    private TextView mHappyIconTv;
    private Button commitBtn;
    private String mTcyRoomId;
    private int mAppId;
    private int mRuleId;
    private RuleSelectActivity.EventClick mEventClick;
    private boolean mNeedPostRule;
    //是否需要使用defaultKey 来初始化默认选择
    private boolean mNeedUseDefaultKey;
    public static final int CODE_RULE_NOT_EXIST = 57;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mNeedPostRule = getArguments().getBoolean("needPostRule");
            mCostRatio = getArguments().getInt("costRatio");
            mPlayerNum = getArguments().getInt("playerNum");
            mGameRule = (GameRule) getArguments().getSerializable("gameRule");
            mUserSelectRule = (UserSelectRule) getArguments().getSerializable("userSelectRule");

            mTcyRoomId = getArguments().getString("tcyRoomId");
            mAppId = getArguments().getInt("appId");
            mRuleId = getArguments().getInt("ruleId");
            mNeedUseDefaultKey = getArguments().getBoolean("needUseDefaultKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rule_select, container, false);
        return v;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        initData();
        initListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    private void initUI(View view) {
        commitBtn = (Button) view.findViewById(R.id.btn_commit);
        mHappyIconTv = (TextView) view.findViewById(R.id.tv_happy_coin);
        contentLL = (LinearLayout) view.findViewById(R.id.ll_rule);
        mGameRuleList =  mGameRule.getChildren();
        mGameRule.setSelect(true);

        if(mNeedUseDefaultKey){
            initDefaultKey(mGameRuleList);
            initRuleDisplayView(mGameRuleList);
        }else{
            initDefaultKey(mGameRuleList, mUserSelectRule);
            initRuleDisplayView(mGameRuleList, mUserSelectRule);
        }
    }

    private void initListener(){
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserSelectRule.setTotalCost(String.valueOf(RuleUtil.getTotalCost(RuleUtil.getSelectRuleList(mGameRule))));
                mUserSelectRule.setCategory(mGameRule.getCategory());
                mUserSelectRule.setUserSelection(RuleUtil.getUserSelection(mGameRule));
                if(mNeedPostRule){
                    String ruleUserDataStr = new Gson().toJson(mUserSelectRule);
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("UserSelectRule", mUserSelectRule);
                }
            }
        });
    }


    private void initData(){
        initTotalCost();
    }

    private void initTotalCost(){
        int totalCost = RuleUtil.getTotalCost(RuleUtil.getSelectRuleList(mGameRule));
        if(mCostRatio != 0){
            mRealCost = (float) totalCost / mCostRatio;
            mHappyIconTv.setText(mRealCost + "欢乐币");
        }

    }


    /**
     * 初始化defaultKey 使用用户选择的数据初始化
     * @param gameRuleList
     */
    private void initDefaultKey(List<GameRule> gameRuleList, UserSelectRule userSelectRule){
        for(GameRule gameRule : gameRuleList){
            List<String> userRuleKeyList = new ArrayList<>();//TODO RuleUtil.getSelectRuleKeyList(userSelectRule.getUserSelection());
            List<GameRule> childRuleList = gameRule.getChildren();
            List<GameRule> childChildGameRuleList = null;
            if(CollectionUtils.isNotEmpty(childRuleList)){
                for(GameRule gameRuleTemp : childRuleList){
                    boolean isSelect = false;
                    for(String ruleKey : userRuleKeyList){
                        if(gameRuleTemp.getControlKey().equals(ruleKey)){
                            isSelect = true;
                        }
                    }
                    gameRuleTemp.setSelect(isSelect);
                    if(gameRuleTemp.getChildren() != null){
                        childChildGameRuleList= gameRuleTemp.getChildren();
                        initDefaultKey(childChildGameRuleList, userSelectRule);
                    }
                }
            }

        }
    }

    /**
     * 初始化defaultKey  使用defaultKey初始化
     * @param gameRuleList
     */
    private void initDefaultKey(List<GameRule> gameRuleList){
        for(GameRule gameRule : gameRuleList){
            String defaultKey = "";
            String[]defaultKeys = null;
            if(gameRule.getControlType().equals("RadioButtonGroup")){
                defaultKey = gameRule.getDefaultKey();
            }else if(gameRule.getControlType().equals("CheckBoxGroup")){
                defaultKeys = gameRule.getDefaultKey().split("&");
            }else if(gameRule.getControlType().equals("DropDownList")){
                defaultKey = gameRule.getDefaultKey();
            }else if(gameRule.getControlType().equals("TabContainer")){
                defaultKey = gameRule.getDefaultKey();
            }

            List<GameRule> childRuleList = gameRule.getChildren();
            List<GameRule> childChildGameRuleList = null;
            if(CollectionUtils.isNotEmpty(childRuleList)){
                for(GameRule gameRuleTemp : childRuleList){
                    if(defaultKeys != null){
                        for(int i = 0; i < defaultKeys.length; i++){
                            if(gameRuleTemp.getControlKey().equals(defaultKeys[i])){
                                gameRuleTemp.setSelect(true);
                            }
                        }
                    }else{
                        if(gameRuleTemp.getControlKey().equals(defaultKey)){
                            gameRuleTemp.setSelect(true);
                        }
                    }
                    if(gameRuleTemp.getChildren() != null){
                        childChildGameRuleList= gameRuleTemp.getChildren();
                        initDefaultKey(childChildGameRuleList);
                    }
                }
            }

        }
    }

    /**
     * 使用用户选择的规则 初始化  规则布局
     * @param gameRuleList
     * @param userSelectRule
     */
    private void initRuleDisplayView(List<GameRule> gameRuleList, UserSelectRule userSelectRule){
        initRuleDisplayView(gameRuleList, userSelectRule, -1);
    }


    /**
     * 使用用户选择的规则 初始化  规则布局
     * @param gameRuleList
     * @param userSelectRule 用户的选择的规则
     * @param parentPostion 父布局的postion
     */
    private void initRuleDisplayView(List<GameRule> gameRuleList, UserSelectRule userSelectRule, int parentPostion){
        for(GameRule gameRule : gameRuleList){

            List<GameRule> childRuleList = gameRule.getChildren();
            List<GameRule> childChildGameRuleList = null;
            if(CollectionUtils.isNotEmpty(childRuleList)){
                for(GameRule gameRuleTemp : childRuleList){
                    List<String> userRuleKeyList = new ArrayList<>();//TODO RuleUtil.getSelectRuleKeyList(userSelectRule.getUserSelection());
                    for(String userRuleKey : userRuleKeyList){
                        if(gameRuleTemp.getControlKey().equals(userRuleKey)){
                            if(gameRuleTemp.getChildren() != null){
                                childChildGameRuleList = gameRuleTemp.getChildren();
                            }
                        }
                    }

                }
            }


            int postion = addRuleView(gameRule);
            if(parentPostion != -1){
                ((RuleLinearLayout)contentLL.getChildAt(parentPostion))
                        .addChildView((RuleLinearLayout) contentLL.getChildAt(postion));
            }
            if(CollectionUtils.isNotEmpty(childChildGameRuleList)){
                initRuleDisplayView(childChildGameRuleList, userSelectRule, postion);
            }
        }
    }



    /**
     * 使用defaultKey初始化  规则布局
     * @param gameRuleList
     */
    private void initRuleDisplayView(List<GameRule> gameRuleList){
        initRuleDisplayView(gameRuleList, -1);
    }

    /**
     * 使用defaultKey初始化  规则布局
     * @param gameRuleList
     * @param parentPostion 父布局的postion
     */
    private void initRuleDisplayView(List<GameRule> gameRuleList, int parentPostion){
        for(GameRule gameRule : gameRuleList){
            String defaultKey = "";
            String[]defaultKeys = null;
            if(gameRule.getControlType().equals("RadioButtonGroup")){
                defaultKey = gameRule.getDefaultKey();
            }else if(gameRule.getControlType().equals("CheckBoxGroup")){
                defaultKeys = gameRule.getDefaultKey().split("&");
            }else if(gameRule.getControlType().equals("ControlContainer")){
                defaultKey = gameRule.getDefaultKey();
            }else if(gameRule.getControlType().equals("TabContainer")){
                defaultKey = gameRule.getDefaultKey();
            }

            List<GameRule> childRuleList = gameRule.getChildren();
            List<GameRule> childChildGameRuleList = null;
            if(CollectionUtils.isNotEmpty(childRuleList)){
                for(GameRule gameRuleTemp : childRuleList){
                    if(gameRuleTemp.getControlKey().equals(defaultKey)){
                        if(gameRuleTemp.getChildren() != null){
                            childChildGameRuleList= gameRuleTemp.getChildren();
                        }
                    }

                }
            }
            int postion = addRuleView(gameRule);
            if(parentPostion != -1){
                ((RuleLinearLayout)contentLL.getChildAt(parentPostion))
                        .addChildView((RuleLinearLayout) contentLL.getChildAt(postion));
            }
            if(CollectionUtils.isNotEmpty(childChildGameRuleList)){
                initRuleDisplayView(childChildGameRuleList, postion);
            }
        }
    }

    private UpdateUI mUpdateUIListener = new UpdateUI() {
        @Override
        public void updateCost() {
            initTotalCost();
        }
    };

    private int addRuleView(GameRule gameRuleTemp){
        RuleLinearLayout ruleLinearLayout = new RuleLinearLayout(getActivity());
        ruleLinearLayout.setUiUpdateListener(mUpdateUIListener);
        ruleLinearLayout.setData(gameRuleTemp, mPlayerNum, mCostRatio);
        contentLL.addView(ruleLinearLayout);
        LinearLayout splitView = null;
        String showName = gameRuleTemp.getControlText();
        if(!TextUtils.isEmpty(showName)){
            splitView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_split_empty, null);
        }else{
            splitView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_split_line, null);

        }
        contentLL.addView(splitView);
        return  (contentLL.getChildCount() - 2);
    }

    public void setEventClick(RuleSelectActivity.EventClick eventClick){
        mEventClick = eventClick;
    }

    public interface UpdateUI{
        /**
         * 更新花费的欢乐币
         */
        void updateCost();
    }
}
