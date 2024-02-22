package com.example.mhike;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.Serializable;

public class Hike implements Serializable {
    private int id;
    private String name;
    private String position;
    private String date;
    private int parking;
    private double length;
    private String level;
    private String note;
    private String image;
    private int image_id;
    private String weather;

    private String observe;

    private String observe_date;

    private String note_other;

    public Hike() {
    }

    public Hike(int id, String name, String position, String date, int parking, double length, String level, String note, String image, int image_id, String weather, String observe, String observe_date, String note_other) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.level = level;
        this.note = note;
        this.image = image;
        this.image_id = image_id;
        this.weather = weather;
        this.observe = observe;
        this.observe_date = observe_date;
        this.note_other = note_other;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getParking() {
        return parking;
    }

    public void setParking(int parking) {
        this.parking = parking;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getObserve() {
        return observe;
    }

    public void setObserve(String observe) {
        this.observe = observe;
    }

    public String getObserve_date() {
        return observe_date;
    }

    public void setObserve_date(String observe_date) {
        this.observe_date = observe_date;
    }

    public String getNote_other() {
        return note_other;
    }

    public void setNote_other(String note_other) {
        this.note_other = note_other;
    }

    public boolean validate(Context context){
        if(TextUtils.isEmpty(this.name)){
            Toast.makeText(context, "Vui lòng nhập tên chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(this.position)){
            Toast.makeText(context, "Vui lòng nhập vị trí chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(this.date)){
            Toast.makeText(context, "Vui lòng nhập ngày chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(this.length == 0){
            Toast.makeText(context, "Vui lòng nhập chiều dài chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(this.observe)){
            Toast.makeText(context, "Vui lòng nhập quan sát trong chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(this.observe)){
            Toast.makeText(context, "Vui lòng nhập ngày quan sát trong chuyến đi", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
