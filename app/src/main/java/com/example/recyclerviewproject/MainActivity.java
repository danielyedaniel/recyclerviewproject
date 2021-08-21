package com.example.recyclerviewproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;
    
    private RecyclerView mRecyclerView;
    private com.example.recyclerviewproject.ExampleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ExampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createExampleList();
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
        super.onActivityResult(requestCode, resultCode, data);//note: requestCode=position
        // check if the request code is same as what is passed  here it is 2
        if(requestCode>=0)
        {
            String message=data.getStringExtra("MESSAGE");
            String budget=data.getStringExtra("BUDGET");
            //((TextView)findViewById(R.id.textView2)).setText(message);
            changeItem(requestCode, message);
            //changeItem(requestCode, budget);
            mExampleList.set(requestCode, new ExampleItem(R.drawable.ic_android, message, "$" + budget));


        }
    }

    public void click(View v){

        int listSize = mAdapter.getItemCount() + 1;
        mExampleList.add( new ExampleItem(R.drawable.ic_android, "Budget " + listSize, "$0"));
        mAdapter.notifyDataSetChanged();
        mRecyclerView=(RecyclerView) findViewById(R.id.recyclerView);


    }



    public void insertItem(int position) {
        mExampleList.add(position, new ExampleItem(R.drawable.ic_android, "New Item At Position" + position, "This is Line 2"));
        mAdapter.notifyItemInserted(position);
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
        mExampleList.add(new ExampleItem(R.drawable.ic_android, "Budget 1", "$0"));
        mExampleList.add(new ExampleItem(R.drawable.ic_audio, "Budget 2", "$0"));
        mExampleList.add(new ExampleItem(R.drawable.ic_sun, "Budget 3", "$0"));
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
                String title = mAdapter.getTitle(position);
                intent.putExtra("TITLE", title);
                startActivityForResult(intent,position);

            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
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