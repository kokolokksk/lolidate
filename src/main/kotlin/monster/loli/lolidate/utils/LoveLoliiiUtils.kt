package monster.loli.lolidate.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import monster.loli.lolidate.utils.LoveLoliiiUtils.Companion.getFileList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.security.MessageDigest
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
                        val key:String = file.toString().split(File.separator)[file.toString().replace(fileList,"").split(File.separator).size-2]
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

        fun compareVersion(one: String, two: String): Int {
            val oneArray = one.split(".")
            val twoArray = two.split(".")

                if(oneArray[0].toInt()>twoArray[0].toInt()){
                    return 1
                }else if (oneArray[0].toInt()<twoArray[0].toInt()){
                    return -1
                }else{
                    if(oneArray[1].toInt()>twoArray[1].toInt()){
                        return 1
                    }else if (oneArray[1].toInt()<twoArray[1].toInt()){
                        return -1
                    }else{
                        return if(oneArray[2].toInt()>twoArray[2].toInt()){
                            1
                        }else if (oneArray[2].toInt()<twoArray[2].toInt()){
                            -1
                        }else{
                            0
                        }
                    }
                }


        }
        @Value("\${env.config.file_path}")
        lateinit var filePath:String
        @Value("\${env.config.file_list}")
        lateinit var fileList:String
        fun getPatchFileList(resultList: List<Map<String, Any>>, filePath: String, fileList: String): ArrayList<LinkedHashMap<String,Any>>{

            val newerFileList:ArrayList<LinkedHashMap<String,Any>> = ArrayList()
            val one = (resultList[0]["version"] as String)
            val two = (resultList[1]["version"] as String)
            val compareResult = compareVersion(one,two);
            var versionPatch = ""
            var newVersionList = ""
            var oldVersionList =""
            if(compareResult == 1){
                versionPatch = two.replace(".","") +"-"+one.replace(".","")
                newVersionList = filePath+one+File.separator+fileList
                oldVersionList = filePath+two+File.separator+fileList
            }else{
                versionPatch = one.replace(".","") +"-"+two.replace(".","")
                newVersionList = filePath+two+File.separator+fileList
                oldVersionList = filePath+one+File.separator+fileList

            }
            val patchFileName = "patch-$versionPatch.7z"
            val patchFile = File(filePath+"patch"+File.separator+patchFileName)
            if(!patchFile.exists()){
                // 对比两个file.list 生成
                val oldFileListJson = File(oldVersionList)
                val newFileListJson = File(newVersionList)
                val old = oldFileListJson .readText()
                val new = newFileListJson.readText()
                val files = JsonParser.parseString(new).asJsonObject
                val oldFiles = JsonParser.parseString(old).asJsonObject
                val filesList = files.get("files").asJsonArray
                val oldFileList = oldFiles.get("files").asJsonArray

                filesList.forEach { element ->
                    val fileName = element.asJsonObject["file_name"].asString
                    val fileItemPath = element.asJsonObject["file_path"].asString
                    val fileHash = element.asJsonObject["hash"].asString
                    var notExist = true
                    oldFileList.forEach{

                        if (it.asJsonObject["file_name"].asString == fileName) {
                            notExist = false
                            if(it.asJsonObject["hash"].asString != fileHash){
                                val temp = LinkedHashMap<String,Any>()
                                temp["file_name"] = fileName
                                temp["file_path"] = fileItemPath
                                temp["hash"] = fileHash
                                newerFileList.add(temp)
                            }
                        }
                    }.also {
                        if(notExist){
                            val temp = LinkedHashMap<String,Any>()
                            temp["file_name"] = fileName
                            temp["file_path"] = fileItemPath
                            temp["hash"] = fileHash
                            newerFileList.add(temp)
                        }
                    }

                }

                log.info(newerFileList.toString())
            }
            return newerFileList
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