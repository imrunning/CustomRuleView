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
import android.widget.CheckBox;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;
import com.yuefor.customruleview.ui.fragment.RuleSelectFragment;

import java.util.ArrayList;
import java.util.List;

public class RuleCheckBoxAdapter extends RecyclerView.Adapter<RuleCheckBoxAdapter.CheckBoxViewHolder>{
    private Context mContext;
    private List<GameRule> mGameRuleList = new ArrayList<>();
    private int mMaxSelect = 0;
    private int mMinSelect = 0;
    private int mPlayerNum;
    private int mCostRatio;
    private RuleSelectFragment.UpdateUI mUpdateUIListener;

    public RuleCheckBoxAdapter(Context context, int playerNum, int maxSelect, int minSelect, int costRatio){
        this.mContext = context;
        this.mMaxSelect = maxSelect;
        this.mMinSelect = minSelect;
        this.mPlayerNum = playerNum;
        this.mCostRatio = costRatio;
    }
    @Override
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckBoxViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_rule_checkbox, parent, false));
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
    public void onBindViewHolder(final CheckBoxViewHolder holder, final int position) {
        final GameRule gameRule = mGameRuleList.get(position);
        if (gameRule == null) {
            return;
        }
        holder.checkBox.setText(gameRule.getControlText());
        int cost = 0;
        try {
            cost = Integer.valueOf(gameRule.getCost());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cost != 0 && mCostRatio != 0 && mCostRatio != 0){
            float realCost = (float) cost / (float)mCostRatio;
            holder.checkBox.append("(");
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
            ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.checkBox.append(spanString);
            holder.checkBox.append("x"+ realCost + ")");
        }

        if(gameRule.isSelect()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gameRule.isSelect()){
                    if(mMinSelect !=0 && getSelectCount() <= mMinSelect){
                        notifyDataSetChanged();
                        return;
                    }
                    gameRule.setSelect(false);
                    holder.checkBox.setChecked(false);
                    if(mUpdateUIListener != null){
                        mUpdateUIListener.updateCost();
                    }

                }else{
                    if(!gameRule.getIsPlayerNumberRequired().equals("0")){
                        if (mPlayerNum > Integer.valueOf(gameRule.getPlayerNumberMax())){
                            notifyDataSetChanged();
                            return;
                        }
                    }

                    if(mMaxSelect != 0 && getSelectCount() >= mMaxSelect){
                        notifyDataSetChanged();
                        return;
                    }
                    gameRule.setSelect(true);
                    holder.checkBox.setChecked(true);
                    if(mUpdateUIListener != null){
                        mUpdateUIListener.updateCost();
                    }
                }
            }
        });
    }

    /**
     * 获取已选择股则数量
     * @return 数量
     */
    private int getSelectCount(){
        int selectCount = 0;
        for(GameRule gameRule : mGameRuleList){
            if(gameRule.isSelect()){
                selectCount++;
            }
        }
        return selectCount;
    }

    @Override
    public int getItemCount() {
        return mGameRuleList.size();
    }

    public class CheckBoxViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox_rule);
        }
    }

}