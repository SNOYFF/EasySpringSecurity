package open.example.easyspringsecurity.service;

import open.example.easyspringsecurity.dao.MenuDao;
import open.example.easyspringsecurity.domain.vo.MenuListVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MenuService {
    @Resource
    private MenuDao menuDao;

    public List<MenuListVo> getMenuAllList() {
        return menuDao.getAllMenuList();
    }
}
