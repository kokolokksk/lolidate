package monster.loli.lolidate.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import monster.loli.lolidate.utils.LoveLoliiiUtils.Companion.getFileList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URI
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest
import kotlin.io.path.getAttribute
import kotlin.io.path.pathString

val log:Logger = LoggerFactory.getLogger(LoveLoliiiUtils::class.java)
class LoveLoliiiUtils{

    companion object {
        fun toHexStr(byteArray: ByteArray) =
            with(StringBuilder()) {
                byteArray.forEach {
                    val hex = it.toInt() and (0xFF)
                    val hexStr = Integer.toHexString(hex)
                    if (hexStr.length == 1) append("0").append(hexStr)
                    else append(hexStr)
                }
                toString()
            }
        /**
         * 获取某目录下的目录结构
         * */
        fun getFileList(start:Path): LinkedHashMap<String, Any> {
            var allFileList :ArrayList<LinkedHashMap<String,Any>> = ArrayList()
            var fileList:LinkedHashMap<String,Any> = LinkedHashMap()
            Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
                // 在访问子目录前触发该方法
                @Throws(IOException::class)
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    log.info("访问${dir}目录前")
                    return FileVisitResult.CONTINUE
                }

                // 在访问文件时触发该方法
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {

                    var fileProperties:LinkedHashMap<String,Any> = LinkedHashMap()
                    fileProperties["file_name"] = file.fileName.toString()
                    fileProperties["file_path"] = file.pathString
                    var fileByte:ByteArray =file.toFile().readBytes()
                    fileProperties["hash"] = toHexStr(MessageDigest.getInstance("SHA-1").digest(fileByte))
                    allFileList.add(fileProperties)
                    log.info("正在访问${file}文件")
                    return FileVisitResult.CONTINUE
                }


                // 在访问目录之后触发该方法
                override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {

                    log.info("正在访问${dir}目录")
                    return super.postVisitDirectory(dir, exc)
                }
            })
            fileList["files"] = allFileList
            fileList["application"] =start.fileName.toString().split(File.separator)[start.fileName.toString().split(File.separator).size-1]
            return fileList
        }
        /**
         * 查找文件
         * */
        fun searchFile(start:Path,fileList: String): ArrayList<HashMap<String, Any>> {
            var b:Boolean = false
            var fList :ArrayList<HashMap<String,Any>> = ArrayList()
            Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
                // 在访问子目录前触发该方法
                @Throws(IOException::class)
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    log.info("访问${dir}目录前")
                    val dirName :String= dir.fileName.toString();
                    if(dir.toString().split(File.separator)[dir.toString().split(File.separator).size-2] == "catcatdm"){
                        val resultData:HashMap<String, Any> = HashMap()
                        resultData["version"]=dirName
                        resultData["path"]=dir.pathString
                        resultData["exist"]=false
                        fList.add(resultData)
                    }
                    return FileVisitResult.CONTINUE
                }

                // 在访问文件时触发该方法
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {

                    log.info("正在访问${file}文件")
                    if(file.fileName.toString().contains(fileList)){
                        val key:String = file.toString().replace(fileList,"").split(File.separator)[file.toString().replace(fileList,"").split(File.separator).size-1]
                        fList.forEach { m->
                            if(m["version"]?.equals(key) == true){
                                m["exist"]= true
                            }

                        }

                        log.info("find file ${file.fileName}")
                        b = true
                        return FileVisitResult.CONTINUE
                    }
                    return FileVisitResult.CONTINUE
                }


                // 在访问目录之后触发该方法
                override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
                    log.info("正在访问${dir}目录")
                    return super.postVisitDirectory(dir, exc)
                }
            })


            return fList
        }
    }



}
fun main(array: Array<String>){
    log.info("test fun")
    //LoveLoliiiUtils.getFileList(Paths.get("f:","catcatdm"),1);
    val fileList:String = "file.list"
    val exist: ArrayList<HashMap<String, Any>> = LoveLoliiiUtils.searchFile(Paths.get("f:","catcatdm\\1.0.6"),fileList)
    val fileListMap :LinkedHashMap<String,Any> = getFileList(Paths.get("f:","catcatdm\\1.0.6"))
    val gson = Gson()
    log.info(gson.toJson(fileListMap))
    log.info("is exist $exist")
    exist.forEach{
        if(!(it["exist"] as Boolean)){
            val fl:LinkedHashMap<String,Any>  = LoveLoliiiUtils.getFileList(Paths.get(it["path"].toString().split("\\")[0],it["path"].toString().split("\\")[1]))
            val listFile = File(it["path"] as String + "\\file.list")
            listFile.writeBytes(Gson().toJson(fl).toByteArray())
        }
    }
}