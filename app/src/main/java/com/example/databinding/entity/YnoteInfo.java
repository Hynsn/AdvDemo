package com.example.databinding.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "noteinfo")
public class YnoteInfo implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int noteId;

    @ColumnInfo(name = "create_time")
    public String createTime;
    @ColumnInfo(name = "update_time")
    public String updateTime;
    @ColumnInfo(name = "note_name")
    public String noteName;
    @ColumnInfo(name = "note_intro")
    public String noteIntro;
    @ColumnInfo(name = "note_content")
    public String noteContent;

    public YnoteInfo(String createTime, String updateTime, String noteName, String noteIntro, String noteContent) {
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.noteName = noteName;
        this.noteIntro = noteIntro;
        this.noteContent = noteContent;
    }
    @Override
    public String toString() {
        return "YnoteInfo("+noteId+","+createTime+","+updateTime+","+noteName+","+noteIntro+","+noteContent+")";
    }
}
