package cn.xmzt.www.rxjava2;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 异步操作
 */
public class AsyncUtil {
    /**
     * 执行异步任务，在主线程回调
     * @param function
     * @param action
     */
    public static void async(Function function, Consumer action) {
        Observable observable = Observable.just("").observeOn(Schedulers.io()).map(function);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action);
    }
    /**
     * 转为主线程回调
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        AsyncUtil.async(new Function<String, String>() {
            @Override
            public String apply(String o) throws Exception {
                return "";
            }
        }, new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                runnable.run();
            }
        });
    }
}
