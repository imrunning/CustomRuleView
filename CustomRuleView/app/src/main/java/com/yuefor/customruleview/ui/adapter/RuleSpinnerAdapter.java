package com.yuefor.customruleview.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.ui.fragment.RuleSelectFragment;
import com.yuefor.customruleview.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class RuleSpinnerAdapter extends RecyclerView.Adapter<RuleSpinnerAdapter.SpinnerViewHolder>{
    private Activity mContext;
    private int mPlayerNum;
    private PopupWindow mRuleListWindow;
    private ListView mListView;
    private RuleSpinnerListAdapter mRuleSpinnerListAdapter;
    private List<GameRule> mGameRuleList = new ArrayList<>();
    private int mCostRatio;
    private RuleSelectFragment.UpdateUI mUpdateUIListener;

    public RuleSpinnerAdapter(Activity context, int playerNum, int costRatio){
        this.mContext = context;
        this.mPlayerNum = playerNum;
        this.mCostRatio = costRatio;
    }
    @Override
    public SpinnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpinnerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_rule_spinner, parent, false));
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    public void setDatas(List<GameRule> gameRuleList) {
        this.mGameRuleList = gameRuleList;
        notifyDataSetChanged();
    }

    public void setUiUpdateListener(RuleSelectFragment.UpdateUI updateUIListener){
        mUpdateUIListener = updateUIListener;
    }

    @Override
    public void onBindViewHolder(final SpinnerViewHolder holder, final int position) {
        final GameRule gameRule = mGameRuleList.get(position);
        if (gameRule == null) {
            return;
        }
        String defaultKey = gameRule.getDefaultKey();
        String defaultText = "";
        final List<GameRule> childGameList = gameRule.getChildren();
        for(int i = 0; i < childGameList.size(); i++){
            if(childGameList.get(i).getControlKey().equals(defaultKey)){
                defaultText = childGameList.get(i).getControlText();
                childGameList.get(i).setSelect(true);
            }
        }

        holder.spinnerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRuleList(holder, childGameList);
            }
        });
        holder.spinnerTextView.setText(defaultText);
    }

    @Override
    public int getItemCount() {
        return mGameRuleList.size();
    }

    public class SpinnerViewHolder extends RecyclerView.ViewHolder{
        TextView spinnerTextView;
        public SpinnerViewHolder(View itemView) {
            super(itemView);
            spinnerTextView = (TextView) itemView.findViewById(R.id.text_spinner_rule);
        }
    }

    private void showRuleList(final SpinnerViewHolder viewHolder, final List<GameRule> gameRuleList) {
        if (mRuleListWindow == null && mListView == null && mRuleSpinnerListAdapter == null) {
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int width = viewHolder.spinnerTextView.getWidth();
            mListView = new ListView(mContext);
            mListView.setDividerHeight(0);
            mRuleSpinnerListAdapter = new RuleSpinnerListAdapter((Activity) mContext, mGameRuleList, mCostRatio);
            mRuleListWindow = new PopupWindow(mListView, width, height, true);
            mRuleListWindow.setOutsideTouchable(true);
            mRuleListWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.mipmap.ic_dropdown_item_bg));
        }
        if(CollectionUtils.isNotEmpty(gameRuleList)){
            mRuleSpinnerListAdapter.setAppBeans(gameRuleList);
            mListView.setAdapter(mRuleSpinnerListAdapter);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mRuleListWindow != null){
                    mRuleListWindow.dismiss();
                }
                GameRule gameRule = (GameRule) parent.getItemAtPosition(position);
                if (!gameRule.getIsPlayerNumberRequired().equals("0")) {
                    if (mPlayerNum > Integer.valueOf(gameRule.getPlayerNumberMax())) {
                        return;
                    }

                }
                viewHolder.spinnerTextView.setText(gameRule.getControlText());

                int cost = 0;
                try {
                    cost = Integer.valueOf(gameRule.getCost());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(cost != 0 && mCostRatio != 0 && mCostRatio != 0){
                    float realCost = (float) cost / (float)mCostRatio;
                    viewHolder.spinnerTextView.append("(");
                    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
                    ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    viewHolder.spinnerTextView.append(spanString);
                    viewHolder.spinnerTextView.append("x"+ realCost + ")");
                }

                for (int i = 0; i < gameRuleList.size(); i++) {
                    if (i != position) {
                        gameRuleList.get(i).setSelect(false);
                    }
                }
                gameRule.setSelect(true);

                if(mUpdateUIListener != null){
                    mUpdateUIListener.updateCost();
                }
            }
        });

        int windowPos[] = calculatePopWindowPos(viewHolder.spinnerTextView, mListView, gameRuleList.size());
        mRuleListWindow.showAtLocation(viewHolder.spinnerTextView, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);

    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView, final View contentView, int listSize) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在window中的左上角坐标位置
        anchorView.getLocationInWindow(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取window的高宽
        int height = mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getHeight();
        int width = mContext.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWidth();

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int contentHeight = contentView.getMeasuredHeight() * listSize;
        final int contentWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (height - anchorLoc[1] - anchorHeight < contentHeight);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] - contentHeight;
        } else {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }
}