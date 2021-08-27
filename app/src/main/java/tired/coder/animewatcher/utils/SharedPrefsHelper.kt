package tired.coder.animewatcher.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class SharedPrefsHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {



    companion object{
        const val SHARED_PREF_FILE_NAME = "animewatcher_shared_prefs"
    }
    fun clearAll(){
        sharedPreferences.edit().clear().apply()
    }
    fun clear(key : String){
        sharedPreferences.edit().remove(key).apply()
    }
    fun put(key : String, value : String?){
        if(value == null) return
        sharedPreferences.edit().putString(key,value).apply()
    }

    fun put(key : String, value : Long?){
        if(value == null) return
        sharedPreferences.edit().putLong(key,value).apply()
    }

    fun put(key : String, value : Boolean){
        sharedPreferences.edit().putBoolean(key,value).apply()
    }

    fun getBoolean(key : String,defaultValue: Boolean  = false) : Boolean{
        return sharedPreferences.getBoolean(key,defaultValue)
    }


    fun getString(key : String, defaultValue : String? = null) =
        sharedPreferences.getString(key, defaultValue)

    fun getLong(key : String, defaultValue : Long = 0) =
        sharedPreferences.getLong(key, defaultValue)

}
