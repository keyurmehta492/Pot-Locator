package com.keyur.potlocator.fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.keyur.potlocator.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keyur on 4/23/2017.
 */

public class potAdaptor extends ArrayAdapter {
    List list = new ArrayList();
    public potAdaptor(Context context, @LayoutRes int resource) {
        super(context, resource);
    }


    public void add(potHole object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }


    @Override
    public Object getItem(int position) {
        return list.get(position);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;
        row = convertView;
        potHolder potHolder;
        if(row ==null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.rowlayout, parent, false);
            potHolder = new potHolder();
            potHolder.tv_latitude =(TextView) row.findViewById(R.id.tv_latitude);
            potHolder.tv_longitude =(TextView) row.findViewById(R.id.tv_longitude);
            potHolder.tv_time =(TextView) row.findViewById(R.id.tv_time);
            row.setTag(potHolder);

        }
        else {
            potHolder = (potHolder) row.getTag( );
        }
        potHole potHole= (potHole) this.getItem(position);
        potHolder.tv_latitude.setText(potHole.getLatitude().toString());
        potHolder.tv_longitude.setText(potHole.getLongitude().toString());
        potHolder.tv_time.setText(potHole.getTime());


        return row;
    }
}

class potHolder {

    TextView tv_latitude, tv_longitude, tv_time;
}