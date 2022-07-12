package com.gscapin.readbookcompose.model

data class User(
    val displayName: String,
    val id: String,
    val userId: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String,
    val email: String
) {

    fun toMap(): MutableMap<String, Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "id" to this.id,
            "avatarUrl" to this.avatarUrl,
            "quote" to this.quote,
            "profession" to this.profession,
            "email" to this.email
        )
    }
}
