package com.yihukurama.app.scanticketdemo.framework.v.showview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.iecheck.app.scanticketdemo.R;
import com.wilddog.client.Wilddog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    Button button1;
    Button scanButton;
    Button yunduanscanButton;
    Button yunduanex;
    Wilddog refTicket1;
    Wilddog refTicket2;
    Wilddog refTicket3;
    Wilddog refTicket4;
    Wilddog refTicket;

    Wilddog be;
    Wilddog user1;
    Wilddog user2;

    int i = 0;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wilddog.setAndroidContext(this);
        refTicket1 = new Wilddog("https://ticketonline.wilddogio.com/5191/ticket1");
        refTicket2 = new Wilddog("https://ticketonline.wilddogio.com/5191/ticket2");
        refTicket3 = new Wilddog("https://ticketonline.wilddogio.com/5191/ticket3");
        refTicket4 = new Wilddog("https://ticketonline.wilddogio.com/5191/ticket4");
        refTicket = new Wilddog("https://ticketonline.wilddogio.com/5191/ticket");

        be = new Wilddog("https://ticketonline.wilddogio.com/5230/be");
        user1 = new Wilddog("https://ticketonline.wilddogio.com/5230/user1");
        user2 = new Wilddog("https://ticketonline.wilddogio.com/5230/user2");

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(this);
        button1 = (Button)findViewById(R.id.button2);
        button1.setOnClickListener(this);
        scanButton = (Button)findViewById(R.id.scan);
        scanButton.setOnClickListener(this);
        yunduanscanButton = (Button)findViewById(R.id.yunduanscan);
        yunduanscanButton.setOnClickListener(this);
        yunduanex = (Button)findViewById(R.id.yunduanexchange);
        yunduanex.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                refTicket1.setValue("500g恰恰香瓜子一包");
                refTicket2.setValue("250ml江小白");
                refTicket3.setValue("旅游大礼包");
                refTicket4.setValue("再来一包");
                refTicket.setValue("再来一瓶");
                be.setValue("0");
                user1.setValue("0");
                user2.setValue("0");
                Toast.makeText(this, "模拟数据重置成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:
                intent = new Intent(this,Detail_Activity.class);
                startActivity(intent);

                break;
            case R.id.scan:
                intent = new Intent(this,CuanHuoCaptureActivity.class);
                startActivity(intent);
                break;

            case R.id.yunduanscan:
                intent = new Intent(this,YunduanCaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.yunduanexchange:
                intent = new Intent(this,YunduanExchangeCaptureActivity.class);
                startActivity(intent);
                break;
        }

    }




}
