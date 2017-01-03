package limeng.com.findyou.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.findyou.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ui.NotiAdapter;
import widget.PullToLoaderRecylerView;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class Notification extends Fragment {
    private View view;
    private List list = new ArrayList();
    private PullToLoaderRecylerView mRecylerView;
    private NotiAdapter adapter;
    private  boolean flag = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_layout,null);
        RecyclerView recyclerView = new RecyclerView(getActivity());
        mRecylerView = (PullToLoaderRecylerView) view.findViewById(R.id.recycler);
        adapter = new NotiAdapter(getActivity(),new ArrayList());
        return view;
    }
}
