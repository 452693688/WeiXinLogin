package com.tywl.myt.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.ui.activity.R;
import com.tywl.myt.utile.ImageUtile;


/**
 * 自定义进度条,当发起网络请求时显示该对话框，让用户等待
 */
public class CustomWaitingDialog extends Dialog {
    private ImageView imgProgress;
    /**
     * 当前百分数
     */
    private int num = 0;
    private Bitmap bmp;
    private int strID = 0;

    public CustomWaitingDialog(Context context) {
        super(context, R.style.WaitingDialog);
    }

    // 设置等待对话框的内容内容
    public void setWaitInfo(int strID) {
        this.strID = strID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_waiting);
        setCanceledOnTouchOutside(false);
        imgProgress = (ImageView) findViewById(R.id.img_progress);
        bmp = BitmapFactory.decodeResource(getContext().getResources(),
                R.mipmap.base_loading_up_true);
        if (strID > 0) {
            TextView tvContent = (TextView) findViewById(R.id.txt_custom_content);
            tvContent.setText(strID);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isShow = CustomWaitingDialog.this.isShowing();
                    if (!isShow) {
                        break;
                    }
                    num++;
                    if (num > 100) {
                        num = 0;
                    }
                    imgProgress.setImageBitmap(ImageUtile.getRoundedCornerBitmap(
                            bmp, num));
                    handler.sendEmptyMessageDelayed(1, 5);
                    break;
            }
        }
    };

    @Override
    public void dismiss() {
        handler.removeMessages(1);
        super.dismiss();
    }

    @Override
    public void cancel() {
        handler.removeMessages(1);
        super.cancel();
    }

    @Override
    public void show() {
        super.show();
        num = 0;
        handler.sendEmptyMessageDelayed(1, 0);
    }

 }
