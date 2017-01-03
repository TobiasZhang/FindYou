package ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tt.findyou.R;

import java.io.IOException;
import java.util.List;

import model.dto.DataRoot;
import model.pojo.Experience;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utils.GsonManager;
import utils.HttpUtils;
import utils.LoginUtils;
import utils.ToastUtils;

/**
 * Created by Administrator on 2016/12/15 0015.
 */
public class GridViewAdapter extends BaseAdapter {
    private Context con;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    int p = (int) msg.obj;
                    list.remove(p);
                    LoginUtils.userInfo.setExpList(list);
                    GridViewAdapter.this.notifyDataSetChanged();
                    break;
                case 1:
                    ToastUtils.showShortMessage(con,"删除失败！");
                    break;
            }
        }
    };
    private List<Experience> list;
    public GridViewAdapter(Context con,List<Experience> eList){
        this.con = con;
        this.list = eList;
    }
    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(con).inflate(R.layout.grid_adpter_item,null);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.typename = (TextView) view.findViewById(R.id.type);
            holder.start = (TextView) view.findViewById(R.id.start_date);
            holder.end = (TextView) view.findViewById(R.id.end_date);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(list.get(position).getName());
        holder.typename.setText(list.get(position).getPlaceType().getName());
        holder.start.setText(list.get(position).getBeginDate());
        holder.end.setText(list.get(position).getEndDate());
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(con)
                        .setTitle("删除经历")
                        .setMessage("确定删除该经历？")
                        .setNegativeButton("取消",null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpUtils.request("exp/delete?id=" + list.get(position).getId(), null, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if(response.code()==200){
                                            DataRoot<Experience> root = GsonManager.parseJson(response.body().string(),new TypeToken<DataRoot<Experience>>(){});
                                            if(root.getResult().equals("ok")){
                                                Message msg = Message.obtain();
                                                msg.obj = position;
                                                msg.what = 0;
                                                handler.sendMessage(msg);
                                            }else{
                                                handler.sendEmptyMessage(1);
                                            }
                                        }
                                    }
                                });
                            }
                        }).show();
                return true;
            }
        });
        return view;
    }
    class ViewHolder{
        private TextView name,typename,start,end;
    }
}
