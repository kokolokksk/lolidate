package monster.loli.lolidate.task

import com.google.gson.Gson
import monster.loli.lolidate.utils.LoveLoliiiUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Component
class GenerateFileListTask {
    @Value("\${env.config.file_path}")
    lateinit var filePath:String
    @Value("\${env.config.file_list}")
    lateinit var fileList:String
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    @Scheduled(fixedRate = 300*1000)
    fun scanFile(){
        var patchFileList = ArrayList<LinkedHashMap<String,Any>>()
        log.info("time:${dateFormat.format(Date())}")
        // 应用版本路径
        val resultList: ArrayList<HashMap<String, Any>> = LoveLoliiiUtils.searchFile(Paths.get(File(filePath).toURI()),fileList)
        var anyListExist:Boolean = true
        resultList.forEach{
            if(!(it["exist"] as Boolean)){
                anyListExist = false
              val fl = LoveLoliiiUtils.getFileList(Paths.get(File(it["path"].toString()).toURI()))
                val listFile = File(it["path"] as String +File.separator +fileList)
                listFile.writeBytes(Gson().toJson(fl).toByteArray())
                log.info("file.list saved in ${it["path"]}")
            }else{
                log.info("file.list is exist in ${it["path"]}")
            }
        }
        if(anyListExist){
            // 检测最近两个版本patch
            resultList.isEmpty().let {
                val versionList:kotlin.collections.ArrayList<String> = ArrayList()
              resultList.forEach {
                  versionList.add(it["version"] as String)
              }
            }
            if(resultList.size == 2){
                val one = (resultList[0]["version"] as String)
                val two = (resultList[1]["version"] as String)
                val compareResult = LoveLoliiiUtils.compareVersion(one, two);
                var newVersionList = ""
                var oldVersionList =""
                if(compareResult == 1){
                    newVersionList = filePath+one+File.separator+fileList
                    oldVersionList = filePath+two+File.separator+fileList
                }else{
                    newVersionList = filePath+two+File.separator+fileList
                    oldVersionList = filePath+one+File.separator+fileList

                }
                val oldFileListJson = File(oldVersionList)
                val newFileListJson = File(newVersionList)
                val old = oldFileListJson .readText()
                val new = newFileListJson.readText()
                patchFileList = LoveLoliiiUtils.getPatchFileList(old,new)

            }
        }
    }
}