package cn.zju.id21632120.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.zju.id21632120.R;

/**
 * Created by Wangli on 2017/6/18.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private List<Tweet> tweets;

    public MyAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int i) {
        return tweets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int[] userImages = new int[] { R.drawable.tweet1, R.drawable.tweet2, R.drawable.tweet3,
            R.drawable.tweet4, R.drawable.tweet5};

    public int[] messageColors = new int[] { R.drawable.tv_radius_bg, R.drawable.tv_radius_bg2,
            R.drawable.tv_radius_bg3, R.drawable.tv_radius_bg4, R.drawable.tv_radius_bg5 };


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_tweet, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUser = (TextView) convertView.findViewById(R.id.tv_user);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.tvCreateTime = (TextView) convertView.findViewById(R.id.create_time);
            viewHolder.imUser = (ImageView) convertView.findViewById(R.id.im_user);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setId(Integer.parseInt(tweets.get(position).getID()));
        viewHolder.tvUser.setText(tweets.get(position).getUser());
        viewHolder.tvMessage.setText(tweets.get(position).getMessage());
        CharSequence time = DateUtils.getRelativeTimeSpanString(Long.parseLong(tweets.get(position).getCreateTime()));
        viewHolder.tvCreateTime.setText(time);
        viewHolder.imUser.setImageResource(userImages[position % 5]);
        viewHolder.tvMessage.setBackgroundResource(messageColors[position % 5]);

        convertView.setTag(viewHolder);

        return convertView;
    }

    public static class ViewHolder {

        public void setId(int id) {
            this.id = id;
        }

        public int id;

        public TextView tvUser;
        public TextView tvMessage;
        public TextView tvCreateTime;
        public ImageView imUser;

    }
}
