package com.yihukurama.app.scanticketdemo.framework.v.showview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.iecheck.app.scanticketdemo.R;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

public class Detail_Activity extends AppCompatActivity {

    Wilddog be;
    Wilddog user1;
    Wilddog user2;

    TextView kaquan;
    TextView xinlian;
    TextView yizhi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Wilddog.setAndroidContext(this);
        be = new Wilddog("https://ticketonline.wilddogio.com/5230/be");
        user1 = new Wilddog("https://ticketonline.wilddogio.com/5230/user1");
        user2 = new Wilddog("https://ticketonline.wilddogio.com/5230/user2");

        kaquan = (TextView)findViewById(R.id.kaquan);
        xinlian = (TextView)findViewById(R.id.xinlian);
        yizhi = (TextView)findViewById(R.id.yizhi);


        be.addValueEventListener(val);
        user1.addValueEventListener(val);
        user2.addValueEventListener(val);

    }

    ValueEventListener val = new ValueEventListener(){
        public void onDataChange(DataSnapshot snapshot){
            if (snapshot.getKey().equals("user1")){
                yizhi.setText("易智收益："+snapshot.getValue().toString()+"元");
            }
            if(snapshot.getKey().equals("user2")){
                xinlian.setText("信联收益："+snapshot.getValue().toString()+"元");
            }
            if(snapshot.getKey().equals("be")){
                kaquan.setText("POS系统已兑换："+ snapshot.getValue().toString() +"张卡券");
            }
        }

        public void onCancelled(WilddogError error){
            if(error != null){
                System.out.println(error.getCode());
            }
        }

    };
}
