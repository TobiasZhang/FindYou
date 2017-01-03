package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.tt.findyou.R;

import java.util.ArrayList;
import java.util.List;

import model.pojo.Hobby;
import utils.LoginUtils;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public class CkAdapter extends BaseAdapter {
    private List<Hobby> hList;
    private List<Hobby> myList;
    private List<Hobby> trueList = new ArrayList<>();
    private Context con;
    public CkAdapter(List<Hobby> list,Context con,List<Hobby> mList){
        this.hList = list;
        this.con = con;
        this.myList = mList;
        trueList = list;
    }
    @Override
    public int getCount() {
        return hList.size();
    }//5

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
        final Holder holder;
        if(view == null){
            view = LayoutInflater.from(con).inflate(R.layout.ck_item_layout,null);
            holder = new Holder();
            holder.ch = (CheckBox) view.findViewById(R.id.ck);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        for(Hobby h:myList){
            if(hList.get(position).getName().equals(h.getName())){
                holder.ch.setChecked(true);
            }
        }
        holder.ch.setText(hList.get(position).getName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.ch.isChecked()){
                    holder.ch.setChecked(false);
                    trueList.remove(position);
                }
                else
                {
                    holder.ch.setChecked(true);
                    trueList.add(hList.get(position));
                }
            }
        });
        return view;
    }
    private class Holder{
        CheckBox ch;
    }
    public List<Hobby> getHobby(){
        return trueList;
    }
}
