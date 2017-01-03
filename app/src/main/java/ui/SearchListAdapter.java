package ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tt.findyou.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import limeng.com.findyou.view.OtherMainActivity;
import model.pojo.UserInfo;
import utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/12/17 0017.
 */
public class SearchListAdapter extends RecyclerView.Adapter {
    private Context con;
    int age;
    private List<UserInfo> ulist;
    private View view;
    private Date d;
    private SearchHolder searchHolder;
    public SearchListAdapter(Context con,List<UserInfo> uList){
        this.con = con;
        this.ulist = uList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(con).inflate(R.layout.search_layout_item,null);
        searchHolder = new SearchHolder(view);
        searchHolder.age = (TextView) view.findViewById(R.id.age);
        searchHolder.from = (TextView) view.findViewById(R.id.place);
        searchHolder.name = (TextView) view.findViewById(R.id.name);
        searchHolder.userImg = (ImageView) view.findViewById(R.id.userimg);
        searchHolder.sex = (ImageView) view.findViewById(R.id.sex);
        return searchHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
          d  = new SimpleDateFormat("yyyy-MM-dd").parse(ulist.get(position).getBirth());
          int year = d.getYear();
          Date now = new Date();
           age = now.getYear()-year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((SearchHolder)holder).name.setText(ulist.get(position).getTruename());
        ((SearchHolder)holder).age.setText(age+"");
        ((SearchHolder)holder).from.setText(ulist.get(position).getDistrict().getDistrict().getName());
        Glide.with(con).load(ulist.get(position).getHeadImage()).transform(new GlideCircleTransform(con)).into(((SearchHolder)holder).userImg);
        if(ulist.get(position).getSex()==1){
            ((SearchHolder)holder).sex.setImageDrawable(con.getResources().getDrawable(R.drawable.female_icon));
        }else{
            ((SearchHolder)holder).sex.setImageDrawable(con.getResources().getDrawable(R.drawable.male_icon));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(con, OtherMainActivity.class);
                intent.putExtra("uid",ulist.get(position).getId());
                con.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return ulist.size();
    }
    class SearchHolder extends RecyclerView.ViewHolder {
        private ImageView userImg,sex;
        private TextView name,from,age;
    public SearchHolder(View itemView) {
        super(itemView);
    }
}
}
