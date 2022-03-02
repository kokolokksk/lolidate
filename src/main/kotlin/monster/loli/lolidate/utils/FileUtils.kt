package monster.loli.lolidate.utils

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.compress.archivers.ArchiveOutputStream
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream
import org.apache.commons.compress.utils.IOUtils
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString


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

    fun compressFileTo7z(list:List<File>,path:Path,name:String){
        if(!path.toFile().exists()){
           // path.toFile().mkdirs()
        }
        val sevenZOutput = SevenZOutputFile(path.toFile())
        list.forEach{
            val entry: SevenZArchiveEntry = sevenZOutput.createArchiveEntry(it.toPath(), it.name)
            sevenZOutput.putArchiveEntry(entry);
            sevenZOutput.write(Files.newInputStream(it.toPath()).readAllBytes())
            sevenZOutput.closeArchiveEntry();
        }
        sevenZOutput.close()
        log.info("compress over")
    }
    fun archiveFile(list: List<File>, path: Path, name: String){
         try{
             var fo: OutputStream = Files.newOutputStream(path)
             var gzo:OutputStream = GzipCompressorOutputStream(fo)
             var o:ArchiveOutputStream = TarArchiveOutputStream(gzo)
             list.forEach {
                 val fis = FileInputStream(it)
                 val bis  = BufferedInputStream(fis)
                 val tae = o.createArchiveEntry(it,it.name)
                 o.putArchiveEntry(tae);
                // o.write(bis.readAllBytes())
                 IOUtils.copy(Files.newInputStream(it.toPath()),o)
                 bis.close();
                 o.closeArchiveEntry()

             }

         }catch (e:Exception){
             e.printStackTrace()
         }
    }

}