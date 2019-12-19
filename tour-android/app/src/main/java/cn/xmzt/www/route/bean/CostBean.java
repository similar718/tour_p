package cn.xmzt.www.route.bean;
import cn.xmzt.www.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 费用
 */
public class CostBean {
    public String person="人";
    private int costType;//费用类型(1: 成人,2:儿童,3:单房差)
    private double price;
    private int quantity;//数量
    public CostBean() {
    }

    /**
     * 费用
     * @param costType 费用类型(1: 成人,2:儿童,3:单房差)
     * @param quantity 数量
     * @param visitorList 出游人列表
     */
    public CostBean(int costType, int quantity, List<ContactForm> visitorList) {
        this.costType = costType;
        this.quantity = quantity;
        this.visitorList = visitorList;
    }

    private List<ContactForm> visitorList;//出游人
    public void setCostType(int costType) {
         this.costType = costType;
     }
     public int getCostType() {
         return costType;
     }
    public String getCostTypeName() {
        if(costType==1){
            return "成人";
        }else if(costType==2){
            return "儿童";
        }else if(costType==3){
            return "单房差";
        }
        return "成人";
    }

    public double getPrice() {
        return price;
    }
    public String getPriceStr() {
        return "¥"+MathUtil.formatDouble(price,2)+"*"+quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
         this.quantity = quantity;
     }
     public int getQuantity() {
         return quantity;
     }

    public void setVisitorList(List<ContactForm> visitorList) {
         this.visitorList = visitorList;
     }
     public List<ContactForm> getVisitorList() {
        if(visitorList==null){
            visitorList=new ArrayList<>();
        }

        return visitorList;
     }

}