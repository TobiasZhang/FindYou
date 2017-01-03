package ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tt.findyou.R;

import java.util.List;


/**
 * Created by Administrator on 2016/12/11 0011.
 */
public class NotiAdapter extends RecyclerView.Adapter {
    Context context;
    List list;
    View view;
    MyHolder holder;
    public NotiAdapter(Context mContext,List list){
        this.context = mContext;
        this.list = list;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.noti_reply,null);
        holder = new MyHolder(view);
        holder.user = (TextView) view.findViewById(R.id.user);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.content = (TextView) view.findViewById(R.id.content);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 1:
                ((MyHolder)holder).user.setText(list.get(position)+"在帖子中回复了你");
                break;
            case 2:
                ((MyHolder)holder).user.setText(list.get(position)+"给你留了言");
                break;
        }
        ((MyHolder)holder).date.setText(list.get(position).getClass()+"");
        ((MyHolder)holder).content.setText(list.get(position).getClass()+"");
    }

    /**
     * @param position 如果是留言则type==2 如果是回复 type==1
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        private TextView user,date,content;
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}
