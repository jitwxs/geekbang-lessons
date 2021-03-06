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

（1）切换分支并打包

```shell
git checkout homework/0227

cd projects/stage-0/user-platform

mvn clean install
```

（2）运行项目

```
java -jar xxx.jar
```

（3）访问注册页面

```shell
http://127.0.0.1:8080/register
```

（4）注册成功后跳转至成功页面

