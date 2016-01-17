package com.angcyo.sample.debug;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by angcyo on 16-01-17-017.
 */
public class BookBean implements Parcelable {
    public int bookId;
    public String bookName;
    public List<String> bookDatas;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.bookId);
        dest.writeString(this.bookName);
        dest.writeStringList(this.bookDatas);
    }

    public BookBean() {
    }

    protected BookBean(Parcel in) {
        this.bookId = in.readInt();
        this.bookName = in.readString();
        this.bookDatas = in.createStringArrayList();
    }

    public static final Creator<BookBean> CREATOR = new Creator<BookBean>() {
        public BookBean createFromParcel(Parcel source) {
            return new BookBean(source);
        }

        public BookBean[] newArray(int size) {
            return new BookBean[size];
        }
    };
}
