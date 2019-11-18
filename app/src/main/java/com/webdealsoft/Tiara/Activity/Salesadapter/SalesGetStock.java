package com.webdealsoft.Tiara.Activity.Salesadapter;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import com.webdealsoft.Tiara.Activity.DatabaseSqlite.Controllerdb;
        import com.webdealsoft.Tiara.Activity.Modelclass.Modelclass;
        import com.webdealsoft.Tiara.Activity.Modelclass.Mymethord;
        import com.webdealsoft.Tiara.Activity.SalesActivity;
        import com.webdealsoft.Tiara.R;

        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.util.ArrayList;


public class SalesGetStock extends RecyclerView.Adapter<SalesGetStock.ViewHolder> {
    ArrayList<Modelclass> result;
    Context context;
    SQLiteDatabase database;
    Mymethord mymethord;

    public SalesGetStock(ArrayList<Modelclass> result, Context context) {
        this.result = result;
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.getstock,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.stockid.setText(result.get(position).getstockid());
        holder.sku.setText(result.get(position).getsku());
        holder.metal.setText(result.get(position).getmetal());

        holder.netwt.setText(String.valueOf(new BigDecimal(Double.parseDouble(result.get(position).getgwt())).setScale(3, RoundingMode.HALF_UP)));
        holder.grosswt.setText(String.valueOf(new BigDecimal(Double.parseDouble(result.get(position).getnwt())).setScale(3, RoundingMode.HALF_UP)));

        holder.relay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Controllerdb db =new Controllerdb(context);
                database=db.getWritableDatabase();
                database.execSQL("INSERT INTO UserDetails(Username,Mailid,Age)VALUES('"+result.get(position).getstockid()+"','"+result.get(position).getsku()+"','"+result.get(position).getgwt()+"')" );
                Toast.makeText(context,"Item Added",Toast.LENGTH_LONG).show();
                if (context instanceof SalesActivity) {
                    ((SalesActivity)context).displayData();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView stockid, sku,metal,netwt,grosswt;
        LinearLayout relay;
        public ViewHolder(View itemView) {
            super(itemView);
            relay = itemView.findViewById(R.id.linear1);
            stockid = itemView.findViewById(R.id.stockidtxt);
            sku = itemView.findViewById(R.id.sku);
            metal = itemView.findViewById(R.id.metal);
            netwt = itemView.findViewById(R.id.netwt);
            grosswt = itemView.findViewById(R.id.grosswt);
        }
    }
}