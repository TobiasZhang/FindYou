package ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/12/16 0016.
 */
public class FindFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> flist;
    private Context con;
    private List<String> title;
    public FindFragmentAdapter(FragmentManager fm, List<Fragment> flist, Context con, List<String> title) {
        super(fm);
        this.flist = flist;
        this.con = con;
        this.title = title;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return flist.get(position);
    }

    @Override
    public int getCount() {
        return flist.size();
    }
}
