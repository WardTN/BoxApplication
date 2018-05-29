package com.example.chen.boxapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.chen.boxapplication.control.GameControl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    //游戏区控件
    View gamePanel;

    //游戏控制器
    GameControl gameControl;



    //
    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

           String type = (String) msg.obj;
           if (type ==null)
           {
               return;
           }
           if (type.equals("invalidate")) {
            //刷新重绘
               gamePanel.invalidate();
           }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameControl = new GameControl(handler,this);
        gameControl.initData(this);
        initView();
        gameControl.startGame();
        initListener();
//        Toast.makeText(MainActivity.this,"width is"+ xWidth,Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this,"y is"+ yHeight,Toast.LENGTH_LONG).show();


    }

    public void initListener(){
        findViewById(R.id.btnLeft).setOnClickListener(this);
        findViewById(R.id.btnTop).setOnClickListener(this);
        findViewById(R.id.btnDown).setOnClickListener(this);
        findViewById(R.id.btnRight).setOnClickListener(this);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
    }

    /**
     *初始化视图
     */
    public void initView(){

        //1、得到父容器
        FrameLayout layoutGame = findViewById(R.id.layoutGame);
        //2、实例化游戏区域
        gamePanel = new View(this){
            //重写游戏区域绘制

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
//                地图绘制
                gameControl.draw(canvas);
            }
        };

        //3、设置游戏区域大小
        gamePanel.setLayoutParams(new ViewGroup.LayoutParams(Config.XWIDTH,Config.YHEIGHT));
        //设置背景颜色
        gamePanel.setBackgroundColor(Color.WHITE);
        //4、添加到父容器中
        layoutGame.addView(gamePanel);

    }


    @Override
    public void onClick(View v) {
       gameControl.onClick(v.getId());
        //重绘view
        gamePanel.invalidate();
    }









}
