package monster.loli.lolidate.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

val log:Logger = LoggerFactory.getLogger(LoveLoliiiUtils::class.java)
class LoveLoliiiUtils{

    /**
     * 获取某目录下的目录结构
     * */companion object {
        fun getFileList(start:Path,deep:Int){
            var visitDeep:Int = 0
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

                    log.info("正在访问${file}文件")
                    return FileVisitResult.CONTINUE
                }


                // 在访问目录之后触发该方法
                override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {

                    log.info("正在访问${dir}目录")
                    return super.postVisitDirectory(dir, exc)
                }
            })

        }

        fun searchFile(start:Path,fileList: String): ArrayList<HashMap<String, Any>> {
            var b:Boolean = false
            var fList :ArrayList<HashMap<String,Any>> = ArrayList()
            Files.walkFileTree(start, object : SimpleFileVisitor<Path>() {
                // 在访问子目录前触发该方法
                @Throws(IOException::class)
                override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                    log.info("访问${dir}目录前")
                    val dirName :String= dir.fileName.toString();
                    if(dir.toString().split("\\").size == 3){
                        val resultData:HashMap<String, Any> = HashMap()
                        resultData["version"]=dirName
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
                        val key:String = file.toString().replace(fileList,"").split("//")[0]
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
    val exist: ArrayList<HashMap<String, Any>> = LoveLoliiiUtils.searchFile(Paths.get("f:","catcatdm"),fileList)
    log.info("is exist $exist")
}