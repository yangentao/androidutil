package dev.entao.base

fun String.head(n: Int): String {
    if (n <= 0) {
        return ""
    }
    if (this.length <= n) {
        return this
    }
    return this.substring(0, n)
}