package cn.xmzt.www.selfdrivingtools.download;

/**
 * 下载过程中的回调处理
 */
public abstract class DownloadVoiceListener {

    /**
     * 开始下载
     */
    public abstract void onStart();

    /**
     * 完成下载
     */
    public abstract void onFinish(String path);


    /**
     * 下载进度
     *
     * @param progress
     */
    public abstract void onProgress(int progress) ;

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param data
     */
    public abstract void onFailure(String data);
}
