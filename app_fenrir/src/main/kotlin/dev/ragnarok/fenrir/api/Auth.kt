package dev.ragnarok.fenrir.api

import android.util.Log
import dev.ragnarok.fenrir.AccountType
import dev.ragnarok.fenrir.Constants
import dev.ragnarok.fenrir.Includes.provideApplicationContext
import dev.ragnarok.fenrir.api.util.VKStringUtils.extractPattern
import dev.ragnarok.fenrir.nonNullNoEmpty
import dev.ragnarok.fenrir.util.Utils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object Auth {
    const val redirect_url = "https://oauth.vk.com/blank.html"
    private const val TAG = "Fenrir.Auth"

    // Client IDs for Web Auth and Direct Auth
    const val ID_KATE = "2685278"
    const val SECRET_KATE = "lxhD8OD7dMsqtXIm5IUY"

    const val ID_ANDROID = "2274003"
    const val SECRET_ANDROID = "hHbZxrka2uZ6jB1inYsH"
    
    const val ID_IPHONE = "3140623"
    const val SECRET_IPHONE = "VeWdmVclDCtn6ihuP1nt"

    const val ID_IPAD = "3682744"
    const val SECRET_IPAD = "m7wByByS3uz63tY943s1"

    const val ID_WINDOWS_PHONE = "3697615"
    const val SECRET_WINDOWS_PHONE = "AlVXZFMUqyrnABp8ncuU"


    @Throws(UnsupportedEncodingException::class)
    fun getUrl(api_id: String, scope: String, groupIds: String?): String {
        // Optimized URL based on successful curl diagnostic
        var url = "https://oauth.vk.com/authorize?client_id=$api_id"
        url = (url + "&display=mobile&scope=" + URLEncoder.encode(scope, "utf-8")
                + "&redirect_uri=" + URLEncoder.encode(redirect_url, "utf-8")
                + "&response_type=token&v=5.131")
        
        if (groupIds.nonNullNoEmpty()) {
            url = "$url&group_ids=$groupIds"
        }
        return url
    }

    // Direct logic from vkhost: Uses specific scope masks and official IDs
    fun getMagicUrlAndroid(): String {
        return "https://oauth.vk.com/authorize?client_id=2274003&scope=1073737727&redirect_uri=https://oauth.vk.com/blank.html&display=mobile&response_type=token&v=5.131"
    }

    fun getMagicUrlIPhone(): String {
        return "https://oauth.vk.com/authorize?client_id=3140623&scope=1073737727&redirect_uri=https://oauth.vk.com/blank.html&display=mobile&response_type=token&v=5.131"
    }



    val scope: String
        get() = "notify,friends,photos,audio,video,docs,status,notes,pages,wall,groups,messages,offline,notifications,stories"
    
    val scopeAll: String = "all"


    @Throws(Exception::class)
    fun parseRedirectUrl(url: String): Array<String> {
        //url is something like http://api.vkontakte.ru/blank.html#access_token=66e8f7a266af0dd477fcd3916366b17436e66af77ac352aeb270be99df7deeb&expires_in=0&user_id=7657164
        val access_token = extractPattern(url, "access_token=(.*?)&")
        Log.i(TAG, "access_token=$access_token")
        val user_id = extractPattern(url, "user_id=(\\d*)")
        Log.i(TAG, "user_id=$user_id")
        if (user_id.isNullOrEmpty() || access_token.isNullOrEmpty()) {
            throw Exception("Failed to parse redirect url $url")
        }
        return arrayOf(access_token, user_id)
    }
}
