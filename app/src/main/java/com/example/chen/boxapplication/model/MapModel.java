package com.example.chen.boxapplication.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.chen.boxapplication.Config;

public class MapModel {

    //地图画笔
    Paint mapPaint;

    //辅助线画笔
    Paint linePaint;

    //状态画笔
    Paint statePaint;

    //地图
    public boolean[][] maps;

    //地图宽度
    int xWidth;
    //地图高度
    int yHeight;
    //方块大小
    int boxSize;

    public MapModel(int xWidth, int yHeight, int boxSize){
        this.boxSize = boxSize;
        this.xWidth = xWidth;
        this.yHeight = yHeight;

        mapPaint = new Paint();
        mapPaint.setColor(Color.BLACK);

        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setAntiAlias(true);

        statePaint = new Paint();
        statePaint.setColor(Color.BLUE);
        statePaint.setTextSize(100);
        statePaint.setAntiAlias(true);

        //初始化地图数据
        maps = new boolean[Config.MAPX][Config.MAPY];
    }



    public void drawMap(Canvas canvas){
        //绘制地图
        for (int x=0;x<maps.length;x++)
        {
            for (int y=0;y<maps[x].length;y++)
            {
                if (maps[x][y] == true)

                canvas.drawRect(x*boxSize,y*boxSize,
                        x*boxSize+boxSize,y*boxSize+boxSize,mapPaint);
            }
        }
    }
    public void drawLines(Canvas canvas)
    {

        for (int x=0;x<maps.length;x++)
        {
            canvas.drawLine(x*boxSize,0,x*boxSize,yHeight,linePaint);
        }
        for (int y=0;y<maps[0].length;y++)
        {
            canvas.drawLine(0,y*boxSize,xWidth,y*boxSize,linePaint);
        }
    }

    public void drawState(Canvas canvas,boolean isPause,boolean isOver) {
        Log.e("TAG","isPause is" + isPause+"  isOver is" + isOver);
        if (isOver)
        {
            canvas.drawText("游戏结束",xWidth/2-statePaint.measureText("游戏结束")/2,yHeight/2+50,statePaint);
        }
        if (isPause)
        {
            canvas.drawText("游戏暂停",xWidth/2-statePaint.measureText("游戏暂停")/2,yHeight/2+50,statePaint);

        }

    }


    //清除地图
    public void cleanMaps() {
        //清除地图
        for (int x =0;x<maps.length;x++)
        {
            for (int y = 0;y<maps[x].length;y++)
            {
                maps[x][y] = false;
            }
        }
    }


    /**
     * 消行判断
     * @param y
     * @return
     */
    public boolean checkLine(int y)
    {
        for (int x=0;x<maps.length;x++)
        {
            //如果有一个不为true,则该行不能消除
            if (!maps[x][y])
                return false;
        }
        //如果每一个maps都为true才执行消行处理
        return true;
    }

    //消行处理
    public void cleanLine(){
        for (int y=maps[0].length-1;y>0;y--)
        {
            //消行判断
            if (checkLine(y))
            {
                //执行消行
                deleteLine(y);
                //从消掉的那一行开始重新遍历
                y++;
            }
        }
    }
    /**
     * 执行消行
     * @param dy
     */
    public void deleteLine(int dy)
    {
        for (int y=maps[0].length-1;y>0;y--)
        {
            for (int x = 0;x<maps.length;x++)
            {
                maps[x][y] = maps[x][y-1];
            }
        }
    }
}
