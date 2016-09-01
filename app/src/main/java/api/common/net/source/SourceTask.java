package api.common.net.source;

import android.text.TextUtils;

import com.tywl.myt.utile.DLog;

import org.apache.http.util.ByteArrayBuffer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

import api.common.json.JSONUtile;
import api.common.net.connect.URLConnectionBase;


/**
 * Created by Administrator on 2016/1/14.
 */
public class SourceTask<T extends AbstractRequestData<?>> implements Runnable {
    private int responseCode;
    private String responseMsg;
    private OnRequestBack onRequestBack;
    private T requestData;
    private byte[] request;
    private final String TAG = "SourceTask";

    public SourceTask(OnRequestBack onRequestBack, T requestData) {
        this.onRequestBack = onRequestBack;
        this.requestData = requestData;

    }

    private byte[] postMethod() {
        try {
            HttpURLConnection urlConn = null;
            //get请求
            String path = requestData.getRequestUrl();
            if (!TextUtils.isEmpty(path)) {
                urlConn = (HttpURLConnection) URLConnectionBase.getURLConnection(path);
                urlConn.setRequestMethod("GET");
                DLog.e("get请求", path);
                request = path.getBytes();
            } else {
                urlConn = (HttpURLConnection) URLConnectionBase.getURLConnection(requestData.postRequestUrl());
                urlConn.setRequestMethod("POST");
                Object data = requestData.getData();
                String str = JSONUtile.obj2String(data);
                DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
                DLog.e("post请求:", str);
                dos.write(str.getBytes());
                dos.flush();
                dos.close();
                request = str.getBytes();
            }
            //设置重定向
            urlConn.setInstanceFollowRedirects(true);
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            responseCode = urlConn.getResponseCode();
            responseMsg = urlConn.getResponseMessage();
            if (responseCode != 200) {
                return null;
            }
            // 读取数据
            /** 判断是否是GZIP **/
            boolean isGzipEncoding = false;
            String contentEncoding = urlConn.getContentEncoding();
            if (!TextUtils.isEmpty(contentEncoding)) {
                if (contentEncoding.contains("gzip")) {
                    isGzipEncoding = true;
                }
            }
            DLog.e("请求gzip:", isGzipEncoding);
            byte[] readBuffer = new byte[1024];
            ByteArrayBuffer buffer = null;
            InputStream input = urlConn.getInputStream();
            // 如果是GZIP压缩
            if (isGzipEncoding) {
                GZIPInputStream inPutStream = new GZIPInputStream(input);
                int size = (inPutStream.available());
                buffer = new ByteArrayBuffer(size);
                int len = 0;
                while ((len = inPutStream.read(readBuffer)) != -1) {
                    buffer.append(readBuffer, 0, len);
                }
                inPutStream.close();
            } else {
                //非GZIP压缩
                int size = (input.available());
                buffer = new ByteArrayBuffer(size);
                int len = 0;
                while ((len = input.read(readBuffer)) != -1) {
                    buffer.append(readBuffer, 0, len);
                }
                input.close();
            }

            return buffer.toByteArray();
        } catch (IOException ioe) {
            responseMsg = ioe.getMessage();
            responseCode = 400;
            return null;
        }
    }

    @Override
    public void run() {
        byte[] result = postMethod();
        if (result == null) {
            DLog.e("请求失败:" + responseCode, responseMsg);
            onRequestBack.onFailed(responseCode, responseMsg);
            return;
        }
        DLog.e("请求成功:" + responseCode, new String(result));
        onRequestBack.onSucess(responseCode, responseMsg, result, request);
    }

    public interface OnRequestBack {
        /**
         * 成功
         *
         * @param bytes 成功返回值
         */
        void onSucess(int responseCode, String responseMsg, byte[] bytes, byte[] request);

        /**
         * 失败
         */
        void onFailed(int responseCode, String responseMsg);
    }
}
