package com.tywl.myt.utile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tywl.myt.activity.BaseApplication;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/2/15.
 */
public class ImageUtile {
    private static final String TAG = "ImageUtile";

    public static void loadingImage(ImageView iv, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.with(BaseApplication.baseApplication)
                .load(url)
                .into(iv);
    }
    public static void loadingImageError(ImageView iv, String url,int errorIcon) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.with(BaseApplication.baseApplication)
                .load(url).error(errorIcon)
                .into(iv);
    }
    public static void loadingImage(ImageView iv, String url, int defout) {
        if (TextUtils.isEmpty(url)) {
            iv.setImageResource(defout);
            return;
        }
        Picasso.with(BaseApplication.baseApplication)
                .load(url)
                .placeholder(defout)
                .into(iv);
    }

    // 这里把bitmap图片截取出来pr是指截取比例

    public static Bitmap getRoundedCornerBitmap(Bitmap bit, float pr) {
        Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.clipRect(0, bit.getHeight(), bit.getWidth(), bit.getHeight()
                - (pr * bit.getHeight() / 100));
        canvas.drawBitmap(bit, 0, 0, paint);
        return bitmap;

    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param resId
     * @return
     */
    public static Bitmap getBitmap(Context context,int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        return getSmallBitmap(filePath, 480, 800);
    }

    @SuppressLint("NewApi")
    private static Bitmap getSmallBitmap(String filePath, int whit, int hight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, whit, hight);
        options.inJustDecodeBounds = false;
        // 重新读出图片
        Bitmap bimap = BitmapFactory.decodeFile(filePath, options);
        float sizeRow = (float) bimap.getRowBytes() / 1024;
        float sizeCount = bimap.getByteCount() / 1024;// 3168
        DLog.e(TAG, "sizeRow:" + sizeRow + " sizeCount:" + sizeCount);
        return bimap;
    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (width > height) {
            int type = reqWidth;
            reqWidth = reqHeight;
            reqHeight = type;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
            if (inSampleSize % 2 == 1) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
}
