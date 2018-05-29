package com.example.jingbiaozhen.sign_in.fragment;

/*
 * Created by jingbiaozhen on 2018/5/23.
 * 请假列表页面
 **/

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
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

public class SignListFragment extends BaseFragment
{

    private static final String TAG = "LeaveListFragment";

    @BindView(R.id.sign_list_rv)
    RecyclerView mSignListRv;

    @Override
    protected int loadLayout()
    {
        return R.layout.fragment_sign_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        getSignList();
    }

    private void getSignList() {
        String username = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("username", "");

        OkHttpUtils.post().url(Constants.QUERY_SIGN_INFO).addParams("student_no", username).build().execute(
                new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        Toast.makeText(mActivity, "网络异常"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        BaseLocalModel model = JsonHelper.parseJson(response);
                        Log.d(TAG, "onResponse: response" + response);

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
                                    itemDesc.title = jsonObject.optString("course_name");
                                    String date= jsonObject.optString("sign_date");
                                    String time= jsonObject.optString("sign_time");
                                    itemDesc.desc =date+"-"+time;
                                    itemDescs.add(itemDesc);
                                }
                            }
                            ListAdapter adapter = new ListAdapter(mActivity, itemDescs);
                            mSignListRv.setAdapter(adapter);
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
        mSignListRv.addItemDecoration(new DividerItemDecoration(mActivity,DividerItemDecoration.VERTICAL));
        mSignListRv.setLayoutManager(manager);

    }
}
