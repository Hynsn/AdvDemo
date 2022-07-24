package com.hynson.databinding.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hynson.databinding.entity.NoteInfo;

import java.util.List;

@Dao
public interface YnoteInfoDao {
    @Query("SELECT * FROM NoteInfo")
    List<NoteInfo> getAll();

    @Insert
    void insertAll(NoteInfo... noteInfos);

    @Delete
    void delete(NoteInfo info);

    @Update
    void updateUsers(NoteInfo... users);
}
