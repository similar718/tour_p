package cn.xmzt.www.smartteam.bean;

import java.util.List;

/**
 * 行程签到列表信息
 */
public class TripSignInListBean {

    /**
     * id : 766
     * answerSigninNum : 8
     * alreadySigninNum : 1
     * notSigninNum : 7
     * groupMemberVOS : [{"status":2,"userId":186409,"userName":"138****0000","image":"https://pf.xmzt.cn/head/defaultHeader.png?v","tel":"13800000000","signinTimeStamp":1575712164000,"gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"17:49完成签到"},{"status":1,"userId":186466,"userName":"乌拉乌拉","image":"https://xmzt-user.oss-cn-shanghai.aliyuncs.com/20191028/215f4a36c3a94f7f8443b4414ac1e3c8.jpg","tel":"13006661987","gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":186481,"userName":"张晓风","image":"https://pf.xmzt.cn/img/20190928/5a1e99f479e441219e3b6f4e0059eebf.jpeg","tel":"18306668267","gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":186795,"gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":186836,"userName":"150****0000","image":"https://pf.xmzt.cn/head/defaultHeader.png","tel":"15000000000","gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":187681,"userName":"181****7833","image":"https://pf.xmzt.cn/head/onlKRwf-2g3kw_A_HeYyELSAoazU.jpg","tel":"18178717833","gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":187718,"gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"},{"status":1,"userId":187725,"userName":"188****7153","image":"https://pf.xmzt.cn/head/defaultHeader.png","tel":"18877367153","gatherTimeStamp":1575715020000,"nowTimeStamp":1575714407000,"signinRemark":"还剩0小时10分钟"}]
     */

    private int id;
    private int answerSigninNum;
    private int alreadySigninNum;
    private int notSigninNum;
    private List<GroupMemberVOSBean> groupMemberVOS;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswerSigninNum() {
        return answerSigninNum;
    }

    public void setAnswerSigninNum(int answerSigninNum) {
        this.answerSigninNum = answerSigninNum;
    }

    public int getAlreadySigninNum() {
        return alreadySigninNum;
    }

    public void setAlreadySigninNum(int alreadySigninNum) {
        this.alreadySigninNum = alreadySigninNum;
    }

    public int getNotSigninNum() {
        return notSigninNum;
    }

    public void setNotSigninNum(int notSigninNum) {
        this.notSigninNum = notSigninNum;
    }

    public List<GroupMemberVOSBean> getGroupMemberVOS() {
        return groupMemberVOS;
    }

    public void setGroupMemberVOS(List<GroupMemberVOSBean> groupMemberVOS) {
        this.groupMemberVOS = groupMemberVOS;
    }

    public static class GroupMemberVOSBean {
        /**
         * status : 2  签到状态 1 待签到 2 准时到达 3 迟到 4 签到失败
         * userId : 186409
         * userName : 138****0000
         * image : https://pf.xmzt.cn/head/defaultHeader.png?v
         * tel : 13800000000
         * signinTimeStamp : 1575712164000
         * gatherTimeStamp : 1575715020000
         * nowTimeStamp : 1575714407000
         * signinRemark : 17:49完成签到
         */

        private int status;
        private int userId;
        private String userName;
        private String image;
        private String tel;
        private long signinTimeStamp;
        private long gatherTimeStamp;
        private long nowTimeStamp;
        private String signinRemark;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public long getSigninTimeStamp() {
            return signinTimeStamp;
        }

        public void setSigninTimeStamp(long signinTimeStamp) {
            this.signinTimeStamp = signinTimeStamp;
        }

        public long getGatherTimeStamp() {
            return gatherTimeStamp;
        }

        public void setGatherTimeStamp(long gatherTimeStamp) {
            this.gatherTimeStamp = gatherTimeStamp;
        }

        public long getNowTimeStamp() {
            return nowTimeStamp;
        }

        public void setNowTimeStamp(long nowTimeStamp) {
            this.nowTimeStamp = nowTimeStamp;
        }

        public String getSigninRemark() {
            return signinRemark;
        }

        public void setSigninRemark(String signinRemark) {
            this.signinRemark = signinRemark;
        }
    }
}
