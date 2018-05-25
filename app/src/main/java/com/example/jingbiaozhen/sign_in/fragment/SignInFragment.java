package com.example.jingbiaozhen.sign_in.fragment;

/*
 * Created by jingbiaozhen on 2018/5/23.
 **/

import android.content.Intent;

import com.example.jingbiaozhen.sign_in.R;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import butterknife.OnClick;

import static com.example.jingbiaozhen.sign_in.HomeActivity.REQUEST_CODE_SCAN;

public class SignInFragment extends BaseFragment
{

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
        /*
         * ZxingConfig是配置类 可以设置是否显示底部布局，闪光灯，相册， 是否播放提示音 震动等动能 也可以不传这个参数 不传的话 默认都为默认不震动
         * 其他都为true
         */
        ZxingConfig config = new ZxingConfig();
        config.setShowbottomLayout(true);// 底部布局（包括闪光灯和相册）
        config.setPlayBeep(true);// 是否播放提示音
        config.setShake(true);// 是否震动
        config.setShowAlbum(true);// 是否显示相册
        config.setShowFlashLight(true);// 是否显示闪光灯

        // 如果不传 ZxingConfig的话，两行代码就能搞定了
        Intent intent = new Intent(mActivity, CaptureActivity.class);
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }



    @OnClick(R.id.scan_iv)
    public void onViewClicked() {
        scanQRCode();
    }
}
