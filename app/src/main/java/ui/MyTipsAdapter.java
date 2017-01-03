package ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tt.findyou.R;

import org.w3c.dom.Text;

import java.util.List;

import model.Test;
import model.pojo.Tip;

/**
 * Created by Administrator on 2016/12/11 0011.
 */
public class MyTipsAdapter extends RecyclerView.Adapter<MyTipsAdapter.MyHolder1> {
    private List<Tip> tList;
    private Context con;
    View view;
    MyHolder1 holder;
    public MyTipsAdapter(Context con,List<Tip> tList){
        this.con = con;
        this.tList = tList;
    }
    @Override
    public MyHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(con).inflate(R.layout.mytips_items,null);
        holder = new MyHolder1(view);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.content = (TextView) view.findViewById(R.id.content);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder1 holder, int position) {
        holder.date.setText(tList.get(position).getFormatPubTime());
        holder.content.setText(tList.get(position).getContent());
        Log.e("tList.get",tList.get(position).getPubTime());
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }
    class MyHolder1 extends RecyclerView.ViewHolder{
    private TextView date,content;
        public MyHolder1(View itemView) {
            super(itemView);
        }
    }
}
