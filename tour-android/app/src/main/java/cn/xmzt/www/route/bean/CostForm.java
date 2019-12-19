package cn.xmzt.www.route.bean;
import java.util.List;

/**
 * 费用
 */
public class CostForm {
    private int costType;//费用类型(1: 成人,2:儿童,3:单房差)
    private int quantity;//数量
    public CostForm() {
    }

    /**
     * 费用
     * @param costType 费用类型(1: 成人,2:儿童,3:单房差)
     * @param quantity 数量
     * @param visitorList 出游人列表
     */
    public CostForm(int costType, int quantity, List<ContactForm> visitorList) {
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
         return visitorList;
     }

}