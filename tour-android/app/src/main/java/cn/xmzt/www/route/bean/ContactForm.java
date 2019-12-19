package cn.xmzt.www.route.bean;

import android.text.TextUtils;

import cn.xmzt.www.utils.TimeUtil;

/**
 * 预订人或者出行人
 */
public class ContactForm {
    private int ageStage;//年龄阶段(0:成年人,1:儿童)
    private String idCard;//证件号码
    private int idType;//证件类型
    private String name;//姓名
    private String phone;//手机
    private String indexName;//位置名称
    private boolean isContact;//是否是预订人 true表示联系人false表示出游人
    private int settingDefault;//是否默认：0否 1是

    public ContactForm() {
    }
    /**
     * @param ageStage 年龄阶段(0:成年人,1:儿童)
     */
    public ContactForm(int ageStage) {
        this.ageStage = ageStage;
    }
    /**
     * @param isContact 是否是预订人 true表示预订人false表示出游人
     * @param ageStage 年龄阶段(0:成年人,1:儿童)
     * @@param indexName 位置名称
     */
    public ContactForm(int ageStage,String indexName,boolean isContact) {
        this.ageStage = ageStage;
        this.indexName = indexName;
        this.isContact = isContact;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setAgeStage(int ageStage) {
         this.ageStage = ageStage;
     }
     public int getAgeStage() {
         return ageStage;
     }

    public void setIdCard(String idCard) {
         this.idCard = idCard;
     }
     public String getIdCard() {
        return idCard;
     }
    public String getIdCardName() {
        if(!TextUtils.isEmpty(idCard)){
            return "身份证 "+idCard;
        }
        return idCard;
    }
    public boolean isThanAge(int ageLimit){
        long age=getAge();
        if(age>ageLimit){
            return true;
        }
        return false;
    }
    public long getAge(){
        if(!TextUtils.isEmpty(idCard)){
            String birthday = idCard.substring(6, 14);
            //当前日期
            String date = TimeUtil.dateToStr("yyyyMMdd");
            //计算年龄差
            long ageBit = Long.parseLong(date) - Long.parseLong(birthday);
            //当年龄差的长度大于4位时，即出生年份小于当前年份
            if (ageBit >= 10000) {
                long age=ageBit/10000;
                return age;
            }
        }
        return 0;
    }
    public void setIdType(int idType) {
         this.idType = idType;
     }
     public int getIdType() {
         return idType;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setPhone(String phone) {
         this.phone = phone;
     }
     public String getPhone() {
         return phone;
     }
    public String getPhoneName() {
        if(!TextUtils.isEmpty(phone)){
            return "手机号 "+phone;
        }
        return phone;
    }

    public boolean isContact() {
        return isContact;
    }

    public void setContact(boolean contact) {
        isContact = contact;
    }

    public int getSettingDefault() {
        return settingDefault;
    }

    public void setSettingDefault(int settingDefault) {
        this.settingDefault = settingDefault;
    }
}