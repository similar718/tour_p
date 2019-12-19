package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import cn.xmzt.www.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 线路页面列表
 */
public class RoutePage{
    private int total;//
    private int totalPage;//
    private List<ItemsBean> items;//

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ItemsBean> getItems() {
        if(items==null){
            items=new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        private int id;
        private int lineType;//线路类型：1自驾游（自由行程） 2跟团游（跟团出行）
        private String officeId; //服务机构id
        private String officeName;// 服务机构名称
        private String lineName;
        private String photoUrl;
        private String resourceUrl;
//        private String language;//线路宣传语
        private String intro;//线路介绍
        private float price;
        private String departTime;//时间

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLineType() {
            return lineType;
        }

        public void setLineType(int lineType) {
            this.lineType = lineType;
        }
        public String getLineTypeName() {
            if(lineType==1){
                return "自由出行";
            }else if(lineType==2){
                return "跟团自驾";
            }
            return "";
        }

        public String getOfficeId() {
            return officeId;
        }

        public void setOfficeId(String officeId) {
            this.officeId = officeId;
        }

        public String getOfficeName() {
            return officeName;
        }
        public String getOfficeNames() {
            if(!TextUtils.isEmpty(officeName)){
                return "由"+officeName+"提供服务";
            }
            return "";
        }
        public void setOfficeName(String officeName) {
            this.officeName = officeName;
        }

        public String getLineName() {
            return lineName;
        }

        public void setLineName(String lineName) {
            this.lineName = lineName;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }

        /*public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }*/

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public float getPrice() {
            return price;
        }
        public String getPrices() {
            return MathUtil.formatDouble(price,2);
        }
        public void setPrice(float price) {
            this.price = price;
        }

        public String getDepartTime() {
            if(departTime==null){
                return "";
            }
            return departTime;
        }
        public String getDepartTimes() {
            if(!TextUtils.isEmpty(departTime)){
                return "出发日期: "+departTime.substring(0,10);
            }else {
                return "出发日期: ";
            }
        }

        public void setDepartTime(String departTime) {
            this.departTime = departTime;
        }
    }
}
