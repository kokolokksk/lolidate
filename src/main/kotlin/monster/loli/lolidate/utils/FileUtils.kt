package monster.loli.lolidate.utils

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


/**
 * 文件工具类
 *
 *
 */
object FileUtils {
    /**
     * 下载文件
     *
     * @param originalFilename 文件名称
     * @param url              下载URL
     * @param response         响应对象
     * @throws IOException
     */
    @Throws(IOException::class)
    fun downloadFile(originalFilename: String, url: String, response: HttpServletResponse) {
        var servletOutputStream: ServletOutputStream? = null
        try {
            servletOutputStream = response.outputStream
            response.characterEncoding = "utf-8"
            response.setHeader("Content-Disposition", "attachment;filename=$originalFilename")
            response.contentType = "application/octet-stream"
           // val rootDirectoryPath = File(url)
            val path = url+originalFilename
            val file = File(path)
            if (!file.exists()) {
                throw FileNotFoundException("找不到此文件： $originalFilename")
            }
            val bytes = Files.readAllBytes(Paths.get(path))
            servletOutputStream.write(bytes)
            servletOutputStream.flush()
        } catch (e: Exception) {
//			throw new ServiceException(e.toString());
        } finally {
            servletOutputStream?.close()
        }
    }

    fun compressFileTo7z(list:List<File>,path:File,name:String){
        if(!path.exists()){
            path.mkdirs()
        }
        val sevenZOutput = SevenZOutputFile(File("f:/lolicate/catcatdm/patch/x.7z"))
        val entry: SevenZArchiveEntry = sevenZOutput.createArchiveEntry(path, name)
        sevenZOutput.putArchiveEntry(entry)
        list.forEach{
            sevenZOutput.write( it.readBytes())
        }

        sevenZOutput.closeArchiveEntry()
    }
    fun archiveFile(list: List<File>, path: Path, name: String){
         try{
             var fo: OutputStream = Files.newOutputStream(path)
             var gzo:OutputStream = GzipCompressorOutputStream(fo)
             var o:ArchiveOutputStream = TarArchiveOutputStream(gzo)
             list.forEach {

                 gzo.write(it.readBytes())
             }





         }catch (e:Exception){
             e.printStackTrace()
         }
    }

}