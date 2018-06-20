package org.launchcode.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Category {


    @Id
    @GeneratedValue
    private int id;


    @NotNull
    @Size(min=3, max=15)
    private String name;


    public Category() { }

    public Category(String name) {
        this.name = name;
    }



    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
