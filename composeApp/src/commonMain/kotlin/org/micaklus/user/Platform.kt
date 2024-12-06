package org.micaklus.user

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

