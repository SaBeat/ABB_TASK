package com.example.abb_task.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.abb_task.data.db.dao.CharacterDao
import com.example.abb_task.data.db.dao.RemoteKeyDao
import com.example.abb_task.data.db.entity.CharacterEntity
import com.example.abb_task.data.db.entity.RemoteKeyEntity

@Database(entities = [CharacterEntity::class,RemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao():CharacterDao
    abstract fun remoteKeyDao():RemoteKeyDao

}