package com.example.kmprpc

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform