package monster.loli.lolidate.mapper

import org.apache.ibatis.jdbc.SQL
import org.springframework.util.StringUtils


class UpdateInfoMapper {
    fun findUpdateInfoList(id: String?): String? {
        val sql = SQL()
        sql.SELECT("id,path,version,time")
        sql.FROM("update_info")
        if (!StringUtils.isEmpty(id)) {
            sql.WHERE("id=#{id}")
        }
        return sql.toString()
    }
    fun findAllList(): String? {
        val sql = SQL()
        sql.SELECT("id,path,version,time")
        sql.FROM("update_info")
        sql.WHERE("1=1")
        return sql.toString()
    }
}