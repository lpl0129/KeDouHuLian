package com.qttx.kedouhulian.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.qttx.kedouhulian.bean.RegionsBean

/**
 * @author huangyr
 * @date 2018/12/4
 */
@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(roomBean: List<RegionsBean>)

    /**
     * 根据pid查询列表
     * @param pid
     * @return
     */
    @Query("SELECT * from city  where pid= :pid ")
    fun getCityByPid(pid: Int): List<RegionsBean>
    /**
     * 根据pid查询列表
     * @param pid
     * @return
     */
    @Query("SELECT * from city  where citycode= :citycode ")
    fun getCityByCityCode(citycode: String): List<RegionsBean>
    /**
     * 根据id查询单个
     * @param id
     * @return
     */
    @Query("SELECT * from city  where id= :id ")
    fun getCityByid(id: Int): RegionsBean

    /**
     * 根据iname查询单个
     * @return
     */
    @Query("SELECT * from city  where name= :name ")
    fun getCityByname(name: String): RegionsBean
    /**
     * 根据iname查询单个
     * @return
     */
    @Query("SELECT * from city  where adcode= :code ")
    fun getCityByAdCode(code: String): RegionsBean
}
