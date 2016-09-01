package api.common.net.source;

/**
 * Created by Administrator on 2016/1/15.
 */
public abstract class AbstractRequestData<T> {
    protected abstract T getData();
    protected abstract String getRequestUrl();
    protected abstract String postRequestUrl();


}
