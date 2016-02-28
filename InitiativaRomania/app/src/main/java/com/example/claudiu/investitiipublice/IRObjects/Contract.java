/**
 This file is part of "Harta Banilor Publici".

 "Harta Banilor Publici" is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 "Harta Banilor Publici" is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

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
