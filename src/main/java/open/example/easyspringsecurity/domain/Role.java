package open.example.easyspringsecurity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role")
public class Role {

    @TableId
    private String id;
    /**
     * 必须设置成ROLE_XXX格式,例如ROLE_ADMIN
     */
    private String name;
    private String desc;

}
