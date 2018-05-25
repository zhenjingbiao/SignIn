package com.example.jingbiaozhen.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.SignInBean;
import com.example.jingbiaozhen.sign_in.fragment.LeaveFragment;
import com.example.jingbiaozhen.sign_in.fragment.LeaveListFragment;
import com.example.jingbiaozhen.sign_in.fragment.ScoreFragment;
import com.example.jingbiaozhen.sign_in.fragment.SignInFragment;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.yzq.zxinglibrary.common.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/*
 * Created by jingbiaozhen on 2018/5/23.
 **/

public class HomeActivity extends FragmentActivity
{
    public static final int REQUEST_CODE_SCAN = 10;

    @BindView(R.id.frame_content)
    FrameLayout mFrameContent;

    @BindView(R.id.tab_content)
    FrameLayout mTabContent;

    @BindView(R.id.tab)
    FragmentTabHost mTabHost;

    private String[] mTitles = {"签到", "请假", "请假列表", "成绩"};

    private Class[] mFragmentClass = {SignInFragment.class, LeaveFragment.class, LeaveListFragment.class,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK)
        {
            if (data != null)
            {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                queryCourseAndSign(content);

            }
        }
    }

    private void queryCourseAndSign(String content)
    {
        String username = getSharedPreferences("user", MODE_PRIVATE).getString("username", "");
        OkHttpUtils.post().url(Constants.UPDATE_SIGN).addParams("course_id", content).addParams("username",
                username).build().execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Toast.makeText(HomeActivity.this, "课程查询失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        BaseLocalModel model = JsonHelper.parseJson(response);
                        SignInBean signInBean = new SignInBean();
                        if (model.isSucess())
                        {
                            Intent intent = new Intent(HomeActivity.this, SignInActivity.class);
                            intent.putExtra("signInfo", signInBean);
                            HomeActivity.this.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "课程查询失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
