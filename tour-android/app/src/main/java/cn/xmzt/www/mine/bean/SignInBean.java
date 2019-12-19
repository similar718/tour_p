package cn.xmzt.www.mine.bean;

import java.util.List;
import java.util.Map;

/**
 * @author tanlei
 * @date 2019/8/24
 * @describe 签到信息
 */

public class SignInBean {

    /**
     * signList : [{"gainIntegral":100,"id":50,"signDate":"2019-08-01 00:00:00","userId":186139,"day":"01"},{"gainIntegral":100,"id":47,"signDate":"2019-07-27 00:00:00","userId":186139,"day":"27"}]
     * weekSignList : [{"gainIntegral":110,"id":117,"signDate":"2019-08-22 00:00:00","userId":186139,"day":"22"},{"gainIntegral":100,"id":110,"signDate":"2019-08-21 00:00:00","userId":186139,"day":"21"}]
     */
    private List<SignListEntity> signList;
    private List<WeekSignListEntity> weekSignList;
    /**
     * awards : [{"awardProb":33,"id":1,"awardPicture":"http://5b0988e595225.cdn.sohucs.com/images/20180605/d9a4160d7dce46b98474da6b3c70b6bc.jpeg","awardNum":10,"awardName":"iphone6"},{"awardProb":33,"id":2,"awardPicture":"http://5b0988e595225.cdn.sohucs.com/images/20180605/d9a4160d7dce46b98474da6b3c70b6bc.jpeg","awardNum":10,"awardName":"iphone7"},{"awardProb":33,"id":3,"awardPicture":"http://5b0988e595225.cdn.sohucs.com/images/20180605/d9a4160d7dce46b98474da6b3c70b6bc.jpeg","awardNum":10,"awardName":"iphone8"}]
     */
    private List<AwardsEntity> awards;
    /**
     * lotteryNum : 0
     * lastGainIntegral : 0
     * continuouNum : 4
     */
    private int lotteryNum;
    private int lastGainIntegral;
    private int continuouNum;
    /**
     * sign : true
     */
    private boolean sign;
    private Map<String, Integer> signIntegralReward;

    public Map<String, Integer> getSignIntegralReward() {
        return signIntegralReward;
    }

    public void setSignIntegralReward(Map<String, Integer> signIntegralReward) {
        this.signIntegralReward = signIntegralReward;
    }

    public void setSignList(List<SignListEntity> signList) {
        this.signList = signList;
    }

    public void setWeekSignList(List<WeekSignListEntity> weekSignList) {
        this.weekSignList = weekSignList;
    }

    public List<SignListEntity> getSignList() {
        return signList;
    }

    public List<WeekSignListEntity> getWeekSignList() {
        return weekSignList;
    }

    public void setAwards(List<AwardsEntity> awards) {
        this.awards = awards;
    }

    public List<AwardsEntity> getAwards() {
        return awards;
    }

    public void setLotteryNum(int lotteryNum) {
        this.lotteryNum = lotteryNum;
    }

    public void setLastGainIntegral(int lastGainIntegral) {
        this.lastGainIntegral = lastGainIntegral;
    }

    public void setContinuouNum(int continuouNum) {
        this.continuouNum = continuouNum;
    }

    public int getLotteryNum() {
        return lotteryNum;
    }

    public int getLastGainIntegral() {
        return lastGainIntegral;
    }

    public int getContinuouNum() {
        return continuouNum;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean isSign() {
        return sign;
    }

    public class SignListEntity {
        /**
         * gainIntegral : 100
         * id : 50
         * signDate : 2019-08-01 00:00:00
         * userId : 186139
         * day : 01
         */
        private int gainIntegral;
        private int id;
        private String signDate;
        private int userId;
        private String day;

        public void setGainIntegral(int gainIntegral) {
            this.gainIntegral = gainIntegral;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getGainIntegral() {
            return gainIntegral;
        }

        public int getId() {
            return id;
        }

        public String getSignDate() {
            return signDate;
        }

        public int getUserId() {
            return userId;
        }

        public String getDay() {
            return day;
        }
    }

    public class WeekSignListEntity {
        /**
         * gainIntegral : 110
         * id : 117
         * signDate : 2019-08-22 00:00:00
         * userId : 186139
         * day : 22
         */
        private int gainIntegral;
        private int id;
        private String signDate;
        private int userId;
        private String day;

        public void setGainIntegral(int gainIntegral) {
            this.gainIntegral = gainIntegral;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setSignDate(String signDate) {
            this.signDate = signDate;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getGainIntegral() {
            return gainIntegral;
        }

        public int getId() {
            return id;
        }

        public String getSignDate() {
            return signDate;
        }

        public int getUserId() {
            return userId;
        }

        public String getDay() {
            return day;
        }
    }

    public class AwardsEntity {
        /**
         * awardProb : 33
         * id : 1
         * awardPicture : http://5b0988e595225.cdn.sohucs.com/images/20180605/d9a4160d7dce46b98474da6b3c70b6bc.jpeg
         * awardNum : 10
         * awardName : iphone6
         */
        private int awardProb;
        private int id;
        private String awardPicture;
        private int awardNum;
        private String awardName;

        public void setAwardProb(int awardProb) {
            this.awardProb = awardProb;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setAwardPicture(String awardPicture) {
            this.awardPicture = awardPicture;
        }

        public void setAwardNum(int awardNum) {
            this.awardNum = awardNum;
        }

        public void setAwardName(String awardName) {
            this.awardName = awardName;
        }

        public int getAwardProb() {
            return awardProb;
        }

        public int getId() {
            return id;
        }

        public String getAwardPicture() {
            return awardPicture;
        }

        public int getAwardNum() {
            return awardNum;
        }

        public String getAwardName() {
            return awardName;
        }
    }
}
