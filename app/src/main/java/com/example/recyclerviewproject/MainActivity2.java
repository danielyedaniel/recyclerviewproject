package com.example.recyclerviewproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {



    FloatingActionButton openDialog;
    TextView infoTv;
    ArrayList<String> colors;
    EditText edtInput;
    PieChart pieChart;
    private ListView itemsListView;
    private List<String> items;
    private EditItemsAdapter adapter;
    RecyclerView editItemsRecyclerView;

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

        openDialog = findViewById(R.id.main_floatingActionButton);


        openDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });
    }

    void showCustomDialog() {
        final Dialog dialog = new Dialog(MainActivity2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        final EditText nameEt = dialog.findViewById(R.id.name_et);
        Button submitButton = dialog.findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                
                EditItemsAdapter.setLayout(name);
                dialog.dismiss();
            }
        });

        dialog.show();
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



    public void datachange (View v){
        pieChart = (PieChart)findViewById(R.id.piechart);
        pieChart.clearChart();

        for (int i = 0; i < items.size(); i++) {

            // Print all elements of List

            pieChart.addPieSlice(
                    new PieModel(
                            "R",
                            Integer.parseInt(String.valueOf(items.get(i))),
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

        items.add("");

        adapter.notifyItemAdded();

        onItemUpdate();
    }
}