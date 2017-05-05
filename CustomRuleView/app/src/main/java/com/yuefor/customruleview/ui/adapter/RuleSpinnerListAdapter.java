package com.yuefor.customruleview.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.yuefor.customruleview.R;
import com.yuefor.customruleview.bean.GameRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by G40-70 on 2015/10/9.
 */
public class RuleSpinnerListAdapter extends BaseAdapter {

    private List<GameRule> mGameRuleList = new ArrayList<>();
    private Activity mContext;
    private int mCostRatio;
    public RuleSpinnerListAdapter(Activity context, List<GameRule> gameRuleList, int costRatio) {
        mContext = context;
        mGameRuleList = gameRuleList;
        this.mCostRatio = costRatio;
    }

    public void setAppBeans(List<GameRule> gameRuleList) {
        this.mGameRuleList = gameRuleList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mGameRuleList.size();
    }

    @Override
    public GameRule getItem(int position) {
        return mGameRuleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final GameRule gameRule = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner_dropdown, parent, false);
            viewHolder.titleTv = (TextView) convertView.findViewById(R.id.text_spinner_rule_2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleTv.setText(gameRule.getControlText());
        int cost = 0;
        try {
            cost = Integer.valueOf(gameRule.getCost());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cost != 0 && mCostRatio != 0 && mCostRatio != 0){
            float realCost = (float) cost / (float)mCostRatio;
            viewHolder.titleTv.append("(");
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_happy_coin);
            ImageSpan imgSpan = new ImageSpan(mContext, bitmap);
            SpannableString spanString = new SpannableString("icon");
            spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.titleTv.append(spanString);
            viewHolder.titleTv.append("x"+ realCost + ")");
        }


        return convertView;
    }


    class ViewHolder {
        public TextView titleTv;
    }

}
