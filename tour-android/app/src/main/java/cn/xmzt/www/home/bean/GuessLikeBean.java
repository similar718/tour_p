package cn.xmzt.www.home.bean;

/**
 * 猜你喜欢
 * @author Averysk
 */
public class GuessLikeBean {
    /**
     * 标签的id
     */
    private int id;
    /**
     * 标签的名字
     */
    private String guessLikeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuessLikeName() {
        return guessLikeName;
    }

    public void setGuessLikeName(String guessLikeName) {
        this.guessLikeName = guessLikeName;
    }
}
