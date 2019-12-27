package com.example.databinding;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.databinding.dao.YnoteInfoDao;
import com.example.databinding.entity.YnoteInfo;

@Database(entities = {YnoteInfo.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract YnoteInfoDao ynoteInfoDao();
}

