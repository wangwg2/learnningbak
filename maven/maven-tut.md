## Maven

#### 安装与配置

###### 安装Maven  
* 下载maven的bin
* 设置环境变量 `MAVEN_HOME`, 在PATH里加入maven的bin的路径
* `mvn -v` 测试安装

######  目录与文件
```
$M2_HOME                    环境变量指向Maven的安装目录。
$M2_HOME/conf/settings.xml  在机器上全局地定制Maven的行为。
~/.m2                       用户maven目录
~/.m2/repository            Maven本地仓库
~/.m2/settings.xml          在用户范围定制Maven的行为
```

###### 修改用户级配置

copy `settings.xml` 到 `~/.m2/settings.xml` 并修改


##### 配置

仓库默认位置
```xml
<localRepository>d:/work/workspace/maven/repository</localRepository>
```

下载路径 (使用阿里云下载路径)
```xml
<mirrors>
  <mirror>
    <id>alimaven</id>
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>
    <url>http://maven.aliyun.com/nexus/content/repositories/central/</url>
  </mirror>
</mirrors>
```


#### 使用练习

###### project: j2se 

```bash
# 创建maven项目
mvn archetype:generate -DgroupId=com.how2java -DartifactId=j2se -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

# 运行package命令
cd j2se
mvn package

# 执行Jar
java -cp target/j2se-1.0-SNAPSHOT.jar com.how2java.App
```

