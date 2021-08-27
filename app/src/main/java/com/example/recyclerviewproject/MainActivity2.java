package com.example.recyclerviewproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {


    ArrayList<String> totalAmount;
    TextView total;
    ArrayList<String> colors;
    EditText edtInput;
    PieChart pieChart;
    private ListView itemsListView;
    private List<Model> items;
    private EditItemsAdapter adapter;
    RecyclerView editItemsRecyclerView;
    private EditText EditText;
    private budget bud;
    private Bundle extra;
    private final static String fileName="memory.txt";

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
        bud=new budget();
        extra=getIntent().getExtras();
        bud.setName(extra.getString("name"));
        bud.setCostNames(extra.getStringArrayList("costNames"));
        bud.setCosts(extra.getIntegerArrayList("costs"));


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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                try {
                    datachange(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        initializeCosts();
    }

    private void initializeCosts(){
        for(int i=0;i<bud.getCostNames().size();i++){
            items.add(new Model(bud.getCostNames().get(i),String.valueOf(bud.getCosts().get(i))));
        }
        adapter.notifyItemAdded();
    }

    private void setupViews() {

        Objects.requireNonNull(items);

        itemsListView = findViewById(R.id.main_itemsListView);

        editItemsRecyclerView = findViewById(R.id.main_editItemsRecyclerView);

        FloatingActionButton floatingActionButton = findViewById(R.id.main_floatingActionButton);

        setupAdapter(editItemsRecyclerView, floatingActionButton);
    }
    //
    private void setupAdapter(
            RecyclerView editItemsRecyclerView,
            FloatingActionButton floatingActionButton
    ) {
        adapter = new EditItemsAdapter(items, this::onItemUpdate);

        editItemsRecyclerView.setAdapter(adapter);

        editItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton.setOnClickListener(this);
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void datachange (View v) throws Exception {
        ArrayList<String> names=new ArrayList<String>();
        ArrayList<Integer> costs=new ArrayList<Integer>();
        for(Model a:items){
            names.add(a.getHint());
            costs.add(Integer.parseInt(a.getValue()));
        }
        try {
            writeAllText(changeFile(names), fileName);
        }catch(Exception t){t.printStackTrace();}
        bud.setCostNames(names);
        bud.setCosts(costs);

        ArrayList<Integer> totalAmount = new ArrayList<>();
        total = (TextView)findViewById(R.id.totalAmount);
        pieChart = (PieChart)findViewById(R.id.piechart);
        totalAmount.clear();
        pieChart.clearChart();
        try {
            final FileOutputStream fos=openFileOutput(fileName,MODE_APPEND);
            for (int i = 0; i < items.size(); i++) {
                fos.write((bud.getName() + "," + items.get(i).getHint() + "," + items.get(i).getValue()+"\n").getBytes());

                // Print all elements of List

                pieChart.addPieSlice(
                        new PieModel(
                                "R",
                                Integer.parseInt(String.valueOf(items.get(i).getValue())),
                                Color.parseColor(colors.get(i))));
                totalAmount.add(Integer.valueOf(items.get(i).getValue()));
            }
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }




        int sum = 0;
        for(int i = 0; i < totalAmount.size(); i++)
            sum += totalAmount.get(i);

        total.setText(Integer.toString(sum));




        // To animate the pie chart
        pieChart.startAnimation();

        editName(v);



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String readAllText(String fileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        Files.lines(Paths.get(fileName)).forEach(sb::append);
        return sb.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String changeFile(ArrayList<String> changes) throws Exception {
        String file = readAllText(fileName);
        String[] arr = file.split("\n"); // every arr items is a line now.
        StringBuilder sb = new StringBuilder();
        for(String s : arr)
        {
            String[] d=s.split(",");
            if(!d[0].equals(bud.getName())){
                sb.append(s+"\n");
            }
        }
        return sb.toString(); //new file that does not contains that lines.
    }

    public void writeAllText(String text, String fileout) {
        try {
            final FileOutputStream fos=openFileOutput(fileout,MODE_APPEND);
            fos.write(text.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sum(){

        String sum = "10";
        for(int i = 0; i < totalAmount.size(); i++)
            sum += String.valueOf(totalAmount.get(i));
        return sum;
    }

    public void editName(View arg0) {
        String message= ((EditText)findViewById(R.id.editName)).getText().toString();
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",message);
        setResult(2,intent);
        //finish();//finishing activity
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