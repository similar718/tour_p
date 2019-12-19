package cn.xmzt.www.home.bean;

/**
 *定制住宿类型实体类
 */
public class CustomizeLiveDialogBean {
    private int type;
    private int position;
    private String title;
    private boolean isSelect;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
