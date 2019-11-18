package com.webdealsoft.Tiara.Activity.Salesadapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webdealsoft.Tiara.Activity.DatabaseSqlite.Controllerdb;
import com.webdealsoft.Tiara.Activity.SalesActivity;
import com.webdealsoft.Tiara.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Created by Shree on 10/22/2016.
 */
public class CustomAdapter extends BaseAdapter {

    CustomAdapter adapter;
    private Context mContext;
    Controllerdb controldb;
    SQLiteDatabase database;
    private ArrayList<String> Id = new ArrayList<String>();
    private ArrayList<String> Stockid = new ArrayList<String>();
    private ArrayList<String> Sku = new ArrayList<String>();
    private ArrayList<String> Item = new ArrayList<String>();
    private ArrayList<String> Metal = new ArrayList<String>();
    private ArrayList<String> Title = new ArrayList<String>();
    private ArrayList<String> Pcs = new ArrayList<String>();
    private ArrayList<String> Gwt = new ArrayList<String>();
    private ArrayList<String> Nwt = new ArrayList<String>();
    public CustomAdapter(Context context, ArrayList<String> Id, ArrayList<String> Stockid, ArrayList<String> Sku, ArrayList<String> Item, ArrayList<String> Metal, ArrayList<String> Title, ArrayList<String> Pcs, ArrayList<String> Gwt, ArrayList<String> Nwt)
    {
        this.mContext = context;
        this.Id = Id;
        this.Stockid = Stockid;
        this.Sku = Sku;
        this.Item=Item;
        this.Metal = Metal;
        this.Title = Title;
        this.Pcs = Pcs;
        this.Gwt=Gwt;
        this.Nwt=Nwt;
    }
    @Override
    public int getCount() {
        return Id.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final    viewHolder holder;
        controldb =new Controllerdb(mContext);
        LayoutInflater layoutInflater;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.customelayout, null);
            holder = new viewHolder();
            holder.tv_metal = (TextView) convertView.findViewById(R.id.txt_metal);
            holder.tv_sku = (TextView) convertView.findViewById(R.id.txt_sku);
            holder.tv_nwt = (TextView) convertView.findViewById(R.id.txt_nwt);
            holder.tv_gwt = (TextView) convertView.findViewById(R.id.txt_gwt);
            holder.tv_sno = (TextView) convertView.findViewById(R.id.tv_sno);
            holder.tv_stockid=(TextView)convertView.findViewById(R.id.txt_stockid);

            holder.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        holder.tv_metal.setText(Metal.get(position));
        holder.tv_sku.setText(Sku.get(position));

        holder.tv_nwt.setText(String.valueOf(new BigDecimal(Double.valueOf(Nwt.get(position))).setScale(3, RoundingMode.HALF_UP)));

        holder.tv_gwt.setText(String.valueOf(new BigDecimal(Double.valueOf(Gwt.get(position))).setScale(3, RoundingMode.HALF_UP)));
        holder.tv_stockid.setText(Stockid.get(position));
        holder.tv_sno.setText(String.valueOf(position+1));

       // System.out.println("Index:"+position);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controldb =new Controllerdb(mContext);
                database=controldb.getWritableDatabase();
                database.execSQL("DELETE FROM " + "UserDetails"+ " WHERE "+"Id "+"='"+Id.get(position)+"'");
                if (mContext instanceof SalesActivity) {
                    ((SalesActivity)mContext).displayData();
                }
                if (mContext instanceof SalesActivity) {
                    ((SalesActivity)mContext).sum();
                }


            }
        });
        return convertView;
    }
    public class viewHolder {
        TextView tv_stockid,tv_sku,tv_item,tv_metal,tv_title,tv_pcs,tv_gwt,tv_nwt,tv_sno;
        ImageView delete;
    }
}