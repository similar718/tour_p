package cn.xmzt.www.mine.bean;

import java.util.List;

/**
 * @author tanlei
 * @date 2019/8/15
 * @describe
 */

public class CashWithdrawalBean {
    @Override
    public String toString() {
        return "CashWithdrawalBean{" +
                "sysUserExtractionAccount=" + sysUserExtractionAccount +
                ", extractionCount=" + extractionCount +
                ", extractionMinnumber=" + extractionMinnumber +
                '}';
    }

    /**
     * sysUserExtractionAccount : [{"extractTel":"string","realName":"string","gmtModified":"2019-08-15T10:01:40.278Z","id":0,"state":0,"gmtCreate":"2019-08-15T10:01:40.278Z","type":0,"userId":0,"account":"string"}]
     * extractionCount : 0
     * extractionMinnumber : 0
     */
    private List<SysUserExtractionAccountEntity> sysUserExtractionAccount;
    private int extractionCount;
    private int extractionMinnumber;

    public void setSysUserExtractionAccount(List<SysUserExtractionAccountEntity> sysUserExtractionAccount) {
        this.sysUserExtractionAccount = sysUserExtractionAccount;
    }

    public void setExtractionCount(int extractionCount) {
        this.extractionCount = extractionCount;
    }

    public void setExtractionMinnumber(int extractionMinnumber) {
        this.extractionMinnumber = extractionMinnumber;
    }

    public List<SysUserExtractionAccountEntity> getSysUserExtractionAccount() {
        return sysUserExtractionAccount;
    }

    public int getExtractionCount() {
        return extractionCount;
    }

    public int getExtractionMinnumber() {
        return extractionMinnumber;
    }

    public static class SysUserExtractionAccountEntity {
        /**
         * extractTel : string
         * realName : string
         * gmtModified : 2019-08-15T10:01:40.278Z
         * id : 0
         * state : 0
         * gmtCreate : 2019-08-15T10:01:40.278Z
         * type : 0
         * userId : 0
         * account : string
         */
        private String extractTel;
        private String realName;
        private String gmtModified;
        private int id;
        private int state;
        private String gmtCreate;
        private int type;
        private int userId;
        private String account;

        public void setExtractTel(String extractTel) {
            this.extractTel = extractTel;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setGmtModified(String gmtModified) {
            this.gmtModified = gmtModified;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getExtractTel() {
            return extractTel;
        }

        public String getRealName() {
            return realName;
        }

        public String getGmtModified() {
            return gmtModified;
        }

        public int getId() {
            return id;
        }

        public int getState() {
            return state;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public int getType() {
            return type;
        }

        public int getUserId() {
            return userId;
        }

        public String getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return "SysUserExtractionAccountEntity{" +
                    "extractTel='" + extractTel + '\'' +
                    ", realName='" + realName + '\'' +
                    ", gmtModified='" + gmtModified + '\'' +
                    ", id=" + id +
                    ", state=" + state +
                    ", gmtCreate='" + gmtCreate + '\'' +
                    ", type=" + type +
                    ", userId=" + userId +
                    ", account='" + account + '\'' +
                    '}';
        }
    }
}
