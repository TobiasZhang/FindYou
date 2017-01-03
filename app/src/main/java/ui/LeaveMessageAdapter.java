package ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tt.findyou.R;

import java.util.List;

import limeng.com.findyou.view.MyselfinformationActivity;
import limeng.com.findyou.view.OtherMainActivity;
import model.pojo.Comment;
import model.pojo.LeaveMsg;
import utils.GlideCircleTransform;
import utils.LoginUtils;

/**
 * Created by Administrator on 2016/12/18 0018.
 */
public class LeaveMessageAdapter extends BaseAdapter {
    private Context con;
    private List<LeaveMsg> cList;
    public LeaveMessageAdapter(Context con,List<LeaveMsg> clist){
        this.con = con;
        this.cList = clist;
    }
    @Override
    public int getCount() {
        return cList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(con).inflate(R.layout.comment_item_layout,null);
            holder = new ViewHolder();
            holder.userimg = (ImageView) convertView.findViewById(R.id.userimg);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(!cList.get(position).getFromUser().getHeadImage().isEmpty()){
            Glide.with(con).load(cList.get(position).getFromUser().getHeadImage()).transform(new GlideCircleTransform(con)).into(holder.userimg);
        }
        if(!cList.get(position).getFromUser().getTruename().isEmpty())
        holder.name.setText(cList.get(position).getFromUser().getTruename());
        if(!cList.get(position).getPubTime().isEmpty())
        holder.date.setText(cList.get(position).getFormatPubTime());
        if(!cList.get(position).getContent().isEmpty())
        holder.content.setText(cList.get(position).getContent());
        holder.userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cList.get(position).getFromUser().getId()== LoginUtils.userInfo.getId()){
                    Intent intent = new Intent(con, MyselfinformationActivity.class);
                    con.startActivity(intent);
                }else{
                    Intent intent = new Intent(con, OtherMainActivity.class);
                    intent.putExtra("uid",cList.get(position).getFromUser().getId());
                    con.startActivity(intent);
                }
            }
        });
        return convertView;
    }
    class ViewHolder{
        private ImageView userimg;
        private TextView name,date,content;
    }
}
