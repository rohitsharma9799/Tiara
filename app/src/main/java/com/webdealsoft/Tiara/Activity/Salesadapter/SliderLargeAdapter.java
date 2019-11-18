package com.webdealsoft.Tiara.Activity.Salesadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;
import com.webdealsoft.Tiara.Activity.Modelclass.SilderData;
import com.webdealsoft.Tiara.R;

import java.util.List;

public class SliderLargeAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> color;
    List<SilderData> adsLargeDataList;


    public SliderLargeAdapter(Context context, List<SilderData> adsLargeDataList) {
        this.context = context;
        this.adsLargeDataList = adsLargeDataList;

    }

    @Override
    public int getCount() {
        return adsLargeDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.banner_slider, null);

        //LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        ImageView imageView=view.findViewById(R.id.imageslide);

        Picasso.with(context).load(adsLargeDataList.get(position).getImage()).into(imageView);
        //linearLayout.setBackgroundResource(imageData.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}