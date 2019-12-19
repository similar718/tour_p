package cn.xmzt.www.utils;

import java.text.NumberFormat;
import java.text.ParseException;

public class MathUtil {
    /**
     * 判断是否为整数倍
     *
     * @param n
     * @param m
     * @return
     */
    public static boolean isPowerOfTwo(double n, double m) {
        double hD = 0;
        long hI = 0;
        if (m == 0){
            return true;
        }else {
            try {
                if (n % m == 0) {
                    System.out.println("n可以被m整除");
                    return true;
                } else {
                    hD = n / m;
                    hI = (long) hD;
                    if (hD == hI) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    public static double parseDouble(String sDouble) {
        double parseDouble = 0;
        try {
            parseDouble = Double.parseDouble(sDouble);
        }catch (Exception e){}
        return parseDouble;
    }
    public static int parseInt(String sInt) {
        int parseInt = 0;
        try {
            parseInt = Integer.parseInt(sInt);
        }catch (Exception e){}
        return parseInt;
    }
    public static long parseLong(String sLong) {
        long parseLong = 0;
        try {
            parseLong = Long.parseLong(sLong);
        }catch (Exception e){}
        return parseLong;
    }

    /**
     * 根据倍数更正数据
     * @param aDouble 要更正的数据
     * @param multiple 倍数
     * @return
     */
    public static String multipleCorrect(double aDouble,double multiple) {
        String fddd="";
        if((long)multiple==multiple){
            int d= (int) (aDouble%multiple);
            if(d==0){
                aDouble=aDouble;
            }else {
                aDouble=aDouble-d;
            }
            fddd=""+(long)aDouble;
        }else {
            NumberFormat mNumberFormat = NumberFormat.getInstance();
            mNumberFormat.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
            String str1=mNumberFormat.format(multiple);
            if(str1.contains(".")){
                str1=str1.replaceAll(".+\\.", "");
            }else {
                str1="";
            }
            int int1= (int) (aDouble*Math.pow(10,str1.length()));
            int int2= (int) (multiple*Math.pow(10,str1.length()));
            int d= int1%int2;
            if(d==0){
                fddd=mNumberFormat.format(aDouble);
            }else {
                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
                double ddd=0;
                try {
                    Number number = format.parse(String.valueOf(d*Math.pow(0.1,str1.length())));
                    ddd=number.doubleValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                NumberFormat format1 = NumberFormat.getInstance();
                format1.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
                format1.setGroupingUsed(false);

                aDouble=aDouble-ddd;
                fddd=format1.format(aDouble);
            }
        }
        return fddd;
    }

    public static String formatDouble(double aDouble) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
        format.setGroupingUsed(false);
        String result=format.format(aDouble);
        if(result==null){
            result="";
        }
        return result;
    }

    /**
     * 格式化Double
     * @param aDouble
     * @param precision 精度
     * @return
     */
    public static String formatDouble(double aDouble,int precision) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(precision);//设置数值的【小数部分】允许的最大位数
        format.setGroupingUsed(false);
        String result=format.format(aDouble);
        if(result==null){
            result="";
        }
        return result;
    }

    public static String formatDouble(String aDouble) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
        format.setGroupingUsed(false);
        String result=format.format(parseDouble(aDouble));
        if(result==null){
            result="";
        }
        return result;
    }

    public static String formatDoubleNoGroupingUsed(double aDouble) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(32);//设置数值的【小数部分】允许的最大位数
        format.setGroupingUsed(false);
        String result=format.format(aDouble);
        if(result==null){
            result="";
        }
        return result;
    }

    /**
     * 获取Double 的精度
     * @param aDouble
     * @return
     */
    public static int getPrecision(double aDouble) {
        int precision=0;
        String str1= formatDouble(aDouble);
        if(str1.contains(".")){
            str1=str1.replaceAll(".+\\.", "");
        }else {
            str1="";
        }
        precision="0".equals(str1)?0:str1.length();
        return precision;
    }

}
