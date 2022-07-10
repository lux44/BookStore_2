package com.lux.bookstore_2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<List_Item> list_items=new ArrayList<>();
    RecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        MaterialToolbar toolbar=view.findViewById(R.id.toolbar_favoriteView);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar= ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        recyclerView=view.findViewById(R.id.recyclerView_favoriteView);
        adapter=new RecyclerAdapter(getContext(),list_items);
        recyclerView.setAdapter(adapter);

        loadData();
    }

    void loadData(){

        String storeName="";
        String storeNo="";

        SQLiteDatabase db=getActivity().openOrCreateDatabase("bookstore", Context.MODE_PRIVATE,null);
        Cursor cursor =db.rawQuery("SELECT * FROM favor",null);
        int rowNum=cursor.getCount();
        cursor.moveToFirst();
        for (int i=0;i<rowNum;i++){
            storeName=cursor.getString(1);
            storeNo=cursor.getString(2);
            for (List_Item t:G.all_items){
                if (t.storeName.equals(storeName)&&t.storeNo.equals(storeNo)){
                    list_items.add(t);
                    adapter.notifyItemInserted(list_items.size()-1);
                }
            }
            cursor.moveToNext();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            list_items.clear();
            adapter.notifyDataSetChanged();
            loadData();
        }
    }
}
