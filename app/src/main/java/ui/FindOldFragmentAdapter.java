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
import com.tt.findyou.utils.TimeUtils;

import java.util.List;

import limeng.com.findyou.view.TieziDetailActivity;
import model.pojo.Topic;
import model.pojo.UserInfo;
import utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class FindOldFragmentAdapter extends RecyclerView.Adapter<FindOldFragmentAdapter.MyHolder> {
    private List<Topic> list;
    private Context context;
    private View view;
    private MyHolder holder;
    public FindOldFragmentAdapter(List<Topic> tlist,Context con){
        this.list = tlist;
        this.context = con;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.findoldfragment_item_layout,null);
        holder = new MyHolder(view);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.sex = (TextView) view.findViewById(R.id.sex);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.pubdate);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.img1 = (ImageView) view.findViewById(R.id.img1);
        holder.img2 = (ImageView) view.findViewById(R.id.img2);
        holder.img3= (ImageView) view.findViewById(R.id.img3);
        holder.userImg = (ImageView) view.findViewById(R.id.user_img);
        holder.userName = (TextView) view.findViewById(R.id.username);
        
        holder.hostSex = (ImageView) view.findViewById(R.id.hostSex);
        holder.hostAge = (TextView) view.findViewById(R.id.hostAge);
        holder.district = (TextView) view.findViewById(R.id.district);
        return holder;
    }
    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        final Topic topic = list.get(position);
        if(topic.getTargetTruename()!=null){
            ((MyHolder)holder).name.setText(topic.getTargetTruename());
        }
        if(topic.getTargetSex()!=null){
            ((MyHolder)holder).sex.setText(topic.getTargetSex()==0?"男":"女");
        }
        if(topic.getTitle()!=null){
            ((MyHolder)holder).title.setText(topic.getTitle());
        }
        if(topic.getPubTime()!=null){
            ((MyHolder)holder).date.setText(topic.getFormatPubTime());
        }
        if(topic.getContent()!=null){
            ((MyHolder)holder).content.setText(topic.getContent());
        }
        if(topic.getUserInfo().getTruename()!=null){
            ((MyHolder)holder).userName.setText(topic.getUserInfo().getTruename());
        }
        if(topic.getUserInfo().getHeadImage()!=null){
            Glide.with(context).load(topic.getUserInfo().getHeadImage()).error(R.mipmap.ic_launcher).transform(new GlideCircleTransform(context)).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).userImg);
        }
        if(topic.getImageList().size()==0){
            holder.img1.setVisibility(View.GONE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
        }
        if(topic.getImageList().size()==1){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            Glide.with(context).load(topic.getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
        }else if(topic.getImageList().size()==2){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            holder.img3.setVisibility(View.GONE);
            Glide.with(context).load(topic.getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(topic.getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
        }else if(topic.getImageList().size()==3){
            ((MyHolder)holder).img1.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img2.setVisibility(View.VISIBLE);
            ((MyHolder)holder).img3.setVisibility(View.VISIBLE);
            Glide.with(context).load(topic.getImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img1);
            Glide.with(context).load(topic.getImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img2);
            Glide.with(context).load(topic.getImageList().get(2).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyHolder)holder).img3);
        }
        UserInfo u = topic.getUserInfo();
        int hostSex = u.getSex();
        holder.hostSex.setImageResource(hostSex==0?R.drawable.profile_sex_male :R.drawable.profile_sex_female);
        holder.hostAge.setText(TimeUtils.parseAge(topic.getUserInfo().getBirth())+"");
        holder.hostAge.setTextColor(hostSex==0?context.getResources().getColor(R.color.profile_sex_male):context.getResources().getColor(R.color.profile_sex_female));
        holder.district.setText(u.getDistrict().getDistrict().getName()+" "+u.getDistrict().getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TieziDetailActivity.class);
                intent.putExtra("tid",topic.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        public MyHolder(View itemView) {
            super(itemView);
        }
        private TextView date,title,content,name,sex,userName;
        private ImageView img1,img2,img3,userImg;
        
        private ImageView hostSex;
        private TextView hostAge,district;
    }
}
