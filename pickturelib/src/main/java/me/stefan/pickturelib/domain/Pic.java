package me.stefan.pickturelib.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 照片实体类
 */
public class Pic implements Parcelable {

    private String path;

    private String name;

    private int uid;


    public Pic(String path, String name, int uid) {
        this.path = path;
        this.name = name;
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeInt(uid);
    }

    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<Pic> CREATOR
            = new Parcelable.Creator<Pic>() {
        public Pic createFromParcel(Parcel in) {
            return new Pic(in);
        }

        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };

    // 读数据进行恢复
    private Pic(Parcel in) {
        path = in.readString();
        name = in.readString();
        uid = in.readInt();
    }


    @Override
    public boolean equals(Object o) {
        Pic pic= (Pic) o;
        if(pic.getUid()==getUid()&&pic.getPath().equals(pic.getPath())&&pic.getName().equals(pic.getName()))return  true;
        return false;
    }
}
