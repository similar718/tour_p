package cn.xmzt.www.home.bean;


import java.util.HashMap;
import java.util.List;

/**
 * 首页实体
 * @author Averysk
 */
public class HomeIndexBean {

    private List<AdvertiseBean> advertiseVOs;//首页头部广告轮播
    private List<AdvertiseBean> advertiseVOsMiddle;//首页中间广告轮播
    private List<ArticleBean> articleList;//精品线路文章信息
    private List<PopularityBean> popularityVOs;//首页人气必玩
    private List<RecommendLineBean> recommendLineVO;//热门推荐
    private List<GuessLikeBean> guessLikeList;//首页的猜你喜欢标签信息
    private HashMap<String, Integer> signIntegralReward;
    private boolean sign;//是否可用签到
    private int msgUnRead;//未读消息数
//    private List signIntegralReward;//description:30天签到得到的积分规则
    private int continuouNum;//累计签到次数

    public List<AdvertiseBean> getAdvertiseVOs() {
        return advertiseVOs;
    }

    public void setAdvertiseVOs(List<AdvertiseBean> advertiseVOs) {
        this.advertiseVOs = advertiseVOs;
    }

    public List<AdvertiseBean> getAdvertiseVOsMiddle() {
        return advertiseVOsMiddle;
    }

    public void setAdvertiseVOsMiddle(List<AdvertiseBean> advertiseVOsMiddle) {
        this.advertiseVOsMiddle = advertiseVOsMiddle;
    }

    public List<PopularityBean> getPopularityVOs() {
        return popularityVOs;
    }

    public void setPopularityVOs(List<PopularityBean> popularityVOs) {
        this.popularityVOs = popularityVOs;
    }

    public List<RecommendLineBean> getRecommendLineVO() {
        return recommendLineVO;
    }

    public void setRecommendLineVO(List<RecommendLineBean> recommendLineVO) {
        this.recommendLineVO = recommendLineVO;
    }

    public List<GuessLikeBean> getGuessLikeList() {
        return guessLikeList;
    }

    public void setGuessLikeList(List<GuessLikeBean> guessLikeList) {
        this.guessLikeList = guessLikeList;
    }

    public HashMap<String, Integer> getSignIntegralReward() {
        return signIntegralReward;
    }

    public void setSignIntegralReward(HashMap<String, Integer> signIntegralReward) {
        this.signIntegralReward = signIntegralReward;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public int getMsgUnRead() {
        return msgUnRead;
    }

    public void setMsgUnRead(int msgUnRead) {
        this.msgUnRead = msgUnRead;
    }

    public List<ArticleBean> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleBean> articleList) {
        this.articleList = articleList;
    }

    public int getContinuouNum() {
        return continuouNum;
    }

    public void setContinuouNum(int continuouNum) {
        this.continuouNum = continuouNum;
    }
}
