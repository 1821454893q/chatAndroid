package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.socket.Client;
import com.example.myapp.socket.i.CallClient;

public class Release extends AppCompatActivity {
    Button btn_submit, btn_cancel;
    TextView tx_biao, tx_miao;
    EditText edt_biao, edt_ping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        init();
    }

    public void init() {
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);
        tx_biao = findViewById(R.id.tx_biao);
        tx_miao = findViewById(R.id.tx_miao);
        edt_biao = findViewById(R.id.edt_biao);
        edt_ping = findViewById(R.id.edt_ping);

        btn_cancel.setText("取消");
        //取消
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Release.this, MainActivity.class);
                intent.putExtra("flag", 2);
                startActivity(intent);
                Release.this.finish();

            }
        });

        // 发布
        btn_submit.setOnClickListener(view -> {
            /**
             * 1.获取文本框内容
             * 2.将内容发送给服务器
             * 3.接收服务器的回应,判断是否上传成功
             */
            //1.获取文本框内容
            // 1.1读取控件内容信息
            String title = edt_biao.getText().toString();
            String context = edt_ping.getText().toString();
            if(!title.isEmpty() && !context.isEmpty()) {
                // 1.2 拼接字符串 组成服务器可以解析的字符串
                // "status:release,title:title,context:context"
                StringBuilder msg = new StringBuilder();
                msg.append("status:release,");
                msg.append("title:@title@").append(title).append("@title@");
                msg.append(",context:@context@").append(context).append("@context@");
                try {
                    // 2.将内容发送给服务器
                    Client.getClient().Send(msg.toString(), info -> {
                        // 3.接收服务器的回应,判断是否上传成功
                        Looper.prepare();
                        if (info.trim().equals("status:success")) {
                            Toast.makeText(Release.this, "发布成功!", Toast.LENGTH_SHORT).show();
                            btn_cancel.setText("返回");
                        }
                        else {
                            Toast.makeText(Release.this, info, Toast.LENGTH_SHORT).show();
                            Toast.makeText(Release.this, "发布失败!", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    });
                } catch (Exception e) {
                    Toast.makeText(Release.this, "连接超时，服务器未响应", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(Release.this, "发布内容不能为空!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}