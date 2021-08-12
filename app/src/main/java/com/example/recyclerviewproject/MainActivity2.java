package com.example.recyclerviewproject;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyclerviewproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {




    ArrayList<String> colors;
    EditText edtInput;
    PieChart pieChart;
    private ListView itemsListView;
    private List<Model> items;
    private EditItemsAdapter adapter;
    RecyclerView editItemsRecyclerView;
    private EditText EditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        colors = new ArrayList<String>(16);
        colors.add("#39add1");
        colors.add("#3079ab");
        colors.add("#c25975");
        colors.add("#e15258");
        colors.add("#f9845b");
        colors.add("#838cc7");
        colors.add("#7d669e");
        colors.add("#53bbb4");
        colors.add("#51b46d");
        colors.add("#e0ab18");
        colors.add("#f092b0");

        items = new ArrayList<>(16);
        edtInput = (EditText) findViewById(R.id.edtInput);

        /*
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pieChart = (PieChart) findViewById(R.id.piechart);
                pieChart.addPieSlice(
                        new PieModel(
                                "R",
                                Integer.parseInt(edtInput.getText().toString()),
                                Color.parseColor("#FFA726")));
            }
        })*/

        setupViews();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {

                items.remove(viewHolder.getAdapterPosition());

                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(editItemsRecyclerView);


        Button data = findViewById(R.id.buttonDataChange);
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datachange();
            }
        });
    }

    private void setupViews() {

        Objects.requireNonNull(items);

        itemsListView = findViewById(R.id.main_itemsListView);

        editItemsRecyclerView = findViewById(R.id.main_editItemsRecyclerView);

        FloatingActionButton floatingActionButton = findViewById(R.id.main_floatingActionButton);

        setupAdapter(editItemsRecyclerView, floatingActionButton);
    }

    private void setupAdapter(
            RecyclerView editItemsRecyclerView,
            FloatingActionButton floatingActionButton
    ) {
        adapter = new EditItemsAdapter(items, this::onItemUpdate);

        editItemsRecyclerView.setAdapter(adapter);

        editItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton.setOnClickListener(this);
    }



    public void datachange (){
        pieChart = (PieChart)findViewById(R.id.piechart);
        pieChart.clearChart();

        for (int i = 0; i < items.size(); i++) {

            // Print all elements of List

            pieChart.addPieSlice(
                    new PieModel(
                            "R",
                            Integer.parseInt(String.valueOf(items.get(i).getValue())),
                            Color.parseColor(colors.get(i))));
        }




        // To animate the pie chart
        pieChart.startAnimation();

    }

    private void onItemUpdate() {

        itemsListView.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        items
                )
        );

    }

    @Override
    public void onClick(@Nullable View v) {


        Dialog d = new Dialog(this);
        d.show();
        d.setContentView(R.layout.cd);
        EditText = d.findViewById(R.id.cd_editText);

        d.findViewById(R.id.cd_button).setOnClickListener(v1 -> {
            d.dismiss();
            Toast.makeText(this, EditText.getText().toString(), Toast.LENGTH_SHORT).show();
            items.add(new Model(EditText.getText().toString(),""));

            adapter.notifyItemAdded();

            onItemUpdate();
        });

    }
}