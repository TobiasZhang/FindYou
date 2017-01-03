package ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/12/10 0010.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private String[]title;
    private List<Fragment> fList;
    public PagerAdapter(FragmentManager fm,String[]title,List<Fragment> flist) {
        super(fm);
        this.title = title;
        this.fList = flist;
    }

    @Override
    public Fragment getItem(int position) {
        return fList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return fList.size();
    }

}
