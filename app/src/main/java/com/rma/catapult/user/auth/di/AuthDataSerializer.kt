package com.rma.catapult.user.auth.di

import androidx.datastore.core.Serializer
import com.rma.catapult.networking.serialization.AppJson
import com.rma.catapult.user.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class AuthDataSerializer : Serializer<User> {

    private val json: Json = AppJson
    override val defaultValue: User = User("", "", "", "")

    override suspend fun readFrom(input: InputStream): User {
        val text = String(input.readBytes(), Charsets.UTF_8)

        return try {
            json.decodeFromString(User.serializer(), text)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        val text = json.encodeToString(User.serializer(), t)
        withContext(Dispatchers.IO) {
            output.write(text.toByteArray())
        }
    }
}