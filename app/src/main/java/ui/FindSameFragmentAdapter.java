package ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tt.findyou.R;

import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.view.OtherMainActivity;
import limeng.com.findyou.view.TieziDetailActivity;
import model.pojo.Topic;
import model.pojo.UserInfo;
import utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class FindSameFragmentAdapter extends RecyclerView.Adapter {
    private List<Topic> list = new ArrayList<>();
    private Context context;
    private View view;
    private MyHolder holder;
    public FindSameFragmentAdapter(List<Topic> list,Context con){
        this.context = con;
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.findnewfragment_item_layout,null);
        holder = new MyHolder(view);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.pubdate);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.img1 = (ImageView) view.findViewById(R.id.img1);
        holder.img2 = (ImageView) view.findViewById(R.id.img2);
        holder.img3= (ImageView) view.findViewById(R.id.img3);
        holder.userName= (TextView) view.findViewById(R.id.username);
        holder.userImg = (ImageView) view.findViewById(R.id.user_img);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyHolder)holder).title.setText(list.get(position).getTitle());
        ((MyHolder)holder).date.setText(list.get(position).getFormatPubTime());
        ((MyHolder)holder).content.setText(list.get(position).getContent());
        Glide.with(context).load(list.get(position).getUserInfo().getHeadImage()).transform(new GlideCircleTransform(context)).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).userImg);
        ((MyHolder)holder).userName.setText(list.get(position).getUserInfo().getTruename());
        if(list.get(position).getImageList().size()==1){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
        }else if(list.get(position).getImageList().size()==2){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(list.get(position).getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
        }else if(list.get(position).getImageList().size()==3){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img3.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position).getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(list.get(position).getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
            Glide.with(context).load(list.get(position).getImageList().get(2).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img3);
        }else{
            ((MyHolder)holder).img1.setVisibility(View.GONE);
            ((MyHolder)holder).img2.setVisibility(View.GONE);
            ((MyHolder)holder).img3.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TieziDetailActivity.class);
                intent.putExtra("tid",list.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
        private TextView date,title,content,userName;
        private ImageView img1,img2,img3,userImg;
    }
}
