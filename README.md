# XDChat 使用介绍

***XDChat 系统是基于Web技术部署的在线聊天系统，以下是相关功能的使用说明***

### 1.项目背景

如今，随着网络技术的迅速普及，人们不仅仅可以在网络上获得自己需要的信息资源，还可以通过互联网进行实时的交流。网络聊天的实时性、便捷性远远超过传统的信件和电子邮件，所需成本也远低于电话。因此网络聊天功能备受人们关注，此方面的应用开发也众多，网络聊天服务已成为互联网的重要功能。许多网站都建有聊天室供用户使用，并且同时产生了众多的聊天网站满足当下人们的网络聊天需求。
“西电聊”的主要功能是面向西安电子科技大学学生内部的群聊系统，学生与学生之间可通过公共的群聊天室进行群聊天，也可通过添加好友的方式进行一对一私人聊天。同时，软件提供网站管理员、群主、群管理员等相关用户组对聊天系统进行服务。



### 2.Web应用建模

- 功能需求建模
- 内容建模
- 超文本建模
- 适应性建模
- 展示建模

### 3. Web应用架构设计
##### MVC三层架构设计
- 表示层：应用的最高层设计，提供聊天组件和UI交互设计
- 逻辑层：对接收到的信息进行逻辑判断，对表示层信息进行修改
- 持久层：负责数据库相关修改，利用MySql对数据封装持久化

### 4. Web应用设计
##### 本产品应用框架采用目前较流行的好友/群列表+聊天模块相结合的模式
- 交互设计部分进行用户界面组织、导航设计及处理出错及出错跳转界面功能实现；
- 展示设计部分采用T型布局，设计成符合用户行为习惯、令人愉悦的UI主界面；
- 内容设计部分为用户提供群聊、群管理及私聊的功能。

### 5. Web应用构建
#### 5.1 应用技术
**1. 前端技术栈**
- Vue
- Element UI
- Axios
- WebSocket

**2. 后端技术栈**
- SpringBoot
- Mybatis
- MySql
- WebSocket


#### 5.2 架构组件

##### 前端组件
**1. Vue+Element UI**
可以定义属性value，显示消息数量或设置Bool值显示红点->isDot
```
<el-badge:
is-dot="isDot[user.username + '#' + item.username]
"style="">
```
```
<el-button @click="chooseChatList('群聊')" 
    class="toolBtn" size="small">
```
**2. Axios**
利用Axios对全局响应进行拦截，将拦截到的信息进行分析，输出错误代码，转换请求和响应数据

```
axios.interceptors.response.use(success=>{
  if (success.status&&success.status==200&&success.data.status==500){//请求成功，但处理出现其他错误
    Message.error({message:success.data.msg})
    return;
  }
```
**3. WebSocket**
订阅消息：SockJS兼容通信协议
```
connect(context){
      //连接Stomp站点
      context.state.stomp=Stomp.over(new SockJS('/ws/ep'));
      context.state.stomp.connect({},success=>{
        //订阅系统广播通知消息
        context.state.stomp.subscribe("/topic/notification",msg=>{
          //判断是否是系统广播通知
            Notification.info({
              title: '系统消息',
              message: msg.body.substr(5),
              position:"top-right"
            });
            //更新用户列表（的登录状态）
            context.commit('GET_USERS');
        });
```
##### 后端组件
**1. SpringBoot+Mybatis**
SpringBoot主启动类，借助@Import的支持，收集和注册特定场景相关的bean定义，加载到IoC容器
Mybatis创建dto层的映射
```
@SpringBootApplication
@MapperScan("xd.xdchat.dao")
@EnableScheduling
```
**2. MySql**
建立MySQL数据库
建立表user_info，存储用户的基本信息,包括用户名、用户昵称、密码、头像、ID（主键）等信息

```
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL COMMENT '登录账号',
  `nickname` varchar(20) NOT NULL COMMENT '昵称',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `user_profile` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `user_state_id` int(11) DEFAULT '2' COMMENT '用户状态id',
  `is_enabled` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `is_locked` tinyint(1) DEFAULT '0' COMMENT '是否被锁定',
  PRIMARY KEY (`id`),
  KEY `user_ibfk_1` (`user_state_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`user_state_id`) REFERENCES `user_state` (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 23 DEFAULT CHARSET = utf8;
```


**3. WebSocket**
注册stomp站点 配置拦截器

```
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  //注册stomp站点
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws/ep").setAllowedOrigins("*").withSockJS();
  }
```

### 6. Web应用部署
#### 6.1 部署粒度
- 初期采用较细粒度的方式部署新组件
- 后期针对采用灰度发布策略，打包发布

#### 6.2 部署原则
* 预测试交付包
* 建立支持制度
* 先改正缺陷再交付

#### 6.3 部署环境
- 操作系统：*Windows 10*
- Web应用服务器：*Apache*

#### 6.4 版本控制
- 利用GitHub仓库进行版本控制记录和更新

### 7. Web应用功能
#### 7.1 注册/登录界面
##### （1）注册
实现用户注册功能，可以在限定要求下编辑昵称、用户名、密码并确认、用户头像（可选）
![register](https://gitee.com/calvin11311/chatpic/raw/master/pics/register.jpg)
##### （2）登录
用户通过输入正确用户名和密码进行登录
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/login.jpg)
#### 7.2 聊天界面
##### （1）私聊
用户可以和好友私发消息
显示好友在线/离线状态
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/selfchat.jpg)
##### （2）群聊
用户通过搜索群聊名称加入群聊
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/joingroup.jpg)
  
  
加入群聊后，会显示已有消息，可以对所有群成员接收和发送消息
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/groupchat.jpg)
##### （3）搜索
聊天室提供的搜索框中，可以搜索到已经加入的群聊和好友
##### （4）群管理
在用户自己创建的群中，可以选择将任意群成员剔除
#### 7.3 管理员界面
**系统切换到管理员状态，可以进行管理员权限的管理**
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/adminlogin.jpg)
##### （1）登录权限管理
管理员可以看到全部在系统中注册的用户，选择是/否锁定该用户，锁定后用户无法发言
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/usermanagement.jpg)
##### （2）聊天记录管理  
管理员可以查看所有用户消息记录，并且可以按关键词、时间段、用户名查找聊天记录，并对聊天记录进行删除
![avatar](https://gitee.com/calvin11311/chatpic/raw/master/pics/messagemangement.jpg)

### 8. Web应用测试
#### 8.1 单元测试
**对项目中最小可测试单元进行检查**
在后端主要应用Postman模拟前端请求发送并检测后端输出内容，测试相关需求组件
- 用户登录组件
- 用户修改信息组件
- 聊天组件

#### 8.2 功能测试
**对项目中的各种预见功能进行检查**
- 链接测试：点击检查页面跳转，均指向正确的资源，无孤立页面
- 交互测试：正确标记表单标识域，对用户输入的合法性进行检查，具有输入异常的处理机制，当用户输入不符合的字符串内容或者空内容，阻止提交并提示错误
- 数据校验：数据的输入进行合法性和正确性校验
- Cookies测试：编辑和破坏cookie，检查网页状态，当cookie被编辑时拒绝访问，cookie被破坏时，网站不会突然崩溃
- 数据库测试：针对数据完整性、提取数据正确性进行检查

#### 8.3 性能测试
- 速度测试：对页面点击的响应速度进行检查，网络良好的情况下，具有良好的响应时间
- 负载测试：多用户同时登录Web应用，当登录用户在20人以内有较好的响应时间
- 压力测试：采用大量输入/接收消息信息方法，测试系统能承受压力



#### 8.4 安全测试
- 对用户的密码进行加密处理，将加密后的字符串存储在数据库中
- 利用Token对登录用户信息进行加密验证传输
- 针对XSS攻击——预防处理：
-- 限制输入，只允许输入数字或受限制的字符
-- Encode Html处理，对输入的字符串进行重新编码，之后利用Decode解码
```
<%
    response.write(Server.HTMLEncode("The image tag: <img>"))
%>
```

#### 8.5 Web UI测试
手动检测UI组件的功能完整性和易用性



### 9.Web应用运维
#### 9.1 内容维护
**栏目撤换**
- 增加管理员对限制用户的时长的设置
- 增加群消息界面对群消息接收不提醒、不接受等功能的选项
- 增加群成员暂时私聊功能

**web页面更换**
- 在节日或者特殊纪念日推出特定聊天室皮肤

**社区维护**
- 提供关于聊天室使用的反馈渠道和交流群组

#### 9.2 SEO策略分析
**内容优化策略**
- 精简用户使用界面，提供简介高对比度的标签和按钮
        
**域名策略**
- 选用简短、便于用户记忆的域名，比如XDChat.room、XDChat.management等

**主机优化策略**
- 选用具有较高性能并且稳定的服务器主机，选取腾讯云等顶尖服务提供商，其具有较高的稳定性和访问速度，以及方便的服务器管理渠道。


### 10. Web应用部署
#### 10.1 前端
利用VSCode，打开frontend文件夹，新建终端，执行命令
- npm install
- npm run serve

#### 10.2 后端
- 新建数据库xdchat，执行文件xdchat.sql
- 修改backend中配置文件application-dev.properties内容username和password
- 利用IDEA运行backend文件夹内容