package web.id.azammukhtar.forumkita.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import web.id.azammukhtar.forumkita.Model.Comment.DataComment;
import web.id.azammukhtar.forumkita.Model.Post.DataPost;
import web.id.azammukhtar.forumkita.R;

import static web.id.azammukhtar.forumkita.Network.ApiNetwork.BASE_URL;

public class RecyclerViewCommentAdapter extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder> {

    private List<DataComment> dataComments = new ArrayList<>();
    private Context context;
    private OnViewClick listener;

    public RecyclerViewCommentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataComment dataComment = dataComments.get(position);
        holder.textTitle.setText(dataComment.getIsi());
//        String postedBy = "Comment By " + dataComment.getUserName();
        holder.textPostedBy.setText(dataComment.getUserName());
        if (dataComment.getImageUser() != null){
            Glide.with(context)
                    .asBitmap()
                    .load(BASE_URL + "image/" + dataComment.getImageUser())
                    .into(holder.imageAvatar);
        }
        holder.imageAvatar.setBackgroundResource(R.drawable.ic_account);

    }

    @Override
    public int getItemCount() {
        return dataComments.size();
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

    public void setDataComments(List<DataComment> dataComments) {
        this.dataComments = dataComments;
        notifyDataSetChanged();
    }

    public void clearList(List<DataComment> dataComments) {
        this.dataComments = dataComments;
        dataComments.clear();;
    }

    public interface OnViewClick {
        void onViewClick(int position);
    }

    public void setOnViewClickListener(OnViewClick listener) {
        this.listener = listener;
    }
}
