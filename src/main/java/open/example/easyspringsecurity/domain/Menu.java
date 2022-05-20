package open.example.easyspringsecurity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("menu")
public class Menu {
    @TableId
    private String id;
    /**
     * url必须遵守 AntPathMatcher 规则 /hello/world/** 或者/hello/world 才能匹配的上
     */
    private String url;
    private String desc;
}
