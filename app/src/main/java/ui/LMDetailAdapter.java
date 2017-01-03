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

import java.util.List;

import limeng.com.findyou.view.OtherMainActivity;
import model.pojo.LeaveMsg;
import utils.GlideCircleTransform;

/**
 * Created by Administrator on 2016/12/18 0018.
 */
public class LMDetailAdapter extends RecyclerView.Adapter<LMDetailAdapter.MyAdapter> {
    private List<LeaveMsg> cList;
    private Context con;
    private View view;
    private MyAdapter holder;
    public LMDetailAdapter(Context con, List<LeaveMsg> lList){
        this.con = con;
        this.cList = lList;
    }
    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(con).inflate(R.layout.comment_item_layout,null);
        holder = new MyAdapter(view);
        holder.userimg = (ImageView) view.findViewById(R.id.userimg);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.content = (TextView) view.findViewById(R.id.content);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, final int position) {
        if(!cList.get(position).getFromUser().getHeadImage().isEmpty()){
            Glide.with(con).load(cList.get(position).getFromUser().getHeadImage()).transform(new GlideCircleTransform(con)).into(holder.userimg);
        }
        if(!cList.get(position).getFromUser().getTruename().isEmpty())
            holder.name.setText(cList.get(position).getFromUser().getTruename());
        if(!cList.get(position).getPubTime().isEmpty())
            holder.date.setText(cList.get(position).getPubTime());
        if(!cList.get(position).getContent().isEmpty())
            holder.content.setText(cList.get(position).getContent());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(con, OtherMainActivity.class);
                intent.putExtra("uid",cList.get(position).getFromUser().getId());
                con.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    class MyAdapter extends RecyclerView.ViewHolder{
        public MyAdapter(View itemView) {
            super(itemView);
        }
        private ImageView userimg;
        private TextView name,date,content;
    }
}
