@file:Suppress("unused")

package yet.util

import android.annotation.TargetApi
import android.os.Build
import android.os.Environment
import yet.log.logd
import yet.util.app.App
import java.io.File
import java.io.IOException

object MyFiles {

    fun ensureDir(root: File, dir: String): File {
        val f = File(root, dir)
        if (!f.exists()) {
            f.mkdirs()
            f.mkdir()
            try {
                File(f, ".nomedia").createNewFile()
            } catch (e: IOException) {
                logd(f.absolutePath)
                e.printStackTrace()
            }
        }
        return f
    }

    object app {
        val filesDir: File = App.app.filesDir

        val cacheDir: File = App.app.cacheDir

        fun cacheFile(fileName: String): File {
            return ensureDir(cacheDir, fileName)
        }

        fun dir(dirName: String): File {
            return ensureDir(filesDir, dirName)
        }

        fun userDir(userName: String): File {
            return dir(Hex.encodeString(userName))
        }

        fun userFile(userName: String, fileName: String): File {
            return File(userDir(userName), fileName)
        }


        val logDir: File
            get() {
                return dir("xlog")
            }

        fun log(file: String): File {
            return File(logDir, file)
        }

        val tempDir: File
            get() {
                return cacheDir
            }


        fun tempFile(): File {
            return tempFile(".tmp")
        }

        fun tempFile(ext: String): File {
            var dotExt = ".tmp"
            if (ext.isNotEmpty()) {
                dotExt = if (ext[0] == '.') {//.x
                    ext
                } else {
                    ".$ext"
                }
            }
            return temp(MyDate.tmpFile() + dotExt)
        }

        fun temp(file: String): File {
            return File(tempDir, file)
        }

        val imageDir: File
            get() {
                return dir("image")
            }

        fun image(file: String): File {
            return File(imageDir, file)
        }
    }


    object ex {
        val filesDir: File = App.app.getExternalFilesDir(null)
        val cacheDir: File = App.app.externalCacheDir

        fun cacheFile(fileName: String): File {
            return ensureDir(cacheDir, fileName)
        }

        fun dir(dirName: String): File {
            return ensureDir(filesDir, dirName)
        }

        fun userDir(userName: String): File {
            return dir(Hex.encodeString(userName))
        }

        fun userFile(userName: String, fileName: String): File {
            return File(userDir(userName), fileName)
        }

        val logDir: File
            get() {
                return dir("xlog")
            }

        fun log(file: String): File {
            return File(logDir, file)
        }

        val tempDir: File
            get() {
                return cacheDir
            }

        fun tempFile(): File {
            return tempFile(".tmp")
        }

        fun tempFile(ext: String): File {
            var dotExt = ".tmp"
            if (ext.isNotEmpty()) {
                dotExt = if (ext[0] == '.') {//.x
                    ext
                } else {
                    ".$ext"
                }
            }
            return temp(MyDate.tmpFile() + dotExt)
        }

        fun temp(file: String): File {
            return File(tempDir, file)
        }

        val imageDir: File
            get() {
                return dir("image")
            }

        fun image(file: String): File {
            return File(imageDir, file)
        }
    }

    object pub {
        val root: File get() = Environment.getExternalStorageDirectory()
        val downloads: File get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val music: File get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        val pictures: File get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val dcim: File get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val documents: File
            @TargetApi(Build.VERSION_CODES.KITKAT)
            get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val movies: File get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    }
}