package cn.xmzt.www.share;

/**
 * @author tanlei
 * @date 2019/8/20
 * @describe
 */

public interface ShareListener {
    void onStart();
    void onResult();
    void onError();
    void onCancel();
}
