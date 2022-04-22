package com.example.custodian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import com.example.custodian.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    int[] images ={
            R.drawable.guardian_slide,
            R.drawable.invite,
            R.drawable.emergency_slide,
    };
    int[] headings={
            R.string.heading1,
            R.string.heading2,
            R.string.heading3,
    };
    int[] descriptions = {
            R.string.description1,
            R.string.description2,
            R.string.description3,
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == (ConstraintLayout)object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slides_layout,container,false);

        ImageView image = view.findViewById(R.id.slide_img);
        TextView head = view.findViewById(R.id.heading_text);
        TextView desc = view.findViewById(R.id.desc_text);

        image.setImageResource(images[position]);
        head.setText(headings[position]);
        desc.setText(descriptions[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}









