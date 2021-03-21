## 01 What This Repo

此项目为[极客时间](https://time.geekbang.org/) 推出的训练营《小马哥的Java项目实战营》第 0 期的个人源码仓库，主要包括个人学习代码以及作业提交。

>参考链接：https://github.com/mercyblitz/geekbang-lessons

## 02 Branch Management

| Branch     | Description        |
| ---------- | ------------------ |
| master     | 同步 Fork 仓库代码 |
| homework/* | 作业分支           |

## 03 Homework

### 2021-03-20

#### 作业要求

1. 参考 my dependency-injection 模块
   - 脱离 web.xml 配置实现 ComponentContext 自动初始化
   - 使用独立模块并且能够在 user-web 中运行成功
2. 晚上 my-configuration 模块
   - Config 对象如何能被 my-web-mvc 使用
     - 可能在 ServletContext 获取
     - 如何通过 ThreadLocal 获取
3. 去提前阅读 Servlet 规范中 Security 章节（Servlet 容器安全）

#### 验证方式

切换分支并打包运行项目

```shell
git checkout homework/0320

cd projects/stage-0/user-platform

mvn clean install

java -jar xxx.jar
```

**验证 ComponentContext 迁移至独立模块脱离 web.xml 的初始化**

1. 回归验证 0306 的注册和查询功能
2. 回归验证 0313 的 Jolokia 和读取 "application.name" 功能

**验证 Config 对象在 my-web-mvc 的使用**

> 没明白题目意思。
>
> 因为 `DefaultConfigProviderResolver` 这个类已经放进 SPI 了，那么我直接使用 `ConfigProviderResolver.instance().getConfig()` 就能取到了。
>
> 参考代码：org.geektimes.projects.user.web.DefaultServletContextListener#testMicroprofile
>
> 为什么要问在 ServletContext 和 ThreadLocal  中如何获取呢，和这个没有关系吧？

### 2021-03-13

#### 作业要求

**需求一（必须）**

- 整合 https://jolokia.org/ ，实现一个自定义 JMX MBean，通过 Jolokia 做 Servlet 代理

**需求二（选做）** 

- 继续完成 Microprofile config API 中的实现扩展 

  - 扩展 org.eclipse.microprofile.config.spi.ConfigSource 实现，包括 OS 环境变量
  - 扩展 org.eclipse.microprofile.config.spi.Converter 实现，提供 String 类型到简单类型
- 通过 org.eclipse.microprofile.config.Config 读取当前 应用名称
  - 应用名称 property name = "application.name"

#### 验证方式

切换分支并打包运行项目

```shell
git checkout homework/0313

cd projects/stage-0/user-platform

mvn clean install

java -jar xxx.jar
```

**验证 Jolokia Servlet 代理打印 MBean 信息**

（1）访问以下 URL，获取 MBean 信息

```
http://127.0.0.1:8080/jolokia/read/org.geektimes.projects.user.management:type=User
```

（2）期望输出

```json
{"request":{"mbean":"org.geektimes.projects.user.management:type=User","type":"read"},"value":{"Email":"mercyblitz@gmail.com","User":{"password":"******","phoneNumber":"abcdefg","name":"小马哥","id":1615697565957,"email":"mercyblitz@gmail.com"},"PhoneNumber":"abcdefg","Id":1615697565957,"Name":"小马哥","Password":"******"},"timestamp":1615697607,"status":200}
```

（3）登录 JConsole 后修改 org.geektimes.projects.user.management:type=User 的属性值。

（4）重新执行第（1）步，发现输出结果同步发生修改。

**验证 OS 环境变量及 Converter**

访问形如下方的 URL，将尝试从 `os env` --> `system properties` -->  `application.properties` 中读取配置信息（越往后优先级越高），并支持类型转换（Converter）

```java
http://127.0.0.1:8080/config/read?key=NUMBER_OF_PROCESSORS&class=java.lang.Integer
```

在我的电脑中输出结果如下：

```
Default Success !
Message: {executeClass=class java.lang.Integer, result=12, requestKey=NUMBER_OF_PROCESSORS, requestClass=java.lang.Integer}
```

**验证读取当前应用名称**

访问如下 URL，将读取配置 application.name

```
http://127.0.0.1:8080/config/read?key=application.name&class=java.lang.String
```

期望输出结果如下：

```
Default Success !
Message: {executeClass=class java.lang.String, result=user-web-read-from-system-properties, requestKey=application.name, requestClass=java.lang.String}
```

尝试去除 `org.geektimes.configuration.microprofile.config.source.impl.SystemPropertiesConfigSource#loadConfigs` 中的优先级测试配置，再次访问该 URL，将会返回 `microprofile-config.properties` 中的配置。如下所示：

```
Default Success !
Message: {executeClass=class java.lang.String, result=user-web-read-from-microprofile-config.properties, requestKey=application.name, requestClass=java.lang.String}
```

### 2021-03-06

#### 作业要求

通过课堂上的简易版依赖注入和依赖查找，实现用户注入功能

- 通过 UserService 实现用户注册
- 注册用户需要校验
  - ID 必须大于 0 的整数
  - 密码 6 - 32 位
  - 电话号码：采用中国大陆方式（11位校验）

#### 验证方式

（1）切换分支并打包运行项目

```shell
git checkout homework/0306

cd projects/stage-0/user-platform

mvn clean install

java -jar xxx.jar
```

（2）访问注册页面

```
http://127.0.0.1:8080/register
```

（3）注册失败后跳转至错误页面，并打印错误原因。例如当手机号码不合法时，页面输出：

```
Default Failure !
Code: -1
Message: Phone Number Illegal
```

（4）注册成功后跳转至成功页面，并打印您的用户ID。例如：

```
Login Success !
Message: 您的用户ID是：1615385567184
```

（5）新开一个窗口，复制您的用户ID，并请求以下 URL：

```
http://127.0.0.1:8080/api/user?id=1615385567184
```

页面将打印您的信息，例如：

```
Login Success !
Message: User{id=1615385567184, name='zhangsan', password='12313131', email='1111@qq.com', phoneNumber='15932123456'}
```

### 2021-02-27

#### 作业要求

- 通过自研 Web MVC 框架实现一个用户注册（`/register`），forward 到一个成功的页面（JSP 用法）
- 通过 Controller -Service -Repository 实现 （数据库实现）
- （非必须）JNDI 的方式获取数据库源 DataSource

#### 验证方式

（1）切换分支并打包运行项目

```shell
git checkout homework/0227

cd projects/stage-0/user-platform

mvn clean install

java -jar xxx.jar
```

（2）访问注册页面

```shell
http://127.0.0.1:8080/register
```

（3）注册成功后跳转至成功页面
