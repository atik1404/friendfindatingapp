package com.friendfinapp.dating.ui.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.friendfinapp.dating.R;
import com.friendfinapp.dating.databinding.ActivityIntroBinding;
import com.friendfinapp.dating.helper.Constants;
import com.friendfinapp.dating.ui.intro.introadapter.AppIntroPagerAdapter;
import com.friendfinapp.dating.ui.signin.SignInActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class IntroActivity extends AppCompatActivity implements  ViewPager.OnPageChangeListener{


    ActivityIntroBinding binding;

    AppIntroPagerAdapter mAdapter;

    private int dotsCount;
    private ImageView[] dots;
    boolean check = false;

    private Context mContext;
    int[] mResources = {R.drawable.ic_intro11, R.drawable.ic_intro22, R.drawable.ic_intro33};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_intro);

        mContext=this;
        mAdapter = new AppIntroPagerAdapter(IntroActivity.this, mContext, mResources);
        binding.viewpager.setAdapter(mAdapter);
        binding.viewpager.setCurrentItem(0);
        binding.viewpager.setOnPageChangeListener(this);
        setPageViewIndicator();

        setUpClickListener();


            AdView  adView = new AdView(this);
            AdRequest adRequest =new AdRequest.Builder().build();

            adView.setAdSize(AdSize.BANNER);

            adView.setAdUnitId(getString(R.string.BannerAdsUnitId));
            binding.adView.loadAd(adRequest);


    }

    private void setUpClickListener() {
        binding.getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    startActivity(new Intent(IntroActivity.this, SignInActivity.class));
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPageViewIndicator() {

        Log.d("###setPageViewIndicator", " : called");
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(mContext);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    70,
                    70
            );

            params.setMargins(4, 20, 4, 0);

            final int presentPosition = i;
            dots[presentPosition].setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    binding.viewpager.setCurrentItem(presentPosition);
                    return true;
                }

            });


            binding.viewPagerCountDots.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        Log.e("###onPageSelected, pos ", String.valueOf(position));
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        if (position == 2) {
            binding.getStarted.setBackgroundResource(R.drawable.get_started_bg);
            check = true;
        } else {
            binding.getStarted.setBackgroundResource(R.drawable.get_started_bg2);
            check = false;
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void scrollPage(int position) {
        binding.viewpager.setCurrentItem(position);
    }
}