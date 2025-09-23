package com.hia.common.model.content

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.google.gson.annotations.SerializedName

@Entity(tableName = "content")
data class  ContentEntity(
    @SerializedName("mcn_nid") @PrimaryKey val mcnNid: String,
    @SerializedName("mcn_ntype") @ColumnInfo(name = "mcn_ntype") val mcnNtype: String? = null,
    @SerializedName("mcn_featured") @ColumnInfo(name = "mcn_featured") val mcnFeatured: Boolean? = null,
    @SerializedName("mcn_category") @ColumnInfo(name = "mcn_category") val mcnCategory: List<String>? = null, // List of categories
    @SerializedName("mcn_map_location") @ColumnInfo(name = "mcn_map_location") val mcnMapLocation: List<String>? = null, // List of map locations
//    @SerializedName("mcn_website_link_text") @ColumnInfo(name = "mcn_website_link_text") val mcnWebsiteText: String? = null, // List of map locations
    @SerializedName("mcn_content_logo") @ColumnInfo(name = "mcn_content_logo") val mcnContentLogo: String? = null,
//    @SerializedName("mcn_website") @ColumnInfo(name = "mcn_website") val mcnWebsite: String? = null,
//    @SerializedName("mcn_external") @ColumnInfo(name = "mcn_external") val mcnExternal: Boolean? = null,
    @SerializedName("mcn_counter_number") @ColumnInfo(name = "mcn_counter_number") val mcnCounterNumber: String? = null,
    @SerializedName("mcn_email") @ColumnInfo(name = "mcn_email") val mcnEmail: String? = null,
    @SerializedName("mcn_telephone") @ColumnInfo(name = "mcn_telephone") val mcnTelephone: String? = null

    )

@Entity(
    tableName = "content_details", indices = [Index(
        value = ["mcn_nid", "mcn_language"], unique = true
    )] // Unique constraint on mcnNid and mcnLanguage
)
data class ContentDetailsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @SerializedName("mcn_language") @ColumnInfo(name = "mcn_language") val mcnLanguage: String? = "",

    @SerializedName("mcn_title") @ColumnInfo(name = "mcn_title") val mcnTitle: String? = "",

    @SerializedName("mcn_body") @ColumnInfo(name = "mcn_body") val mcnBody: String? = "",

    @SerializedName("mcn_created_on") @ColumnInfo(name = "mcn_created_on") val mcnCreatedOn: Long? = null,

    @SerializedName("mcn_modified_on") @ColumnInfo(name = "mcn_modified_on") val mcnModifiedOn: Long? = null,

    @SerializedName("mcn_status") @ColumnInfo(name = "mcn_status") val mcnStatus: Int? = null,

    @SerializedName("mcn_header_image") @ColumnInfo(name = "mcn_header_image") val mcnHeaderImage: List<String>? = arrayListOf(),// Store images as CSV or JSON

    @ColumnInfo(name = "mcn_nid") var mcnNid: String? = "", // Foreign key referencing ContentEntity

    @ColumnInfo(name = "mcn_type") var mcnType: String? = "", // Foreign key referencing ContentEntity
    @SerializedName("mcn_website_link_text") @ColumnInfo(name = "mcn_website_link_text") val mcnWebsiteText: String? = null, // List of map locations
    @SerializedName("mcn_website") @ColumnInfo(name = "mcn_website") val mcnWebsite: String? = null,
    @SerializedName("mcn_external") @ColumnInfo(name = "mcn_external") val mcnExternal: Boolean? = null,

    @SerializedName("mcn_address") @ColumnInfo(name = "mcn_address") val mcnAddress: String? = null,
    @SerializedName("mcn_po_box") @ColumnInfo(name = "mcn_po_box") val mcnPoBox: String? = null,


)


data class ContentWithDetails(
    @Embedded val contentWithEntity: ContentEntity? = null, @Relation(
        parentColumn = "mcnNid", entityColumn = "mcn_nid"
    ) val contentDetailsEntityList: List<ContentDetailsEntity> = listOf()
)