## What This Repo

此项目为[极客时间](https://time.geekbang.org/) 推出的训练营《小马哥的Java项目实战营》第 0 期的个人源码仓库，主要包括个人学习代码以及作业提交。

>参考链接：https://github.com/mercyblitz/geekbang-lessons

## Branch Management

| Branch     | Description        |
| ---------- | ------------------ |
| master     | 同步 Fork 仓库代码 |
| homework/* | 作业分支           |

## Homework

### 2021-02-27

**一、作业要求**

- 通过自研 Web MVC 框架实现一个用户注册（`/register`），forward 到一个成功的页面（JSP 用法）
- 通过 Controller -Service -Repository 实现 （数据库实现）
- （非必须）JNDI 的方式获取数据库源 DataSource

**二、验证方式**

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

### 2021-03-06

**一、作业要求**

通过课堂上的简易版依赖注入和依赖查找，实现用户注入功能

- 通过 UserService 实现用户注册
- 注册用户需要校验
  - ID 必须大于 0 的整数
  - 密码 6 - 32 位
  - 电话号码：采用中国大陆方式（11位校验）

**二、验证方式**

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

