package api.common.net.able;

/**
 * Created by Administrator on 2016/1/15.
 */
public interface RequsetListener<T> {
    /**
     * 请求成功或者失败详情：
     * resultCode 业务请求响应吗；
     * responseCode 响应请求码；
     * obj 数据；
     * msg 提示信息
     **/
    public void RequsetDetails(int resultCode, int responseCode, T t, String msg);
}
