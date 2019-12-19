package cn.xmzt.www.selfdrivingtools.bean;

public class SettingDestinationTypeInfo {
    private String content;
    private boolean is_Checked = false;
    private int id;
    public SettingDestinationTypeInfo() {
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIs_Checked() {
        return is_Checked;
    }

    public void setIs_Checked(boolean is_Checked) {
        this.is_Checked = is_Checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
