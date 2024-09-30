    package com.example.mycocktail.preferences

    import android.content.Context
    import android.content.SharedPreferences
    import com.example.mycocktail.utils.Constants

    class SharedPreferencesManager {

        private lateinit var _sharedPreferences: SharedPreferences

        fun initSharedWithContext(context: Context){
            _sharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        fun isSharedPreferencesEmpty(): Boolean {
            return _sharedPreferences.all.isEmpty()
        }

        fun getSharedPreferenceType(type: String):String?{
            return _sharedPreferences.getString(type,"")
        }
        fun getSharedPreferencesFavorite():String?{
            return _sharedPreferences.getString(Constants.FAVORITE_DRINK,"")
        }

        fun setSharedPreferencesType(type: String, responseString: String){
            val editor = _sharedPreferences.edit()
            editor.putString(type,responseString)
            editor.apply()
        }

        fun setSharedPreferencesFavorite(responseString: String){
            val editor = _sharedPreferences.edit()
            editor.putString(Constants.FAVORITE_DRINK,responseString)
            editor.apply()
        }

        fun isSharedPreferenceTypeIsEmpty(type: String): Boolean{
            return getSharedPreferenceType(type)?.isEmpty() ?: false
        }
        fun isSharedPreferencesFavoriteIsEmpty(): Boolean{
            return getSharedPreferencesFavorite()?.isEmpty() ?: false
        }

        fun cleanSharedPreferences() {
            val editor = _sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

    }