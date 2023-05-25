package com.example.jointpurch

import android.content.Context
import com.example.jointpurch.data.User
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.security.MessageDigest
import java.util.UUID

const val SERVER_ADDRESS = "http://192.168.0.116:8080/"

class RestApi(private val context: Context){
    private var client: HttpClient
    init {
        try {
            val user = SharedPreferenceManager.getUser(context)
            client = HttpClient(CIO) {
                install(Auth) {
                    basic {
                        credentials {
                            BasicAuthCredentials(username = user.id, password = user.passwordHash!!)
                        }
                        realm = "Access to the '/' path"
                    }
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
            client = HttpClient(CIO){
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
            }
        }
    }

    fun register(name: String, password: String){
        val passwordHash: String = MessageDigest.getInstance("SHA-512")
            .digest(password.toByteArray())
            .joinToString(separator = "") {
                ((it.toInt() and 0xff) + 0x100)
                    .toString(16)
                    .substring(1)
            }

        val newUser = User(
            UUID.randomUUID().toString(),
            name,
            passwordHash
        )

        GlobalScope.launch {
            client.post(SERVER_ADDRESS + "user/register") {
                contentType(ContentType.Application.Json)
                setBody(newUser)
            }
        }
    }
}