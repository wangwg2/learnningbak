---
html: {embed_local_images: true, embed_svg: true, offline: true, toc: true}
print_background: false
toc: {depth_from: 1, depth_to: 3, ordered: false}
---
# Gradle {ignore=true}
[TOC]

---
## Gradle User Guide

**文档参考**
* [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
* [Gradle User Guide 中文版](http://udn.yyuap.com/doc/wiki/project/GradleUserGuide-Wiki/index.html)
* [Gradle入门系列](hhttp://blog.jobbole.com/71999/)
* [Getting Started With Gradle](https://www.petrikainulainen.net/getting-started-with-gradle/)

---

## Gradle Cargo plugin部署项目到远程服务器 

参考文档
* [http://zhangley.com/article/cargo-gradle/](http://zhangley.com/article/cargo-gradle/)
* [GitHub bmuschko/gradle-cargo-plugin](https://github.com/bmuschko/gradle-cargo-plugin)

```Groovy
apply plugin: 'cargo'

cargo {
    containerId = 'tomcat6x'
    port = 9090

    deployable {
        context = 'myawesomewebapp'
    }

    remote {
        hostname = 'cloud.internal.it'
        username = 'superuser'
        password = 'secretpwd'
    }

    local {
        homeDir = file('/home/user/dev/tools/apache-tomcat-6.0.32')
        outputFile = file('build/output.log')
        timeout = 60000

        containerProperties {
            property 'cargo.tomcat.ajp.port', 9099
        }
    }
}
```


---
## Gradle SSH Plugin部署项目到远程服务器 

参考文档
* [Gradle构建并自动部署Java Web项目到远程服务器](http://blog.csdn.net/honghailiang888/article/details/54944621)
* [Deploy your App from Gradle](https://gradle-ssh-plugin.github.io/)
* [GitHub gradle-ssh-plugin/template](https://github.com/gradle-ssh-plugin/template)

**Gradle SSH Plugin**  
```Groovy
// build.gradle
plugins {
  id 'org.hidetake.ssh' version '2.9.0'
}

remotes {
  webServer {
    host = '192.168.1.101'
    user = 'jenkins'
    identity = file('id_rsa')
  }
}

task deploy {
  doLast {
    ssh.run {
      session(remotes.webServer) {
        put from: 'example.war', into: '/webapps'
        execute 'sudo service tomcat restart'
      }
    }
  }
}
```

思路介绍：
* 停止对应项目的tomcat服务器
* 删除webapps其下的对应项目文件
* 构建war包并将war包传送到tomcat服务器webapps目录下
* 启动tomcat

配置build.gradle
* 配置ssh插件、提供远程服务器登陆，文件传输，命令执行等支持 
* 配置远程服务器信息
* 配置gradle task、共四个任务：关闭tomcat、删除war包及目录、拷贝文件、启动tomcat，且后者依赖前者

##### Gradle SSH Plugin Template
build.gradle 模板
```Groovy
plugins {
    id 'war'
    id 'org.hidetake.ssh' version '2.9.0'
}

ssh.settings {
    dryRun = project.hasProperty('dryRun')
}

remotes {
    localhost {
        host = 'localhost'
        user = System.properties['user.name']
        identity = file("${System.properties['user.home']}/.ssh/id_rsa")
    }
}

task showPlatformVersion << {
    println ssh.version
    ssh.run {
        session(remotes.localhost) {
            execute 'uname -a'
            execute 'cat /etc/*-release', ignoreError: true
        }
    }
}

task deploy(dependsOn: war) << {
    ssh.run {
        session(remotes.localhost) {
            put from: war.archivePath.path, into: '/webapps'
            execute 'sudo service tomcat restart'
        }
    }
}
```


----

## Gradle入门系列：简介

参考文档
* [GradGradle入门系列（1）：简介](http://blog.jobbole.com/71999/)
* [Getting Started With Gradle](http://www.petrikainulainen.net/getting-started-with-gradle/)

下载安装gradle。  
安装验证： `gradle -v`

### Gradle构建简介

在Gradle中，有两个基本概念：项目和任务。请看以下详解：
* 项目是指我们的构建产物（比如Jar包）或实施产物（将应用程序部署到生产环境）。一个项目包含一个或多个任务。
* 任务是指不可分的最小工作单元，执行构建工作（比如编译项目或执行测试）。

那么，这些概念和Gradle的构建又有什么联系呢？好，每一次Gradle的构建都包含一个或多个项目。

我们能够使用以下配置文件对Gradle的构建进行配置：
* [Gradle构建脚本](https://docs.gradle.org/current/userguide/tutorial_using_tasks.html)（`build.gradle`）指定了一个项目和它的任务。
* [Gradle属性文件](http://www.gradle.org/docs/current/userguide/build_environment.html#sec:gradle_configuration_properties)（`gradle.properties`）用来配置构建属性。
* [Gradle设置文件](http://www.gradle.org/docs/current/userguide/build_lifecycle.html#sec:settings_file)（`gradle.settings`）对于只有一个项目的构建而言是可选的，如果我们的构建中包含多于一个项目，那么它就是必须的，因为它描述了哪一个项目参与构建。每一个多项目的构建都必须在项目结构的根目录中加入一个设置文件。

### Gradle插件简介

Gradle的设计理念是，所有有用的特性都由[Gradle插件](http://www.gradle.org/docs/current/userguide/plugins.html)提供，一个Gradle插件能够：
* 在项目中添加新任务
* 为新加入的任务提供默认配置，这个默认配置会在项目中注入新的约定（如源文件位置）。
* 加入新的属性，可以覆盖插件的默认配置属性。
* 为项目加入新的依赖。

Gradle用户手册提供了[一系列标准Gradle插件](https://docs.gradle.org/current/userguide/standard_plugins.html)。
在我们为项目加入Gradle插件时，我们可以根据名称或类型来指定Gradle插件。
我们可以将下面这行代码加入到`build.gradle`文件中，它通过名称指定Gradle插件（这里的名称是foo）：  
`apply plugin: 'foo'`


---
## Gradle入门系列：第一个Java项目

参考文档
* [Gradle入门系列（2）：第一个Java项目](http://blog.jobbole.com/72558/)
* [Getting Started with Gradle: Our First Java Project](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-our-first-java-project/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/first-java-project)

该Java项目只有一个需求：我们的构建脚本必须创建一个可执行的Jar文件，换句话说，我们必须能够使用命令java -jar jarfile.jar 来运行我们的程序。

使用Java插件, build.gradle
```Groovy
apply plugin: 'java'

jar {
    manifest {
        attributes 'Main-Class': 'net.petrikainulainen.gradle.HelloWorld'
    }
}
```

Java项目结构
* `src/main/java`目录包含了项目的源代码。
* `src/main/resources`目录包含了项目的资源（如属性文件）。
* `src/test/java`目录包含了测试类。
* `src/test/resources`目录包含了测试资源。

所有我们构建生成的文件都会在`build` 目录下被创建，这个目录涵盖了以下的子目录。
* `classes`目录包含编译过的`.class`文件。
* `libs`目录包含构建生成的`jar`或`war`文件。

```java
package net.petrikainulainen.gradle;
 
public class HelloWorld {
 
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

Java工程中的任务
Java插件在我们的构建中加入了很多任务，我们这篇教程涉及到的任务如下：
* `assemble`任务会编译程序中的源代码，并打包生成Jar文件，这个任务不执行单元测试。
* `build`任务会执行一个完整的项目构建。
* `clean`任务会删除构建目录。
* `compileJava`任务会编译程序中的源代码。

运行: `java -jar first-java-project.jar`

配置Jar文件的主类
* Java插件在我们的项目中加入了一个Jar任务，每一个Jar对象都一个`manifest`属性，这个属性是`Manifest`的一个实例。
* 我们可以对生成的Jar文件的主类进行配置，使用`Manifest`接口的`attributes()`方法。
    `jar { manifest { attributes 'Main-Class': 'net.petrikainulainen.gradle.HelloWorld' } }`


<span id="gradleguide003"></span>
## Gradle入门系列：依赖管理

参考文档
* [Gradle入门系列（3）：依赖管理](http://blog.jobbole.com/72992/)
* [Getting Started with Gradle: Dependency Management](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-dependency-management/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/dependency-management)

在现实生活中，要创造一个没有任何外部依赖的应用程序并非不可能，但也是极具挑战的。这也是为什么依赖管理对于每个软件项目都是至关重要的一部分。

这篇教程主要讲述如何使用Gradle管理我们项目的依赖，我们会学习配置应用仓库以及所需的依赖，我们也会理论联系实际，实现一个简单的演示程序。

#### 仓库管理

本质上说，仓库是一种存放依赖的容器，每一个项目都具备一个或多个仓库。   
Gradle支持以下仓库格式：
* Ivy仓库
* Maven仓库
* Flat directory仓库

**在构建中加入Ivy仓库**  
我们可以通过URL地址或本地文件系统地址，将Ivy仓库加入到我们的构建中。
```Groovy
repositories {
  ivy { url "http://ivy.petrikainulainen.net/repo" }
  ivy { url "../ivy-repo" }
}
```

**在构建中加入Maven仓库**  
可以通过URL地址或本地文件系统地址，将Maven仓库加入到我们的构建中。  

```Groovy
repositories {
  maven { url "http://maven.petrikainulainen.net/repo" }
  maven { url "../maven-repo" }
  jcenter()
  mavenLocal()
  mavenCentral()
}
```

在加入Maven仓库时，Gradle提供了三种“别名”供我们使用，它们分别是：
* `jcenter()`别名，表示依赖是从Bintary’s JCenter Maven 仓库中获取的。
* `mavenLocal()`别名，表示依赖是从本地的Maven仓库中获取的。
* `mavenCentral()`别名，表示依赖是从Central Maven 2 仓库中获取的。

**在构建中加入Flat Directory仓库**  
这意味着系统将在`lib`, `libB`目录下搜索依赖

```Groovy
repositories {
  flatDir { dirs 'lib','libB' }
}
```

#### 依赖管理
在配置完项目仓库后，我们可以声明其中的依赖，如果我们想要声明一个新的依赖，可以采用如下步骤：
1. 指定依赖的配置。
2. 声明所需的依赖。

**配置中的依赖分类**  
在Gradle中，依赖是按照指定名称进行分类的，这些分类被称为配置项，我们可以使用配置项声明项目的外部依赖。  
Java插件指定了若干依赖配置项，其描述如下：
* 当项目的源代码被编译时，`compile`配置项中的依赖是必须的。
* `runtime`配置项中包含的依赖在运行时是必须的。
* `testCompile`配置项中包含的依赖在编译项目的测试代码时是必须的。
* `testRuntime`配置项中包含的依赖在运行测试代码时是必须的。
* `archives`配置项中包含项目生成的文件（如Jar文件）。
* `default`配置项中包含运行时必须的依赖。

**声明项目依赖**    
最普遍的依赖称为外部依赖，这些依赖存放在外部仓库中。一个外部依赖可以由以下属性指定：
* `group`属性指定依赖的分组（在Maven中，就是`groupId`）。
* `name`属性指定依赖的名称（在Maven中，就是`artifactId`）。
* `version`属性指定外部依赖的版本（在Maven中，就是`version`）。

>小贴士：这些属性在Maven仓库中是必须的，如果你使用其他仓库，一些属性可能是可选的。打个比方，如果你使用Flat directory仓库，你可能只需要指定名称和版本。

指定以下依赖：依赖的分组是foo,依赖的名称是foo,依赖的版本是0.1。
```Groovy
dependencies {
    compile group: 'foo', name: 'foo', version: '0.1'
}
```

我们也可以采用一种快捷方式声明依赖：`[group]:[name]:[version]`。
```Groovy
dependencies {
  compile 'foo:foo:0.1'
}
```

也可以在同一个配置项中加入多个依赖，传统的方式如下：
```Groovy
dependencies {
    compile (
        [group: 'foo', name: 'foo', version: '0.1'],
        [group: 'bar', name: 'bar', version: '0.1']
    )
}
```
或

```Groovy
dependencies {
    compile 'foo:foo:0.1', 'bar:bar:0.1'
}
```

声明属于不同配置项的依赖(快捷方式)
```Groovy
dependencies {
    compile 'foo:foo:0.1'
    testCompile 'test:test:0.1'
}
```


<span id="dependency-management"></span>
### Java演示项目
[dependency-management on GitHub](https://github.com/pkainulainen/gradle-examples/tree/master/dependency-management)

**项目说明**
* 演示程序的构建脚本必须使用Maven central仓库。
* 演示程序必须使用Log4j写入日志。
* 演示程序必须包含包含单元测试，保证正确的信息返回，单元测试必须使用JUnit编写。
* 演示程序必须创建一个可执行的Jar文件。

**项目目录结构**
```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── dsl
    │   │           └── gradle
    │   │               ├── HelloWorld.java
    │   │               └── MessageService.java
    │   └── resources
    │       └── log4j.properties
    └── test
        └── java
            └── com
                └── dsl
                    └── gradle
                        └── MessageServiceTest.java
```

说明
* `MessageService.java` 创建一个`MessageService`类，当其中的`getMessage()`方法被调用时，返回字符串`“Hello World!”`。
* `MessageServiceTest.java` 创建一个`MessageServiceTest`类，确保M`essageService`类中的`getMessage()`方法返回字符串`“Hello World!”`。
* `HelloWorld.java` 创建程序的主类，从`MessageService`对象获取信息，并使用`Log4j`写入日志。
* `log4j.properties` 配置`Log4j`。

**`build.gradle` 文件**
```Groovy
apply plugin: 'java'
apply plugin: 'application'

// 配置仓库
repositories {
  mavenCentral()
}

// 依赖声明
dependencies {
  compile 'log4j:log4j:1.2.17'
  testCompile 'junit:junit:4.12'
}

// 打包jar
jar {
  from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
  manifest {
    attributes 'Main-Class': 'net.petrikainulainen.gradle.HelloWorld'
  }
}

mainClassName = 'net.petrikainulainen.gradle.HelloWorld'
```

>说明：
>jar包中包含依赖的库
>`from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }`


**执行测试**  

`gradle test`
* `build/test-results` 目录包含每次测试执行的原始数据。
* `build/reports/tests` 目录包含一个HTML报告，描述了测试的结果。

**打包和运行程序**  

`gradle assemble`或`gradle build`  
* 生成: `build/libs/dependency-management.jar`  
* 运行: `java -jar build/libs/dependency-management.jar`  



<span id="gradleguide004"></span>
## Gradle入门系列：创建二进制发布版本

参考文档
* [Gradle入门系列（4）：创建二进制发布版本](http://blog.jobbole.com/80340/)
* [Getting Started with Gradle: Creating a Runnable Binary Distribution](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-creating-a-binary-distribution/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/runnable-binary-distribution)

在创建了一个实用的应用程序之后，我们可能想将其与他人分享。其中一种方式就是创建一个可以从网站上下载的二进制文件。
创建一个二进制发布版本，满足以下需求：
* 二进制发布一定不能使用所谓的“fat jar”方式。换句话说，我们应用程序中的所有依赖一定不能被打包到该程序相同的jar包中。
* 二进制发布必须包含针对*nix和Windows操作系统的启动副本。
* 二进制发布的根目录必须包含许可证。

`application`插件是一种Gradle插件，让我们可以运行、安装应用程序并用非`“fat jar”`方式创建二进制发布版本。  
在`build.gradle`文件中做一些相应的更改，就可以进行二进制发布了。
* 移除jar任务的配置。
* 为项目应用`application`插件。
* 对应用程序的主类进行配置，设置`mainClassName`属性。

```
apply plugin: 'application'
mainClassName = 'net.petrikainulainen.gradle.HelloWorld'
```

`application`插件在项目中添加了5个任务：
* `run`任务用以启动应用程序。
* `startScripts`任务会在`build/scripts`目录中创建启动脚本，这个任务所创建的启动脚本适用于Windows和*nix操作系统。
* `installApp`任务会在`build/install/[project name]`目录中安装应用程序。
* `distZip`任务用以创建二进制发布并将其打包为一个`zip`文件。可以在`build/distributions`目录下找到。
* `distTar`任务用以创建二进制发布并将其打包为一个`tar`文件。可以在`build/distributions`目录下找到。

`gradle distZip`或`gradle distTar` 创建二进制文件。

如果将`application`插件创建的二进制文件解压缩，可以得到以下目录结构：
* `bin`目录：包括启动脚本。
* `lib`目录：包括应用程序的jar文件以及它的依赖。

在二进制发布版本中添加应用程序许可证
* 创建一个任务，将许可证从项目的根目录复制到`build`目录下。
* 将许可证加入到所创建的二进制发布的根目录下。

将许可证文件复制到build目录下
* 创建一个新的`Copy`任务，名为`copyLicense`。
* 使用`CopySpec`接口中的`from()`方法配置源文件，将“`LICENSE`”作为参数调用。
* 使用`CopySpec`接口中`into()`方法配置`target`目录，将`$buildDir`属性作为参数调用。

```Groovy
task copyLicense(type: Copy) {
    from "LICENSE"
    into "$buildDir"
}
```

将许可证文件加入到二进制发布文件中
* 将`copyLicense`任务从一个`Copy`任务改为正常的Gradle任务，只需在它的声明中移除“`(type: Copy)`”字符串。
* 按照以下步骤修改copyLicense任务
    - 配置`copyLicense`任务输出。创建一个新的文件对象，指向`build`目录的许可证文件，并将其设置为`outputs.file`属性值。
    - 将许可证文件从项目的根目录复制到`build`目录下。
* `application`插件在项目中设置了一个`CopySpec`属性，名为`applicationDistribution`。我们可以使用这个属性在已创建的二进制文件中加入许可证文件，步骤如下：
    - 使用`CopySpec`接口中的`from()`方法配置许可证文件的位置，将`copyLicense`任务的输出作为方法参数。
    - 使用`CopySpec`接口中`into()`方法配置`target`目录，将一个空的字符串作为参数调用方法。

```Groovy
apply plugin: 'application'
apply plugin: 'java'
 
repositories {
    mavenCentral()
}
 
dependencies {
    compile 'log4j:log4j:1.2.17'
    testCompile 'junit:junit:4.11'
}
 
mainClassName = 'net.petrikainulainen.gradle.HelloWorld'
 
task copyLicense {
    outputs.file new File("$buildDir/LICENSE")
    doLast {
        copy {
            from "LICENSE"
            into "$buildDir"
        }
    }
}
 
applicationDistribution.from(copyLicense) {
    into ""
}
```


---
## Gradle入门系列：创建多项目构建

参考文档
* [Gradle入门系列（5）：创建多项目构建](http://blog.jobbole.com/84471/)
* [Getting Started with Gradle: Creating a Multi-Project Build](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-creating-a-multi-project-build/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/multi-project-build)

尽管我们可以仅使用单个组件来创建可工作的应用程序，但有时候更广泛的做法是将应用程序划分为多个更小的模块。
由于这是一个非常普通的案例，因此每个成熟的构建工具都必须支持这项功能，Gradle也不例外。倘若Gradle项目拥有多于一个组件，我们就将其称之为多项目构建（multi-project build）。

### Gradle Build 的需求

我们的示例程序拥有两个模块：
* `core`模块包含一些通用的组件，它们能够被程序的其他模块使用。在我们的例子上，只包含一个类：`MessageService`类返回‘Hello World!’字符串。该模块只有一个依赖：它包含一个单元测试，使用`Junit 4.11`。
* `app`模块包含`HelloWorld`类，是程序的开端，它从`MessageService`对象中获取信息，并将接收到的信息写入一个日志文件中。该模块拥有两个依赖：它需要`core`模块，还使用`Log4j 1.2.17`作为日志库。

我们的Gradle构建还有其他两个需求：
* 我们必须要使用Gradle运行程序。
* 我们必须要创建一个可运行的二进制发布，而且不能使用所谓的`“fat jar”`方式。

### 创建一个多项目构建
下一步，我们将创建一个多项目的Gradle构建，包括两个子项目：`app` 和 `core`。初始阶段，先要建立Gradle构建的目录结构。

**建立目录结构**   
由于`core`和`app`模块都使用Java语言，而且它们都使用Java项目的默认项目布局，我们根据以下步骤建立正确的目录结构：
* 建立`core`模块的根目录(`core`)，并建立以下子目录：
    - `src/main/java`目录包含`core`模块的源代码。
    - `src/test/java`目录包含`core`模块的单元测试。
* 建立`app`模块的根目录(`app`)，并建立以下子目录：
    - `src/main/java`目录包含`app`模块的源代码。
    - `src/main/resources`目录包含`app`模块的资源文件。

**对包含在多项目构建中的项目进行配置**
* 在根项目的根目录下创建`settings.gradle`文件，一个多项目Gradle构建必须含有这个文件，因为它指明了那些包含在多项目构建中的项目。
* 确保`app`和`core`项目包含在我们的多项目构建中。

`settings.gradle`文件如下：
```Groovy
include 'app'
include 'core'
```

**配置 core 项目**
* 在`core`项目的根目录下创建`build.gradle`文件。
* 使用Java插件创建一个Java项目。
* 确保`core`项目从Maven2中央仓库(central Maven2 repository)中获取依赖。
* 声明JUnit依赖(版本4.11)，并使用`testCompile`配置项，该配置项表明：`core`项目在它的单元测试被编译前，需要JUnit库。

core项目的`build.gradle`文件如下：
```Groovy
apply plugin: 'java'
 
repositories {
    mavenCentral()
}
 
dependencies {
    testCompile 'junit:junit:4.11'
}
```

**配置App项目**  

在配置app项目之前，我们先来快速浏览一下对一些特殊依赖的依赖管理，这些依赖都是同一个多项目构建的一部分，我们将其称之为”项目依赖“。  
如果多项目构建拥有项目A和B，同时，项目B的编译需要项目A，我们可以通过在项目B的`build.gradle`文件中添加以下依赖声明来进行依赖配置。
* 在app项目的根目录下创建build.gradle文件。
* 用Java插件创建一个Java项目。
* 确保app项目从Maven2中央仓库(central Maven2 repository)中获取依赖。
* 配置所需的依赖，app项目在编译时需要两个依赖：
    - Log4j (version 1.2.17)
    - core 模块
* 创建二进制发布版本

`app`项目的`build.gradle`文件如下：
```Groovy
apply plugin: 'application'
apply plugin: 'java'

repositories {
    mavenCentral()
}
 
dependencies {
    compile 'log4j:log4j:1.2.17'
    compile project(':core')
}

mainClassName = 'net.petrikainulainen.gradle.client.HelloWorld'

task copyLicense {
    outputs.file new File("$buildDir/LICENSE")
    doLast {
        copy {
            from "LICENSE"
            into "$buildDir"
        }
    }
}

applicationDistribution.from(copyLicense) {
    into ""
}
```


**移除重复配置**

当我们对多项目构建中的子项目进行配置时，我们在core和app项目的构建脚本中添加了重复的配置。
* 由于两个项目都是Java项目，因此它们都使用Java插件。
* 两个项目都使用Maven2中央仓库(central Maven2 repository)。

两个构建脚本都包含以下配置：
```Groovy
apply plugin: 'java'
 
repositories {
    mavenCentral()
}
```

我们将这项配置转移到根项目的build.gradle文件中
* `subprojects {}` 在根项目的子项目中添加通用的配置
* `allprojects {}` 配置项是被多项目构建中的所有项目所共享
* `project ':core' {}` `core` 项目的配置

在根项目的`build.gradle`文件中移除了重复配置后，代码如下：
```Groovy
subprojects {
    apply plugin: 'java'
 
    repositories {
        mavenCentral()
    }
}
```


---
## Gradle入门系列：创建Web应用项目

参考文档
* [Gradle入门系列（6）：创建Web应用项目](http://blog.jobbole.com/94707/)
* [Getting Started with Gradle: Creating a Web Application Project](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-creating-a-web-application-project/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/web-application)

如果要用 Java 和 Gradle 创建一个 Web 应用项目，我们首先需要创建一个 Java 项目.  

**打包Web应用**

在我们使用War插件打包Web应用前，需要将其加入到构建中。  
```Groovy
apply plugin: 'java'
apply plugin: 'war'
```

War插件在项目的目录布局下添加了一个新的目录，加入了两个新的依赖管理配置项，以及在项目中添加了一个新的任务。这些变化的详细描述如下：
* War插件在项目的目录布局下添加了`src/main/webapp`目录，这个目录包含Web应用的源文件（CSS文件、Javascript文件、JSP文件等）。
* War插件加入了两个新的依赖管理配置项`providedCompile `和 `providedRuntime`.，这两个配置项与`compile` 和 `runtime` 配置项的作用域相同，但是区别是这两个配置项所属的依赖不会被添加到WAR文件中。
* War插件也会在应用项目中添加`war`任务，这个任务会将WAR文件置于`build/libs`目录中。

即便War插件在项目的目录布局下添加了`src/main/webapp`目录，但它实际上并没有被真正创建。如果我们需要这个目录，那就必须自己来创建。

**运行Web应用**

我们可以使用Gretty在开发环境中运行Web应用，它支持Jetty和Tomcat，它也不会被Gradle缺少SLF4J绑定所导致的问题所困扰。我们继续进行配置构建并使用Gretty运行Web应用。

配置构建脚本的依赖。可以通过以下步骤完成：
* 使用Bintray的JCenter Maven仓库配置构建脚本，进行依赖解析。
* 将Gretty插件的依赖加入到构建脚本的`classpath`中。

应用Gretty插件

配置Gretty：
* 配置Gretty，当运行Web应用时，使用Jetty 9作为servlet容器。
* 配置Jetty，监听8080端口。
* 配置Jetty，使用上下文路径’/’运行Web应用。

```Groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'org.akhikhl.gretty:gretty:+'
    }
}
 
apply plugin: 'java'
apply plugin: 'war'
apply plugin: 'org.akhikhl.gretty'
 
gretty {
    port = 8080
    contextPath = '/'
    servletContainer = 'jetty9'
}
```

**构建运行应用**

现在，我们可以通过在命令提示符下运行以下命令，开启或终止我们的Web应用：
* `gradle appStart`命令能运行Web应用。
* `gradle appStop`命令能终止Web应用。


---
## Gradle入门：创建 Spring Boot Web 应用项目

参考文档
* [Gradle入门：创建 Spring Boot Web 应用项目](http://blog.jobbole.com/99638/)
* [Getting Started with Gradle: Creating a Spring Boot Web Application Project](http://www.petrikainulainen.net/programming/gradle/getting-started-with-gradle-creating-a-spring-boot-web-application-project/)
* [Github](https://github.com/pkainulainen/gradle-examples/tree/master/spring-boot-web-application)

在一台远程服务器上运行Spring Web应用程序的传统做法是，将其打包为war文件并部署到servlet容器中。  
虽然在过去这个方法很好很强大，然而要同时管理多个servlet容器总是有些繁琐。  
Spring Boot针对该问题提供了一种解决方案，它允许我们将web应用程序打包为一个可执行的jar文件，这个文件可以使用嵌入式的servlet容器。

这篇博文将描述如何创建一个满足以下要求的Spring Boot web应用程序：
* Spring Boot应用程序必须使用Thymeleaf作为模版引擎。
* Spring Boot应用程序必须提供一种对其进行监控的方式。
* Gradle项目必须拥有独立的源文件和资源文件目录，以便进行单元和集成测试。

**创建Java项目**  
由于我们需要创建的是一个Java项目，所以必须使用Java插件。通过以下步骤可以完成：
* 应用Gradle的Java插件。
* 设置Java源码的版本为1.8。
* 配置Gradle生成Java1.8的class文件。

**在Gradle构建中添加集成测试**  
我们可以使用Spring Boot Gradle插件在Gradle项目中添加Spring Boot的支持。通过以下步骤使用该插件：
* 在构建脚本的classpath中添加Spring Boot Gradle插件（版本：1.2.5.RELEASE）。
* 应用Spring Boot Gradle插件。

build.gradle文件的源代码如下：
```Groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies { classpath( 'org.unbroken-dome.gradle-plugins:gradle-testsets-plugin:1.0.2' ) }
}

apply plugin: 'java'
apply plugin: 'org.unbroken-dome.test-sets'

sourceCompatibility = 1.8
targetCompatibility = 1.8

testSets { integrationTest { dirName = 'integration-test' }  }
 
project.integrationTest { outputs.upToDateWhen { false } }
 
check.dependsOn integrationTest
integrationTest.mustRunAfter test
 
tasks.withType(Test) { reports.html.destination = file("${reporting.baseDir}/${name}") }
```

**在Gradle项目中添加Spring Boot的支持**  

我们可以使用Spring Boot Gradle插件在Gradle项目中添加Spring Boot的支持。通过以下步骤使用该插件：
* 在构建脚本的classpath中添加Spring Boot Gradle插件（版本：1.2.5.RELEASE）。
    `'org.springframework.boot:spring-boot-gradle-plugin:1.2.5.RELEASE'`
* 应用Spring Boot Gradle插件。
    `apply plugin: 'spring-boot'`

我们无须使用Bintray的Jcenter Maven仓库，但是由于Gradle测试集插件依赖于该仓库，因此本文中的演示程序也将其加入。  
在应用Spring Boot Gradle插件后，我们可以：
* 将应用程序打包为可执行的jar文件。
* 使用`bootrun`任务运行程序。
* 省略Spring Boot依赖的版本信息
* 将应用程序打包为war文件。
*
当然，我们也可以对Spring Boot Gradle插件进行配置，并自定义执行和打包应用程序的任务。

**获取所需的依赖项**  
我们可以通过所谓的starter POM来获取Spring Boot应用的依赖。Spring Boot的参考指南将starter POM描述如下：   
starter POM是一族可以被包含到项目中的便捷依赖描述符。你可以一站式的获取所有需要的Spring和相关技术，无需苦苦寻找演示代码，也无需复制粘贴大量的依赖描述符。

换句话说，我们只需选择正确的starter POM，并将其加入到Gradle构建中即可。
* 确保所有的依赖都从Maven2的中央仓库获取。
* 在`compile`配置里添加`spring-boot-starter-actuator`依赖，我们之所以需要这个依赖，是因为它提供了一种监控应用运行状态的方法。
* 在`compile`配置里添加`spring-boot-starter-thymeleaf`依赖，我们之所以需要该依赖，是因为我们需要使用`Thymeleaf`作为创建Web应用的模版引擎。
* 在`testCompile`配置里添加`spring-boot-starter-test`依赖，我们之所以需要该依赖，是因为我们需要在Web应用中编写单元测试和集成测试。

build.gradle
```Groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath(
                'org.springframework.boot:spring-boot-gradle-plugin:1.2.5.RELEASE',
                'org.unbroken-dome.gradle-plugins:gradle-testsets-plugin:1.0.2'
        )
    }
}
 
apply plugin: 'java'
apply plugin: 'org.unbroken-dome.test-sets'
apply plugin: 'spring-boot'
 
sourceCompatibility = 1.8
targetCompatibility = 1.8
 
repositories {
    mavenCentral()
}
  
dependencies {
    compile(
            'org.springframework.boot:spring-boot-starter-actuator',
            'org.springframework.boot:spring-boot-starter-thymeleaf'
    )
    testCompile('org.springframework.boot:spring-boot-starter-test')
}
 
testSets {
    integrationTest { dirName = 'integration-test' }
}
 
project.integrationTest {
    outputs.upToDateWhen { false }
}
 
check.dependsOn integrationTest
integrationTest.mustRunAfter test
 
tasks.withType(Test) {
    reports.html.destination = file("${reporting.baseDir}/${name}")
}
```

**运行Spring Boot 应用程序**  

使用Spring Boot Gradle插件中的`bootRun`任务运行应用程序，而无需创建jar文件。
* 我们应当在开发阶段使用这个方法，因为它可以使我们静态的classpath资源（即：在src/main/resources下的文件）都成为可重载的资源。
* 换句话说，如果我们使用这个方法，就可以在Spring Boot应用程序运行时对这些文件进行更改，而且可以在不重启应用的情况下观察到变化。

可以将应用程序打包为一个可执行的jar文件，继而执行所创建的文件。
* 如果想要在一台远程服务器上运行Spring Boot应用，应当采用这种方法。
* `gradle clean build` 命令会在`build/libs` 目录下创建`spring-boot-web-application.jar`文件。
* 在将其复制到远程服务器上后，可以通过以下命令运行应用程序。  
    `java -jar spring-boot-web-application.jar`




---
## Gradle构建java项目

### Java 插件

`apply plugin: 'java'`

如你所见, Gradle 是一种多用途的构建工具. 它可以在你的构建脚本里构建任何你想要实现的东西. 但前提是你必须先在构建脚本里加入代码, 不然它什么都不会执行.

大都数 Java 项目是非常相像的: 你需要编译你的 Java 源文件, 运行一些单元测试, 同时创建一个包含你类文件的 JAR. 如果你可以不需要为每一个项目重复编写这些, 我想你会非常乐意的.

幸运的是, 你现在不再需要做这些重复劳动了. Gradle 通过使用插件解决了这个问题. 插件是 Gradle 的扩展, 它会通过某种方式配置你的项目, 典型的有加入一些预配置任务. Gradle 自带了许多插件, 你也可以很简单地编写自己的插件并和其他开发者分享它. Java 插件就是一个这样的插件. 这个插件在你的项目里加入了许多任务， 这些任务会编译和单元测试你的源文件, 并且把它们都集成一个 JAR 文件里.

Java 插件是基于合约的. 这意味着插件已经给项目的许多方面定义了默认的参数, 比如 Java 源文件的位置. 如果你在项目里遵从这些合约, 你通常不需要在你的构建脚本里加入太多东西. 如果你不想要或者是你不能遵循合约, Gradle 也允许你自己定制你的项目. 事实上, 因为对 Java 项目的支持是通过插件实现的, 如果你不想要的话, 你一点也不需要使用这个插件来构建你的项目.

### 最简单java项目

目录结构
```
├── build.gradle
└── src
    └── main
        └── java
            └── SimpApp.java
```

**相关文件**

build.gradle
````Groovy
apply plugin: 'java'
````
Gradle 希望能在 `src/main/java` 找到你的源代码, 在 `src/test/java` 找到你的测试代码, 也就是说 Gradle 默认地在这些路径里查找资源. 另外, 任何在 `src/main/resources` 的文件都将被包含在 JAR 文件里, 同时任何在 `src/test/resources` 的文件会被加入到 `classpath` 中以运行测试代码. 所有的输出文件将会被创建在构建目录里, JAR 文件存放在 `build/libs` 文件夹里.

SimpApp.java
```java
/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class SimpApp {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new SimpApp().getGreeting());
    }
}
```

**构建运行**
* 构建： `gradle build`  
* 运行：   
    `java -cp build/classes/main SimpApp`  
    `java -cp build/libs/simpjava.jar SimpApp`

**修改 build.gradle**
```Groovy
apply plugin: 'java'

//jar文件增加manifest信息
jar {
    manifest {
        attributes 'Main-Class': 'SimpApp'
    }
}

//使用application插件，支持 "gradle run"
apply plugin: 'application'
mainClassName = 'SimpApp'
```

**构建运行**
* 构建： `gradle build`  
* 运行： `java -jar build/libs/simpjava.jar`
* 构建运行： `gradle run`



### 初始化java项目 (japp)

**初始化 java-application 项目**  
`gradle init --type=java-application`

目录结构
```
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   └── java
    │       └── App.java
    └── test
        └── java
            └── AppTest.java
```

**相关文件**

build.gradle  
````Groovy
/*
 * This build file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java project to get you started.
 * For more details take a look at the Java Quickstart chapter in the Gradle
 * user guide available at https://docs.gradle.org/3.4/userguide/tutorial_java_projects.html
 */

// Apply the java plugin to add support for Java
apply plugin: 'java'

// Apply the application plugin to add support for building an application
apply plugin: 'application'

// In this section you declare where to find the dependencies of your project
repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // This dependency is found on compile classpath of this component and consumers.
    compile 'com.google.guava:guava:20.0'

    // Use JUnit test framework
    testCompile 'junit:junit:4.12'
}

// Define the main class for the application
mainClassName = 'App'
````

settings.gradle  
````Groovy
/*
 * This settings file was generated by the Gradle 'init' task.
 *
 * The settings file is used to specify which projects to include in your build.
 * In a single project build this file can be empty or even removed.
 *
 * Detailed information about configuring a multi-project build in Gradle can be found
 * in the user guide at https://docs.gradle.org/3.4/userguide/multi_project_builds.html
 */

/*
// To declare projects as part of a multi-project build use the 'include' method
include 'shared'
include 'api'
include 'services:webservice'
*/

rootProject.name = 'japp'
````

**构建运行**
* 构建： `gradle build`  
* 运行：   
    `java -cp build/classes/main App`  
    `java -cp build/libs/japp.jar App`  
* 构建运行： `gradle run`


### Java演示项目 (javaapp)

**项目说明**
- 演示程序的构建脚本必须使用Maven central仓库。
- 演示程序必须使用Log4j写入日志。
- 演示程序必须包含包含单元测试，保证正确的信息返回，单元测试必须使用JUnit编写。
- 演示程序必须创建一个可执行的Jar文件。

**项目目录结构**
```
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── dsl
    │   │           └── gradle
    │   │               ├── HelloWorld.java
    │   │               └── MessageService.java
    │   └── resources
    │       └── log4j.properties
    └── test
        └── java
            └── com
                └── dsl
                    └── gradle
                        └── MessageServiceTest.java
```

说明
* `MessageService.java` 创建一个`MessageService`类，当其中的`getMessage()`方法被调用时，返回字符串`“Hello World!”`。
* `MessageServiceTest.java` 创建一个`MessageServiceTest`类，确保M`essageService`类中的`getMessage()`方法返回字符串`“Hello World!”`。
* `HelloWorld.java` 创建程序的主类，从`MessageService`对象获取信息，并使用`Log4j`写入日志。
* `log4j.properties` 配置`Log4j`。

**`build.gradle` 文件**
```Groovy
apply plugin: 'java'
apply plugin: 'application'

// 配置仓库
repositories {
  mavenCentral()
}

// 依赖声明
dependencies {
  compile 'log4j:log4j:1.2.17'
  testCompile 'junit:junit:4.12'
}

// 打包jar
jar {
  from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }
  manifest {
    attributes 'Main-Class': 'com.dsl.gradle.HelloWorld'
  }
}

mainClassName = 'com.dsl.gradle.HelloWorld'
```

>说明：
>jar包中包含依赖的库
>`from { configurations.compile.collect { it.isDirectory() ? it : zipTree(it) } }`


**执行测试**  

`gradle test`
* `build/test-results` 目录包含每次测试执行的原始数据。
* `build/reports/tests` 目录包含一个HTML报告，描述了测试的结果。

**打包和运行程序**  

`gradle assemble`或`gradle build`  
* 生成: `build/libs/javaapp.jar`  
* 运行:  
    `java -jar build/libs/javaapp.jar`  
    `java -cp build/classes/main com.dsl.gradle.HelloWorld`  



-------------------

## Java 构建入门

[Gradle docs 46. Java Quickstart](https://docs.gradle.org/3.4.1/userguide/userguide.html)

### Java 插件

如你所见, Gradle 是一种多用途的构建工具. 它可以在你的构建脚本里构建任何你想要实现的东西. 但前提是你必须先在构建脚本里加入代码, 不然它什么都不会执行.

大都数 Java 项目是非常相像的: 你需要编译你的 Java 源文件, 运行一些单元测试, 同时创建一个包含你类文件的 JAR. 如果你可以不需要为每一个项目重复编写这些, 我想你会非常乐意的.

幸运的是, 你现在不再需要做这些重复劳动了. Gradle 通过使用插件解决了这个问题. 插件是 Gradle 的扩展, 它会通过某种方式配置你的项目, 典型的有加入一些预配置任务. Gradle 自带了许多插件, 你也可以很简单地编写自己的插件并和其他开发者分享它. Java 插件就是一个这样的插件. 这个插件在你的项目里加入了许多任务， 这些任务会编译和单元测试你的源文件, 并且把它们都集成一个 JAR 文件里.

Java 插件是基于合约的. 这意味着插件已经给项目的许多方面定义了默认的参数, 比如 Java 源文件的位置. 如果你在项目里遵从这些合约, 你通常不需要在你的构建脚本里加入太多东西. 如果你不想要或者是你不能遵循合约, Gradle 也允许你自己定制你的项目. 事实上, 因为对 Java 项目的支持是通过插件实现的, 如果你不想要的话, 你一点也不需要使用这个插件来构建你的项目.

### 一个基础的 Java 项目
`build.gradle` 文件
````Groovy
apply plugin: 'java'
````
Gradle 希望能在 `src/main/java` 找到你的源代码, 在 `src/test/java` 找到你的测试代码, 也就是说 Gradle 默认地在这些路径里查找资源. 另外, 任何在 `src/main/resources` 的文件都将被包含在 JAR 文件里, 同时任何在 `src/test/resources` 的文件会被加入到 `classpath` 中以运行测试代码. 所有的输出文件将会被创建在构建目录里, JAR 文件存放在 `build/libs` 文件夹里.

**创建可执行应用**  
`build.gradle` 文件
````Groovy
apply plugin: 'application'
mainClassName = 'com.dm.todo.ToDoApp'
````

**外部依赖**

一个 Java 项目有许多外部的依赖, 既是指外部的 JAR 文件. 在 Gradle 中, JAR 文件位于一个仓库中， 这里的仓库类似于 MAVEN 的仓库. 仓库可以被用来提取依赖, 或者放入一个依赖, 或者两者皆可.    
`build.gradle` 文件
```Groovy
// 加入 Maven 仓库
repositories {
    mavenCentral()
}

// 加入一些依赖
dependencies {
    compile group: 'commons-collections', name: 'commons-collections', version: '3.2.2'
    testCompile group: 'junit', name: 'junit', version: '4.+'
}
```

**定制项目**

Java 插件给项目加入了一些属性 (propertiy). 这些属性已经被赋予了默认的值, 已经足够来开始构建项目了. 如果你认为不合适, 改变它们的值也是很简单的.  
`build.gradle` 文件
````Groovy
//定制 MANIFEST.MF 文件
sourceCompatibility = 1.7
version = '1.0'
jar {
    manifest {
        attributes 'Implementation-Title': 'Gradle Quickstart', 'Implementation-Version': version
    }
}

//测试阶段加入一个系统属性
test {
    systemProperties 'property': 'value'
}
````

**发布 JAR 文件**

通常 JAR 文件需要在某个地方发布. 为了完成这一步, 你需要告诉 Gradle 哪里发布 JAR 文件. 在 Gradle 里, 生成的文件比如 JAR 文件将被发布到仓库里. 在我们的例子里, 我们将发布到一个本地的目录. 你也可以发布到一个或多个远程的地点.  
`build.gradle` 文件
````Groovy
uploadArchives {
    repositories {
       flatDir {
           dirs 'repos'
       }
    }
}
````

**创建 Eclipse 项目**

为了把你的项目导入到 Eclipse, 你需要加入一个插件。 运行 gradle eclipse 命令来生成 Eclipse 的项目文件。
`build.gradle` 文件
```Groovy
apply plugin: 'eclipse'
```

**完整build.gradle文件**
build.gradle
```Groovy
apply plugin: 'java'
apply plugin: 'eclipse'

sourceCompatibility = 1.7
version = '1.0'
jar {
    manifest {
        attributes 'Implementation-Title': 'Gradle Quickstart', 'Implementation-Version': version
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compile group: 'commons-collections', name: 'commons-collections', version: '3.2.2'
    testCompile group: 'junit', name: 'junit', version: '4.+'
}

test {
    systemProperties 'property': 'value'
}

uploadArchives {
    repositories {
       flatDir {
           dirs 'repos'
       }
    }
}


test {
    systemProperty "file.encoding", "utf-8"
    filter {
        includeTestsMatching "sia.knights.*"
    }
}

// Pass-through 'System.out & err' into output window during execution of JUnit test
test.testLogging {
    showStandardStreams = true
}
```


### 多项目的 Java 构建

典型的多项目构建的项目布局
````Groovy
multiproject/
  api/
  services/webservice/
  shared/
  services/shared/
````

**定义一个多项目构建**

为了定义一个多项目构建, 你需要创建一个设置文件 `settings.gradle`. 设置文件放在源代码的根目录, 它指定要包含哪个项目.  
`settings.gradle` 文件
````
include "shared", "api", "services:webservice", "services:shared"
````

**通用配置**

对于绝大多数多项目构建, 有一些配置对所有项目都是常见的或者说是通用的. 在我们的例子里, 我们将在根项目里定义一个这样的通用配置, 使用一种叫做配置注入的技术 (configuration injection). 这里, 根项目就像一个容器, `subprojects` 方法遍历这个容器的所有元素并且注入指定的配置 . 通过这种方法, 我们可以很容易的定义所有档案和通用依赖的内容清单:

`build.gradle` 文件
````
subprojects {
    apply plugin: 'java'
    apply plugin: 'eclipse-wtp'

    repositories {
       mavenCentral()
    }

    dependencies {
        testCompile 'junit:junit:4.12'
    }

    version = '1.0'
    jar {
        manifest.attributes provider: 'gradle'
    }
}
````

**项目之间的依赖**

你可以在同一个构建里加入项目之间的依赖, 举个例子, 一个项目的 JAR 文件被用来编译另外一个项目. 在 `api` 构建文件里我们将加入一个由 `shared` 项目产生的 JAR 文件的依赖. 由于这个依赖, Gradle 将确保 `shared` 项目总是在 `api` 之前被构建.

`api/build.gradle` 文件
````Groovy
dependencies {
    compile project(':shared')
}
````

**创建一个发行版本**

我们同时也加入了一个发行版本, 将会送到客户端:

`api/build.gradle` 文件
```Groovy
task dist(type: Zip) {
    dependsOn spiJar
    from 'src/dist'
    into('libs') {
        from spiJar.archivePath
        from configurations.runtime
    }
}

artifacts {
   archives dist
}
```



---

## Java Gradle之gretty插件

参考文档
* [Gretty documentation](http://akhikhl.github.io/gretty-doc/)
* [GitHub akhikhl/gretty](https://github.com/akhikhl/gretty)
* [Java Gradle入门指南之gretty插件（安装、命令与核心特性） ](http://www.cnblogs.com/gzdaijie/p/5267166.html)

gretty 插件支持 Jetty 和 Tomcat。  
gretty支持热部署、HTTPS、转发、调试、自动化运行环境等诸多特性，让开发和部署变得更加简单。

**安装gretty**

`build.gradle` 加入 gretty 插件
```Groovy
apply from: 'https://raw.github.com/akhikhl/gretty/master/pluginScripts/gretty.plugin'
```
或者
```Groovy
buildscript {
  repositories { jcenter() }
  dependencies { classpath 'org.akhikhl.gretty:gretty:+' }
}

apply plugin: 'org.akhikhl.gretty'
```

**常用命令**
* gradle appRun
  - 编译当前项目
  - 不依赖于war任务
  - 另有appRunWar、appRunDebug、appRunWarDebug
* gradle appStart
  - 编译当前项目
  - 使用新java线程开启服务，监听端口，等待HTTP请求
  - 不依赖于war任务
  - 不主动关闭服务，即一直在运行，需用gradle appStop关闭
  - 另有appStartWar、appStartDebug、appStartWarDebug
* gradle jetty* / gradle tomcat*
  包含Start、Run、Stop等

**核心特性**

选择servlet 容器
* `contextPath` 设置根路径，默认为项目名称
* `httpPort` 端口默认`8080`, `port`已作废
* `serlvetContainer` 支持 jetty7/8/9，tomcat7/8
* `build.gradle` 添加
```
gretty {
  httpPort = 8080
  serlvetContainer = 'jetty9'
  contextPath = '/'
}
```

热部署
* scanInterval：监视周期，单位为秒，设置为0等于完全关闭热部署
* scanDir：需要监视的文件夹
* recompileOnSourceChange：监视源码变动，自动编译
* reloadOnClassChange：编译的类发生改变，自动加载
* reloadOnConfigChange：WEB-INF或META-INF发生改变
* reloadOnLibChange：依赖发生改变

快速加载
* fastReload属性，默认为true，监听webapp/中的内容，文件发生改变，无需重启。

添加新的资源目录
```Groovy
gretty{
    // ...
    extraResourceBase 'dir1',
    extraResourceBases 'dir2','dir3'
    // ...
}
```

HTTPS 支持
- 生成自签名证书，仅在开发时使用
  ```Groovy
  gretty {
  httpsEnabled = true
  // httpEnabled = false 禁用http
  // httpsPort = 443 httpsPort默认为 8443
  }
  ```
* 支持手动配置
  转发
* 在`WEB-INF/web.xml`中加入 `RedirectFilter`设置
* 创建WEB-INF/filter.groovy，设置转发规则

调试（Debug）
```Groovy
// 为所有的debug命令配置参数
gretty {
  debugPort = 5005      // 默认
  debugSuspend = true   // 默认
}

// 仅针对appRunDebug
gretty {
  afterEvaluate {
    appRunDebug {
      debugPort = 5005
      debugSuspend = true
    }
  }
}
```

**产品生成**

`gradle buildProduct`
* 生成安装文件
* 生成目录位于 `build/output/${project.name}`

`gradle archiveProduct`
* 打包生成的安装文件
* 生成目录位于 `build/output/${project.name}`

