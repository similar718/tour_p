package cn.xmzt.www.route.bean;
public class OrderInvoiceForm {

    private int invoiceTitleId;//发票抬头id
    private String itemTitle;//开票项
    private String receiveEmail;//接收邮箱
    public void setInvoiceTitleId(int invoiceTitleId) {
         this.invoiceTitleId = invoiceTitleId;
     }
     public int getInvoiceTitleId() {
         return invoiceTitleId;
     }

    public void setItemTitle(String itemTitle) {
         this.itemTitle = itemTitle;
     }
     public String getItemTitle() {
         return itemTitle;
     }

    public void setReceiveEmail(String receiveEmail) {
         this.receiveEmail = receiveEmail;
     }
     public String getReceiveEmail() {
         return receiveEmail;
     }

}