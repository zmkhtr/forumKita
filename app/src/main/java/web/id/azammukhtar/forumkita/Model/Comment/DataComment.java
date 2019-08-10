package web.id.azammukhtar.forumkita.Model.Comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataComment {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("isi")
    @Expose
    private String isi;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("image_user")
    @Expose
    private String imageUser;
    @SerializedName("id_post")
    @Expose
    private String idPost;
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

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
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

}