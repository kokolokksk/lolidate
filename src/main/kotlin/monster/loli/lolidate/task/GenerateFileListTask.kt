package monster.loli.lolidate.task

import monster.loli.lolidate.utils.LoveLoliiiUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Component
@ConfigurationProperties("config.properties")
class GenerateFileListTask {
    @Value("\${file_path}")
    lateinit var filePath:String
    @Value("\${file_list}")
    lateinit var fileList:String
    val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    @Scheduled(fixedRate = 300*1000)
    fun scanFile(){
        log.info("time:${dateFormat.format(Date())}")
        // 应用版本路径
        val resultMap: ArrayList<HashMap<String, Any>> = LoveLoliiiUtils.searchFile(Paths.get("f:","catcatdm"),fileList)

    }
}