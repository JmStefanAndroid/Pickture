package me.stefan.pickturelib.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件对象
 */
public class PicFolder implements Parcelable {
    public String id;
    public String name;
    public String cover;
    public String path;
    public List<Pic> pics = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PicFolder)) return false;

        PicFolder directory = (PicFolder) o;

        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);

        if (hasId && otherHasId) {
            if (!TextUtils.equals(id, directory.id)) {
                return false;
            }

            return TextUtils.equals(name, directory.name);
        }

        return false;
    }

    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(pics.size());
        for (Pic photo : pics) {
            paths.add(photo.getPath());
        }
        return paths;
    }

    public void addPhoto(int id, String path, String name) {
        pics.add(new Pic(path, name, id));
    }

    public void addPhoto(Pic pic) {
        if (pic != null)
            pics.add(pic);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(cover);
        dest.writeString(path);
        dest.writeTypedList(pics);
    }


    // 用来创建自定义的Parcelable的对象
    public static final Parcelable.Creator<PicFolder> CREATOR
            = new Parcelable.Creator<PicFolder>() {
        public PicFolder createFromParcel(Parcel in) {
            return new PicFolder(in);
        }

        public PicFolder[] newArray(int size) {
            return new PicFolder[size];
        }
    };

    // 读数据进行恢复
    private PicFolder(Parcel in) {
        id = in.readString();
        name = in.readString();
        cover = in.readString();
        path = in.readString();
        in.readTypedList(pics, Pic.CREATOR);
    }

    public PicFolder() {
    }
}
