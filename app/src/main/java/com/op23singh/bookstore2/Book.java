package com.op23singh.bookstore2;
import android.view.View;

import java.io.Serializable;

public class Book implements Serializable {
    private String Id,pages;
    private String Name,Author,imageUrl,shortDesc,longDesc;
    protected String Expanded;
    public Book(){

    }
    public Book(String id, String name, String author, String pages, String imageUrl, String shortDesc, String longDesc,String Expanded) {
        this.Id = id;
        this.Name = name;
        this.Author = author;
        this.pages = pages;
        this.imageUrl = imageUrl;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.Expanded=Expanded;
    }

    public String getExpanded() {
        return Expanded;
    }

    public void setExpanded(String expanded) {
        Expanded = expanded;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }
}
