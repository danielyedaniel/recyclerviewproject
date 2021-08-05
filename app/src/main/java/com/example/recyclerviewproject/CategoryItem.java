package com.example.recyclerviewproject;

public class CategoryItem {


    private String mText1;
    private String mText2;

    public CategoryItem(String text1, String text2) {

        mText1 = text1;
        mText2 = text2;
    }

    public void changeText2(String text) {
        mText2 = text;
    }



    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

}
