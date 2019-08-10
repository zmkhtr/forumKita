package web.id.azammukhtar.forumkita.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import web.id.azammukhtar.forumkita.Model.Post.DataPost;
import web.id.azammukhtar.forumkita.Model.Post.Post;
import web.id.azammukhtar.forumkita.R;

import static web.id.azammukhtar.forumkita.Network.ApiNetwork.BASE_URL;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<DataPost> dataPosts = new ArrayList<>();
    private Context context;
    private OnViewClick listener;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataPost dataPost = dataPosts.get(position);
        holder.textTitle.setText(dataPost.getJudul());
        String postedBy = "Posted By " + dataPost.getUserName();
        holder.textPostedBy.setText(postedBy);
        if (dataPost.getImage() != null){
            Glide.with(context)
                    .asBitmap()
                    .load(BASE_URL + "image/" + dataPost.getImageUser())
                    .into(holder.imageAvatar);
        }
        holder.imageAvatar.setBackgroundResource(R.drawable.ic_account);

    }

    @Override
    public int getItemCount() {
        return dataPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageAvatar;
        TextView textTitle, textPostedBy;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageListAvatar);
            textTitle = itemView.findViewById(R.id.textListTitle);
            textPostedBy = itemView.findViewById(R.id.textListPostedBy);
            itemView.setOnClickListener(view -> listener.onViewClick(getAdapterPosition()));
        }
    }

    public void setDataPosts(List<DataPost> dataPosts) {
        this.dataPosts = dataPosts;
        notifyDataSetChanged();
    }

    public void clearList(List<DataPost> dataPosts) {
        this.dataPosts = dataPosts;
        dataPosts.clear();
    }

    public interface OnViewClick {
        void onViewClick(int position);
    }

    public void setOnViewClickListener(OnViewClick listener) {
        this.listener = listener;
    }
}
