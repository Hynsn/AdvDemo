package com.example.databinding.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.databinding.entity.YnoteInfo;

import java.util.List;

@Dao
public interface YnoteInfoDao {
    @Query("SELECT * FROM noteinfo")
    List<YnoteInfo> getAll();

    @Insert
    void insertAll(YnoteInfo... ynoteInfos);

    @Delete
    void delete(YnoteInfo info);

    @Update
    void updateUsers(YnoteInfo... users);
}
