## Maven 3
参考
* [Welcome to Apache Maven](https://maven.apache.org/index.html)
* [Maven Archetype Plugin](https://maven.apache.org/archetype/maven-archetype-plugin/index.html)
* [Guide to Creating Archetypes](https://maven.apache.org/guides/mini/guide-creating-archetypes.html)
* [Gradle - The Maven Plugin](https://docs.gradle.org/current/userguide/maven_plugin.html)
* [Maven Intro](http://www.trinea.cn/android/maven/)

内容
* 使用
* [概述](#intro)
* [安装与配置](#install)
* [核心概念](#concept)
* [pom.xml](#pom)
* [生命周期](#lifecycle)

## 使用
初始化  
`mvn archetype:generate -DgroupId=net.dsl -DartifactId=myproject -Dversion=1.0-SNAPSHOT`

#### Maven Archetype
简而言之，Archetype是一个Maven项目模板工具包。一个原型被定义为一种原始模式或模型，其他所有同类事物都是从该原始模式或模型中作出的。

使用原型提供了一种很好的方式，可以让开发人员以与您的项目或组织采用的最佳实践相一致的方式快速开展工作。

您可能想要在组织内标准化J2EE开发，以便您可以为EJB或WAR或Web服务提供原型。一旦这些原型被创建并部署到组织的存储库中，它们就可供组织中的所有开发人员使用。

###### Maven Archetype 插件
archetype 插件有四个直接使用的目标:
* `archetype:generate`
  从原型创建一个 maven 项目。
* `archetype:create-from-project`
   从现有项目中创建一个原型。
* `archetype:crawl`
   搜索原型库并更新目录。

并且通过 `'maven-archetype'` 包装将三个目标绑定到默认生命周期：
* `archetype:jar`
  绑定到包阶段）用于构建原型jar工件。
* `archetype:integration-test`
  （绑定到集成测试阶段）用于通过从刚刚构建的原型生成示例项目来执行原型集成测试。
* `archetype:update-local-catalog`
  （绑定到安装阶段）用于更新本地目录。 

###### 示例: 以批处理模式生成项目
通过将 `interactive` 属性设置为`false`或使用`-B`标志，可以摆脱Maven Archetype Plugin的交互性。然后需要一些有意义的属性：
* `archetypeGroupId`， `archetypeArtifactId` 和 `archetypeVersion` 定义用于项目生成的原型。
* `groupId`，`artifactId`，版本和包是要设置的主要属性。每个原型都需要这些属性。一些原型定义了其他属性; 如果需要，请参考相应的原型文档。

`mvn archetype:generate -B -DarchetypeGroupId=org.apache.maven.archetypes -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.1 -DgroupId=com.company -DartifactId=project -Dversion=1.0-SNAPSHOT -Dpackage=com.company.project`

###### 示例： 使用替代 catalog 生成项目
通过将 `archetypeCatalog` 属性定义为一个特定值，可以使用替代目录作为内部目录：
* `internal` 仅使用内部目录。
* `local` 只能使用本地目录。
* `remote` 使用Maven的远程目录。目前没有提供目录。

默认值是远程的，本地的。因此本地目录显示在远程目录之后。

```bash
mvn archetype:generate
```

###### 示例: 原型创建
从现有项目创建原型涉及三个步骤：
* 原型决议
* 原型安装：部署
* 原型用法

* 调用 `mvn archetype:create-from-project` 插件首先通过猜测项目目录来解析包。
* 然后它在目标 `target/generated-sources/archetype` 目录中生成原型的目录树。
* 然后移至生成的目录，并在创建的原型上调用 `mvn install`。
  ```bash
  $ cd target/generated-sources/archetype/
  $ mvn install  
  ```
* 最后移动到一个新的目录并使用你的原型。
  ```bash
  $ mkdir /tmp/archetype
  $ cd /tmp/archetype
  $ mvn archetype:generate -DarchetypeCatalog=local
  ```

###### 示例: 从多模块项目创建一个原型
创建一个多模块项目的原型和创建一个单个模块项目原型同样简单。

移动到多模块项目的根目录并调用 `mvn archetype:create-from-project`。在作为例子的原型中，一些文件需要不被过滤。这是通过给 archetype 插件 `archetype.filteredExtensions` 属性中的一些值来实现的。

```bash
mvn archetype:create-from-project -Darchetype.filteredExtensions=java
```

#### mvn archetype:generate
mvn archetype:generate
```bash
mvn archetype:generate                         \
  -DarchetypeGroupId=<archetype-groupId>       \
  -DarchetypeArtifactId=<archetype-artifactId> \
  -DarchetypeVersion=<archetype-version>       \
  -DgroupId=<my.groupid>                       \
  -DartifactId=<my-artifactId>
```

* `archetype:generate`
* -D`groupId`  项目创建团体或组织的唯一标志符. 全局中唯一的标识符
  -D`groupId`=`com.mycompany.helloworld`  
  -D`groupId`=`com.trinea.maven.test`
* -D`artifactId`  项目产生的主要产品的基本名称. 
  -D`artifactId`=`helloworld`  
  -D`artifactId`=`CounterWebApp`
* -D`package`=`com.mycompany.helloworld`
* -D`version`=`1.0-SNAPSHOT`
* -D`interactiveMode`=`false`

其中
* `archetype` 
* -D`groupId` 指定groupId.  
  `groupId` 项目创建团体或组织的唯一标志符，通常是域名倒写.
* -D`artifactId` 指定`artifactId`.
  `artifactId` 指明此项目产生的主要产品的基本名称。项目的主要产品通常为一个JAR文件。
  象源代码包通常使用`artifactId`作为最后名称的一部分。典型的产品名称使用这个格式：  
   `<artifactId>-<version>.<extension>`(比如：myapp-1.0.jar)。
* -D`archetypeArtifactId` 指定`ArchetypeId`，
* -D`interactiveMode` 表示是否使用交互模式，交互模式会让用户填写版本信息之类的，


#### Maven 与 Gradle 互相转化

**Gradle项目 转为 Maven项目**  
在`build.gradle`中增加以下内容
```groovy
apply plugin: 'java'
apply plugin: 'maven'

group = 'net.dsl'
version = '1.0-dev'
sourceCompatibility = 1.8
```
`gradle install` 将生成 `build/poms/pom-default.xml`

**Maven项目 转为 Gradle项目**  
在maven根目录下运行  
`gradle init --type pom`


#### 库
Maven库：http://repo2.maven.org/maven2/
Maven依赖查询：http://mvnrepository.com/

#### 常用命令
* 创建Maven的普通java项目：  
  `mvn archetype:generate -DgroupId=packageName -DartifactId=projectName`
* 创建Maven的Web项目：  
  `mvn archetype:generate -DgroupId=packageName -DartifactId=webappName archetypeArtifactId=maven-archetype-webapp`
* 编译源代码： `mvn compile`
* 编译测试代码：`mvn test-compile`
* 运行测试：`mvn test`
* 产生site：`mvn site`
* 打包：`mvn package`
* 在本地Repository中安装jar：mvn install
* 清除产生的项目：`mvn clean`
* 生成eclipse项目：`mvn eclipse:eclipse`
* 生成idea项目：`mvn idea:idea`
* 组合使用goal命令，如只打包不测试：`mvn -Dtest package`
* 编译测试的内容：`mvn test-compile`
* 只打jar包: `mvn jar:jar`
* 只测试而不编译，也不测试编译：`mvn test -skipping compile -skipping test-compile` ( -skipping 的灵活运用，当然也可以用于其他组合命令)
* 清除eclipse的一些系统设置:` mvn eclipse:clean`

```
mvn compile         编译源代码
mvn test-compile    编译测试代码
mvn test            运行测试
mvn site            产生site
mvn package         打包
mvn install         在本地Repository中安装jar
mvn clean           清除产生的项目
mvn eclipse:eclipse 生成eclipse项目
mvn idea:idea       生成idea项目
mvn -Dtest package  组合使用goal命令，如只打包不测试
mvn test-compile    测试编译的内容
mvn jar:jar         只打jar包
mvn test -skipping compile   只测试而不编译
mvn eclipse:clean   清除eclipse的一些系统设置
```

ps：
一般使用情况是这样，首先通过cvs或svn下载代码到本机，然后执行mvn eclipse:eclipse生成ecllipse项目文件，然后导入到eclipse就行了；修改代码后执行mvn compile或mvn test检验，也可以下载eclipse的maven插件。
mvn -version/-v 显示版本信息
mvn archetype:generate 创建mvn项目
mvn archetype:create -DgroupId=com.oreilly -DartifactId=my-app 创建mvn项目
mvn package 生成target目录，编译、测试代码，生成测试报告，生成jar/war文件
mvn jetty:run 运行项目于jetty上,
mvn compile 编译
mvn test 编译并测试
mvn clean 清空生成的文件
mvn site 生成项目相关信息的网站
mvn -Dwtpversion=1.0 eclipse:eclipse 生成Wtp插件的Web项目
mvn -Dwtpversion=1.0 eclipse:clean 清除Eclipse项目的配置信息(Web项目)
mvn eclipse:eclipse 将项目转化为Eclipse项目
mvn -e                显示详细错误 信息.
mvn validate          验证工程是否正确，所有需要的资源是否可用。
mvn test-compile      编译项目测试代码。 。
mvn integration-test  在集成测试可以运行的环境中处理和发布包。
mvn verify            运行任何检查，验证包是否有效且达到质量标准。
mvn generate-sources  产生应用需要的任何额外的源代码，如xdoclet。

常用命令：
```
mvn -v   显示版本
mvn help:describe -Dplugin=help  使用 help 插件的 describe 目标来输出 Maven Help 插件的信息。
mvn help:describe -Dplugin=help -Dfull   使用Help 插件输出完整的带有参数的目标列
mvn help:describe -Dplugin=compiler -Dmojo=compile -Dfull  获取单个目标的信息,设置 mojo 参数和 plugin 参数。此命令列出了Compiler 插件的compile 目标的所有信息
mvn help:describe -Dplugin=exec -Dfull   列出所有 Maven Exec 插件可用的目标
mvn help:effective-pom   看这个“有效的 (effective)”POM，它暴露了 Maven的默认设置
mvn archetype:create -DgroupId=org.sonatype.mavenbook.ch03 -DartifactId=simple -DpackageName=org.sonatype.mavenbook  创建Maven的普通java项目，在命令行使用Maven Archetype 插件
mvn exec:java -Dexec.mainClass=org.sonatype.mavenbook.weather.Main Exec  插件让我们能够在不往 classpath 载入适当的依赖的情况下，运行这个程序
mvn dependency:resolve   打印出已解决依赖的列表
mvn dependency:tree  打印整个依赖树
mvn install -X   想要查看完整的依赖踪迹，包含那些因为冲突或者其它原因而被拒绝引入的构件，打开 Maven 的调试标记运行
mvn install -Dmaven.test.skip=true   给任何目标添加maven.test.skip 属性就能跳过测试
mvn install assembly:assembly  构建装配Maven Assembly 插件是一个用来创建你应用程序特有分发包的插件
mvn jetty:run  调用 Jetty 插件的 Run 目标在 Jetty Servlet 容器中启动 web 应用
mvn compile  编译你的项目
mvn clean install  删除再编译
mvn hibernate3:hbm2ddl   使用 Hibernate3 插件构造数据库
```


在应用程序用使用多个存储库
```xml
<repositories>
  <repository>
    <id>Ibiblio</id>
    <name>Ibiblio</name>
    <url>http://www.ibiblio.org/maven/</url>
  </repository>
  <repository>
    <id>PlanetMirror</id>
    <name>Planet Mirror</name>
    <url>http://public.planetmirror.com/pub/maven/</url>
  </repository>
</repositories>
```

`mvn deploy:deploy-file -DgroupId=com -DartifactId=client -Dversion=0.1.0 -Dpackaging=jar -Dfile=d:\client-0.1.0.jar -DrepositoryId=maven-repository-inner -Durl=ftp://xxxxxxx/opt/maven/repository/`

发布第三方Jar到本地库中：
`mvn install:install-file -DgroupId=com -DartifactId=client -Dversion=0.1.0 -Dpackaging=jar -Dfile=d:\client-0.1.0.jar -DdownloadSources=true -DdownloadJavadocs=true`


----
<span id="intro"></span>
### 概述

Maven主要服务于基于Java平台的项目构建、依赖管理和项目信息管理。
* Maven是一个异常强大的构建工具，能够帮我们自动化构建过程，从清理、编译、测试到生成报告，再到打包和部署。
* Maven抽象了一个完整的构建生命周期模型，这个模型吸取了大量其他的构建脚本和构建工具的优点，总结了大量项目的实际需求。如果遵循这个模型，可以避免很多不必要的错误，可以直接使用大量成熟的Maven插件来完成我们的任务（很多时候我们可能都不知道自己在使用Maven插件）。此外，如果有非常特殊的需求，我们也可以轻松实现自己的插件。
* 能帮助我们标准化构建过程。所有项目的构建命令都是简单一致的。
* Maven还能帮助我们管理原本分散在项目中各个角落的项目信息，包括项目描述、开发者列表、版本控制系统地址、许可证、缺陷管理系统地址等。
* Maven还为全世界的Java开发者提供了一个免费的中央仓库，在其中几乎可以找到任何的流行开源类库。
* Maven对于项目目录结构、测试用例命名方式等内容都有既定的规则，只要遵循了这些成熟的规则，用户在项目间切换的时候就免去了额外的学习成本，可以说是约定优于配置（Convention Over Configuration）。

<span id="install"></span>
### 安装与配置

**安装Maven**  
* 下载maven的bin
* 设置环境变量 `MAVEN_HOME`, 在PATH里加入maven的bin的路径
* `mvn -v` 测试安装

**目录与文件**
```
$M2_HOME                    环境变量指向Maven的安装目录。
$M2_HOME/conf/settings.xml  在机器上全局地定制Maven的行为。
~/.m2                       用户maven目录
~/.m2/repository            Maven本地仓库
~/.m2/settings.xml          在用户范围定制Maven的行为
```

`mvn help:system`  
打印出所有的Java系统属性和环境变量

**设置HTTP代理**  
在`settings.xml`中为Maven配置HTTP代理

**设置MAVEN_OPTS环境变量**  
通常需要设置`MAVEN_OPTS`的值为`-Xms128m-Xmx512m`

**Maven Eclipse配置**  
* Eclipse Marketplace搜索关键字maven到插件Maven Integration for Eclipse 并点击安装即可。
* 点击Window -> Preference -> Maven -> Installation -> Add进行设置


<br/>

---
@import "./maven.-concept.md"
