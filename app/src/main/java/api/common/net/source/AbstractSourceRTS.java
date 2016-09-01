package api.common.net.source;

import android.text.TextUtils;

import com.tywl.myt.receiver.NetStateReceiver;

import api.common.net.able.RequsetListener;
import api.common.net.thread.TaskManager;


/**
 * Created by Administrator on 2016/1/14.
 */
public abstract class AbstractSourceRTS<T extends AbstractResponseData, V extends AbstractRequestData> {
    private RequestState requestState = new RequestState();
    private RequsetListener requsetListener;

    /**
     * 获取请求参数
     */
    protected abstract V getRequest();

    /**
     * 转化Resp
     */
    protected abstract T parseResp(byte[] bytes, byte[] request);

    public void setRequsetListener(RequsetListener requsetListener) {
        this.requsetListener = requsetListener;
    }

    public void doRequest() {
        if (NetStateReceiver.getNetWorkState() == 0) {
            requestState.onFailed(AbstractSourceHandler.WHAT_LOCALITY_NET_WORK_ERROR, "网络初始化失败");
            return;
        }
        if (NetStateReceiver.getNetWorkState() == NetStateReceiver.NET_STATE_NONET) {
            requestState.onFailed(AbstractSourceHandler.WHAT_LOCALITY_NET_WORK_ERROR, "没有网络");
            return;
        }
        SourceTask sourceTask = new SourceTask(requestState, getRequest());
        TaskManager.getInstance().execute(sourceTask);
    }

    class RequestState implements SourceTask.OnRequestBack {

        @Override
        public void onSucess(int responseCode, String responseMsg, byte[] bytes, byte[] request) {
            T t = parseResp(bytes, request);

            if (t == null) {
                //数据错误
                responseMsg = TextUtils.isEmpty(new String(bytes)) ? "无数据返回" : "数据解析失败";
                requsetListener.RequsetDetails(AbstractSourceHandler.WHAT_DATA_ERROR, responseCode, null, responseMsg);
                return;
            }
            responseMsg = t.msg;
            String code = t.code;
            if ("0".equals(code) || TextUtils.isEmpty(code)) {
                //业务请求成功
                requsetListener.RequsetDetails(AbstractSourceHandler.WHAT_DEAL_SUCCEED, responseCode, t, responseMsg);
                return;
            }
            //业务请求 其他原因失败
            requsetListener.RequsetDetails(AbstractSourceHandler.WHAT_DEAL_FAILED, responseCode, t, responseMsg);
        }

        @Override
        public void onFailed(int responseCode, String responseMsg) {
            requsetListener.RequsetDetails(0, responseCode, null, responseMsg);
        }

        public void onFailed(int responseCode, Object obj, String responseMsg) {
            requsetListener.RequsetDetails(0, responseCode, obj, responseMsg);
        }
    }
}
