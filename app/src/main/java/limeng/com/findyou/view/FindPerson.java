package limeng.com.findyou.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tt.findyou.R;

import java.util.ArrayList;
import java.util.List;

import ui.FindFragmentAdapter;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class FindPerson extends Fragment {
    private TabLayout tl;
    private ViewPager pager;
    private List<String> tlist = new ArrayList<>();
    private List<Fragment> flist = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_fragment,null);
        tl = (TabLayout) view.findViewById(R.id.tab_layout);
        pager = (ViewPager) view.findViewById(R.id.viewpager);
        tl.setTabMode(TabLayout.MODE_FIXED);
        tl.setupWithViewPager(pager);
        tlist.add("寻老友");
        tlist.add("交新友");
        flist.add(new FindOldFragment());
        flist.add(new FindSameHobyFragment());
        pager.setAdapter(new FindFragmentAdapter(getActivity().getSupportFragmentManager(),flist,getActivity(),tlist));
        return view;
    }
}
