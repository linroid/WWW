package io.hackathon.www;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by linroid on 6/6/15.
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>{
    List<AVObject> data;
    Picasso picasso;
    Resources res;
    public TimelineAdapter(Context context) {
        picasso = new Picasso.Builder(context)
                .build();
        res = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AVObject item = data.get(position);

        holder.content.setText(item.getString("content"));
        holder.laughCountBtn.setText(res.getString(R.string.laugh_count, item.getInt("laugh_count")));
        holder.sympathyCountBtn.setText(res.getString(R.string.laugh_count, item.getInt("sympathy_count")));
        final AVUser user = item.getAVObject("own");
        user.fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                holder.username.setText(user.getUsername());
                picasso.load(avObject.getString("avatar")).placeholder(R.mipmap.ic_launcher).into(holder.avatar);
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
        @InjectView(R.id.username)
        TextView username;
        @InjectView(R.id.content)
        TextView content;
        @InjectView(R.id.avatar)
        ImageView avatar;
        @InjectView(R.id.btn_laugh_count)
        Button laughCountBtn;
        @InjectView(R.id.btn_sympathy_count)
        Button sympathyCountBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            laughCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getLayoutPosition();
                    final AVObject item = data.get(position);
                    item.increment("laugh_count");
                    item.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null) {
//                                item.put("laugh_count", item.getInt("laugh_count"));
                                notifyItemChanged(position);
                            }
                        }
                    });
                }
            });
            sympathyCountBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final  int position = getLayoutPosition();
                    AVObject item = data.get(position);
                    item.increment("sympathy_count");
                    item.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
//                                item.put("sympathy_count", item.getInt("sympathy_count"));
                                notifyItemChanged(position);
                            }
                        }
                    });
                }
            });
        }
    }
}
