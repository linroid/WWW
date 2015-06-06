package io.hackathon.www;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linroid on 6/6/15.
 */
public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder>{
    List<AVObject> data;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AVObject item = data.get(position);

        holder.level.setText(String.valueOf(position+1));
        holder.count.setText(item.getInt("count")+"个包");
        final AVUser user = item.getAVObject("own");
        user.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                holder.username.setText(user.getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public void setData(List<AVObject> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.level)
        TextView level;
        @InjectView(R.id.count)
        TextView count;
        @InjectView(R.id.username)
        TextView username;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
