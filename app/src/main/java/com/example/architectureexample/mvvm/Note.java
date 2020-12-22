package com.example.architectureexample.mvvm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    @PrimaryKey(autoGenerate = true)

    private int id;
    private String title;
    private String discription;
    private int priority;
    private boolean selected;

    public Note(String title, String discription, int priority) {
        this.title = title;
        this.discription = discription;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDiscription() {
        return discription;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean toggleSelected() {
        selected=!selected;
        return selected;
    }
}
