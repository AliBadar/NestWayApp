package com.example.hackatonprjoect.common.model.content

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hia.common.model.content.ContentDetailsEntity
import com.hia.common.model.content.ContentEntity
import com.hia.common.model.content.ContentWithDetails

@Dao
interface ContentDao {


    @Query("DELETE FROM content")
    suspend fun deleteAllContent()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: ContentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContentDetails(contentDetails: ContentDetailsEntity)

    @Query("SELECT * FROM `content`")
    suspend fun getAllContents(): List<ContentEntity>

    // Retrieve all ShopContent entities for a specific shop ID
    @Query("SELECT * FROM content WHERE mcnNid = :mcnNid")
    suspend fun getContentById(mcnNid: String): List<ContentEntity>

    // Retrieve all ShopContent entities for a specific shop ID
    @Query("SELECT * FROM content WHERE mcn_ntype = :nType")
    suspend fun getContentByNType(nType: String): List<ContentEntity>

    @Query("SELECT * FROM content WHERE mcn_map_location LIKE '%' || :mcnMapLocation || '%'")
    suspend fun getContentByLocation(mcnMapLocation: String): List<ContentEntity>

    @Query("SELECT * FROM content WHERE mcn_ntype = :nType AND mcn_category LIKE '%' || :categoryID || '%'")
    suspend fun getContentByNTypeAndCategory(nType: String, categoryID: String): List<ContentEntity>

    @Query("SELECT * FROM content WHERE mcn_ntype = :nType AND mcn_map_location LIKE '%' || :location || '%'")
    suspend fun getContentByNTypeAndLocation(nType: String, location: String): List<ContentEntity>

    @Query("SELECT * FROM content WHERE mcn_ntype = :nType AND mcn_category LIKE '%' || :categoryID || '%' AND mcn_map_location LIKE '%' || :location || '%'")
    suspend fun getContentByFilter(
        nType: String,
        categoryID: String,
        location: String
    ): List<ContentEntity>

    @Query("SELECT * FROM `content_details` WHERE mcn_language = :lang AND mcn_status = 1")
    suspend fun getAllContentDetails(lang: String): List<ContentDetailsEntity>

    @Query("SELECT * FROM content_details WHERE mcn_nid = :nid AND mcn_language = :language AND mcn_status = 1")
    suspend fun getContentDetailsByNid(nid: String, language: String): List<ContentDetailsEntity>

    @Query("SELECT * FROM 'content' WHERE mcnNid LIKE :query")
    suspend fun searchContents(query: String): List<ContentEntity>


    @Transaction
    @Query("SELECT * FROM content WHERE mcnNid IN (SELECT mcn_nid FROM content_details WHERE mcn_language = :language AND mcn_status = 1)")
    suspend fun getContentByLanguage(language: String): List<ContentEntity> // Get content that has matching language

    @Query("SELECT * FROM content_details WHERE mcn_language = :language AND mcn_nid IN (:mcnNidList) AND mcn_status = 1")
    suspend fun getContentDetailsByLanguage(
        language: String, mcnNidList: List<String>
    ): List<ContentDetailsEntity>


    @Transaction
    @Query("SELECT * FROM content WHERE mcnNid = :mcnNid")
    fun getContentWithDetailsById(mcnNid: String): ContentWithDetails
}
