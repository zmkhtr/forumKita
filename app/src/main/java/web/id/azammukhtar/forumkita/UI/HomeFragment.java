package web.id.azammukhtar.forumkita.UI;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import web.id.azammukhtar.forumkita.Adapter.RecyclerViewAdapter;
import web.id.azammukhtar.forumkita.Model.Post.DataPost;
import web.id.azammukhtar.forumkita.Model.Post.Post;
import web.id.azammukhtar.forumkita.Network.ApiInterface;
import web.id.azammukhtar.forumkita.Network.ApiNetwork;
import web.id.azammukhtar.forumkita.R;
import web.id.azammukhtar.forumkita.Utils.SessionManager;

import static web.id.azammukhtar.forumkita.UI.MainActivity.POST_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private static final String LIST_STATE_KEY = "LIST_KEY";
    private RecyclerViewAdapter recyclerViewAdapter;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private List<DataPost> dataPosts = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private Parcelable parcelableList;
    private ShimmerFrameLayout mShimmerViewContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        recyclerViewAdapter = new RecyclerViewAdapter(getContext());
        sessionManager = new SessionManager(getContext());
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerHome);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        recyclerViewAdapter.setOnViewClickListener(position -> {
            DataPost dataPost = dataPosts.get(position);
            Intent i = new Intent(getActivity(), DetailActivity.class);
            i.putExtra(POST_KEY, dataPost);
            startActivity(i);
        });
        getListData();
    }

    private void getListData() {
        mShimmerViewContainer.startShimmerAnimation();
        ApiInterface apiInterface;
        apiInterface = ApiNetwork.getClient().create(ApiInterface.class);
        Call<Post> call = apiInterface.getPost("Bearer " + sessionManager.getUserToken());
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d(TAG, "onResponse: " + response.code());
                List<DataPost> postList = response.body().getData();
                recyclerViewAdapter.setDataPosts(postList);
                dataPosts.addAll(postList);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        parcelableList = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, parcelableList);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null)
            parcelableList = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerViewAdapter.clearList(dataPosts);
        getListData();
        if (parcelableList != null)
            mLayoutManager.onRestoreInstanceState(parcelableList);
    }
}
