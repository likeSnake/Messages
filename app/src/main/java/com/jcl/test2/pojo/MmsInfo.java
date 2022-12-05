package com.jcl.test2.pojo;

import android.graphics.Bitmap;

public class MmsInfo {
    private int id;
    private int time;
    private String body;
    private Bitmap bitmap;
    private int type;
    private int thread_id;
    public MmsInfo(int time, String body, Bitmap bitmap,int thread_id,int type,int id) {
        this.time = time;
        this.body = body;
        this.bitmap = bitmap;
        this.thread_id = thread_id;
        this.type = type;
        this.id = id;

    }

    public int getThread_id(){
        return thread_id;
    }
    public int getType(){
        return type;
    }
    public int getId(){
        return id;
    }
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
