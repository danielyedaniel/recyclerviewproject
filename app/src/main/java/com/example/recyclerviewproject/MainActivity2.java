package com.example.recyclerviewproject;

// Import the required libraries
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.recyclerviewproject.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class MainActivity2
        extends AppCompatActivity {

    // Create the object of TextView
    // and PieChart class
    TextView  tvPython, tvCPP, tvJava;
    PieChart pieChart;
    EditText edittext;
    private RecyclerView mRecyclerView;
    private com.example.recyclerviewproject.CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CategoryItem> diffExampleList;

    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Link those objects with their
        // respective id's that
        // we have given in .XML file

        pieChart = findViewById(R.id.piechart);

        // Creating a method setData()
        // to set the text in text view and pie chart
        //setData();
        createExampleList();
        buildRecyclerView();
        adapter = new CategoryAdapter(this, diffExampleList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView2, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {

                diffExampleList.remove(viewHolder.getAdapterPosition());

                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    public void click(View v){

        diffExampleList.add( new CategoryItem( "New Category" , "0"));
        mAdapter.notifyDataSetChanged();
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView2);


    }



    public void insertItem(int position) {
        diffExampleList.add(position, new CategoryItem( "New Item At Position" + position, "0"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position) {
        diffExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        diffExampleList.get(position).changeText2(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList() {
        diffExampleList = new ArrayList<>();
        diffExampleList.add(new CategoryItem( "Category 1", "100"));
        diffExampleList.add(new CategoryItem( "Category 2", "100"));
        diffExampleList.add(new CategoryItem( "Category 3", "100"));
        diffExampleList.add(new CategoryItem( "Category 4", "100"));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new com.example.recyclerviewproject.CategoryAdapter(this, diffExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }


    

    private void setData()
    {

        // Set the percentage of language used
        edittext.setText(Integer.toString(100));





    }

    public void datachange (View v){

        pieChart.clearChart();

        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt(edittext.getText().toString()),
                        Color.parseColor("#FFA726")));


        // To animate the pie chart
        pieChart.startAnimation();

    }



}
