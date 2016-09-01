package api.common.net.connect;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/1/14.
 */
public class URLConnectionBase {

    public static URLConnection getURLConnection(String urlPath) throws IOException {
        URL url= new URL(urlPath);
        URLConnection connection=url.openConnection();
        // 设置连接超时时间
        connection.setConnectTimeout(5 * 1000);
        //  请求必须设置允许输出
        connection.setDoOutput(true);
        connection.setDoInput(true);
         // 请求不使用缓存
        connection.setUseCaches(false);
        // 配置请求Content-Type
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", "UTF-8");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        return connection;
    }

}
