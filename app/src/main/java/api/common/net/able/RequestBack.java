package api.common.net.able;

/**
 * Created by Administrator on 2016/1/15.
 */
public interface RequestBack {
    /**Http请求
     * activity回调
     * @param what
     * @param obj
     * @param msg
     */
    public void OnBack(int what, Object obj, String msg, String other);
}
