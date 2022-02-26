package monster.loli.lolidate.utils

import kotlin.Throws
import java.io.IOException
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.ServletOutputStream
import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 文件工具类
 *
 * @版权所有 广东国星科技有限公司 www.mscodecloud.com
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
}