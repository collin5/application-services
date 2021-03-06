/* Copyright 2018 Mozilla
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. */
package org.mozilla.sync15.logins
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject;

/**
 * Raw password data that is stored by the LoginsStorage implementation.
 */
data class ServerPassword (

     /**
      * The unique ID associated with this login.
      *
      * It is recommended that you not make assumptions about its format, but in practice it is
      * typically (but not guaranteed to be) either 12 random Base64URL-safe characters or a
      * UUID-v4 surrounded in curly-braces.
      */
    val id: String,

    /**
     * The hostname this record corresponds to. It is an error to
     * attempt to insert or update a record to have a blank hostname.
     */
    val hostname: String,

    val username: String?,

    /**
     * The password field of this record. It is an error to attempt to insert or update
     * a record to have a blank password.
     */
    val password: String,

    /**
     * The HTTP realm, which is the challenge string for HTTP Basic Auth). May be null in the case
     * that this login has a formSubmitURL instead.
     */
    val httpRealm: String? = null,

    /**
     * The formSubmitURL (as a string). This may be null in the case that this login has a
     * httpRealm instead.
     */
    val formSubmitURL: String? = null,

    /**
     * Number of times this password has been used.
     */
    val timesUsed: Int,

    /**
     * Time of creation in milliseconds from the unix epoch.
     */
    val timeCreated: Long,

    /**
     * Time of last use in milliseconds from the unix epoch.
     */
    val timeLastUsed: Long,

    /**
     * Time of last password change in milliseconds from the unix epoch.
     */
    val timePasswordChanged: Long,

    val usernameField: String? = null,
    val passwordField: String? = null
) {

    fun toJSON(): JSONObject {
        val o = JSONObject()
        o.put("id", this.id)
        o.put("hostname", this.hostname)
        o.put("password", password)
        o.put("timesUsed", timesUsed)
        o.put("timeCreated", timeCreated)
        o.put("timeLastUsed", timeLastUsed)
        o.put("timePasswordChanged", timePasswordChanged)
        if (username != null) {
            o.put("username", username)
        }
        if (httpRealm != null) {
            o.put("httpRealm", httpRealm)
        }
        if (formSubmitURL != null) {
            o.put("formSubmitURL", formSubmitURL)
        }
        if (usernameField != null) {
            o.put("usernameField", usernameField)
        }
        if (passwordField != null) {
            o.put("passwordField", passwordField)
        }
        return o
    }

    companion object {
        fun fromJSON(jsonObject: JSONObject): ServerPassword {

            return ServerPassword(
                    id = jsonObject.getString("id"),

                    hostname = jsonObject.getString("hostname"),
                    password = jsonObject.getString("password"),
                    username = jsonObject.optString("username", null),

                    httpRealm = jsonObject.optString("httpRealm", null),
                    formSubmitURL = jsonObject.optString("formSubmitURL", null),

                    usernameField = jsonObject.optString("usernameField", null),
                    passwordField = jsonObject.optString("passwordField", null),

                    timesUsed = jsonObject.getInt("timesUsed"),

                    timeCreated = jsonObject.getLong("timeCreated"),
                    timeLastUsed = jsonObject.getLong("timeLastUsed"),
                    timePasswordChanged = jsonObject.getLong("timePasswordChanged")
            )
        }


        fun fromJSON(jsonText: String): ServerPassword {
            return fromJSON(JSONObject(jsonText))
        }

        fun fromJSONArray(jsonArrayText: String): List<ServerPassword> {
            val result: MutableList<ServerPassword> = mutableListOf();
            val array = JSONArray(jsonArrayText);
            for (index in 0 until array.length()) {
                result.add(fromJSON(array.getJSONObject(index)));
            }
            return result
        }

    }
}
