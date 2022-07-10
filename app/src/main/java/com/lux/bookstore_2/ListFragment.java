package com.lux.bookstore_2;

import android.content.ClipData;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<List_Item> list_items=new ArrayList<>();
    ArrayList<Map_Point_Item> map_point_items=new ArrayList<>();

    Spinner spinner_code_value, spinner_store_type;
    ArrayAdapter arrayAdapter;

    String[] arrayCodeValue;
    String[] arrayStoreType;

    ToggleButton toggleButton;

    SearchView searchView;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_list,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        MaterialToolbar toolbar=view.findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar=((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        spinner_code_value=view.findViewById(R.id.ac_spinner_code_value);
        arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.code_value,R.layout.spinner_sample);
        spinner_code_value.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);


        spinner_store_type=view.findViewById(R.id.ac_spinner_store_type);
        arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.store_type,R.layout.spinner_sample);
        spinner_store_type.setAdapter(arrayAdapter);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        toggleButton=view.findViewById(R.id.toggle_favorite);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerAdapter=new RecyclerAdapter(getActivity(), list_items);
        recyclerView.setAdapter(recyclerAdapter);

        arrayCodeValue=getResources().getStringArray(R.array.code_value);
        arrayStoreType=getResources().getStringArray(R.array.store_type);

        searchView=view.findViewById(R.id.search_list);
        searchView.setQueryHint("서점 이름");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchViewData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length()==0){
                    list_items.clear();
                    list_items.addAll(G.all_items);
                }else searchViewData(s);
                return false;
            }
        });

        loadData();

    }


    void loadData(){
        Thread thread=new Thread(){
            @Override
            public void run() {

                String api="http://openapi.seoul.go.kr:8088/4877447573766f6c38325a45597972/xml/TbSlibBookstoreInfo/1/500/";

                try {
                    URL url=new URL(api);

                    InputStream inputStream= url.openStream();
                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream);

                    XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser=factory.newPullParser();
                    xmlPullParser.setInput(inputStreamReader);

                    int eventType=xmlPullParser.getEventType();
                    List_Item list_item=null;
                    Map_Point_Item map_point_item=null;

                    while (eventType!=XmlPullParser.END_DOCUMENT){
                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                break;
                            case XmlPullParser.START_TAG:
                                String tagName=xmlPullParser.getName();
                                if (tagName.equals("row")){
                                    list_item=new List_Item();
                                }else if (tagName.equals("STORE_NAME")){
                                    xmlPullParser.next();
                                    if (list_item!=null) list_item.storeName=xmlPullParser.getText();
                                }
                                else if (tagName.equals("ADRES")){
                                    xmlPullParser.next();
                                    if (list_item!=null) list_item.addr=xmlPullParser.getText();
                                }
                                else if (tagName.equals("TEL_NO")){
                                    xmlPullParser.next();
                                    if (list_item!=null) list_item.num=xmlPullParser.getText();
                                }
                                else if (tagName.equals("STORE_TYPE_NAME")) {
                                    xmlPullParser.next();
                                    if (list_item != null)
                                        list_item.storeType = xmlPullParser.getText();
                                }
                                else if (tagName.equals("STORE_SEQ_NO")){
                                    xmlPullParser.next();
                                    if (list_item!=null) list_item.storeNo=xmlPullParser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                String endTagName=xmlPullParser.getName();
                                if (endTagName.equals("row")){
                                    if (list_item!=null) G.all_items.add(list_item);

                                }
                                break;
                        }
                        eventType=xmlPullParser.next();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            list_items.addAll(G.all_items);
                            recyclerAdapter.notifyDataSetChanged();

                            spinner_code_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    searchData(i,spinner_store_type.getSelectedItemPosition());

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            spinner_store_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    searchData(spinner_code_value.getSelectedItemPosition(),i);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });



                        }
                    });


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }


            }
        };

        thread.start();


    }

    void searchData(int codePosition, int typePosition){

        list_items.clear();
        if (codePosition==0) list_items.addAll(G.all_items);
        else {
            for (List_Item item: G.all_items){
                if (item.addr.contains(arrayCodeValue[codePosition])){
                    list_items.add(item);
                }
            }
        }

        if (typePosition!=0){
            for (int i=list_items.size()-1;i>=0;i--){
                List_Item item=list_items.get(i);
                if (!item.storeType.contains(arrayStoreType[typePosition])){
                    list_items.remove(i);
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    void searchViewData(String search){
        list_items.clear();
        for (List_Item item:G.all_items){
            if (item.storeName.contains(search)){
                list_items.add(item);
            }
        }

    }


}
