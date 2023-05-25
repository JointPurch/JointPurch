package com.example.jointpurch.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: String,
    var login: String,
    var passwordHash: String?
)