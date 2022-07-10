package com.lux.bookstore_2;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VH> {

    Context context;
    ArrayList<List_Item> list_items;


    public RecyclerAdapter(Context context, ArrayList<List_Item> list_items) {
        this.context = context;
        this.list_items = list_items;
    }



    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemview=inflater.inflate(R.layout.recycler_item,parent,false);
        VH holder=new VH(itemview);
        return holder;
    }

    boolean isFavor(List_Item item){
        SQLiteDatabase db=context.openOrCreateDatabase("bookstore",Context.MODE_PRIVATE,null);
        Cursor cursor =db.rawQuery("SELECT * FROM favor WHERE title=? AND storeid=?",new String[]{item.storeName,item.storeNo});
        int rowNum=cursor.getCount();
        if (rowNum>0) return true;
        else return false;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        List_Item list_item=list_items.get(position);

        holder.vh_storeName.setText(list_item.storeName);
        holder.vh_addr.setText(list_item.addr);
        holder.vh_storeType.setText(list_item.storeType);
        holder.vh_num.setText(list_item.num);
        holder.vh_num.setOnClickListener(view -> {
            Intent intent=new Intent().setAction(Intent.ACTION_DIAL);
            Uri uri=Uri.parse("tel:"+holder.vh_num.getText().toString());
            intent.setData(uri);
            context.startActivity(intent);

        });
        boolean isfavor=isFavor(list_item);
        holder.toggleButton.setChecked(isfavor);
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SQLiteDatabase db=context.openOrCreateDatabase("bookstore",Context.MODE_PRIVATE,null);
                if (b){
                    Toast.makeText(context, "찜", Toast.LENGTH_SHORT).show();
                    db.execSQL("INSERT INTO favor(title,storeid) VALUES(?,?)",new String[]{list_item.storeName,list_item.storeNo});
                }else {
                    Toast.makeText(context, "찜 해제", Toast.LENGTH_SHORT).show();
                    db.execSQL("DELETE FROM favor WHERE title=?",new String[]{list_item.storeName});
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_items.size();
    }


    class VH extends RecyclerView.ViewHolder {

        TextView vh_storeName;
        TextView vh_addr;
        TextView vh_storeType;
        TextView vh_num;
        ToggleButton toggleButton;

        public VH(@NonNull View itemView) {
            super(itemView);

            vh_storeName=itemView.findViewById(R.id.textView_Store_Name);
            vh_addr=itemView.findViewById(R.id.textView_Adres);
            vh_storeType=itemView.findViewById(R.id.textView_Store_Type_Name);
            vh_num=itemView.findViewById(R.id.textView_num);
            toggleButton=itemView.findViewById(R.id.toggle_favorite);

        }
    }

}
