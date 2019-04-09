package dev.entao.util.imgloader

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

interface DownListener {
	fun onDownload(url: String, ok: Boolean)
}