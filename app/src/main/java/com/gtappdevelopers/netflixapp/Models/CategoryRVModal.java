package com.gtappdevelopers.netflixapp.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class CategoryRVModal {

    private String categoryName;
    private String categoryDesc;
    @ServerTimestamp
    public Date timestamp;

    public CategoryRVModal(){

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return categoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        this.categoryDesc = categoryDesc;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
