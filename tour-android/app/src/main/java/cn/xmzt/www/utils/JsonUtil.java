package cn.xmzt.www.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtil {
    private static Gson gson;

    static {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * json字符串转单个简单对象
     *
     * @param jsonStr
     * @param t
     *            Order.class
     * @return
     * @throws Exception
     */
    public static <T> T getObject(String jsonStr, Class<T> t) throws Exception {
        return gson.fromJson(jsonStr, t);
    }

    /**
     * json字符串转单个复杂对象
     *
     * @param jsonStr
     * @param type
     *            new TypeToken<JsonResult<Order>>() { }.getType()
     * @return
     * @throws Exception
     */
    public static <T> T getObject(String jsonStr, Type type) {
        return gson.fromJson(jsonStr, type);
    }

    public static String toString(Object t) {
        return gson.toJson(t);
    }
}
