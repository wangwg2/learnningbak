## Maven 3
参考
* [Welcome to Apache Maven](https://maven.apache.org/index.html)
* [Maven Archetype Plugin](https://maven.apache.org/archetype/maven-archetype-plugin/index.html)
* [Guide to Creating Archetypes](https://maven.apache.org/guides/mini/guide-creating-archetypes.html)
* [Gradle - The Maven Plugin](https://docs.gradle.org/current/userguide/maven_plugin.html)
* 

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


<span id="concept"></span>
## Maven 核心概念

**概念**
* LifeCycle : 生命周期，maven内置default,sie,clean三个生命周期
* Phase : 阶段，每个生命周期有不同的阶段
* Plugin : 插件，实现实际的构建功能
* Goal : 一个插件可以实现多个goal，goal具备具体的功能
* Execution : 通过配置，决定在某个Phase执行哪些Goal
* Project : maven管理的目标：软件工程，小的工程可以聚合成大工程
* PackageType : 为了便于管理工程，按照构建目标区分成不同的工程类型，如jar，war，ear等
* Dependency : 依赖，project之间存在依赖关系
* DependencyScope : maven对依赖定义了不同的作用范围
* Management : 可以配置一个工程如何管理依赖关系
* Repository : 仓库，存放包，分为本地库和远程库
* Build : 构建的动作。使用maven管理工程，主要是指定将project构建到某个phase

**项目对象模型（POM）**: POM是Maven中的里程碑式的模型。该模型的一部分已经构建到 Maven引擎（被亲切地称为反应堆 ）中，其余部分则通过一个名叫 pom.xml 的基于 XML 的元数据文件来声明。

**依赖项管理模型** : Maven 对如何管理项目的依赖项很在行. 依赖项管理是一片灰色地带, 典型的构建-管理工具和系统都未明确涉及. Maven 2 构建了 Maven 依赖项管理模型, 该模型能够适应大多数需求. 这个模型被证明是有效而高产的模型, 目前主要的开源项目都部署了该模型。

**构建生命周期和阶段**：和 POM 相关的概念是构建生命周期 和阶段。这是 Maven 2 的内嵌概念模型和现实物理世界之间的接口。使用 Maven 时，工作主要是通过插件来执行的。在构建周期中，Maven 2 通过遵循一系列定义好的阶段，将这些插件协调起来。

**坐标**： 对Maven坐标的使用贯穿于Maven配置文件和POM文件中。

#### pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.dsl.maven</groupId>
  <artifactId>demo</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>Maven Demo Project</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```
**pom.xml说明**
* `project`：pom.xml文件中的顶层元素;
* `modelVersion`：指明POM使用的对象模型的版本。这个值很少改动。
* `groupId`：指明创建项目的组织或者小组的唯一标识。  
  GroupId是项目的关键标识，典型的，此标识以组织的完全限定名来定义。比如，`org.apache.maven.plugins`是所有Maven插件项目指定的`groupId`。 
* `artifactId`：指明此项目产生的主要产品的基本名称。  
  项目的主要产品通常为一个JAR文件。  
  第二，象源代码包通常使用`artifactId`作为最后名称的一部分。典型的产品名称使用这个格式：  
   `<artifactId>- <version>. <extension>`(比如：myapp-1.0.jar)。 
* `version`：项目产品的版本号。  
  Maven帮助你管理版本，可以经常看到SNAPSHOT这个版本，表明项目处于开发阶段。 
* `name`：项目的显示名称，通常用于maven产生的文档中。 
* `url`：指定项目站点，通常用于maven产生的文档中。 
* `description`：描述此项目，通常用于maven产生的文档中。


#### Maven 标准目录结构
````
project/              项目根
    pom.xml           Maven2的pom.xml文件
    src/
        main/         项目主体目录根
            java      源代码目录
            resources 所需资源目录
            filters   资源过滤文件目录
            assembly  Assembly descriptors
            config    配置文件目录根
            scripts   脚本库
            webapp    web应用的目录。WEB-INF、css、js等
        test/         项目测试目录根
            java      测试代码目录
            resources 所需资源目录
            filters   资源过滤文件目录
        site          与site相关的资源目录
    target/           输出目录根
        classes       项目主体输出目录
        test-classes  项目测试输出目录
````

#### 使用Archetype生成项目骨架
````
mvn archetype:generate
````


#### Maven坐标
Maven坐标是一组可以唯一标识工件的三元组值(组ID，工件ID，版本)。  

Maven坐标为各种构件引入了秩序，任何一个构件都必须明确定义自己的坐标，而一组Maven坐标是通过一些元素定义的，它们是`groupId`、`artifactId`、`version`、`packaging`、`classifier`。
* `groupId`：定义当前Maven项目隶属的实际项目。
* `artifactId`：该元素定义实际项目中的一个Maven项目（模块），推荐的做法是使用实际项目名称作为artifactId的前缀。
* `version`：该元素定义Maven项目当前所处的版本。  
  Maven定义了一套完整的版本规范，以及快照（SNAPSHOT）的概念。
* `packaging`：该元素定义Maven项目的打包方式。
* `classifier`：该元素用来帮助定义构建输出的一些附属构件。  
  不能直接定义项目的`classifier`，因为附属构件不是项目直接默认生成的，而是由附加的插件帮助生成。

上述5个元素中，`groupId`、`artifactId`、`version`是必须定义的，`packaging`是可选的（默认为jar），而`classifier`是不能直接定义的。

**坐标规划的原则**
* groupId ：坐标规划一个原则是基于项目域名衍生。项目就必须用groupId来定义。
* artifactId : artifactId来定义模块，而不是定义项目。在定义`artiafctId`时也加入项目的信息。
* version ： `<主版本>.<次版本>.<增量版本>-<限定符>`
  主版本主要表示大型架构变更，次版本主要表示特性的增加，增量版本主要服务于bug修复，而限定符如alpha、beta等等是用来表示里程碑。


#### 依赖


#### 存储库
Maven 存储库是普通的目录树。

Maven 维护了一个工件的 POM 文件，同时也为该工件和其存储库中的 POM 维护了检验和散列。当工件在存储库间转移时，这些文件帮助确保工件的完整性。该工件已由 Maven 的依赖项管理引擎从中央存储库下载并放置到本地存储库中。

仓库主要用于获取工程依赖的其他工程的生成物，也可用来部署（deploy）maven工程的生成物。 生成物包括各种打包的生成物以及pom文件。
如果有必要，一个工程可以部署到多个仓库。
仓库可以分为本地库（local）和远程库（remote）。本地库通常位于本机的~/.m2/repository文件夹， 远程库最常见的是maven中央库（），此外也会有一些私服库用于企业内部。



<span id="lifecycle"></span>
## 生命周期
Maven的生命周期就是为了对所有的构建过程进行抽象和统一。这个生命周期包含了项目的清理、初始化、编译、测试、打包、集成测试、验证、部署和站点生成等几乎所有构建步骤。

Maven将工程（Project）的构建过程理解为不同的生命周期(LifeCycle)和阶段（Phase）。
在工程的构建过程中，存在着不同的生命周期，这些生命周期互相独立，之间也没有一定的顺序关系。
每个生命周期又划分为不同的阶段（Phase）。阶段之间有明确的顺序关系， 同一生命周期内的阶段必须按顺序依次执行。

生命周期抽象了构建的各个步骤，定义了它们的次序，但没有提供具体实现。
Maven设计了插件机制。每个构建步骤都可以绑定一个或者多个插件行为，而且Maven为大多数构建步骤编写并绑定了默认插件。


**三套生命周期**

Maven拥有三套相互独立的生命周期，它们分别为clean、default和site。
* `clean` 生命周期的目的是清理项目
* `default` 生命周期的目的是构建项目
* `site` 生命周期的目的是建立项目站点。

再次强调一下它们是相互独立的，你可以仅仅调用`clean`来清理工作目录，仅仅调用`site`来生成站点。当然你也可以直接运行`mvn clean install site`运行所有这三套生命周期。

每套生命周期都由一组阶段(Phase)组成。
如`clean`生命周期包括 `pre-clean`,`clean`,`post-clean` 三个阶段。
我们平时在命令行输入的命令总会对应于一个特定的阶段。
比如，运行`mvn clean`，这个的`clean`是Clean生命周期的一个阶段。

在一个生命周期中，运行某个阶段的时候，它之前的所有阶段都会被运行。
也就是说，`mvn clean`等同于`mvn pre-clean clean`。
如果我们运行`mvn post-clean`，那么`pre-clean`，`clean`都会被运行。
这是Maven很重要的一个规则，可以大大简化命令行的输入。


**clean生命周期**

lean生命周期的目的是清理项目，它包含三个阶段：
* `pre-clean` 执行一些清理前需要完成的工作。
* `clean` 清理上一次构建生成的文件。
* `post-clean` 执行一些清理后需要完成的工作。

**default生命周期**

default生命周期定义了真正构建时所需要执行的所有步骤，它是所有生命周期中最核心的部分，其包含的阶段如下：代码生成器可以开始生成在以后阶段中处理或编译的源代码。
* `validate` 确保当前配置和 POM 的内容是有效的。这包含对 pom.xml 文件树的验证。
* `initialize` 在执行构建生命周期的主任务之前可以进行初始化。
* `generate-sources` 代码生成器可以开始生成在以后阶段中处理或编译的源代码。
* `process-sources` 处理项目主资源文件。一般来说，是对`src/main/resources`目录的内容进行变量替换等工作后，复制到项目输出的主`classpath`目录中。  
  提供解析、修改和转换源码。常规源码和生成的源码都可以在这里处理。
* `generate-resources` 可以生成非源码资源。通常包括元数据文件和配置文件。
* `process-resources` 处理非源码资源。修改、转换和重定位资源都能在这阶段发生。
* `compile` 编译项目的主源码。一般来说，是编译`src/main/java`目录下的Java文件至项目输出的主`classpath`目录中。  
  编译源码。编译过的类被放到目标目录树中。
* `process-classes` 处理类文件转换和增强步骤。字节码交织器和常用工具常在这一阶段操作。
* `generate-test-sources` mojo 可以生成要操作的单元测试代码。
* `process-test-sources` 处理项目测试资源文件。一般来说，是对`src/test/resources`目录的内容进行变量替换等工作后，复制到项目输出的测试`classpath`目录中。  
  在编译前对测试源码执行任何必要的处理。在这一阶段，可以修改、转换或复制源代码。  
* `generate-test-resources` 允许生成与测试相关的（非源码）资源。
* `process-test-resources` 可以处理、转换和重新定位与测试相关的资源。
* `test-compile` 编译项目的测试代码。一般来说，是编译`src/test/java`目录下的Java文件至项目输出的测试`classpath`目录中。
* `process-test-classes`
* `test` 使用单元测试框架运行测试，测试代码不会被打包或部署。  
  运行编译过的单元测试并累计结果。
* `prepare-package`
* `package` 接受编译好的代码，打包成可发布的格式，如JAR。
* `pre-integration-test` 准备集成测试。这种情况下的集成测试是指在一个受到一定控制的模拟的真实部署环境中测试代码。这一步能将归档文件部署到一个服务器上执行。
* `integration-test` 执行真正的集成测试。
* `post-integration-test` 解除集成测试准备。这一步涉及测试环境重置或重新初始化。
* `verify` 检验可部署归档的有效性和完整性。过了这个阶段，将安装该归档。
* `install` 将包安装到Maven本地仓库，供本地其他Maven项目使用。
* `deploy` 将最终的包复制到远程仓库，供其他开发人员和Maven项目使用。

若想了解进一步的这些阶段的详细信息，可以参阅[官方的解释](http://maven.apache.org/guides/introduc-tion/introduction-to-the-lifecycle.html)。

**site生命周期**

site生命周期的目的是建立和发布项目站点，Maven能够基于POM所包含的信息，自动生成一个友好的站点，方便团队交流和发布项目信息。该生命周期包含如下阶段：
* `pre-site` 执行一些在生成项目站点之前需要完成的工作。
* `site` 生成项目站点文档。
* `post-site` 执行一些在生成项目站点之后需要完成的工作。
* `site-deploy` 将生成的项目站点发布到服务器上。
