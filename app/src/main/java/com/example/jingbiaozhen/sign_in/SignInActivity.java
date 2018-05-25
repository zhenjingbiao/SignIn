package com.example.jingbiaozhen.sign_in;
/*
 * Created by jingbiaozhen on 2018/5/24.
 * 签到页面activity
 **/

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.SignInBean;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.example.jingbiaozhen.sign_in.utils.TimeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SignInActivity extends Activity
{
    @BindView(R.id.location_rv)
    RecyclerView mLocationRv;

    @BindView(R.id.sign_in_tv)
    TextView mSignInTv;

    @BindView(R.id.countdown_tv)
    TextView mCountdownTv;

    @BindView(R.id.course_name_tv)
    TextView mCourseNameTv;

    @BindView(R.id.sign_in_btn)
    Button mSignInBtn;

    private SignInBean mSignInBean;

    private List<SignInBean.Seat> mSignedList;

    private boolean mIsSelected;

    private int mSelectPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        initData();

    }

    private void initData()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            mSignInBean = (SignInBean) intent.getSerializableExtra("signInfo");
            if (mSignInBean != null)
            {
                if (mSignInBean.signedList != null)
                {
                    mSignedList = mSignInBean.signedList;
                }
                else
                {
                    mSignedList = new ArrayList<>();
                }
                if (!TextUtils.isEmpty(mSignInBean.courseName))
                {
                    mCourseNameTv.setText(mSignInBean.courseName);
                }
                GridLayoutManager layoutManager = new GridLayoutManager(this, 18);
                mLocationRv.setLayoutManager(layoutManager);
                LocationAdapter locationAdapter = new LocationAdapter(this, mSignedList);
                mLocationRv.setAdapter(locationAdapter);
                if (mSignInBean.isSigned)
                {
                    mSignInTv.setVisibility(View.GONE);
                    mCountdownTv.setText("你已完成签到");
                    mSignInBtn.setEnabled(false);
                    mLocationRv.setEnabled(false);
                }
                locationAdapter.setOnLocationCheckListener(new LocationAdapter.OnLocationCheckListener()
                {
                    @Override
                    public void onLocationCheck(int position, boolean isCheck, boolean isSelected)
                    {
                        mSelectPosition = position;
                        mIsSelected = isSelected;
                    }
                });

                if (!TextUtils.isEmpty(mSignInBean.deadline))
                {
                    long time = TimeUtils.getTimeDifference(mSignInBean.deadline);
                    if (time > 0)
                    {
                        // 计时器
                        CountDownTimer countDownTimer = new CountDownTimer(time, 1000)
                        {
                            @Override
                            public void onTick(long l)
                            {
                                mCountdownTv.setText(TimeUtils.formatDuring(l));
                            }

                            @Override
                            public void onFinish()
                            {
                                mSignInTv.setVisibility(View.GONE);
                                mCountdownTv.setText("已截止签到");
                                mSignInBtn.setEnabled(false);
                                mLocationRv.setEnabled(false);
                            }
                        }.start();
                    }

                }

            }

        }

    }

    /**
     * 点击签到
     */
    @OnClick(R.id.sign_in_btn)
    public void onViewClicked()
    {
        if (mIsSelected)
        {
            // 上传服务器，签到信息
            uploadSignInfo();
        }
        else
        {
            Toast.makeText(this, "请先选择您的座位", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadSignInfo()
    {
        String course = mSignInBean.courseId;
        String currentTime = TimeUtils.getCurrentTime();
        String username = getSharedPreferences("username", MODE_PRIVATE).getString("username", "");
        OkHttpUtils.post().url(Constants.USER_SIGN).addParams("course_id", course).addParams("current_time",
                currentTime).addParams("username", username).addParams("position",
                        mSelectPosition + "").build().execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {

                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                BaseLocalModel model = JsonHelper.parseJson(response);
                                if (model.isSucess())
                                {
                                    Toast.makeText(SignInActivity.this, "签到成功", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
    }
}
