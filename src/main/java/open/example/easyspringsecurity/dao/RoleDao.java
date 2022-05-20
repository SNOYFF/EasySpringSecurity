package open.example.easyspringsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import open.example.easyspringsecurity.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDao extends BaseMapper<Role> {

    List<String> selectRoleList(@Param("userId") String userId);

}
