package com.yuefor.customruleview.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.bean.UserSelectRule;
import com.yuefor.customruleview.ui.fragment.RuleSelectFragment;
import com.yuefor.customruleview.util.CollectionUtils;
import com.yuefor.customruleview.util.CommonUtil;
import com.yuefor.customruleview.util.PackageUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuleSelectActivity extends FragmentActivity {
    private LinearLayout contentLL;
    private ImageView mCloseIv;
    private GameRule mGameRule;
    private UserSelectRule mUserSelectRule;
    private List<GameRule> mGameRuleList;
    private int mPlayerNum = 10; //当前房间的人数
    private int mCostRatio;
    private String[] mTitle = null; //= new String[] { "血战" };
    private int mSelectTabPostion = 0;
//    private CustomPageIndicator mIndicator;
    private ViewPager mViewPager;
    private String mGameRuleStr = "";
    private String mUserSelectRuleStr = "";
    private String mTcyRoomId;
    private int mAppId;
    private int mRuleId;
    private LinearLayout mParentLl;
    private boolean mNeedPostRule = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null){
            mPlayerNum = getIntent().getExtras().getInt("playerNum");
            mGameRuleStr = getIntent().getExtras().getString("gameRuleStr");
            mUserSelectRuleStr = getIntent().getExtras().getString("userSelectRuleStr");
            mCostRatio = getIntent().getExtras().getInt("costRatio");

            mTcyRoomId = getIntent().getExtras().getString("tcyRoomId");
            mAppId = getIntent().getExtras().getInt("appId");
            mRuleId = getIntent().getExtras().getInt("ruleId");
            mNeedPostRule = getIntent().getExtras().getBoolean("needPostRule");

        }
        String ruleStr = PackageUtils.getAssetsString("rule.json");
        try {
            JSONObject jsonObject = new JSONObject(ruleStr);
            mGameRuleStr = jsonObject.optString("UiData");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_select_rule);
        initView();
        initData();
        initListener();

    }

    private void initView(){
        mParentLl = (LinearLayout) findViewById(R.id.ll_parent);
        contentLL = (LinearLayout) findViewById(R.id.ll_rule);
        mCloseIv = (ImageView) findViewById(R.id.iv_close);
        mViewPager = (ViewPager)findViewById(R.id.pager);
//        mIndicator = (CustomPageIndicator)findViewById(R.id.indicator);
//        mIndicator.setLayoutRes(R.layout.layout_rule_select_tab_title);
    }

    private void initData(){
        try {
            mGameRule = new Gson().fromJson(mGameRuleStr, new TypeToken<GameRule>(){}.getType());
            //初始化用户选择的规则
            mUserSelectRule = new Gson().fromJson(mUserSelectRuleStr, new TypeToken<UserSelectRule>(){}.getType());
        }catch (Exception e){
            e.printStackTrace();
        }
        mGameRuleList = mGameRule.getChildren();
        mTitle = new String[mGameRuleList.size()];
        //TODO
//        if(CollectionUtils.isNotEmpty(mGameRuleList)){
//            for(int i = 0; i < mGameRuleList.size(); i++){
//                mTitle[i] = mGameRuleList.get(i).getControlText();
//                Map userRuleJSONObject = mUserSelectRule.getUserSelection();
//
//                Set entries = userRuleJSONObject.entrySet();
//                if (entries != null) {
//                    Iterator iterator = entries.iterator();
//                    while (iterator.hasNext()) {
//                        Map.Entry entry = (Map.Entry) iterator.next();
//                        if(entry.getKey().equals(mGameRuleList.get(i).getControlKey())){
//                            mSelectTabPostion = i;
//                        }
//                    }
//                }
//            }
//        }

        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    private void initListener(){
        mCloseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            RuleSelectFragment fragment = new RuleSelectFragment();
            Bundle args = new Bundle();

            //是否需要使用defaultKey 来初始化默认选择
            if(mSelectTabPostion == position){
                args.putBoolean("needUseDefaultKey", false);
            }else{
                args.putBoolean("needUseDefaultKey", true);
            }
            args.putString("category", mTitle[position]);
            args.putInt("playerNum", mPlayerNum);
            args.putInt("costRatio", mCostRatio);
            args.putSerializable("gameRule", mGameRuleList.get(position));
            args.putSerializable("userSelectRule", mUserSelectRule);

            args.putString("tcyRoomId", mTcyRoomId);
            args.putInt("appId", mAppId);
            args.putInt("ruleId", mRuleId);
            args.putBoolean("needPostRule", mNeedPostRule);
            fragment.setEventClick(mEventClick);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position % mTitle.length];
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }
    }




    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.x = 5;
        lp.y = 5;
        int height = CommonUtil.dip2px(500);
        int width = CommonUtil.dip2px(300);
        lp.width = width;
        lp.height = height;
        getWindowManager().updateViewLayout(view, lp);
    }

    private EventClick mEventClick = new EventClick() {
        @Override
        public void commitClick() {
            mParentLl.setVisibility(View.GONE);
        }
    };

    public interface EventClick{
        /**
         * 确认点击
         */
        void commitClick();
    }


}
