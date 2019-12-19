package cn.xmzt.www.intercom.bean;

/**
 * @author tanlei
 * @date 2019/8/7
 * @describe 用户基本信息实体类
 */

public class UserBasicInfoBean {

    /**
     * head : http://pf.tour.com/img/20190806/91557836583747c5a219a0ec33134979.jpeg
     * safeTel : 18310095100
     * gender : 1
     * grade : {"code":1,"label":"普通会员"}
     * tel : 18310095100
     * userId : 186139
     * age : 28
     * username : 大师兄
     */
    private String head;
    private String safeTel;
    private int gender;
    private GradeEntity grade;
    private String tel;
    private int userId;
    private int age;
    private String username;
    private String refCode;//推荐码

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setSafeTel(String safeTel) {
        this.safeTel = safeTel;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setGrade(GradeEntity grade) {
        this.grade = grade;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead() {
        return head;
    }

    public String getSafeTel() {
        return safeTel;
    }

    public int getGender() {
        return gender;
    }

    public GradeEntity getGrade() {
        return grade;
    }

    public String getTel() {
        return tel;
    }

    public int getUserId() {
        return userId;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public class GradeEntity {
        /**
         * code : 1
         * label : 普通会员
         */
        private int code;
        private String label;

        public void setCode(int code) {
            this.code = code;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }
    }
}
