package cn.xmzt.www.selfdrivingtools.bean;

public class ElectricGuideBean {
    private int type = 0;//1 智能出行 2 智慧景区导览 3 一键救援
    private int num ;
    private int img;
    private String titlr;
    private String content;
    private String content2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitlr() {
        return titlr;
    }

    public void setTitlr(String titlr) {
        this.titlr = titlr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent2() {
        return content2;
    }

    public void setContent2(String content2) {
        this.content2 = content2;
    }
}
