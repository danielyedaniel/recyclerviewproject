package com.example.recyclerviewproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<budget> budgets;
    
    private RecyclerView mRecyclerView;
    private com.example.recyclerviewproject.ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final static String fileName="memory.txt";


    ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        budgets=new ArrayList<budget>();
        createExampleList();
        readData();
        buildRecyclerView();
        adapter = new ExampleAdapter(this, mExampleList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull  RecyclerView.ViewHolder viewHolder, @NonNull  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull  RecyclerView.ViewHolder viewHolder, int direction) {

                mExampleList.remove(viewHolder.getAdapterPosition());

                mAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    //Sets the textview to message from activity2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode>=0)
        {
            String message=data.getStringExtra("MESSAGE");
            //((TextView)findViewById(R.id.textView4)).setText(message);
            changeItem(requestCode, message);

        }
    }

    public void click(View v){
        Dialog d = new Dialog(this);
        d.show();
        d.setContentView(R.layout.cd);
        EditText e= d.findViewById(R.id.cd_editText);
        try {
            final FileOutputStream fos=openFileOutput(fileName,MODE_APPEND);


        d.findViewById(R.id.cd_button).setOnClickListener(v1 -> {
            Toast.makeText(this, e.getText().toString(), Toast.LENGTH_SHORT).show();
            try {
                String info="newBudget,"+e.getText().toString()+"\n";
                fos.write(info.getBytes());
                fos.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            mExampleList.add( new ExampleItem(R.drawable.ic_android, e.getText().toString() , "This is Line 2"));
            d.dismiss();
            mAdapter.notifyDataSetChanged();
            mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView);

        });
    }catch (Exception r){
            r.printStackTrace();

    }
    }


    public void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text) {
        mExampleList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
        mExampleList.add(new ExampleItem(R.drawable.ic_android, "First Budget", ""));
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new com.example.recyclerviewproject.ExampleAdapter(this, mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new com.example.recyclerviewproject.ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //changeItem(position, "Clicked");
                Intent intent = new Intent (MainActivity.this, MainActivity2.class);
                String bud=mAdapter.getItem(position).getText1();
                for(budget d:budgets){
                    if(d.getName().equals(bud)){
                        intent.putExtra("name",bud);
                        intent.putExtra("costNames",d.getCostNames());
                        intent.putExtra("costs",d.getCosts());
                        break;
                    }
                }
                startActivityForResult(intent,position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void reinstateBudgets(){
        for(budget d:budgets){
            mExampleList.add(new ExampleItem(R.drawable.ic_android, d.getName(), ""));
        }
    }

    public void readData(){
        FileInputStream fis=null;

        try {
            fis = openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            while ((text = br.readLine()) != null) {
                Log.d("Here is the text",text);
                String[] text1=text.split(",");
                if (text1[0].equals("newBudget")){
                    budgets.add(new budget(text1[1]));
                }else{
                    for(budget d:budgets){
                        if(d.getName().equals(text1[0])){
                            d.addCost(text1[1],Integer.parseInt(text1[2]));
                        }
                    }
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        reinstateBudgets();

    }


    /*public void setButtons() {
        buttonInsert = findViewById(R.id.floatingActionButton);

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                insertItem(position);
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                removeItem(position);
            }
        });
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(this,Settings.class);
                startActivity(i);
                return true;
            case R.id.faq:
                Toast.makeText(this, "FAQ is empty at the moment", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item4:
                Toast.makeText(this, "Sub Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}