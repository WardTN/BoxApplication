package com.example.chen.boxapplication.control;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.chen.boxapplication.Config;
import com.example.chen.boxapplication.R;
import com.example.chen.boxapplication.model.BoxsModel;
import com.example.chen.boxapplication.model.MapModel;

/**
 * 游戏控制器
 */
public class GameControl {



    Handler handler;
    //暂停状态
    public boolean isPause;
    //游戏结束状态
    public boolean isOver;

    //地图模型
    MapModel mapModel;
    //方块模型
    BoxsModel boxsModel;
    //方块大小
    public int boxSize;



    //自动下落线程
    public Thread downThread;

    public GameControl(Handler handler,Context context) {
    this.handler = handler;
    initData(context);
    }


    /**
     * 初始化数据
     */
    public void initData(Context context){
        //获得屏幕宽度
        int width = getScreenWidth(context);
        //设置游戏区域宽度 = 屏幕宽的2/3
        Config.XWIDTH = width*2/3;
        //游戏区域的高度为宽度的两倍
        Config.YHEIGHT = Config.XWIDTH*2;
//        Toast.makeText(MainActivity.this,"width is"+ xWidth,Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this,"y is"+ yHeight,Toast.LENGTH_LONG).show();
        //初始化方块 = 游戏区域宽度/10
        boxSize = Config.XWIDTH/ Config.MAPX;
//        Toast.makeText(MainActivity.this,"boxSize = "+boxSize,Toast.LENGTH_LONG).show();
        //实例化地图模型
        mapModel = new MapModel(Config.XWIDTH,Config.YHEIGHT,boxSize);
        //实例化方块模型
        boxsModel = new BoxsModel(boxSize);

    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 开始游戏
     */
    public void  startGame(){
        if (downThread ==null)
        {
            downThread = new Thread()
            {
                @Override
                public void run() {
                    super.run();
                    while (true)
                    {
                        try {
                            sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isOver || isPause)
                            //继续循环
                            continue;
                        //执行一次下落
                        moveBottom();
//                      通知主线程刷新
                        Message msg = new Message();
                        msg.obj = "invalidate";
                        handler.sendMessage(msg);
                    }
                }
            };
            downThread.start();
        }
        //清除地图
        mapModel.cleanMaps();
        //生成新的方块
        boxsModel.newBoxs();
        //重置游戏状态
        isOver = false;
    }

    /**
     * 下落
     * @return
     */
    public boolean moveBottom(){
        //1、移动成功
        if (boxsModel.move(0,1,mapModel))
            return true;
        //2、移动失败，堆积处理
        for (int i=0;i<boxsModel.boxs.length;i++)
            mapModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y] = true;
        //3、消行处理
        mapModel.cleanLine();
        //4、生成新的方块
        boxsModel.newBoxs();
        //5、游戏结束判断
        isOver = checkOver();
        return false;
    }


    //结束判断
    public boolean checkOver(){
        for (int i=0;i<boxsModel.boxs.length;i++)
        {
            if (mapModel.maps[boxsModel.boxs[i].x][boxsModel.boxs[i].y])
            {

                return true;
            }
        }
        return false;
    }

    //控制绘制
    public void draw(Canvas canvas) {
        mapModel.drawMap(canvas);
        //辅助线绘制
        mapModel.drawLines(canvas);
        //方块绘制
        boxsModel.drawBoxs(canvas);
        //绘制状态
        mapModel.drawState(canvas,isPause,isOver);
    }

    public void onClick(int id)
    {
        switch (id)
        {
            case R.id.btnLeft:
                if (isPause)
                    return;
                boxsModel.move(-1,0,mapModel);
                break;
            case R.id.btnRight:
                if (isPause)
                    return;
                boxsModel.move(1,0,mapModel);
                break;
            case R.id.btnTop:
                if (isPause)
                    return;
                boxsModel.rotate(mapModel);
                break;
            case R.id.btnDown:
                if (isPause)
                    return;
                //快速下落
                while (true)
                {
                    //如果下落成功,继续执行下落
                    if (!moveBottom())
                    {
                        break;
                    }
                }
                break;
            case R.id.btnStart:
                mapModel.cleanMaps();
                startGame();
                break;
            case R.id.btnPause:
                isPause = !isPause;
                Log.e("TAG","ispause is "+isPause);
                break;
        }

    }
}
