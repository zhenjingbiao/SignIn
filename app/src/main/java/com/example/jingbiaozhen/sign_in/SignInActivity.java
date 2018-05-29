package com.example.jingbiaozhen.sign_in;
/*
 * Created by jingbiaozhen on 2018/5/24.
 * 签到页面activity
 **/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.Course;
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
    private static final String TAG = "SignInActivity";

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

    private Integer mSelectPosition;

    private Course mCourse;

    private SignInBean mSignInBean = new SignInBean();

    List<SignInBean.Seat> initSeatList = mSignInBean.getInitSignList();

    private LocationAdapter locationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        initData();
        loadSeatList();

    }

    private void loadSeatList()
    {
        String data = TimeUtils.getCurrentData();
        String username = getSharedPreferences("user", MODE_PRIVATE).getString("username", "");
        Log.d(TAG, "queryCourseAndSign: 参数" + "student_no" + username);
        Log.d(TAG, "queryCourseAndSign: 参数" + "sign_date" + data);
        Log.d(TAG, "queryCourseAndSign: 参数" + "courseId" + mCourse.courseId);
        OkHttpUtils.post().url(Constants.QUERY_SIGN_INFO).addParams("course_id", mCourse.courseId).addParams(
                "student_no", username).addParams("sign_date", data).build().execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Toast.makeText(SignInActivity.this, "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        Log.d(TAG, "onResponse: response" + response);
                        BaseLocalModel model = JsonHelper.parseJson(response);

                        if (model.isSucess())
                        {

                            JSONArray jo = model.data;
                            for (int i = 0; i < jo.length(); i++)
                            {
                                JSONObject jsonObject = jo.optJSONObject(i);
                                for (SignInBean.Seat seat : initSeatList)
                                {
                                    int seatNo = Integer.parseInt(jsonObject.optString("seat_no"));
                                    if (seatNo == seat.location)
                                    {
                                        seat.isSigned = true;
                                    }
                                }
                            }
                            mSignInBean.signedList = initSeatList;
                            locationAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(SignInActivity.this, model.errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initRecycleViewData(List<SignInBean.Seat> seatList)
    {
        if (!seatList.isEmpty())
        {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 14);
            mLocationRv.setLayoutManager(layoutManager);
            locationAdapter = new LocationAdapter(this, seatList);
            mLocationRv.setAdapter(locationAdapter);
            locationAdapter.setOnLocationCheckListener(new LocationAdapter.OnLocationCheckListener()
            {
                @Override
                public void onLocationCheck(Integer position, boolean isCheck)
                {
                    mSelectPosition = position;
                }
            });

        }
    }

    private void initData()
    {
        Intent intent = getIntent();
        if (intent != null)
        {

            mCourse = (Course) intent.getSerializableExtra("course");
            if (mCourse != null)
            {
                if (!TextUtils.isEmpty(mCourse.courseName))
                {
                    mCourseNameTv.setText(mCourse.courseName);
                }
                initRecycleViewData(initSeatList);
                if (!TextUtils.isEmpty(mCourse.deadline))
                {
                    mCountdownTv.setText(mCourse.deadline);
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
        if (mSelectPosition != null)
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
        String course = mCourse.courseId;
        String username = getSharedPreferences("user", MODE_PRIVATE).getString("username", "");
        String date = TimeUtils.getCurrentData();
        String time = TimeUtils.getSimpleTime();
        Log.d(TAG, "uploadSignInfo: course" + course);
        Log.d(TAG, "uploadSignInfo: date" + date);
        Log.d(TAG, "uploadSignInfo: time" + time);
        Log.d(TAG, "uploadSignInfo: seat_no" + mSelectPosition + "");
        Log.d(TAG, "uploadSignInfo: student_no" + username);

        OkHttpUtils.post().url(Constants.USER_SIGN).addParams("course_id", course).addParams("sign_time",
                time).addParams("sign_date", date).addParams("student_no", username).addParams("seat_no",
                        mSelectPosition + "").build().execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e, int id)
                            {
                                Toast.makeText(SignInActivity.this, "网络异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id)
                            {
                                Log.d(TAG, "onResponse: response" + response);
                                BaseLocalModel model = JsonHelper.parseJson(response);
                                if (model.isSucess())
                                {
                                    loadSeatList();
                                }
                                else if ("-3".equals(model.errorNo))
                                {
                                    locationAdapter.notifyDataSetChanged();
                                    mLocationRv.setEnabled(false);
                                }
                                Toast.makeText(SignInActivity.this, model.errorInfo, Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
