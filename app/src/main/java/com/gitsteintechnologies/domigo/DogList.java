package com.gitsteintechnologies.domigo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aditya on 5/17/2018.
 */

public class DogList extends Fragment  {


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v  =inflater.inflate(R.layout.fragment_list,container,false);
        ListView listView;
        List<String> names= new ArrayList<String>();
        listView=  v.findViewById(R.id.listView);
        names.add("one");
        names.add("two"); names.add("one");
        names.add("one");
        names.add("two"); names.add("one"); names.add("one");
        names.add("two"); names.add("one"); names.add("one");
        names.add("two"); names.add("one"); names.add("one");
        names.add("two"); names.add("one");

        Button next= v.findViewById(R.id.button13);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment;
                fragment = new DayChooser();
                replaceFragment(fragment);
            }
        });


        CustomAdapter adapter= new CustomAdapter(getActivity(), names);
        listView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Booking");
    }


    public void replaceFragment(android.support.v4.app.Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
