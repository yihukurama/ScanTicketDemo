package com.yihukurama.app.scanticketdemo.framework.v.showview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iecheck.app.scanticketdemo.R;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    Intent intent;
    String resultString;
    Button scanBtn;
    Button backBtn;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        intent = getIntent();
        resultString = intent.getStringExtra("DATA");

        scanBtn = (Button)findViewById(R.id.scan);
        backBtn = (Button)findViewById(R.id.back);
        scanBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.result_id);
        textView.setText("已成功兑换\n"+resultString);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.scan:

                intent = new Intent(this,
                        CuanHuoCaptureActivity.class);
                startActivity(intent);
                finish();

                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
}
