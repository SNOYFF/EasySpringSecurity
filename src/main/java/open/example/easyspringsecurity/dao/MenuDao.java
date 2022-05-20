package open.example.easyspringsecurity.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import open.example.easyspringsecurity.domain.Menu;
import open.example.easyspringsecurity.domain.User;
import open.example.easyspringsecurity.domain.vo.MenuListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuDao extends BaseMapper<Menu> {

    List<MenuListVo> getAllMenuList();

    // 修改菜单或者新增菜单需要把菜单缓存删除或者更新。

}
