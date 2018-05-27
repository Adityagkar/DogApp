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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aditya on 5/17/2018.
 */

public class DogList extends android.app.Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> names;
    List<String> breed;
    CustomAdapter adapter;
        ListView listView;
      String noOfDays;
        long price=0;

    Bundle bundle2;
    Bundle bundle;
    String bookid_retrieved;

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



        names= new ArrayList<String>();
        breed=new ArrayList<String>();
        listView=  v.findViewById(R.id.listView);

        noOfDays=getNoOfDays();
        Log.d("DAYS",getNoOfDays()+"dl");
        bundle=getArguments();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();





        Button next= v.findViewById(R.id.button13);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle2=new Bundle();
                bundle2.putString("fromDate",bundle.getString("fromDate"));
                bundle2.putString("toDate",bundle.getString("toDate"));
                bundle2.putString("cid",bundle.getString("cid") );
                bundle2.putString("name",bundle.getString("name"));
                bundle2.putSerializable("ARRAYLIST",(Serializable)names);

//
                for (int i=0;i<names.size();i++){
                  //  BookingInfoDatabase bookingInfoDatabase=
                           // new BookingInfoDatabase(bundle.getString("cid"),bundle.getString("name"),names.get(i).toString(),
                            //        "0","1",bundle.getString("fromDate"),bundle.getString("toDate"),"0" );
                   // addToDatabse(bookingInfoDatabase);
                Log.d("TESTER",names.get(i).toString());

                }

//

                android.app.Fragment fragment;
                long price=adapter.tillnowtotalPrice();

                FinalBooking finalBooking=new FinalBooking();
                finalBooking.setArguments(bundle2);
                fragment = finalBooking;
                finalBooking.setPriceFinal(price);
                replaceFragment(fragment);
            }
        });




        return v;
    }

    private boolean addToDatabse(BookingInfoDatabase bookingInfoDatabase) {


        bookid_retrieved=databaseReference.child("booking_details").push().getKey();


        databaseReference.child(bookid_retrieved).setValue(bookingInfoDatabase);

        return true;
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


    public void replaceFragment(android.app.Fragment someFragment) {
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
                names=adapter.fetcher();

                listView.setAdapter(adapter);

               // breedCalculator();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public ArrayList fetcher_list(){
        return (ArrayList) names;
    }




}
