package web.id.azammukhtar.forumkita.Network;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import web.id.azammukhtar.forumkita.Model.Comment.Comment;
import web.id.azammukhtar.forumkita.Model.Comment.DataComment;
import web.id.azammukhtar.forumkita.Model.Post.Post;
import web.id.azammukhtar.forumkita.Model.Response;
import web.id.azammukhtar.forumkita.Model.ResponseLogin;
import web.id.azammukhtar.forumkita.Model.User.User;

public interface ApiInterface {

    /*ACCOUNT*/

    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> login(@Field("email") String email,
                             @Field("password") String password);

    @Multipart
    @POST("register")
    Call<ResponseLogin> register(@Part("email") RequestBody email,
                        @Part("password") RequestBody password,
                        @Part("name") RequestBody name,
                        @Part MultipartBody.Part image);

    @GET("user")
    Call<User> getUser(@Header("Authorization") String token);

    /*POST*/

    @Multipart
    @POST("post")
    Call<Response> tambahPost(@Header("Authorization") String token,
                              @Part("judul") RequestBody judul,
                              @Part("isi") RequestBody isi,
                              @Part MultipartBody.Part image);

    @GET("post")
    Call<Post> getPost(@Header("Authorization") String token);

    @GET("post/user")
    Call<Post> getPostUser(@Header("Authorization") String token);

    @FormUrlEncoded
    @PUT("post/{id}")
    Call<Response> editPost(@Header("Authorization") String token,
                            @Path("id") Integer id,
                            @Field("judul") String judul,
                            @Field("isi") String isi);

    @DELETE("post/{id}")
    Call<Response> deletePost(@Header("Authorization") String token,
                              @Path("id") Integer id);

    /*COMMENT*/

    @GET("comment/{idPost}")
    Call<Comment> getComment(@Header("Authorization") String token,
                             @Path("idPost") Integer idPost);


    @FormUrlEncoded
    @POST("comment/{idPost}")
    Call<Response> tambahComment(@Header("Authorization") String token,
                                 @Path("idPost") Integer idPost,
                                 @Field("isi") String comment);


}
