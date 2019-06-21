package com.ccnu.hiic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ccnu.hiic.activity.MainActivity;
import com.ccnu.hiic.fragment.AdminFragment;
import com.ccnu.hiic.fragment.HomeFragment;
import com.ccnu.hiic.fragment.UserFragment;

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter{
//    private List<Fragment> mList = new ArrayList<Fragment>();
//    public ViewPagerFragmentAdapter(FragmentManager fm , List<Fragment> list) {
//        super(fm);
//        this.mList = list;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mList != null ? mList.size() : 0;
//    }
    private final int PAGER_COUNT = 3;
    private AdminFragment adminFragment = null;
    private HomeFragment homeFragment = null;
    private UserFragment userFragment = null;


    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        adminFragment = new AdminFragment();
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
    }

    @Override
    public int getCount(){
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup,int position){
        return super.instantiateItem(viewGroup,position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = adminFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = homeFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = userFragment;
                break;
        }
        return fragment;
    }
    }



