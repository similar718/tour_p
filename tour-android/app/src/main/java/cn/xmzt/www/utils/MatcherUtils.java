package cn.xmzt.www.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tanlei
 * @date 2019/8/6
 * @describe 正则判断的工具类
 */

public class MatcherUtils {
//    public static final String mobilePattern = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    public static final String mobilePattern = "^(1[34578])\\d{9}$";
    /**
     * 判断是否为手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        Pattern p = Pattern.compile(mobilePattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /**
     * 检查身份证是否有效
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard){
        if(TextUtils.isEmpty(idCard)){
            return false;
        }
        String reg = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        Pattern pattern = Pattern.compile(reg);
        Matcher mc = pattern.matcher(idCard);
        // 加权因子系数列表
        int[] ratioArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        // 校验码列表
        char[] checkCodeList = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        // 获取身份证号字符数组
        char[] cIds = idCard.toCharArray();
        // 获取最后一位（身份证校验码）
        char oCode=cIds[cIds.length-1];
        int[] iIds = new int[17];
        int idSum = 0;// 身份证号第1-17位与系数之积的和
        int residue = 0;// 余数(用加出来和除以11，看余数是多少？)

        for (int i = 0; i < 17; i++) {
            if(i<cIds.length){
                iIds[i] = cIds[i] - '0';
                idSum += iIds[i] * ratioArr[i];
            }
        }
        residue = idSum % 11;// 取得余数
        return Character.toUpperCase(oCode) == checkCodeList[residue] && mc.matches();
    }
    /**
     * 判断是否为有效的车牌
     * @param str
     * @return
     */
    public static boolean isCarCard(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        String carCardpattern = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼]{1}(([A-HJ-Z]{1}[A-HJ-NP-Z0-9]{5})|([A-HJ-Z]{1}(([DF]{1}[A-HJ-NP-Z0-9]{1}[0-9]{4})|([0-9]{5}[DF]{1})))|([A-HJ-Z]{1}[A-D0-9]{1}[0-9]{3}警)))|([0-9]{6}使)|((([沪粤川云桂鄂陕蒙藏黑辽渝]{1}A)|鲁B|闽D|蒙E|蒙H)[0-9]{4}领)|(WJ[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼·•]{1}[0-9]{4}[TDSHBXJ0-9]{1})|([VKHBSLJNGCE]{1}[A-DJ-PR-TVY]{1}[0-9]{5})";
        Pattern p = Pattern.compile(carCardpattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
    /**
     * 验证税号
     * 15或者17或者18或者20位字母、数字组成
     * @param str
     * @returns {Boolean}
     */
    public static boolean isTaxNo(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        String reg = "^[A-Z0-9]{15}$|^[A-Z0-9]{17}$|^[A-Z0-9]{18}$|^[A-Z0-9]{20}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str.toUpperCase());
        return m.matches();
    }
    /**
     * 判断是否为有效的Emial
     * @param str
     * @return
     */
    public static boolean isEmial(String str) {
        if(TextUtils.isEmpty(str)){
            return false;
        }
        String reg="[A-z0-9_-]+\\@[A-z0-9]+\\.[A-z]+";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
