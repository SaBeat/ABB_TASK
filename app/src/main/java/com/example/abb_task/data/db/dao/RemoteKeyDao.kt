package com.example.abb_task.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.abb_task.data.db.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM tb_remote_keys WHERE characterId = :id")
    suspend fun remoteKeysCharacterId(id: Int): RemoteKeyEntity?

    @Query("DELETE FROM tb_remote_keys")
    suspend fun deleteAll()
}