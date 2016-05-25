package vn.itplus.quanlyxekhach.item;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AnhlaMrDuc on 18-May-16.
 */
public class Seat implements Parcelable {
    private int id;
    private int status;
    private Drawable bgColor;
    private int type;
    private long price;

    public Seat() {
    }

    public Seat(int id, int status, Drawable bgColor, int type, long price) {
        this.id = id;
        this.status = status;
        this.bgColor = bgColor;
        this.type = type;
        this.price = price;
    }

    protected Seat(Parcel in) {
        status = in.readInt();
        type = in.readInt();
        price = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(status);
        dest.writeInt(type);
        dest.writeLong(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Seat> CREATOR = new Creator<Seat>() {
        @Override
        public Seat createFromParcel(Parcel in) {
            return new Seat(in);
        }

        @Override
        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Drawable getBgColor() {
        return bgColor;
    }

    public void setBgColor(Drawable bgColor) {
        this.bgColor = bgColor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

}
