package com.stringrest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
public class StringSet {

    @Id
    @GeneratedValue
    @Column(name = "SET_ID")
    private int id;

    @Column(name = "SET_VALUE")
    private HashSet<String> value;

    public StringSet() {
    }

    public StringSet(HashSet<String> value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<String> getValue() {
        return value;
    }

    public void setValue(HashSet<String> value) {
        this.value = value;
    }

}
