package cn.xmzt.www.selfdrivingtools.bean;

public class SuggestAndFeedBackNaviBean {

    private int id;
    private boolean is_Checked = false;
    private String titel;
    private int icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_Checked() {
        return is_Checked;
    }

    public void setIs_Checked(boolean is_Checked) {
        this.is_Checked = is_Checked;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
