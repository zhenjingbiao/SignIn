package com.example.jingbiaozhen.sign_in;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.example.jingbiaozhen.sign_in.utils.RandomCodeView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MainActivity extends Activity
{

    private static final String TAG = "MainActivity";
    @BindView(R.id.usernameEt)
    EditText mUsernameEt;

    @BindView(R.id.passwordEt)
    EditText mPasswordEt;

    @BindView(R.id.verificationEt)
    EditText mVerificationEt;

    @BindView(R.id.countDownBtn)
    RandomCodeView mCountDownBtn;

    @BindView(R.id.loginBtn)
    Button mLoginBtn;

    private String username;

    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mCountDownBtn.setOnClickRefresh(true);
    }

    @OnClick(R.id.loginBtn)
    public void onViewClicked()
    {
        username = mUsernameEt.getText().toString();
        password = mPasswordEt.getText().toString();
        String verification = mVerificationEt.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "请正确填写用户名密码", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(verification) || !mCountDownBtn.checkRes(verification))
        {
            Toast.makeText(this, "请正确填写验证码", Toast.LENGTH_SHORT).show();
        }
        else
        {
          /*  MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();*/
            loginIn();
        }
    }

    private void loginIn()
    {
        OkHttpUtils.post().url(Constants.USER_LOGIN).addParams("student_no", username)
                .addParams("student_password",
                password).build().execute(new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e, int id)
            {
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id)
            {
                Log.d(TAG, "onResponse: response" + response);
                BaseLocalModel model = JsonHelper.parseJson(response);
                if (model.isSucess())
                {
                    // 登录成功
                    saveUserInfo();
                    MainActivity.this.startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this, model.errorInfo, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserInfo() {
        SharedPreferences.Editor editor=getSharedPreferences("user",MODE_PRIVATE).edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.apply();
    }
}
