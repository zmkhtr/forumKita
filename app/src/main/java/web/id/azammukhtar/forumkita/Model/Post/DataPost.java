package web.id.azammukhtar.forumkita.Model.Post;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPost implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("isi")
    @Expose
    private String isi;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_user")
    @Expose
    private String imageUser;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.judul);
        dest.writeString(this.isi);
        dest.writeString(this.image);
        dest.writeString(this.imageUser);
        dest.writeString(this.idUser);
        dest.writeString(this.userName);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public DataPost() {
    }

    protected DataPost(Parcel in) {
        this.id = in.readInt();
        this.judul = in.readString();
        this.isi = in.readString();
        this.image = in.readString();
        this.imageUser = in.readString();
        this.idUser = in.readString();
        this.userName = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<DataPost> CREATOR = new Parcelable.Creator<DataPost>() {
        @Override
        public DataPost createFromParcel(Parcel source) {
            return new DataPost(source);
        }

        @Override
        public DataPost[] newArray(int size) {
            return new DataPost[size];
        }
    };
}