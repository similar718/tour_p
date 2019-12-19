package cn.xmzt.www.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * The empty String <code>""</code>.
     *
     * @since 2.0
     */
    public static final String EMPTY = "";

    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 在字符串判断是否存在匹配的字符
     *
     * @param paramStr 要查找的字符串
     * @param subStr   待匹配的字符
     * @return
     */
    public static boolean isExistSubString(String paramStr, String subStr) {
        if (null == paramStr || null == subStr) {
            return false;
        }
        if (paramStr.indexOf(subStr) >= 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        if (null == str || "".equals(str)) {
            return false;
        } else if ("".equals(str.trim())) {
            return false;
        } else if ("".equals(replaceBlank(str))) {
            return false;
        }
        return true;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\\\\r|\\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][3758]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(mobiles))
//            return false;
//        else
            return !TextUtils.isEmpty(mobiles)&&mobiles.matches(telRegex);
    }



    public static String replaceSqlBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\\\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String trimString(String str) {
        if (null != str) {
            return str.trim();
        }
        return str;
    }

    /**
     * <p>
     * <code>StringUtils</code> instances should NOT be constructed in standard
     * programming. Instead, the class should be used as
     * <code>StringUtils.trim(" foo ");</code>.
     * </p>
     * <p/>
     * <p>
     * This constructor is public to permit tools that require a JavaBean
     * instance to operate.
     * </p>
     */
    public StringUtils() {
        super();
    }

    // Empty checks
    // -----------------------------------------------------------------------

    /**
     * <p>
     * Checks if a String is empty ("") or null.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     * <p/>
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * String. That functionality is available in isBlank().
     * </p>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is not empty ("") and not null.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     * <p/>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (null==str || str.length() == 0) {
            return true;
        }
//        for (int i = 0; i < strLen; i++) {
//            if ((Character.isWhitespace(str.charAt(i)) == false)) {
//                return false;
//            }
//        }
        return false;
    }

    /**
     * 比较两个字符串是否相等
     * @param str
     * @param str2
     * @return
     */
    public static boolean equlas(String str, String str2) {
        return str.equals(str2);
    }

    /**
     * <p>
     * Removes control characters (char &lt;= 32) from both ends of this String
     * returning an empty String ("") if the String is empty ("") after the trim
     * or if it is <code>null</code>.
     * <p/>
     * <p>
     * The String is trimmed using {@link String#trim()}. Trim removes start and
     * end characters &lt;= 32. To strip whitespace use
     * </p>
     * <p/>
     * <pre>
     * StringUtils.trimToEmpty(null)          = ""
     * StringUtils.trimToEmpty("")            = ""
     * StringUtils.trimToEmpty("     ")       = ""
     * StringUtils.trimToEmpty("abc")         = "abc"
     * StringUtils.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str the String to be trimmed, may be null
     * @return the trimmed String, or an empty String if <code>null</code> input
     * @since 2.0
     */
    public static String trimToEmpty(String str) {
        if (str == null || str.equals("null")) {
            return EMPTY;
        }
        return str.trim();
    }

    /**
     * 如果str 为 null ‘null’ 则返回""
     *
     * @param str nullToEmpty
     * @return
     */
    public static String nullToEmpty(String str) {
        if (StringUtils.isEmpty(str) || "null".equals(str)) {
            return EMPTY;
        }
        return str;
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String stringTrimAll(String str) {
        return stringFilter(ToDBC(replaceSqlBlank(str)));
    }

    public static boolean isNumberNotZero(String str) {
        if (isNotBlank(str)) {
            try {
                Double i = Double.parseDouble(str);
                if (i != 0) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Decimal 转 字符串
     *
     * @param str
     * @return
     */
    public static String getStr2Decimal(String str) {
        if (isBlank(str)) {
            return "0";
        }
        String reuslt = "";
        try {
            BigDecimal bg = new BigDecimal(str);
            BigDecimal resValue = bg.setScale(2, BigDecimal.ROUND_HALF_UP);
            if (resValue.doubleValue() == 0) {
                reuslt = "0";
            } else {
                reuslt = resValue.toString();
            }
        } catch (Exception e) {
            reuslt = "";
        }
        return reuslt;
    }

    public static BigDecimal getDecimal(String str) {
        BigDecimal bg = new BigDecimal(str);

        return bg;
    }

    /**
     * 将String 转化成 long
     */
    public static long getLong(String str) {
        long l = 0;
        try {
            l = Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        } finally {
        }
        return l;
    }

    /**
     * 将String 转化成 Int
     */
    public static int getInt(String str) {
        int l = 0;
        try {
            l = Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        } finally {
        }
        return l;
    }

    /**
     * 校验是否为空 返回String
     *
     * @param value
     * @return
     */
    public static String getString(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }

    /**
     * 得到控件 的值
     */
    public static String getViewValue(View view) {
        String value = "";
        if (view instanceof TextView) {
            value = ((TextView) view).getText().toString().trim();
        } else if (view instanceof EditText) {
            value = ((EditText) view).getText().toString().trim();
        }
        return value;
    }

    /**
     * 26英文字母字符串
     */
    public static String[] ENGLIST_LETTER = {"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 判断字符串是否为空或者空字符串 如果字符串是空或空字符串则返回true，否则返回false。也可以使用Android自带的TextUtil
     *
     * @param str
     * @return
     */
    // public static boolean isBlank(String str) {
    // if (str == null || "".equals(str)) {
    // return true;
    // } else {
    // return false;
    // }
    // }
    public static String string(String str) {
        if (str == null || "".equals(str) || str.equals("null")) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 判断是否是中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否超过指定字符数(待测试)
     *
     * @param content
     * @param stringNum 指定字符数 如：140
     * @return
     */
    public static boolean countStringLength(String content, int stringNum) {
        int result = 0;
        if (content != null && !"".equals(content)) {
            char[] contentArr = content.toCharArray();
            if (contentArr != null) {
                for (int i = 0; i < contentArr.length; i++) {
                    char c = contentArr[i];
                    if (isChinese(c)) {
                        result += 3;
                    } else {
                        result += 1;
                    }
                }
            }
        }
        if (result > stringNum * 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为无符号数字
     *
     * @param num
     * @return
     */
    public static boolean isNum(String num) {
        if (isBlank(num)) {
            return false;
        }
        String check = "^[0-9]*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(num);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 将double转换为字符串，保留小数点位数
     *
     * @param doubleNum 需要解析的double
     * @param digitNum  小数点位数，小于0则默认0
     * @return
     */
    public static String DoubleToAmountString(Double doubleNum, int digitNum) {
        if (digitNum < 0)
            digitNum = 0;
        StringBuilder strBuilder = new StringBuilder("#");
        for (int i = 0; i < digitNum; i++) {
            if (i == 0)
                strBuilder.append(".#");
            else
                strBuilder.append("#");
        }
        DecimalFormat df = new DecimalFormat(strBuilder.toString());
        return df.format(doubleNum);
    }

    /**
     * 提取英文的首字母，非英文字母用#代替
     *
     * @param str
     * @return
     */
    public static String getInitialAlphaEn(String str) {
        if (str == null) {
            return "#";
        }

        if (str.trim().length() == 0) {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是26字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase(Locale.getDefault()); // 大写输出
        } else {
            return "#";
        }
    }

    /**
     * 去除String中的某一个字符
     *
     * @param orgStr
     * @param splitStr 要去除的字符串
     * @return
     */
    public static String removeAllChar(String orgStr, String splitStr) {
        String[] strArray = orgStr.split(splitStr);
        String res = "";
        for (String tmp : strArray) {
            res += tmp;
        }
        return res;
    }

    /**
     * 获取非空edittext
     *
     * @param text
     * @return
     */
    public static String getEditText(EditText text) {
        if (null == text || text.getText().toString().trim().equals("")) {
            return "";
        }
        return text.getText().toString().trim();
    }

    /**
     * MD5加密 32位小写
     *
     * @param sSecret
     * @return
     */
    public static String getMd5Value(String sSecret) {
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(sSecret.getBytes());
            int i;
            StringBuilder buf = new StringBuilder();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param content 整体内容
     * @param start   开始位置
     * @param end     结束位置
     * @return
     */
    public static SpannableStringBuilder changStringColor(String content,
                                                          int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }

    public static SpannableStringBuilder changStringColor(String content,
                                                          int[] start, int[] end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        for (int i = 0; i < end.length; i++) {
            ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
            builder.setSpan(redSpan, start[i], end[i],
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * String 转 int
     */
    public static int paseInt(String str) {
        if (isNumber(str)) {
            return Integer.parseInt(str);
        }
        return 0;
    }

    /**
     * String 转 int
     */
    public static double paseDouble(String str) {
        if (isNumber(str)) {
            return Double.parseDouble(str);
        }
        return 0;
    }

    /**
     * int 转String
     */

    public static String paseString(int in) {
        return String.valueOf(in);
    }

    /**
     * 可以用异常来做校验 判断字符串是否是整数
     */
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是浮点数
     */
    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains("."))
                return true;
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String value) {
        return isInteger(value) || isDouble(value);
    }

    /**
     * 判断值不能为空
     */
    public static String checkValue(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            if (!StringUtils.isBlank(entry.getKey())) {
                if (StringUtils.isBlank(entry.getValue().toString())) {
                    sb.append("" + entry.getKey() + "\n");
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static boolean checkValue2(Map<String, Object> map, Context context) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            if (!StringUtils.isBlank(entry.getKey())) {
                if (null == entry.getValue()
                        || StringUtils.isBlank(entry.getValue().toString())) {
                    sb.append("" + entry.getKey() + "\n");
                    break;
                }
            }
        }
        if (!StringUtils.isBlank(sb.toString())) {
            ToastUtils.showText(context, sb.toString());
            return false;
        }
        return true;
    }

    /**
     * List 转数组
     */
    public static String[] list2array(List<String> list) {
        int size = list.size();
        String[] array = list.toArray(new String[size]);
        return array;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
//        if (bit == 'N') {
//            return false;
//        }
        return bit != 'N'&&cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断手机号码
     * @return
     */
    public static boolean isMobileNo(String mobile){
        if (StringUtils.isBlank(mobile)) {
            return false;
        }
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        Matcher m = p.matcher(mobile);
        return m.matches();

    }

}
