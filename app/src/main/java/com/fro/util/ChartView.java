package com.fro.util;

import com.fro.util.Constant;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.jar.Attributes;

public class ChartView extends View {
    public int XPoint=40;    //原点的X坐标
    public int YPoint=40;     //原点的Y坐标
    public int XScale=55;     //X的刻度长度
    public int YScale=40;     //Y的刻度长度
    public int relen = 0;
    public int XLength=Constant.point.y-400;        //X轴的长度
    public int YLength=Constant.point.x-100;        //Y轴的长度
    public String[] XLabel;    //X的刻度
    public String[] YLabel;    //Y的刻度
    public String[] Data;      //数据
    public String Title;    //显示的标题

    public ChartView(Context context) {
        super(context);
    }
    public ChartView(Context context,AttributeSet attributeSet,int defStyle) {
        super(context,attributeSet,defStyle);
    }
    public ChartView(Context context,AttributeSet attributeSet) {
        this(context,attributeSet,0);
    }
    public void SetInfo(String[] XLabels,String[] YLabels,String[] AllData,String strTitle,int rel) {
        relen = rel;
        XLabel=XLabels;
        YLabel=YLabels;
        Data=AllData;
        Title=strTitle;
        XScale=XLength/rel;//实际X的刻度长度
        YScale=YLength/YLabels.length;
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);//重写onDraw方法

        //canvas.drawColor(Color.WHITE);//设置背景颜色
        Paint paint= new Paint();
        paint.setStyle(Paint.Style.STROKE); //仅描线
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.BLACK);//颜色
        //paint.setFakeBoldText(true);
        //paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        paint.setStrokeWidth(2.5f);
        Paint paint1=new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//去锯齿
        paint1.setColor(Color.DKGRAY);
        paint.setTextSize(25);  //设置轴文字大小
        //设置Y轴
        canvas.drawLine(XPoint, YPoint, XPoint + YLength, YPoint, paint);   //轴线
        for(int i=0;i*YScale<YLength ;i++) {
            canvas.drawLine(XPoint + i*YScale,YPoint, XPoint + i*YScale, YPoint + 5, paint);  //刻度
            try {
                canvas.drawText(YLabel[i] , XPoint + i*YScale, YPoint - 5, paint);  //文字
            }
            catch(Exception e) { }
        }
        canvas.drawLine(XPoint + YLength,YPoint,XPoint + YLength -12,YPoint - 6,paint);  //箭头
        canvas.drawLine(XPoint + YLength,YPoint,XPoint + YLength -12,YPoint + 6,paint);
        //设置字体的大小角度等
        paint.setTextSize(30);
        drawText(canvas,"单位:kWh", XPoint + YLength - 20, YPoint - 10, paint,0);

        //设置X轴
        paint.setTextSize(25);
        canvas.drawLine(XPoint,YPoint,XPoint,YPoint + XLength,paint);   //轴线
        for(int i=0;i*XScale<XLength;i++) {
            canvas.drawLine(XPoint, YPoint + i*XScale, XPoint+5, YPoint + i*XScale, paint);  //刻度
            try {
//                canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
//                        YPoint + 20, paint); // 文字
                drawText(canvas,XLabel[i], XPoint - 25,
                        YPoint + i*XScale, paint,90); // 文字
                // 数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999
                        && YCoord(Data[i]) != -999) // 保证有效数据
                    canvas.drawLine(YCoord(Data[i - 1]),YPoint + (i-1)*XScale
                            , YCoord(Data[i]),YPoint + i*XScale
                            , paint);
                canvas.drawCircle(YCoord(Data[i]), YPoint + i*XScale, 2,
                        paint);
            } catch (Exception e) { }
        }
        canvas.drawLine(XPoint,YPoint + XLength,XPoint + 6,YPoint + XLength - 12,paint);    //箭头
        canvas.drawLine(XPoint,YPoint + XLength,XPoint - 6,YPoint + XLength - 12,paint);
        //设置标题位置
        paint.setTextSize(28);
    }
    //设置文字显示方向
    void drawText(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle) {
        if(angle != 0) {
            canvas.rotate(angle, x, y); //旋转angle角度
        }
        canvas.drawText(text, x, y, paint);
        if(angle != 0) {
            canvas.rotate(-angle, x, y); //将旋转过的canvas复原
        }
    }

    private int YCoord(String y0) {  //计算绘制时的Y坐标，无数据时返回-999
        int y;
        try {
            y=Integer.parseInt(y0);
        }
        catch(Exception e) {
            return -999;    //出错则返回-999
        }
        try {
            return XPoint + y*YScale/Integer.parseInt(YLabel[1]); //按刻度比例找到对应高度
        } catch(Exception e) { }
        return y;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
