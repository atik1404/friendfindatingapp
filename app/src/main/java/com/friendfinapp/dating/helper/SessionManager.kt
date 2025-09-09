package com.friendfinapp.dating.helper

import android.content.Context
import android.content.SharedPreferences
import com.friendfinapp.dating.helper.SessionHelper.AUTH_TOKEN
import com.friendfinapp.dating.helper.SessionHelper.ENGAGE_APP
import com.friendfinapp.dating.helper.SessionHelper.FB_LIKE
import com.friendfinapp.dating.helper.SessionHelper.HAS_SHOWN_ONCE
import com.friendfinapp.dating.helper.SessionHelper.IG_FOLLOW
import com.friendfinapp.dating.helper.SessionHelper.IS_LOGIN
import com.friendfinapp.dating.helper.SessionHelper.LOCALE
import com.friendfinapp.dating.helper.SessionHelper.NAV_INDICATOR
import com.friendfinapp.dating.helper.SessionHelper.PREF_NAME
import com.friendfinapp.dating.helper.SessionHelper.PROFILE_URI
import com.friendfinapp.dating.helper.SessionHelper.RATE_APP
import com.friendfinapp.dating.helper.SessionHelper.TOOLTIP
import com.friendfinapp.dating.helper.SessionHelper.TW_FOLLOW
import com.friendfinapp.dating.helper.SessionHelper.USER_Activate
import com.friendfinapp.dating.helper.SessionHelper.USER_COUNTRY
import com.friendfinapp.dating.helper.SessionHelper.USER_City
import com.friendfinapp.dating.helper.SessionHelper.USER_EMAIL
import com.friendfinapp.dating.helper.SessionHelper.USER_Fullname
import com.friendfinapp.dating.helper.SessionHelper.USER_Gender
import com.friendfinapp.dating.helper.SessionHelper.USER_ID
import com.friendfinapp.dating.helper.SessionHelper.USER_Interested
import com.friendfinapp.dating.helper.SessionHelper.USER_NAME
import com.friendfinapp.dating.helper.SessionHelper.USER_PROFILE
import com.friendfinapp.dating.helper.SessionHelper.USER_State
import com.friendfinapp.dating.helper.SessionHelper.USER_TOKEN
import com.friendfinapp.dating.helper.SessionHelper.USER_dateofbirth
import com.friendfinapp.dating.helper.SessionHelper.WHEEL_STATE
import com.friendfinapp.dating.helper.SessionHelper.isFavoriteData
import com.friendfinapp.dating.helper.SessionHelper.isFirstOpen

import java.util.*

class SessionManager(var context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sharedPreferences.edit()
    fun setFirstOpen(state: Boolean) {
        editor.putBoolean(isFirstOpen, state)
        editor.apply()
    }

    var navIndicator: Int
        get() = sharedPreferences.getInt(NAV_INDICATOR, 1)
        set(position) {
            editor.putInt(NAV_INDICATOR, position)
            editor.apply()
        }

    fun setFavoriteData(state: Boolean) {
        editor.putBoolean(isFavoriteData, state)
        editor.apply()
    }

    fun setAppRate(state: Boolean) {
        editor.putBoolean(RATE_APP, state)
        editor.apply()
    }

    fun setAppEngage(state: Boolean) {
        editor.putBoolean(ENGAGE_APP, state)
        editor.apply()
    }

    fun setFacebookLike(state: Boolean) {
        editor.putBoolean(FB_LIKE, state)
        editor.apply()
    }

    fun setInstagramFollow(state: Boolean) {
        editor.putBoolean(IG_FOLLOW, state)
        editor.apply()
    }

    fun setTwitterFollow(state: Boolean) {
        editor.putBoolean(TW_FOLLOW, state)
        editor.apply()
    }

    fun setTooltip(state: Boolean) {
        editor.putBoolean(TOOLTIP, state)
        editor.apply()
    }

    var locale: String?
        get() = sharedPreferences.getString(LOCALE, "fr-rDZ")
        set(locale) {
            editor.putString(LOCALE, locale)
            editor.apply()
        }


    var token: String?
        get() = sharedPreferences.getString(AUTH_TOKEN, "")
        set(token) {
            editor.putString(AUTH_TOKEN, token)
            editor.apply()
        }

    fun setInfo(id: String?, email: String?, name: String?, userFullName: String?,userInterest:Int?,userGender:Int?,userActivate:Int?,userCountry:String?,userState:String?,
    userCity:String?,userDateOfBirth:String?) {
        editor.putString(USER_ID, id)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_NAME, name)
        editor.putString(USER_Fullname, userFullName)
        editor.putInt(USER_Interested, userInterest!!)
        editor.putInt(USER_Gender, userGender!!)
        editor.putInt(USER_Activate, userActivate!!)
        editor.putString(USER_COUNTRY, userCountry)
        editor.putString(USER_City, userCity)
        editor.putString(USER_State, userState)
        editor.putString(USER_dateofbirth, userDateOfBirth)

        editor.apply()
    }

    fun setProfile(image:String){
        editor.putString(USER_PROFILE,image)
        editor.apply()
        editor.commit()
    }

    fun setPassword(newPassword:String){
        editor.putString(USER_ID, newPassword)
        editor.apply()
        editor.commit()
    }
    val getProfileImage:String
        get() = sharedPreferences.getString(USER_PROFILE,"").toString()

    fun setTokens(tokens:String){
        editor.putString(USER_TOKEN,tokens)
        editor.apply()
    }
    val getTokens: String?
        get() = sharedPreferences.getString(USER_TOKEN,"")



    val fullName: String?
        get() = sharedPreferences.getString(USER_Fullname,"")
    val username: String?
    get() = sharedPreferences.getString(USER_NAME,"")
    val password: String?
        get() = sharedPreferences.getString(USER_ID,"")
    val interest: Int
        get() = sharedPreferences.getInt(USER_Interested, 1)

    fun setShownDialogOnce(state: Boolean) {
        editor.putBoolean(HAS_SHOWN_ONCE, state)
        editor.apply()
    }

    fun hasShownDialogOnce(): Boolean {
        return sharedPreferences.getBoolean(HAS_SHOWN_ONCE, false)
    }

    var login: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGIN, false)
        set(loginState) {
            editor.putBoolean(IS_LOGIN, loginState)
            editor.apply()
        }
    val userId: String?
        get() = sharedPreferences.getString(USER_ID, "")

    fun setFavoriteDataRoom(): Boolean {
        return sharedPreferences.getBoolean(isFavoriteData, false)
    }

    val rateAppState: Boolean
        get() = sharedPreferences.getBoolean(RATE_APP, false)
    val rateEngageState: Boolean
        get() = sharedPreferences.getBoolean(ENGAGE_APP, false)
    val facebookLikeState: Boolean
        get() = sharedPreferences.getBoolean(FB_LIKE, false)
    val instagramFollowState: Boolean
        get() = sharedPreferences.getBoolean(IG_FOLLOW, false)
    val twitterFollowState: Boolean
        get() = sharedPreferences.getBoolean(TW_FOLLOW, false)
    val tooltip: Boolean
        get() = sharedPreferences.getBoolean(TOOLTIP, false)
    var wheelState: Boolean
        get() = sharedPreferences.getBoolean(WHEEL_STATE, true)
        set(state) {
            editor.putBoolean(WHEEL_STATE, state)
            editor.apply()
        }
    val loginInfo: HashMap<String, String?>
        get() {
            val hashMap = HashMap<String, String?>()
            hashMap[USER_ID] = sharedPreferences.getString(USER_ID, "")
            hashMap[USER_EMAIL] = sharedPreferences.getString(USER_EMAIL, "")
            hashMap[USER_NAME] = sharedPreferences.getString(USER_NAME, "")
            hashMap[PROFILE_URI] = sharedPreferences.getString(PROFILE_URI, "").toString()
            return hashMap
        }

    fun removeLoginInfo() {

    }

    companion object {
        const val PRIVATE_MODE = 0
    }
}