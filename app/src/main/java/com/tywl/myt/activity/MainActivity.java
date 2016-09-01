package com.tywl.myt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ui.activity.R;
import com.tywl.myt.net.parameter.result.WxUserRes;
import com.tywl.myt.utile.DataSaveUtile;
import com.tywl.myt.utile.ImageUtile;
import com.tywl.myt.wxapi.WXEntryActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userMsg;
    private ImageView headIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        headIv = (ImageView) findViewById(R.id.user_head_iv);
        userMsg = (TextView) findViewById(R.id.user_msg_tv);
        findViewById(R.id.user_login_btn).setOnClickListener(this);
        setUser();
    }

    private WxUserRes user;

    private void setUser() {
        user = (WxUserRes) DataSaveUtile.getObjectFromData(this, DataSaveUtile.WX_USER);
        if (user == null) {
            return;
        }
        String msg = "昵称：" + user.getNickname() + "\n"
                + "性别：" + user.getSex() + "\n"
                + "省份：" + user.getProvince() + "\n"
                + "城市：" + user.getCity() + "\n"
                + "国家：" + user.getCountry();
        userMsg.setText(msg);
        ImageUtile.loadingImage(headIv, user.getHeadimgurl(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user == null) {
            setUser();
        }
    }

    @Override
    public void onClick(View view) {
        Intent it = new Intent();
        it.setClass(this, WXEntryActivity.class);
        startActivity(it);
    }
}
