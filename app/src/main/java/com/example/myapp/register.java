package com.example.myapp;


import static java.lang.Thread.*;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.*;

import com.example.myapp.socket.Client;
import com.example.myapp.socket.i.CallClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;


public class register extends AppCompatActivity {
    EditText edt_reuser, edt_repwd;
    Button bt_relog, bt_return;

    public int state = -1;

    public String edtreuser;
    public String edtrepwd;

    Socket sk;
    OutputStream ops = null;
    InputStream ips = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏头部
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init() {
        edt_reuser = findViewById(R.id.edt_reuser);
        edt_repwd = findViewById(R.id.edt_repwd);
        bt_relog = findViewById(R.id.bt_relog);
        bt_return = findViewById(R.id.bt_return);

        edtreuser = edt_reuser.getText().toString();
        edtrepwd = edt_repwd.getText().toString();

        //返回登录界面
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(register.this, login.class);
                startActivity(mIntent);
                com.example.myapp.register.this.finish(); //结束当前activity
            }
        });

        //注册按钮
        bt_relog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtreuser = edt_reuser.getText().toString();
                edtrepwd = edt_repwd.getText().toString();
                if (edtreuser.length() > 10 || edtrepwd.length() > 8 || edtreuser.length() < 5 || edtrepwd.length() <= 3) {
                    Toast.makeText(register.this, "无法注册：用户名或密码格式不正确", Toast.LENGTH_SHORT).show();
                } else {
//                    zc_init ct = new zc_init();
//                    ct.start();
                    String sendtext = "status:register,account:" + edtreuser + ",passwd:" + edtrepwd;
                    try {
                        Looper.prepare();
                        Client.getClient().Send(sendtext, new CallClient() {
                            @Override
                            public void Receive(String info) {
                                if (info.trim().equals("status:success")) {
                                    Toast.makeText(register.this, "注册成功，请返回登录", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(register.this, "注册失败,用户名已被注册", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Looper.loop();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(register.this, "连接超时，服务器未响应", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    class zc_init extends Thread {
        @Override
        public void run() {
            //super.run();

            String sendtext = "status:register,account:" + edtreuser + ",passwd:" + edtrepwd + " ";
            byte[] readbuffer = new byte[2048];
            int readlength = 0;
            //int ontime = Geintime();
            try {
                Looper.prepare();
                sk = new Socket("175.178.217.212", 12345);
                ops = sk.getOutputStream();
                ips = sk.getInputStream();
                //Toast.makeText(register.this, sendtext, Toast.LENGTH_SHORT).show();
                ops.write(sendtext.getBytes());
                while (true) {
                    readlength = ips.read(readbuffer);
                    if (readlength < 0) {
                        Toast.makeText(register.this, "无法获取数据", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (readlength > 0) {
                        String text = new String(readbuffer).trim();
                        if (text.equals("status:success")) {
                            Toast.makeText(register.this, "注册成功，请返回登录", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(register.this, "注册失败,用户名已被注册", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    Toast.makeText(register.this, "注册失败！无法连接", Toast.LENGTH_SHORT).show();
                    break;
                }
                sk.close();
                Looper.loop();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(register.this, "连接超时，服务器未响应", Toast.LENGTH_SHORT).show();
            }
        }
    }

        public int Geintime() {
            SimpleDateFormat spd = new SimpleDateFormat("ss");
            Date dt = new Date(System.currentTimeMillis());
            String str = spd.format(dt);
            return Integer.parseInt(str);
        }

}