package cn.xmzt.www.bean;

import com.blankj.utilcode.util.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import cn.xmzt.www.base.TourApplication;
import cn.xmzt.www.helper.UserHelper;
import cn.xmzt.www.main.activity.MainActivity;
import cn.xmzt.www.mine.activity.LoginActivity;
import cn.xmzt.www.mine.event.LogOutEvent;

/**
 * 实体的基类
 */
public class BaseDataBean<T> implements Serializable {
    private static String CODE_SUCCESS = "X0000";//成功的code
    public static String CODE_TOKEN_INVALID = "X1004";//token失效
    public static String CODE_ORDER_PAY_TIMEOUT = "X8036";//订单支付超时
    public static String CODE_UNBIND = "X1006";//该手机号已绑定了其他微信账号，是否确认继续绑定新的微信账号
    public static String CODE_NO_GROUP_MEMBER = "X9109";//非群成员
    private String reCode;//响应码
    private String reMsg;//响应描述
    private String requestUrl;//请求接口
    private T rel;//数据集
    public boolean isSuccess() {
        if (CODE_TOKEN_INVALID.equals(getReCode())) {
            TourApplication.setToken(null);
            TourApplication.setUser(null);
            UserHelper.logout();
            EventBus.getDefault().post(new LogOutEvent());
            ActivityUtils.startActivity(LoginActivity.class);
        }
        return CODE_SUCCESS.equals(getReCode());
    }
    public String getReCode() {
        return reCode;
    }

    public void setReCode(String reCode) {
        this.reCode = reCode;
    }

    public String getReMsg() {
        return reMsg;
    }

    public void setReMsg(String reMsg) {
        this.reMsg = reMsg;
    }

    public T getRel() {
        return rel;
    }

    public void setRel(T rel) {
        this.rel = rel;
    }
}
