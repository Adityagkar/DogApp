package com.gitsteintechnologies.domigo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aditya on 5/17/2018.
 */

public class DogList extends Fragment  {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> names;
    List<String> breed;
    CustomAdapter adapter;
        ListView listView;
      String noOfDays;
        long price=0;
    long totalprice=0;

    public String getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(String noOfDays) {
        this.noOfDays = noOfDays;
    }

    public DogList(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      final View v  =inflater.inflate(R.layout.fragment_list,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        names= new ArrayList<String>();
        breed=new ArrayList<String>();
        listView=  v.findViewById(R.id.listView);

        noOfDays=getNoOfDays();
        Log.d("DAYS",getNoOfDays()+"dl");


        Button next= v.findViewById(R.id.button13);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                CustomAdapter adapter= new CustomAdapter(getActivity(), names);
//                adapter.setNoOfDays(noOfDays);
//                               ArrayList arrayListfetched=adapter.retrieve("");
//
//                for (int i=0;i<arrayListfetched.size();i++){
//
//                    Log.d("DBB",arrayListfetched.get(i).toString());
//                    if(arrayListfetched.get(i).toString().contains("small")){
//                        Log.d("DB","yes");
//                        price=priceCalculator("small",450);
//                    }else if((arrayListfetched.get(i).toString()).contains("medium")){
//                        price=priceCalculator("small",500);
//                    }else if((arrayListfetched.get(i).toString()).contains("large")){
//                        price=priceCalculator("small",600);
//                    }else if((arrayListfetched.get(i).toString()).contains("indies")){
//                        price=priceCalculator("small",350);
//                    }
//                totalprice=totalprice+price;
//                }
//
//                Log.d("PRICE",totalprice+"");

                Fragment fragment;
                long price=adapter.tillnowtotalPrice();
                FinalBooking finalBooking=new FinalBooking();
                fragment = finalBooking;
                finalBooking.setPriceFinal(price);
                replaceFragment(fragment);
            }
        });




        return v;
    }

    private long priceCalculator(String breed,int rate) {

    int days=10;

        if(days<10){
            price=days*rate;
        }else if(days<20 && days>=10){
            price=days*(rate-50);
        }else if(days>=20){
            price=days*(rate-100);
        }
        Log.d("PRICE",price+"in");
      return price;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Booking");
        UserInfoDatabase userInfoDatabase=new UserInfoDatabase();
        retrieve(userInfoDatabase.getUsername().toString());
    }


    public void replaceFragment(android.support.v4.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void retrieve(final String username_u){

        Query query= databaseReference.child("dog_details").orderByChild("username").equalTo(username_u);

//        namesnew[0]="";
//        i=0;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    //Log.d("TEST!!!!","VAlue found !");

                    Map<String,Object> map= (Map<String, Object>) dataSnapshot1.getValue();
                    String dogname=map.get("dogname").toString();
                    String dogbreed=map.get("dog_breed").toString();

                    names.add(dogname);
                    breed.add(dogbreed);
                    Log.d("TEST STRING",dogname+"added");


                }
                 adapter= new CustomAdapter(getActivity(), names);
                adapter.setNoOfDays(noOfDays);

                listView.setAdapter(adapter);

               // breedCalculator();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
