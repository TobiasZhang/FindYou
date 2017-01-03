package ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tt.findyou.R;

import java.util.List;

import model.pojo.Topic;

/**
 * Created by Administrator on 2016/12/11 0011.
 */
public class MyThemeAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<Topic> list;
    private View view;
    private MyHolder holder;
    public MyThemeAdapter(Context con,List<Topic> tList){
        this.context = con;
        this.list = tList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.mytheme_item_findnew,null);
                holder = new MyHolder(view);
                break;
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.mytheme_item_findold,null);
                holder = new MyHolder(view);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.sex = (TextView) view.findViewById(R.id.sex);
                break;
        }
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.pubdate);
        holder.content = (TextView) view.findViewById(R.id.content);
        holder.img1 = (ImageView) view.findViewById(R.id.img1);
        holder.img2 = (ImageView) view.findViewById(R.id.img2);
        holder.img3= (ImageView) view.findViewById(R.id.img3);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 1:
                break;
            case 0:
                ((MyHolder)holder).name.setText(list.get(position).getTargetTruename());
                ((MyHolder)holder).sex.setText(list.get(position).getTargetSex()==0?"男":"女");
                break;
        }
        ((MyHolder)holder).title.setText(list.get(position).getTitle());
        ((MyHolder)holder).date.setText(list.get(position).getFormatPubTime());
        ((MyHolder)holder).content.setText(list.get(position).getContent());
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
        }
    }
    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder {
        private TextView date,title,content,name,sex;
        private ImageView img1,img2,img3;
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
