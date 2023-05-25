package com.example.jointpurch

import android.content.Context
import android.content.SharedPreferences
import com.example.jointpurch.data.User

object SharedPreferenceManager{
    fun saveUser(context: Context, user: User){
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("id", user.id)
        editor.putString("login", user.login)
        editor.putString("password", user.passwordHash)
        editor.apply()
    }

    fun getUser(context: Context): User{
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return User(
            sharedPreferences.getString("id", null)!!,
            sharedPreferences.getString("login", null)!!,
            sharedPreferences.getString("password", null)!!
        )
    }
}