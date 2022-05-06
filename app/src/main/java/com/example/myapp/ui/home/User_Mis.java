package com.example.myapp.ui.home;

public class User_Mis {
    private String u_name = "";
    private String u_title = "";
    private String u_text = "";
    private String u_date = "";


    User_Mis(String name, String title, String text, String date)
    {
        this.u_name = name;
        this.u_text = text;
        this.u_title = title;
        this.u_date = date;
    }

    public String getU_name() {
        return u_name;
    }

    public String getU_title() {
        return u_title;
    }

    public String getU_text() {
        return u_text;
    }

    public String getU_date() {
        return u_date;
    }
}
