package com.example.hackatonprjoect.core.fomatters

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class JsonTransformerClass(private val context: Context, private val filename: String = "content.json") {

    private val rawJson: String = readJsonFromAssets() // read immediately in constructor

    private fun readJsonFromAssets(): String =
        context.assets.open(filename).bufferedReader().use { it.readText() }

    fun getSimplifiedJsonString(): String {
        val simplified = simplifyJson(rawJson)
        return simplified.toString()
    }

    /** Simplify JSON into {visioId: {name, description, category}} format */
    private fun simplifyJson(jsonString: String): String {
        val result = mutableMapOf<String, Map<String, Any>>()

        val rootArray = try {
            JSONArray(jsonString) // top-level array
        } catch (e: Exception) {
            val rootObj = JSONObject(jsonString)
            rootObj.optJSONArray("nodes") ?: JSONArray()
        }

        for (i in 0 until rootArray.length()) {
            val node = rootArray.optJSONObject(i) ?: continue

            val mapLocations = node.optJSONArray("mcn_map_location")
            val ids: List<String> = if (mapLocations != null && mapLocations.length() > 0) {
                (0 until mapLocations.length()).map { mapLocations.optString(it).trim() }
            } else {
                listOf(node.optString("mcn_nid").trim())
            }

            val contents = node.optJSONArray("mcn_content") ?: JSONArray()
            val content = (0 until contents.length())
                .mapNotNull { contents.optJSONObject(it) }
                .firstOrNull { it.optString("mcn_language","").equals("en", true) }
                ?: contents.optJSONObject(0)

            val name = content?.optString("mcn_title", "") ?: ""
            val desc = content?.optString("mcn_body", "")
                ?.replace(Regex("<.*?>"), " ")
                ?.replace(Regex("\\s+"), " ")
                ?.trim() ?: ""

            val type = node.optString("mcn_ntype", "")

            ids.forEach { id ->
                if (id.isNotEmpty()) {
                    result[id] = mapOf(
                        "name" to name,
                        "description" to desc,
                        "category" to listOf(type)
                    )
                }
            }
        }

        return JSONObject(result as Map<*, *>).toString()
    }

    /** Get VisioGlobe IDs for a given MCN ID */
    fun getVisioGlobeIdsForMcnNid(mcnNid: String): List<String> {
        val ids = mutableListOf<String>()

        val rootArray: JSONArray = try {
            JSONArray(rawJson) // root is an array
        } catch (e: Exception) {
            // fallback: root is object
            val rootObj = JSONObject(rawJson)
            rootObj.optJSONArray("nodes") ?: JSONArray()
        }

        for (i in 0 until rootArray.length()) {
            val node = rootArray.optJSONObject(i) ?: continue
            if (node.optString("mcn_nid") == mcnNid) {
                val mapLocations = node.optJSONArray("mcn_map_location") ?: JSONArray()
                for (j in 0 until mapLocations.length()) {
                    ids.add(mapLocations.optString(j))
                }
                break
            }
        }

        return ids
    }
}
