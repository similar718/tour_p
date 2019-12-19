package cn.xmzt.www.route.bean;

/**
 * 车辆信息
 */
public class CarForm {
    private String carNumber;
    private int numberType;//号牌类型(0:普通车牌,1:新能源汽车,2:大型车,3:大使馆或港澳出入境车辆,4:其他)
    private String phone;
    private String remark;
    public void setCarNumber(String carNumber) {
         this.carNumber = carNumber;
     }
     public String getCarNumber() {
         return carNumber;
     }

    public void setNumberType(int numberType) {
         this.numberType = numberType;
     }
     public int getNumberType() {
         return numberType;
     }
    public String getNumberTypeName() {
        if(numberType==1){
            return "新能源汽车";
        }else if(numberType==2){
            return "大型车";
        }else if(numberType==3){
            return "大使馆或港澳出入境车辆";
        }else if(numberType==4){
            return "其他";
        }
        return "普通车牌";
    }
    public void setPhone(String phone) {
         this.phone = phone;
     }
     public String getPhone() {
         return phone;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

}