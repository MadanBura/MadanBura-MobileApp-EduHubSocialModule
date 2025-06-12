package com.connect.eduhubconnect.utils

import com.connect.eduhubconnect.model.ChatMessage
import com.google.gson.Gson

fun ChatMessage.toJson(): String = Gson().toJson(this)