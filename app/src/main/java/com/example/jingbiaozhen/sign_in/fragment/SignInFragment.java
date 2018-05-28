package com.example.jingbiaozhen.sign_in.fragment;

/*
 * Created by jingbiaozhen on 2018/5/23.
 * 签到页面Fragment
 **/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.R;
import com.example.jingbiaozhen.sign_in.SignInActivity;
import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.SignInBean;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.example.jingbiaozhen.sign_in.utils.TimeUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.OnClick;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;
import static com.example.jingbiaozhen.sign_in.HomeActivity.REQUEST_CODE_SCAN;

public class SignInFragment extends BaseFragment
{

    private static final String TAG = "SignInFragment";

    @Override
    protected int loadLayout()
    {
        return R.layout.fragment_sign_in;
    }

    /**
     * 扫描二维码
     */
    private void scanQRCode()
    {
        Intent intent = new Intent(mActivity, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @OnClick(R.id.scan_iv)
    public void onViewClicked()
    {
        scanQRCode();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN)
        {
            Log.d(TAG, "onActivityResult: data" + data);
            if (data != null)
            {
                Bundle bundle = data.getExtras();
                if (bundle == null)
                {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS)
                {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    queryCourseAndSign(result);
                }
                else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED)
                {
                    Toast.makeText(mActivity, "二维码不正确", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    SignInBean signInBean = new SignInBean();

    private void queryCourseAndSign(String content)
    {
        String data = TimeUtils.getCurrentData();
        String username = mActivity.getSharedPreferences("user", MODE_PRIVATE).getString("username", "");
        Log.d(TAG, "queryCourseAndSign: 参数" + "student_no" + username);
        Log.d(TAG, "queryCourseAndSign: 参数" + "course_id" + content);
        Log.d(TAG, "queryCourseAndSign: 参数" + "sign_date" + data);

        OkHttpUtils.post().url(Constants.USER_SIGN).addParams("course_id", content).build().execute(new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e, int id)
            {
                Toast.makeText(mActivity, "网络异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id)
            {
                BaseLocalModel model = JsonHelper.parseJson(response);
                if (model.isSucess())
                {
                    JSONArray jo = model.data;
                    JSONObject jsonObject0 = jo.optJSONObject(0);
                    if (jsonObject0 != null)
                    {
                        signInBean.deadline = jsonObject0.optString("course_signforbid_time");
                        signInBean.courseId = jsonObject0.optString("course_id");
                        signInBean.courseName = jsonObject0.optString("course_name");
                    }

                }
                else
                {
                    Toast.makeText(mActivity, model.errorInfo, Toast.LENGTH_SHORT).show();
                }
            }
        });

        OkHttpUtils.post().url(Constants.QUERY_SIGN_INFO).addParams("course_id", content).addParams("student_no",
                username).addParams("sign_date", data).build().execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Toast.makeText(mActivity, "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        Log.d(TAG, "onResponse: response" + response);
                        BaseLocalModel model = JsonHelper.parseJson(response);

                        List<SignInBean.Seat> initSeatList = signInBean.getInitSignList();

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
                            signInBean.signedList = initSeatList;
                            Intent intent = new Intent(mActivity, SignInActivity.class);
                            intent.putExtra("signInfo", signInBean);
                            mActivity.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(mActivity, model.errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
