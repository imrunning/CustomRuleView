package com.yuefor.customruleview.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.ui.fragment.RuleSelectFragment;
import com.yuefor.customruleview.ui.widget.RuleLinearLayout;
import com.yuefor.customruleview.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class RuleRadioButtonAdapter extends RecyclerView.Adapter<RuleRadioButtonAdapter.RadioButtonViewHolder>{
    private Context mContext;
    private List<GameRule> mGameRuleList = new ArrayList<>();
    private int mPlayerNum;
    private int mCostRatio;
//    private RuleLinearLayout mChildView;
    private List<RuleLinearLayout> mChildViewList = new ArrayList<>();
    private RuleSelectFragment.UpdateUI mUpdateUIListener;

    public RuleRadioButtonAdapter(Context context, int playerNum, int costRatio){
        this.mContext = context;
        this.mPlayerNum = playerNum;
        this.mCostRatio = costRatio;
    }

    @Override
    public RadioButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RadioButtonViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rule_radio_button, parent, false));
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

    public void addChildView(RuleLinearLayout ruleLinearLayout){
        mChildViewList.add(ruleLinearLayout);
    }

    @Override
    public void onBindViewHolder(final RadioButtonViewHolder holder, final int position) {
        final GameRule gameRule = mGameRuleList.get(position);
        if (gameRule == null) {
            return;
        }
        if (gameRule.isSelect()){
            holder.radioButton.setChecked(true);
        }else{
            holder.radioButton.setChecked(false);
        }
        holder.radioButton.setText(gameRule.getControlText());

        int cost = 0;
        try {
                cost = Integer.valueOf(gameRule.getCost());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cost != 0 && mCostRatio != 0 && mCostRatio != 0){
            float realCost = (float) cost / (float)mCostRatio;
            holder.radioButton.append("(");
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
            ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.radioButton.append(spanString);
            holder.radioButton.append("x"+ realCost + ")");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioButton.setChecked(true);
                if (gameRule.isSelect()) {
                    return;
                }
                if(!gameRule.getIsPlayerNumberRequired().equals("0")){
                    if (mPlayerNum > Integer.valueOf(gameRule.getPlayerNumberMax())){
                        notifyDataSetChanged();
                        return;
                    }
                }
                gameRule.setSelect(true);

                if(CollectionUtils.isNotEmpty(mChildViewList) && CollectionUtils.isNotEmpty(gameRule.getChildren())
                        && mChildViewList.size() == gameRule.getChildren().size()){
                    for(int i = 0; i< mChildViewList.size(); i++){
                        mChildViewList.get(i).setData(gameRule.getChildren().get(i), mPlayerNum, mCostRatio);
                    }
                }
                for (int i = 0; i < mGameRuleList.size(); i++) {
                    if (i != position) {
                        mGameRuleList.get(i).setSelect(false);
                    }
                }

                if(mUpdateUIListener != null){
                    mUpdateUIListener.updateCost();
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(CollectionUtils.isNotEmpty(mGameRuleList)){
            return mGameRuleList.size();
        }else{
            return 0;
        }

    }

    public class RadioButtonViewHolder extends RecyclerView.ViewHolder{
        public RadioButton radioButton;
        public RadioButtonViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_button_rule);
        }
    }

}