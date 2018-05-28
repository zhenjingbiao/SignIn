package com.example.jingbiaozhen.sign_in.fragment;

/*
 * Created by jingbiaozhen on 2018/5/23.
 * 成绩列表页面
 **/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.jingbiaozhen.sign_in.ListAdapter;
import com.example.jingbiaozhen.sign_in.R;
import com.example.jingbiaozhen.sign_in.bean.BaseLocalModel;
import com.example.jingbiaozhen.sign_in.bean.ItemDesc;
import com.example.jingbiaozhen.sign_in.utils.Constants;
import com.example.jingbiaozhen.sign_in.utils.JsonHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 查询成绩列表页面
 */

public class ScoreFragment extends BaseFragment
{

    private static final String TAG = "ScoreFragment";

    @BindView(R.id.score_list_rv)
    RecyclerView mScoreListRv;

    @Override
    protected int loadLayout()
    {
        return R.layout.fragment_score;
    }

    @Override
    protected void initData(Bundle bundle)
    {
        String username = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "");

        OkHttpUtils.post().url(Constants.QUERY_SCORE_LIST).addParams("student_no", username).build().execute(
                new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Toast.makeText(mActivity, "查询成绩列表失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        Log.d(TAG, "onResponse: response" + response);
                        BaseLocalModel model = JsonHelper.parseJson(response);
                        if (model.isSucess())
                        {
                            // 查询成功,解析成绩列表
                            List<ItemDesc> itemDescs = new ArrayList<>();
                            JSONArray jsonArray = model.data;
                            if (jsonArray != null)
                            {
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                                    ItemDesc itemDesc = new ItemDesc();
                                    itemDesc.title = jsonObject.optString("course_id");
                                    itemDesc.desc = jsonObject.optString("score_count");
                                    itemDescs.add(itemDesc);
                                }
                            }
                            ListAdapter adapter = new ListAdapter(mActivity, itemDescs);
                            mScoreListRv.setAdapter(adapter);
                        }
                        else
                        {
                            Toast.makeText(mActivity, model.errorInfo, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void initView()
    {
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        mScoreListRv.setLayoutManager(manager);

    }
}
