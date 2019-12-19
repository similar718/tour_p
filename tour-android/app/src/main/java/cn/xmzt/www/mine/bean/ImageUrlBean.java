package cn.xmzt.www.mine.bean;

/**
 * @author tanlei
 * @date 2019/8/9
 * @describe 上传图片成功后返回的对象
 */

public class ImageUrlBean {

    /**
     * fileName : 文件名称
     * state : 0 上传状态1成功2失败3上传文件类型不支持
     * fileBase : string
     * url : string
     */
    private String fileName;
    private int state;
    private String fileBase;
    private String url;


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setFileBase(String fileBase) {
        this.fileBase = fileBase;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public int getState() {
        return state;
    }

    public String getFileBase() {
        return fileBase;
    }

    public String getUrl() {
        return url;
    }
}
