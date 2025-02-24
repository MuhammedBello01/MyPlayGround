package com.emperormoh.myplayground.utils

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class GenericTypeAdapterFactory: TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T> {
        val delegate = gson?.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>(){
            override fun write(out: JsonWriter?, value: T) {
                gson?.toJson(value, type?.type, out)
            }

            override fun read(reader: JsonReader?): T? {
                return gson?.fromJson(reader, type?.type)
            }

        }
    }
}