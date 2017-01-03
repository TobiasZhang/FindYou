package ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tt.findyou.R;

import java.util.ArrayList;
import java.util.List;

import limeng.com.findyou.view.MyselfinformationActivity;
import limeng.com.findyou.view.OtherMainActivity;
import limeng.com.findyou.view.TieziDetailActivity;
import model.pojo.Hobby;
import model.pojo.TuiJian;
import utils.GlideCircleTransform;
import utils.LoginUtils;
import widget.FlowLayout;
import widget.MyLinearLayout;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class RecyclerAdapeter extends RecyclerView.Adapter<RecyclerAdapeter.MyViewHolder>{
    private List<TuiJian> list = new ArrayList();
    private Context con;
    private View view ;

    public RecyclerAdapeter(Context con,List<TuiJian> list){
        this.con = con;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         MyViewHolder vh = null;
        switch (viewType){
            case 1: view = LayoutInflater.from(con).inflate(R.layout.recommand_type1,null);
                vh = new MyViewHolder(view);
                vh.name = (TextView) view.findViewById(R.id.name);
                vh.titles = (TextView) view.findViewById(R.id.titles);
                vh.sex = (TextView) view.findViewById(R.id.sex);
                vh.content = (TextView) view.findViewById(R.id.content);
                vh.pubdate = (TextView) view.findViewById(R.id.pubdate);
                vh.img1 = (ImageView) view.findViewById(R.id.img1);
                vh.img2 = (ImageView) view.findViewById(R.id.img2);
                vh.img3= (ImageView) view.findViewById(R.id.img3);
                break;
            case 2: view = LayoutInflater.from(con).inflate(R.layout.recommand_type2,null);
                    vh = new MyViewHolder(view);
                    vh.second_title = (TextView) view.findViewById(R.id.second_title);
                    vh.from = (TextView) view.findViewById(R.id.from);
                break;
            case 3: view = LayoutInflater.from(con).inflate(R.layout.recommand_type3,parent,false);
                    vh = new MyViewHolder(view);
                    vh.second_title = (TextView) view.findViewById(R.id.second_title);
                    vh.common_hobies = (FlowLayout) view.findViewById(R.id.MyStream);
                break;
        }
        vh.userImg = (ImageView) view.findViewById(R.id.userImg);
        vh.pubUser = (TextView) view.findViewById(R.id.pubUserName);
        vh.first_title = (TextView) view.findViewById(R.id.title);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder vh, final int position) {
        switch (list.get(position).getType()){
            case 1:((MyViewHolder)vh).first_title.setText(list.get(position).getSex().intValue()==0?"他可能在找你":"她可能在找你");
                ((MyViewHolder)vh).name.setText(list.get(position).getTargetTruename());
                ((MyViewHolder)vh).sex.setText(list.get(position).getTargetSex()==null ||list.get(position).getTargetSex().intValue()==0 ?"男":"女");
                ((MyViewHolder)vh).content.setText(list.get(position).getContent());
                ((MyViewHolder)vh).titles.setText(list.get(position).getTitle());
                if(list.get(position).getTopicImageList().size()==1){
                    ((MyViewHolder)vh).img1.setVisibility(View.VISIBLE);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img1);
                }else if(list.get(position).getTopicImageList().size()==2){
                    ((MyViewHolder)vh).img1.setVisibility(View.VISIBLE);
                    ((MyViewHolder)vh).img2.setVisibility(View.VISIBLE);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img1);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img2);
                }else if(list.get(position).getTopicImageList().size()==3){
                    ((MyViewHolder)vh).img1.setVisibility(View.VISIBLE);
                    ((MyViewHolder)vh).img2.setVisibility(View.VISIBLE);
                    ((MyViewHolder)vh).img3.setVisibility(View.VISIBLE);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(0).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img1);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(1).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img2);
                    Glide.with(con).load(list.get(position).getTopicImageList().get(2).getUrl()).error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(((MyViewHolder)vh).img3);
                }else{
                    ((MyViewHolder)vh).img1.setVisibility(View.GONE);
                    ((MyViewHolder)vh).img2.setVisibility(View.GONE);
                    ((MyViewHolder)vh).img3.setVisibility(View.GONE);
                }
                ((MyViewHolder)vh).pubdate.setText(list.get(position).getFormatPubTime());
                break;
            case 2:
                String expType = null;
                int expTypeId = list.get(position).getExperience().getPlaceType().getId();
                if(expTypeId<11){
                    expType = "同学";
                }else if(expTypeId==11){
                    expType = "战友";
                }else if(expTypeId==12){
                    expType = "同事";
                }
                ((MyViewHolder)vh).first_title.setText("你们可能是"+expType);
                ((MyViewHolder)vh).from.setText(list.get(position).getExperience().getName());
                ((MyViewHolder)vh).second_title.setText("你们来自同一"+list.get(position).getExperience().getPlaceType().getName());
                break;
            case 3:((MyViewHolder)vh).first_title.setText(list.get(position).getSex().intValue()==0?"也许你想认识他":"也许你想认识她");
                vh.common_hobies.removeAllViews();
                int size = list.get(position).getHobbyList().size();
                for(Hobby h:list.get(position).getHobbyList()){
                    int tagColor=0;
                    Drawable tagBgDrawable=null;
                    switch (h.getName()){
                        case "吃货":
                            tagColor = con.getResources().getColor(R.color.colorEating_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.eat_shape);
                            break;
                        case "电影":
                            tagColor = con.getResources().getColor(R.color.colorMovie_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.televation_shape);
                            break;
                        case "户外运动":
                            tagColor = con.getResources().getColor(R.color.colorSport_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.sport_textshape);
                            break;
                        case "书籍":
                            tagColor = con.getResources().getColor(R.color.colorReading_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.book_shape);
                            break;
                        case "电子游戏":
                            tagColor = con.getResources().getColor(R.color.colorTravaling_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.travl_shape);
                            break;
                        case "音乐":
                            tagColor = con.getResources().getColor(R.color.colorMusic_TX);
                            tagBgDrawable = con.getResources().getDrawable(R.drawable.music_shape);
                            break;
                    }
                    for(Hobby h2:h.getSubList()){
                        TextView tx = new TextView(con);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        tx.setText(h2.getName());
                        tx.setTextColor(tagColor);
                        tx.setBackgroundDrawable(tagBgDrawable);
                        params.setMargins(0,5,5,5);
                        vh.common_hobies.addView(tx);
                    }
                }
                break;
        }
        Glide.with(con).load(list.get(position).getHeadImage()).placeholder(R.mipmap.ic_launcher).transform(new GlideCircleTransform(con)).into(((MyViewHolder)vh).userImg);
        ((MyViewHolder)vh).pubUser.setText(list.get(position).getTruename());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (list.get(position).getType()){
                    case 1:Intent intents = new Intent(con,TieziDetailActivity.class);
                        intents.putExtra("tid",list.get(position).getId());
                        Log.e("ididdidididi",list.get(position).getId()+"");
                        con.startActivity(intents);break;
                    case 2:
                    case 3:
                        if(list.get(position).getId()!=null){
                            if(list.get(position).getId()== LoginUtils.userInfo.getId()){
                                Intent intent = new Intent(con,MyselfinformationActivity.class);
                                con.startActivity(intent);break;
                            }else{
                                Intent intent = new Intent(con,OtherMainActivity.class);
                                intent.putExtra("uid",list.get(position).getId());
                                con.startActivity(intent);break;
                            }
                        }

                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView name,sex,age,key,pubdate,content,pubUser,titles,first_title,second_title,from;
        private ImageView userImg,img1,img2,img3;
        private FlowLayout common_hobies;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
