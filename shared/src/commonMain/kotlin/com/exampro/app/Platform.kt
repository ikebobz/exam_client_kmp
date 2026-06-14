package com.exampro.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform