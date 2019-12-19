package cn.xmzt.www.mine.bean;

/**
 * app版本
 */
public class AppVersionBean {
    private String clientType;//客户端类型
    private String version;//版本号
    private String tagline;//版本标语、提示语
    private String title;
    private String content;
    private String url;
    private boolean forceUpgrade;
    private boolean latest;

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getVersion() {
        return version;
    }
    private String lowerVersion="";
    public String getVersionReplaceV() {
        if(version!=null){
            lowerVersion=version.toUpperCase();
            lowerVersion=lowerVersion.replace("V","");
        }
        return lowerVersion;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTagline() {
        if(tagline==null){
            return "";
        }
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        if(content==null){
            return "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }
}
