## 导读
EasySpringSecurity项目为SpringSecurity+Jwt+Redis+MySQL+RBAC权限管理系统后端的脚手架，已经把各种扩展点暴露出来,能快速理解上手使用,学习成本低,权限可以精确到控制到用户的每一个请求，现权限逻辑部分功能已经实现，可以基于本脚手架进行二次开发权限系统后台。本脚手架适合一定基础的开发人员，懂得rbac权限模型已经相关Springboot,SpringSecurity,jwt等知识和概念。
## 前期准备
数据库准备：MySQL、Redis  
MySQL==>创建dev库 账号：root 密码：root
<br>
Redis ===> 不设密码
<br>
执行dev.sql脚本。
表设计遵循rbac权限模型，即用户拥有的角色去获得相应角色拥有的权限，表有用户表，角色表，菜单表，以及两两的关系表。

## 运行示例
用户默认有 **root** 和 **admin** 密码为123，其中root拥有/hello，/admin访问权限，admin只有/hello权限。
<br>
1.**登录**
<br>
使用PostMan Post 请求路径http://localhost:8080/login 参数为form表单：username:admin password:123 登录拿到 accessToken的值:accessTokenValue
![输入图片说明](https://images.gitee.com/uploads/images/2022/0520/112653_c4f14b5e_6544483.png "屏幕截图.png")
<br>
2.**请求路径-权限成功**
使用PostMan Post 请求路径http://localhost:8080/hello Headers设置为accessToken:accessTokenValue
![输入图片说明](https://images.gitee.com/uploads/images/2022/0520/113052_ea3a93e5_6544483.png "屏幕截图.png")
<br>
3.**请求路径-权限不足**
使用PostMan Post 请求路径http://localhost:8080/admin Headers设置为accessToken:accessTokenValue
![输入图片说明](https://images.gitee.com/uploads/images/2022/0520/113154_3085af2b_6544483.png "屏幕截图.png")
<br>
## SpringSecurity说明
SpringSecurity其实不难，本质上就是一个个过滤器链，其实现步骤主要为**认证**和**鉴定**两个主要步骤。
<br>
认证：认证用户是否合法
<br>
鉴定：鉴定用户请求资源是否合法
<br>
**SpringSecurity过滤链顺序为:用户认证链-jwtToken认证链-用户鉴定链**
<br>
**用户认证链**:用户是否存在，密码是否正确
<br>
**jwtToken认证链**：用户提交的token是否正确，否则请求失败
<br>
**用户鉴定链**：用户请求的资源是否合法，否则拦截提示权限不足
## SpringSecurity额外
如果想入门SpringSecurity，推荐王松的深入浅出SpringSecurity书。