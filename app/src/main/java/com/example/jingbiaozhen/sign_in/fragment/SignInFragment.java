package com.example.jingbiaozhen.sign_in.fragment;

/*
 * Created by jingbiaozhen on 2018/5/23.
 * 签到页面Fragment
 **/

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.R;
import com.example.jingbiaozhen.sign_in.SignInActivity;
import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.Course;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.OnClick;
import okhttp3.Call;

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

    private void queryCourseAndSign(String content)
    {

        OkHttpUtils.post().url(Constants.QUERY_COURSE).addParams("course_id", content).build().execute(
                new StringCallback()
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
                        Log.d(TAG, "onResponse: response" + response);
                        if (model.isSucess())
                        {
                            JSONArray jo = model.data;
                            JSONObject jsonObject0 = jo.optJSONObject(0);
                            Course course = new Course();
                            if (jsonObject0 != null)
                            {
                                course.deadline = jsonObject0.optString("course_signforbid_time");
                                course.courseId = jsonObject0.optString("course_id");
                                course.courseName = jsonObject0.optString("course_name");
                                Intent intent = new Intent(mActivity, SignInActivity.class);
                                intent.putExtra("course", course);
                                mActivity.startActivity(intent);
                                Toast.makeText(mActivity, model.errorInfo, Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(mActivity, model.errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
