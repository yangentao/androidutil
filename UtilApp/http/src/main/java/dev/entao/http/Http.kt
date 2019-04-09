@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package dev.entao.http


import android.app.Application
import android.net.Uri
import android.os.NetworkOnMainThreadException
import android.util.Base64
import dev.entao.base.Progress
import dev.entao.base.closeSafe
import dev.entao.base.copyStream
import dev.entao.base.head
import dev.entao.json.YsonObject
import dev.entao.log.log
import dev.entao.log.logd
import dev.entao.log.loge
import java.io.*
import java.net.*
import java.nio.charset.Charset
import java.util.*
import java.util.zip.GZIPInputStream

/**
 * Created by entaoyang@163.com on 2015-11-20.
 */


class Http(val url: String) {
    enum class HttpMethod {
        GET, POST, POST_MULTIPART, POST_RAW_DATA
    }


    var reqCharset = Charsets.UTF_8

    private val BOUNDARY = UUID.randomUUID().toString()
    private val BOUNDARY_START = "--$BOUNDARY\r\n"
    private val BOUNDARY_END = "--$BOUNDARY--\r\n"


    private var method: HttpMethod = HttpMethod.GET

    private val headerMap = HashMap<String, String>()
    val argMap = HashMap<String, String>()

    private val fileList = ArrayList<FileParam>()

    private var timeoutConnect = 20000
    private var timeoutRead = 20000
    //	private var rawData: ByteArray? = null
    private var rawData: ByteArray? = null

    private var saveToFile: File? = null
    private var progress: Progress? = null

    private var retryTimes: Int = 0
    private var retryDelay: Int = 0

    //返回true, 会执行重试
    private var retryBlock: (HttpResult) -> Boolean = {
        !it.OK
    }

    init {
        userAgent("android")
        accept("application/json,text/plain,text/html,*/*")
        acceptLanguage("zh-CN,en-US;q=0.8,en;q=0.6")
        headerMap["Accept-Charset"] = "UTF-8,*"
        headerMap["Connection"] = "close"

    }

    fun charsetUTF8(): Http {
        reqCharset = Charsets.UTF_8
        return this
    }

    fun charset8859_1(): Http {
        reqCharset = Charsets.ISO_8859_1
        return this
    }

    fun charsetGBK(): Http {
        try {
            reqCharset = Charset.forName("GBK")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return this
    }

    fun charset(cs: Charset): Http {
        reqCharset = cs
        return this
    }

    fun retry(times: Int, delay: Int = 100) {
        retry(times, delay) {
            !it.OK
        }
    }

    //block 返回true, 会执行重试
    fun retry(times: Int, delay: Int = 100, block: (HttpResult) -> Boolean) {
        retryTimes = times
        retryDelay = delay
        retryBlock = block
    }

    fun saveTo(file: File): Http {
        this.saveToFile = file
        return this
    }

    //recv progress
    fun progress(p: Progress?): Http {
        this.progress = p
        return this
    }

    fun header(vararg pairs: Pair<String, String>): Http {
        for ((k, v) in pairs) {
            headerMap[k] = v
        }
        return this
    }

    fun header(key: String, value: String): Http {
        headerMap[key] = value
        return this
    }

    fun headers(map: Map<String, String>): Http {
        headerMap.putAll(map)
        return this
    }

    fun timeoutConnect(millSeconds: Int): Http {
        this.timeoutConnect = millSeconds
        return this
    }

    fun timeoutRead(millSeconds: Int): Http {
        this.timeoutRead = millSeconds
        return this
    }

    /**
     * @param accept "* / *", " plain/text"
     * *
     * @return
     */
    fun accept(accept: String): Http {
        headerMap["Accept"] = accept
        return this
    }

    fun acceptLanguage(acceptLanguage: String): Http {
        headerMap["Accept-Language"] = acceptLanguage
        return this
    }

    fun auth(user: String, pwd: String): Http {
        val usernamePassword = "$user:$pwd"
        val encodedUsernamePassword = Base64.encodeToString(usernamePassword.toByteArray(reqCharset), Base64.NO_WRAP)
        headerMap["Authorization"] = "Basic $encodedUsernamePassword"
        return this
    }

    fun userAgent(userAgent: String): Http {
        return header("User-Agent", userAgent)
    }

    infix fun String.to(v: String) {
        arg(this, v)
    }

    infix fun String.to(v: Int) {
        arg(this, v.toString())
    }

    infix fun String.to(v: Long) {
        arg(this, v.toString())
    }

    infix fun String.to(v: Double) {
        arg(this, v.toString())
    }

    infix fun String.to(v: Boolean) {
        arg(this, v.toString())
    }

    fun arg(key: String, value: String): Http {
        argMap[key] = value
        return this
    }

    fun arg(key: String, value: Long): Http {
        argMap[key] = "" + value
        return this
    }

    fun arg(key: String, value: Int): Http {
        argMap[key] = "" + value
        return this
    }

    fun arg(key: String, value: Double): Http {
        argMap[key] = "" + value
        return this
    }

    fun arg(key: String, value: Boolean): Http {
        argMap[key] = "" + value
        return this
    }

    fun args(vararg args: Pair<String, String>): Http {
        for ((k, v) in args) {
            argMap[k] = v
        }
        return this
    }

    fun args(map: Map<String, String>): Http {
        argMap.putAll(map)
        return this
    }

    fun args(yo: YsonObject): Http {
        yo.keys.forEach {
            argMap[it] = yo.str(it) ?: ""
        }
        return this
    }


    fun file(fileParam: FileParam): Http {
        fileList.add(fileParam)
        return this
    }

    fun file(key: String, file: Uri): Http {
        val p = FileParam(key, file)
        return file(p)
    }

    fun file(key: String, file: File): Http {
        return file(key, Uri.fromFile(file))
    }

    fun file(key: String, file: Uri, block: FileParam.() -> Unit): Http {
        val p = FileParam(key, file)
        p.block()
        return file(p)
    }


    /**
     * [from, to]

     * @param from
     * *
     * @param to
     * *
     * @return
     */
    fun range(from: Int, to: Int): Http {
        headerMap["Range"] = "bytes=$from-$to"
        return this
    }

    fun range(from: Int): Http {
        headerMap["Range"] = "bytes=$from-"
        return this
    }

    @Throws(ProtocolException::class, UnsupportedEncodingException::class)
    private fun preConnect(connection: HttpURLConnection) {
        HttpURLConnection.setFollowRedirects(true)
        connection.doOutput = method != HttpMethod.GET
        connection.doInput = true
        connection.connectTimeout = timeoutConnect
        connection.readTimeout = timeoutRead
        if (method == HttpMethod.GET) {
            connection.requestMethod = "GET"
        } else {
            connection.requestMethod = "POST"
            connection.useCaches = false
        }

        for (e in headerMap.entries) {
            connection.setRequestProperty(e.key, e.value)
        }
        if (fileList.size > 0) {
            val os = SizeStream()
            sendMultipart(os)
            connection.setFixedLengthStreamingMode(os.size)
        }
    }

    @Throws(IOException::class)
    private fun write(os: OutputStream, vararg arr: String) {
        for (s in arr) {
            os.write(s.toByteArray(reqCharset))
        }
    }

    @Throws(IOException::class)
    private fun sendMultipart(os: OutputStream) {

        if (argMap.size > 0) {
            for (e in argMap.entries) {
                write(os, BOUNDARY_START)
                write(os, "Content-Disposition: form-data; name=\"", e.key, "\"\r\n")
                write(os, "Content-Type:text/plain;charset=${reqCharset.name()}\r\n")
                write(os, "\r\n")
                write(os, e.value, "\r\n")
            }
        }
        if (fileList.size > 0) {
            val appCtx = appContext
                ?: throw IllegalArgumentException("You should set Http.appContext only once before use multipart method")
            for (fp in fileList) {
                write(os, BOUNDARY_START)
                write(os, "Content-Disposition:form-data;name=\"${fp.key}\";filename=\"${fp.filename}\"\r\n")
                write(os, "Content-Type:${fp.mime}\r\n")
                write(os, "Content-Transfer-Encoding: binary\r\n")
                write(os, "\r\n")
                val progress = fp.progress
                val fis = appCtx.contentResolver.openInputStream(fp.file)
                val total = fis.available()
                if (os is SizeStream) {
                    os.incSize(total)
                    fis.closeSafe()
                } else {
                    copyStream(fis, true, os, false, total, progress)
                }
                write(os, "\r\n")
            }
        }
        os.write(BOUNDARY_END.toByteArray())
    }

    private fun buildArgs(): String {
        val sb = StringBuilder(argMap.size * 32 + 16)
        for (e in argMap.entries) {
            try {
                val name = URLEncoder.encode(e.key, reqCharset.name())
                val value = URLEncoder.encode(e.value, reqCharset.name())
                if (sb.isNotEmpty()) {
                    sb.append("&")
                }
                sb.append(name)
                sb.append("=")
                sb.append(value)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }
        return sb.toString()
    }

    @Throws(MalformedURLException::class)
    fun buildGetUrl(): String {
        val sArgs = buildArgs()
        var u: String = url
        if (sArgs.isNotEmpty()) {
            val n = u.indexOf('?')
            if (n < 0) {
                u += "?"
            }
            if ('?' != u[u.length - 1]) {
                u += "&"
            }
            u += sArgs
        }
        return u
    }

    @Throws(IOException::class)
    private fun onResponse(connection: HttpURLConnection): HttpResult {
        val result = HttpResult()
        result.responseCode = connection.responseCode
        result.responseMsg = connection.responseMessage
        result.contentType = connection.contentType
        result.headerMap = connection.headerFields
        val total = connection.contentLength
        result.contentLength = total

        logd("--HttpStatus:", result.responseCode, result.responseMsg ?: "")

        val os: OutputStream = if (this.saveToFile != null) {
            val dir = this.saveToFile!!.parentFile
            if (dir != null) {
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        loge("创建目录失败")
                        throw IOException("创建目录失败!")
                    }
                }
            }
            FileOutputStream(saveToFile)
        } else {
            ByteArrayOutputStream(if (total > 0) total else 64)
        }
        var input = connection.inputStream
        val mayGzip = connection.contentEncoding
        if (mayGzip != null && mayGzip.contains("gzip")) {
            input = GZIPInputStream(input)
        }
        copyStream(input, true, os, true, total, progress)
        if (os is ByteArrayOutputStream) {
            result.response = os.toByteArray()
        }
        return result
    }

    @Throws(IOException::class)
    private fun onSend(connection: HttpURLConnection) {
        if (HttpMethod.GET == method) {
            return
        }
        val os = connection.outputStream
        try {
            when (method) {
                HttpMethod.POST -> {
                    val s = buildArgs()
                    if (s.isNotEmpty()) {
                        write(os, s)
                    }
                }
                HttpMethod.POST_MULTIPART -> sendMultipart(os)
                HttpMethod.POST_RAW_DATA -> os.write(rawData!!)
                else -> {
                }
            }
            os.flush()
        } finally {
            os.closeSafe()
        }
    }

    private fun request(): HttpResult {
        logd("Charset: ", reqCharset.name())
        var r = request2()
        if (!retryBlock(r)) {
            return r
        }
        var times = retryTimes
        val delay = retryDelay
        while (times > 0) {
            times -= 1
            if (delay > 0) {
                try {
                    Thread.sleep(delay.toLong())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            r = request2()
            if (!retryBlock(r)) {
                return r
            }
        }
        return r
    }

    private fun request2(): HttpResult {
        var connection: HttpURLConnection? = null
        try {
            log("Http Request:", url)
            for ((k, v) in argMap) {
                log("--arg:", k, "=", v.head(256))
            }
            for (fp in fileList) {
                log("--file:", fp)
            }
            connection = if (method == HttpMethod.GET || method == HttpMethod.POST_RAW_DATA) {
                URL(buildGetUrl()).openConnection() as HttpURLConnection
            } else {
                URL(url).openConnection() as HttpURLConnection
            }

            preConnect(connection)
            connection.connect()
            onSend(connection)
            return onResponse(connection)
        } catch (ex: Exception) {
            if (ex is NetworkOnMainThreadException) {
                loge("主线程中使用了网络请求!")
            }
            ex.printStackTrace()
            loge(ex)
            val result = HttpResult()
            result.exception = ex
            return result
        } finally {
            connection?.disconnect()
        }
    }

    fun get(): HttpResult {
        method = HttpMethod.GET
        return request()
    }

    fun post(): HttpResult {
        method = HttpMethod.POST
        header("Content-Type", "application/x-www-form-urlencoded;charset=${reqCharset.name()}")
        return request()
    }

    fun multipart(): HttpResult {
        method = HttpMethod.POST_MULTIPART
        header("Content-Type", "multipart/form-data; boundary=$BOUNDARY")
        return request()
    }

    fun postRawData(contentType: String, data: ByteArray): HttpResult {
        method = HttpMethod.POST_RAW_DATA
        header("Content-Type", contentType)
        this.rawData = data
        return request()
    }

    fun postRawJson(json: String): HttpResult {
        return postRawData("text/json;charset=utf-8", json.toByteArray(Charsets.UTF_8))
    }

    fun postRawXML(xml: String): HttpResult {
        return postRawData("text/xml;charset=utf-8", xml.toByteArray(Charsets.UTF_8))
    }

    fun download(saveto: File, progress: Progress?): HttpResult {
        return saveTo(saveto).progress(progress).get()
    }

    companion object {
        var appContext: Application? = null
    }

}

