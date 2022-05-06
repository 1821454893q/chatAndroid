package com.example.myapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.widget.*;

import com.example.myapp.socket.Client;
import com.example.myapp.socket.i.CallClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class login extends AppCompatActivity {

    public int ifontable = -1;
    public String getuser;
    public String getpasswd;

    public Socket sk;
    OutputStream ops = null;
    InputStream ips = null;

    EditText edt_louser, edt_lopwd;
    Button bt_login, bt_loreg;
    Resources text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edt_louser = findViewById(R.id.edt_louser);
        edt_lopwd = findViewById(R.id.edt_lopwd);
        bt_login = findViewById(R.id.bt_login);
        bt_loreg = findViewById(R.id.bt_loreg);
        edt_louser = findViewById(R.id.edt_louser);
        edt_lopwd = findViewById(R.id.edt_lopwd);



        //注册
        bt_loreg.setOnClickListener(view -> {
            Intent mIntent = new Intent(login.this, register.class);
            startActivity(mIntent);
        });

        //登录
        bt_login.setOnClickListener(view -> {
            getuser = edt_louser.getText().toString();
            getpasswd = edt_lopwd.getText().toString();
            if (getuser.length() > 0 && getpasswd.length() > 0) {
                StringBuilder msg = new StringBuilder();
                msg.append("status:login,account:").append(getuser).append(",passwd:").append(getpasswd);
                Toast.makeText(login.this, "登录中... ", Toast.LENGTH_SHORT).show();
                try {
                    Client.getClient().Send(msg.toString(), new CallClient() {
                        @Override
                        public void Receive(String info) {
                            Looper.prepare();

                            if (info.trim().equals("status:success")) {
                                //Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent nIntent = new Intent(login.this, MainActivity.class);
                                startActivity(nIntent);
                                login.this.finish(); //结束当前activity
                            } else if(info.trim().equals("status:fail")){
                                Toast.makeText(login.this, "登录失败,用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }
                            else if(ErrorPD(info).equals("4031"))
                            {
                                Toast.makeText(login.this, "服务器未响应！", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(login.this, info, Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(login.this, "连接超时，服务器未响应", Toast.LENGTH_SHORT).show();
                }
            }
            else if(getuser.equals("root"))//调试快捷登陆
            {
                Intent nIntent = new Intent(login.this, MainActivity.class);
                startActivity(nIntent);
                login.this.finish(); //结束当前activity
            }
            else {
                Toast.makeText(login.this, "登录失败，账号或密码为空", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public String ErrorPD(String src)
    {
        String intrun = "";
        intrun = src.substring(src.indexOf("errno:") + 6,src.length());
        return intrun;

    }

}