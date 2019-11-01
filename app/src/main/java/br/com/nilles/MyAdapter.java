package br.com.nilles;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }
    @Override
    //switch que pega a posição de cada TAB

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeAct homeAct = new HomeAct();
                return homeAct;
            case 1:
                GpsAct gpsAct = new GpsAct();
                return gpsAct;
            case 2:
                SupportAct supportAct = new SupportAct();
                return supportAct;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }

}
