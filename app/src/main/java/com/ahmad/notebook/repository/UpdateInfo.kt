package com.ahmad.notebook.repository

data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val updateMessage: String,
    val downloadUrl: String
)

