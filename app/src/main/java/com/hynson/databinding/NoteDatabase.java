package com.hynson.databinding;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hynson.databinding.dao.YnoteInfoDao;
import com.hynson.databinding.entity.NoteInfo;

@Database(entities = {NoteInfo.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    public abstract YnoteInfoDao ynoteInfoDao();
}

