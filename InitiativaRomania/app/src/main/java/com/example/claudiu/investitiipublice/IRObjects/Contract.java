package com.example.claudiu.investitiipublice.IRObjects;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by claudiu on 2/9/16.
 */
public class Contract implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;

    public int id;

    public String title;
    public String address;
    public String CPVCode;
    public double latitude;
    public double longitude;
    public String valueEUR;
    public String number;
    public String date;
    public int votes;

    public LinkedList<Comment> comments = null;
    public Company company = null;
    public String authority;
    public LinkedList<Category> categories = null;

    public Contract() {
        comments = new LinkedList<Comment>();
        categories = new LinkedList<Category>();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
