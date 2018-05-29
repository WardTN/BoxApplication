package com.example.chen.boxapplication.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;


import java.util.Random;

public class BoxsModel {
    //方块
    public Point[] boxs;
    //方块的类型
    public int boxtype;
    //方块大小
    public int boxSize;

    //方块画笔
    Paint boxPaint;


    public BoxsModel(int boxSize) {
        this.boxSize = boxSize;
    }

    /**
     * 新的方块
     */
    public void newBoxs(){
        //随机数生成一个方块
        Random random = new Random();
        boxtype = random.nextInt(7);
        switch (boxtype)
        {
            case 0: //田字型
                boxs = new Point[]{new Point(4,0),new Point(5,0),new Point(4,1),new Point(5,1)};
                Log.e("TAG","田字型");
                break;
            case 1: //L型
                boxs = new Point[]{new Point(4,1),new Point(5,0),new Point(3,1),new Point(5,1)};
                Log.e("TAG","L字型");
                break;
            case 2: //反L型
                boxs = new Point[]{new Point(4,1),new Point(3,0),new Point(3,1),new Point(5,1)};
                Log.e("TAG","反L字型");
                break;
            case 3: //长条型
                boxs = new Point[]{new Point(4,0),new Point(5,0),new Point(6,0),new Point(7,0)};
                Log.e("TAG","长字型");
                break;
            case 4: //土字型
                boxs = new Point[]{new Point(5,1),new Point(5,0),new Point(4,1),new Point(6,1)};
                Log.e("TAG","土字型");
                break;
            case 5: //z字型
                boxs = new Point[]{new Point(5,1),new Point(4,0),new Point(4,1),new Point(6,1)};
                Log.e("TAG","z字型");
                break;
            case 6: //反z字型
                boxs = new Point[]{new Point(5,1),new Point(5,0),new Point(4,1),new Point(4,2)};
                Log.e("TAG","反z字型");
                break;
        }
    }

    //画方块
    public void drawBoxs(Canvas canvas){
        boxPaint = new Paint();
        boxPaint.setColor(Color.GRAY);
        boxPaint.setAntiAlias(true);
        if (boxs!=null)
        {
            for (int i=0;i<boxs.length;i++)
            {
                canvas.drawRect(boxs[i].x*boxSize,boxs[i].y*boxSize,boxs[i].x*boxSize+boxSize,boxs[i].y*boxSize+boxSize,boxPaint);
            }
        }
    }

    //边界判断
    public boolean checkBoundary(int x,int y,MapModel mapModel){
        return (x<0||y<0||x>=mapModel.maps.length||y>=mapModel.maps[0].length||mapModel.maps[x][y]);
    }

    //移动
    public boolean move(int x,int y,MapModel mapModel){

        for (int i=0;i<boxs.length;i++)
        {
            //边界判断
            if (checkBoundary(boxs[i].x+x,boxs[i].y+y,mapModel)){
                //出界移动失败
                return false;
            }
            //遍历方块数组,
        }
        //   遍历方块数组,每一块加上偏移的量
        for (int i=0;i<boxs.length;i++)
        {
            boxs[i].x +=x;
            boxs[i].y +=y;

        }
        return true;
    }
    //旋转
    public boolean rotate(MapModel mapModel){
        if (boxtype==0)
        {
            return false;
        }
        for (int i=0;i<boxs.length;i++)
        {
            //旋转算法      笛卡尔公式
            int checkX = -boxs[i].y +boxs[0].y+boxs[0].x;
            int checkY =  boxs[i].x-boxs[0].x+boxs[0].y;
            //将旋转后的点，传入边界
            if (checkBoundary(checkX,checkY,mapModel))
            {
                return false;   //旋转失败
            }
            boxs[i].x = checkX;
            boxs[i].y = checkY;
        }
        return true;
    }

}
