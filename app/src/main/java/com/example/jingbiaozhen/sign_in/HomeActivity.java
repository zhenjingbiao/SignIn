package com.example.jingbiaozhen.sign_in;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.jingbiaozhen.sign_in.fragment.LeaveFragment;
import com.example.jingbiaozhen.sign_in.fragment.LeaveListFragment;
import com.example.jingbiaozhen.sign_in.fragment.ScoreFragment;
import com.example.jingbiaozhen.sign_in.fragment.SignInFragment;
import com.example.jingbiaozhen.sign_in.fragment.SignListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by jingbiaozhen on 2018/5/23.
 **/

public class HomeActivity extends FragmentActivity
{

    private static final String TAG = "HomeActivity";

    public static final int REQUEST_CODE_SCAN = 10;

    @BindView(R.id.frame_content)
    FrameLayout mFrameContent;

    @BindView(R.id.tab_content)
    FrameLayout mTabContent;

    @BindView(R.id.tab)
    FragmentTabHost mTabHost;

    private String[] mTitles = {"签到", "签到列表","请假", "请假列表", "成绩"};

    private Class[] mFragmentClass = {SignInFragment.class, SignListFragment.class,LeaveFragment.class, LeaveListFragment.class,
            ScoreFragment.class};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();
    }

    private void initView()
    {
        setupTabHost();
    }

    private void setupTabHost()
    {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.frame_content);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {

            }
        });
        for (int i = 0; i < mFragmentClass.length; i++)
        {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTitles[i]).setIndicator(getView(i));
            mTabHost.addTab(tabSpec, mFragmentClass[i], null);
        }

    }

    protected View getView(int index)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_content, null);
        TextView textView = view.findViewById(R.id.tab_textview);
        textView.setText(mTitles[index]);

        return view;
    }

}
