package com.leicx.weixin.util;


import java.math.BigDecimal;

/**
 * 金额计算&显示相关帮助方法
 * Created by kaka on 2015/3/29.
 */

public class MoneyUtil {
	
	/**
     * 格式化金额,并按参数保留相应的小数位数，
     * 小于1万显示原值；
     * 小于1亿大于1万显示xx.xx万；
     * 大于1亿显示xx.xx亿
     * 采用四舍五入
     * 负数不处理，返回原值---更改：负数也处理（毛利等有负数情况）
     * PS: 暂用于中文金额显示
     *
     * @param amount       待格式化的金额
     * @param decimalDigit 小数位数
     * @param unit         单位
     * 修改时间：2018-5-25 by zcp 负数也要处理，要指定舍入模式
     * @return
     */
    public static String formatAmount(BigDecimal amount, int decimalDigit, String unit) {
        double amountDouble = amount.doubleValue();
        StringBuilder sb = new StringBuilder();
        String suffix = unit;
        if (amountDouble < 10000 && amountDouble >= 0) {
            return amount.setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).toPlainString() + suffix;
        } else if (amountDouble >= 10000 && amountDouble < 100000000) {
            suffix = "万" + unit;
            return amount.divide(new BigDecimal(10000)).setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).toPlainString() + suffix;
        } else if (amountDouble >= 100000000) {
            suffix = "亿" + unit;
            return amount.divide(new BigDecimal(100000000)).setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).toPlainString() + suffix;
        }

        //负数
        return amount.setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).toPlainString() + suffix;
    }

    /**
     * 格式化金额,并按参数保留相应的小数位数，
     * 小于1万显示原值；
     * 小于1亿大于1万显示xx.xx万；
     * 大于1亿显示xx.xx亿
     * 负数不处理，返回原值
     * PS: 暂用于中文金额显示
     * 单位默认为元
     *
     * @param amount 待格式化的金额
     * @return
     */
    public static String formatAmount(BigDecimal amount, int decimalDigit) {
        return formatAmount(amount, decimalDigit, "元");
    }

    /**
     * 格式化金额,并按参数保留相应的小数位数，
     * 小于1万显示原值；
     * 小于1亿大于1万显示xx.xx万；
     * 大于1亿显示xx.xx亿
     * 负数不处理，返回原值
     * PS: 暂用于中文金额显示
     * 单位默认为元 保留两位有效数字
     *
     * @param amount 待格式化的金额
     * @return
     */
    public static String formatAmount(BigDecimal amount) {
        return formatAmount(amount, 2);
    }

    /**
     * 格式化金额,并按参数保留相应的小数位数，
     * 小于1万显示原值；
     * 小于1亿大于1万显示xx.xx万；
     * 大于1亿显示xx.xx亿
     * 负数不处理，返回原值
     * PS: 暂用于中文金额显示
     *
     * @param amount 待格式化的金额
     * @return
     */
    public static String formatAmount(double amount, int decimalDigit, String unit) {
        return formatAmount(BigDecimal.valueOf(amount), decimalDigit, unit);
    }

    /**
     * 格式化金额,并按参数保留相应的小数位数，
     * 小于1万显示原值；
     * 小于1亿大于1万显示xx.xx万；
     * 大于1亿显示xx.xx亿
     * 负数不处理，返回原值
     * PS: 暂用于中文金额显示
     * 单位默认为元 保留两位有效数字
     *
     * @param amount 待格式化的金额
     * @return
     */
    public static String formatAmount(double amount) {
        return formatAmount(BigDecimal.valueOf(amount));
    }


    /**
     * 四舍五入格式化数据，返回纯数字
     * @param doub
     * @param decimalDigit 保留小数位
     * @return 纯Double数字
     * *
     * @author 郑初晓
     * 创建时间： 2017/8/10 15:16
     * 修改时间：
     * @version v3.10
     * @since
     */
    public static Double formatDouble (Double doub, int decimalDigit){
        return new BigDecimal(doub).setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static String formatDouble(BigDecimal amount, int decimalDigit, String unit) {
        return amount.setScale(decimalDigit, BigDecimal.ROUND_HALF_UP).toPlainString() + unit;
    }
    /**
     * 将对象转为double浮点数，如果有错误或为空则返回0
     * @param object 需要转化的数
     *
     * @return 转化后的结果
     *
     * @author 章华隽
     * 创建时间：2018/1/12 下午8:48
     * 修改时间：
     * @version 3.16
     * @since 3.16
     */
    public static Double parseDouble(Object object) {
        Double retDouble = 0D;
        if(object == null) {
            return retDouble;
        }
        
        if (object instanceof BigDecimal) {
            retDouble = ((BigDecimal) object).doubleValue();
        } else if(object instanceof Double){
            retDouble = (Double) object;
        } else {
            try {
                retDouble = Double.parseDouble(object.toString());
            } catch (NumberFormatException e) {
                retDouble = 0D;
            }
        }
        return retDouble;
    }
    
    /**
     * 金额大写
     * @param numValue 金额
     * @return 金额的大写值
     * 创建时间： 2018年2月23日 下午3:43:58
     * 修改时间： 2018年2月23日 下午3:43:58 梁鲁江
     * @author 梁鲁江
     */
    public static String digitUppercase(double numValue) {
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
      
        String head = numValue < 0 ? "负" : "";
        numValue = Math.abs(numValue);
      
        String s = "";
        for (int i = 0; i < fraction.length; i++) {  
            s += (digit[(int) (Math.floor(numValue * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }  
        if (s.length() < 1) {  
            s = "整";  
        }  
        int integerPart = (int) Math.floor(numValue);
      
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {  
            String p = "";
            for (int j = 0; j < unit[1].length && numValue > 0; j++) {  
                p = digit[integerPart % 10] + unit[1][j] + p;  
                integerPart = integerPart / 10;  
            }  
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;  
        }  
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");  
    }  
}
