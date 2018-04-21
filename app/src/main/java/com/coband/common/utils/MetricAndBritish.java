package com.coband.common.utils;


/**
 * Created by tgc on 17-4-18.
 */

public class MetricAndBritish {
    // unit 0: Metric    1 :Inch

    public static float getWeight(int unit,float weight) {

        //第一个参数是本地设置的制度，unit是后台的制度
        //first num is local,unit is background

        //用户设置的是英制，后台是公制，千克到磅
        //user is set british,background is metric,kg to pount
        if (!CocoUtils.isMetric() && unit == 0) {
            return (float) Math.round((weight * 2.2) * 10) / 10;
        } else if (CocoUtils.isMetric() && unit == 1){
            return (float) Math.round((weight / 2.2) * 10) / 10;
        } else {
            return (float) Math.round(weight * 10) / 10;
        }
    }


    //第一个参数是本地设置的制度，unit是后台的制度
    //first num is local,unit is background

    public static float getHight(int unit,float hight){

        //用户设置的是英制，后台是公制，厘米到英寸
        //user is set british,background is metric,cm to inch
        if (!CocoUtils.isMetric() && unit == 0) {
            return (float) (hight/0.394);
        } else if (CocoUtils.isMetric() && unit == 1){
            return (float) (hight*2.54);
        } else {
            return hight;
        }
    }

    //取英尺整数
    public static int getFootFromInch(int cm){
        return (int)(cm*0.0328);
    }

    //厘米到英寸
    public static int cmToInch(int cm){
        return (int)(cm*0.394);
    }

    //英尺小数点后的数换算成int的英寸
    public static int inchToRestFoot(int cm){
        return (cmToInch(cm)-(((int)(cm*0.0328))*12));
    }

    public static int inchToCM(int inch,int foot){
        return (int)((foot*30.48)+(inch*2.54));
    }
}
