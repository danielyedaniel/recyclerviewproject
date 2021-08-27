package com.example.recyclerviewproject;

import java.util.ArrayList;

public class budget {
    public String name;
    public ArrayList<String> costNames;
    public ArrayList<Integer> costs;

    public budget(){}
    public budget(String name, ArrayList<String> costNames, ArrayList<Integer> costs){
        this.name=name;
        this.costNames=costNames;
        this.costs=costs;
    }

    public budget(String name){
        this.name=name;
        costNames=new ArrayList<String>();
        costs=new ArrayList<Integer>();
    }

    public String getName(){
        return name;
    }

    public ArrayList<String> getCostNames(){
        return costNames;
    }

    public ArrayList<Integer> getCosts() {
        return costs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCostNames(ArrayList<String> costNames) {
        this.costNames = costNames;
    }

    public void setCosts(ArrayList<Integer> costs) {
        this.costs = costs;
    }

    public void addCost(String name,int cost){
        costNames.add(name);
        costs.add(cost);
    }

    public double getTotal(){
        int total=0;
        for(double x:costs){
            total+=x;
        }
        return total;
    }


}
