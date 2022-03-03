package monster.loli.lolidate.dao

import monster.loli.lolidate.mapper.UpdateInfoMapper
import monster.loli.lolidate.vo.UpdateInfoModel
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.SelectProvider
import org.springframework.stereotype.Component


@Mapper
@Component
interface UpdateDao {
    @SelectProvider(type = UpdateInfoMapper::class, method = "findUpdateInfoList")
    fun findUserInfoList(id: String?): List<UpdateInfoModel?>?
    @SelectProvider(type = UpdateInfoMapper::class, method = "findAllList")
    fun findAllList(): List<UpdateInfoModel?>?
}