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
        ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      final View v  =inflater.inflate(R.layout.fragment_list,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        names= new ArrayList<String>();
        breed=new ArrayList<String>();
        listView=  v.findViewById(R.id.listView);


        Button next= v.findViewById(R.id.button13);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment fragment;
                fragment = new FinalBooking();
                replaceFragment(fragment);
            }
        });




        return v;
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
                CustomAdapter adapter= new CustomAdapter(getActivity(), names);
                listView.setAdapter(adapter);

               // breedCalculator();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
