package com.example.aidl.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Dto implements Parcelable {

    private String data1;


    protected Dto(Parcel in) {
        data1 = in.readString();
    }

    public Dto () {

    }

    public static final Creator<Dto> CREATOR = new Creator<Dto>() {
        @Override
        public Dto createFromParcel(Parcel in) {
            return new Dto(in);
        }

        @Override
        public Dto[] newArray(int size) {
            return new Dto[size];
        }
    };

    public String getData1 () {
        return data1;
    }
    public void setData1 (String data1){
        this.data1 = data1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(data1);
    }
}
