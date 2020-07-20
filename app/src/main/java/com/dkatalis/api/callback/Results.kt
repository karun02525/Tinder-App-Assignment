package com.dkatalis.api.callback

import com.google.gson.annotations.SerializedName
import com.dkatalis.model.User

data class Results(
    @SerializedName("user") val user: User
)

data class UserCallback(
    @SerializedName("results") val results: List<Results>
)