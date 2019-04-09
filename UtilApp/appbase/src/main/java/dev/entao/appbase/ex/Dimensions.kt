package dev.entao.appbase.ex

import dev.entao.appbase.App

val Int.dp :Int get() = App.dp2px(this)