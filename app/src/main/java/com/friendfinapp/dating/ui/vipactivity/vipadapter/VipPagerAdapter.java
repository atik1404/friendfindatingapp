package com.friendfinapp.dating.ui.vipactivity.vipadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.friendfinapp.dating.R;
import com.friendfinapp.dating.ui.vipactivity.VipPremiumActivity;

public class VipPagerAdapter extends PagerAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater;
    private int[] mResources;
    private VipPremiumActivity activity;


    public VipPagerAdapter(VipPremiumActivity appIntroActivity, Context mContext, int[] mResources) {
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mResources = mResources;
        this.activity = appIntroActivity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_slider_layout, container, false);
        ImageView ivImage = (ImageView) itemView.findViewById(R.id.ivImage);

        TextView ctvText = (TextView) itemView.findViewById(R.id.ctvText);
        TextView ctvTextdecrib = (TextView) itemView.findViewById(R.id.ctvTextdecrib);
        // TextView iv_icon = (TextView) itemView.findViewById(R.id.iv_icon);
        ivImage.setImageResource(mResources[position]);
        setDescText(position, ctvText, ctvTextdecrib);


        container.addView(itemView);
        ctvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int pos = position + 1;
                activity.scrollPage(pos);


            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setDescText(int pos, TextView ctvText, TextView ctvTextdecrib) {
        switch (pos) {
            case 0:
                ctvTextdecrib.setText(mContext.getString(R.string.viptext));
                ctvText.setText(mContext.getString(R.string.vipdesc));
                break;
            case 1:
                ctvTextdecrib.setText(mContext.getString(R.string.viptext2));
                ctvText.setText(mContext.getString(R.string.vipdesc2));
                break;
//            case 2:
//                ctvTextdecrib.setText(mContext.getString(R.string.viptext3));
//                ctvText.setText(mContext.getString(R.string.vipdesc3));
//                break;
//            case 3:
//                ctvTextdecrib.setText(mContext.getString(R.string.viptext4));
//                ctvText.setText(mContext.getString(R.string.vipdesc4));
//                break;
//            case 4:
//                ctvTextdecrib.setText(mContext.getString(R.string.viptext5));
//                ctvText.setText(mContext.getString(R.string.vipdesc5));
//                break;
//            case 5:
//                ctvTextdecrib.setText(mContext.getString(R.string.viptext6));
//                ctvText.setText(mContext.getString(R.string.vipdesc6));
//                break;

        }
    }
}
