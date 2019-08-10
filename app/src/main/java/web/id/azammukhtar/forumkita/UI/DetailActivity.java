    package web.id.azammukhtar.forumkita.UI;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.content.Intent;
    import android.os.Bundle;
    import android.os.Parcelable;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.request.RequestOptions;
    import com.facebook.shimmer.ShimmerFrameLayout;

    import java.util.ArrayList;
    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import web.id.azammukhtar.forumkita.Adapter.RecyclerViewCommentAdapter;
    import web.id.azammukhtar.forumkita.Model.Comment.Comment;
    import web.id.azammukhtar.forumkita.Model.Comment.DataComment;
    import web.id.azammukhtar.forumkita.Model.Post.DataPost;
    import web.id.azammukhtar.forumkita.Model.Response;
    import web.id.azammukhtar.forumkita.Model.User.DataUser;
    import web.id.azammukhtar.forumkita.Model.User.User;
    import web.id.azammukhtar.forumkita.Network.ApiInterface;
    import web.id.azammukhtar.forumkita.Network.ApiNetwork;
    import web.id.azammukhtar.forumkita.R;
    import web.id.azammukhtar.forumkita.Utils.SessionManager;

    import static web.id.azammukhtar.forumkita.Network.ApiNetwork.BASE_URL;
    import static web.id.azammukhtar.forumkita.UI.MainActivity.POST_KEY;

    public class DetailActivity extends AppCompatActivity {
        private static final String TAG = "DetailActivity";
        private static final String LIST_STATE_KEY = "LIST_KEY";
        private TextView textJudul, textPostedBy, textDeskripsi;
        private ImageView imageAvatar, imageThread;
        private EditText etComment;
        private Button btnSendComment;
        private SessionManager sessionManager;
        private DataPost dataPost;
        private List<DataComment> dataComments = new ArrayList<>();
        private ApiInterface apiInterface;
        private RecyclerView recyclerView;
        private ShimmerFrameLayout mShimmerViewContainer;
        private RecyclerViewCommentAdapter recyclerViewCommentAdapter;
        private LinearLayoutManager mLayoutManager;
        private Parcelable parcelableList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);

            getSupportActionBar().setTitle("Thread Detail");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setId();
            setData();
            setOnClick();
            initRecycler();
        }

        private void setId() {
            textJudul = findViewById(R.id.textDetailTitle);
            textPostedBy = findViewById(R.id.textDetailPostedBy);
            textDeskripsi = findViewById(R.id.textDetailDescription);
            imageAvatar = findViewById(R.id.imageDetailAvatar);
            imageThread = findViewById(R.id.imageDetailImage);
            etComment = findViewById(R.id.edtDetailAddComment);
            btnSendComment = findViewById(R.id.buttonDetailSendComment);
            sessionManager = new SessionManager(this);
            apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        }

        private void initRecycler(){
            recyclerViewCommentAdapter = new RecyclerViewCommentAdapter(this);
            mLayoutManager = new LinearLayoutManager(this);
            recyclerView = findViewById(R.id.recyclerComment);
            recyclerView.setAdapter(recyclerViewCommentAdapter);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
            recyclerViewCommentAdapter.setOnViewClickListener(position -> {
                Log.d(TAG, "initRecycler: " + position);
            });
        }

        private void setData() {
            dataPost = getIntent().getParcelableExtra(POST_KEY);
            String postedBy = "Posted By " + dataPost.getUserName();

            textJudul.setText(dataPost.getJudul());
            textPostedBy.setText(postedBy);
            textDeskripsi.setText(dataPost.getIsi());

            if (dataPost.getImageUser() != null || dataPost.getImage() != null) {
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .apply(new RequestOptions().override(300, 300))
                        .load(BASE_URL + "image/" + dataPost.getImage())
                        .into(imageThread);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(BASE_URL + "image/" + dataPost.getImageUser())
                        .into(imageAvatar);
            }
            imageAvatar.setBackgroundResource(R.drawable.ic_account);
        }

        private void setOnClick() {
            btnSendComment.setOnClickListener(view -> {
                String comment = etComment.getText().toString();
                if (comment.isEmpty()) {
                    Toast.makeText(DetailActivity.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                } else{
                    sendComment(comment);
                }
            });

        }

        private void sendComment(String comment){
            Call<Response> call = apiInterface.tambahComment("Bearer " + sessionManager.getUserToken(),dataPost.getId(), comment);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Log.d(TAG, "onResponse: success " + response);
                    Toast.makeText(DetailActivity.this, "Comment Success", Toast.LENGTH_SHORT).show();
                    recyclerViewCommentAdapter.clearList(dataComments);
                    readComment();
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    Toast.makeText(DetailActivity.this, "Comment Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void readComment(){
            mShimmerViewContainer.startShimmerAnimation();
            apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
            Call<Comment> call = apiInterface.getComment("Bearer " + sessionManager.getUserToken(),dataPost.getId());
            call.enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, retrofit2.Response<Comment> response) {
                    List<DataComment> dataComments = response.body().getData();
                    recyclerViewCommentAdapter.setDataComments(dataComments);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Log.e(TAG, "onFailure: ", t);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                }
            });

        }

        @Override
        protected void onStart() {
            super.onStart();
            readComment();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            parcelableList = mLayoutManager.onSaveInstanceState();
            outState.putParcelable(LIST_STATE_KEY, parcelableList);
        }


        @Override
        protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            if (savedInstanceState != null)
                parcelableList = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

        @Override
        public void onResume() {
            super.onResume();
            if (parcelableList != null)
                mLayoutManager.onRestoreInstanceState(parcelableList);
        }
    }
