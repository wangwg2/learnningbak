---
html:
  embed_local_images: true
  embed_svg: true
  offline: true
  toc: Gradle
print_background: false
toc:
  depth_from: 1
  depth_to: 3
  ordered: false
---
# Gradle {ignore=true}
[TOC]

---
## Getting Started

#### 在线文档代码
* [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)
* [Gradle User Guide 中文版](http://udn.yyuap.com/doc/wiki/project/GradleUserGuide-Wiki/index.html)
* 构建语言参考(DSL)：[Gradle Build Language Reference](https://docs.gradle.org/current/dsl/)
* Gradle in Action：[gradle-in-action-source](https://github.com/bmuschko/gradle-in-action-source)

#### 目录文件说明
* `gradle.md ` -- Gradle文档
* `gradle-guide.md` -- Gradle入门
* `gradle-examples` -- Gradle入门系列 (示例代码)
* `gradle-guide` : gradle guide 示例
* `gradle-demo`: gradle项目示例
* `gradle-in-action-source`: gradle-in-action代码

---
## Gradle 使用

#### 常用项目构建任务
* `gradle build`: 编译和测试你的代码, 并且创建一个包含类和资源的 JAR 文件
* `gradle clean`: 删除 build 生成的目录和所有生成的文件.
* `gradle assemble`: 编译并打包你的代码, 但是并不运行单元测试.
* `gradle check`: 编译并测试你的代码.


---
## Gradle 插件

Gradle 的核心为真实世界提供了很少的自动化. 所有的实用特性,类似编译java源码的能力, 是由插件提供的. 
插件添加了新的任务(如:JavaCompile),域对象(如:SourceSet),公约(如:Java资源位置是src/main/java)以及来自其他插件延伸核心对象和对象。

应用插件到项目允许插件来扩展项目的能力。它可以做的事情，如：
* 扩展摇篮模型（如:添加可配置新的DSL元素）
* 按照惯例配置项目(如:添加新的任务或配置合理的默认值)
* 应用特定的配置（如:增加组织库或执行标准）

通过应用插件,而不是向项目构建脚本添加逻辑,我们可以收获很多好处.应用插件:
* 促进重用和减少维护在多个项目类似的逻辑的开销
* 允许更高程度的模块化，提高综合性和组织
* 封装必要的逻辑，并允许构建脚本尽可能是声明性地

**插件的类型**  
在Gradle中一般有两种类型的插件,脚本插件和二进制插件(对象插件).
* 脚本插件是普通的Gradle构建脚本,可以被导入到其他的构建脚本中。
* 二进制插件是实现了`org.gradle.api.Plugin`接口的类。二进制插件的源代码通常放在`buildSrc`目录下，要么和项目在一起， 要么是独立项目，作为JAR文件发布。

**使用插件**   
脚本插件
* 脚本插件可以从本地文件系统或在远程位置的脚本中应用.
* 文件系统的位置是相对于项目目录,而远程脚本位置的是由一个HTTP URL指定的.
* 多个脚本插件（两种形式之一）可以被应用到给定的构建。  
* 使用：`apply from: 'other.gradle'` 

二进制插件  
* 插件可以使用插件ID应用。 插件的id作为给定插件的唯一标识符。 
* 核心插件注册一个可以用作插件的id的短名称. 在上述情况下,我们可以使用简称java的插件以应用JavaPlugin.
* 社区插件,一方面会使用一个完全合格的形式的插件id (如`com.github.foo.bar`),但还是有一些传统的插件可能仍然使用很短的不合格的格式.
* 使用：`apply plugin: 'java'`

**使用 `buildscript` 应用插件**  
* 项目可以通过添加向构建脚本中加入插件的类路径然后再应用插件,添加作为外部JAR文件的二进制插件。
* 外部jar可以使用`buildscrip{}`块添加到构建脚本类路径  

`buildscript` 定义插件的位置、仓库和插件依赖
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.jfrog.bintray.gradle:gradle-bintray-plugin:0.4.1"
    }
}

apply plugin: "com.jfrog.bintray"
```


**使用插件的DSL应用插件**
* 新的插件DSL提供了更为简洁,方便的方式来声明插件的依赖关系。它的适用于与新的Gradle Plugin Portal，同时提供了方便的核心和社区插件.该插件脚本块配置PluginDependenciesSpec的实例.
* 要应用的核心插件,可以使用短名称:  
  `plugins { id 'java' }`  
* 要从插件门户应用一个社区插件,必须使用插件的完全限定id:  
  `plugins { id "com.jfrog.bintray" version "0.4.1" }`
* 不必要进行进一步的配置,就是说没有必要配置`buildscript`的类路径,Gradle会从插件门户找到该插件,并使构建可用.

#### 标准的 Gradle 插件
* 语言插件： `java`,`groovy`,`scala`,`antlr`
* 正在孵化的语言插件: `assembler`,`c`,`cpp`,`objective-c`,`objective-cpp`,`windows-resources`
* 集成插件: `application`,`ear`,`jetty`,`maven`,`osgi`,`war`
* 孵化中的集成插件: `distribution`,`java-library-distribution`,`ivy-publish`,`maven-publish`
* 软件开发插件: `announce`,`checkstyle`,`eclipse`,`eclipse-wtp`,`idea`,`sonar`,`project-report`等等
* 基本插件： `base`,`java-base`,`groovy-base`,`scala-base`,`reporting-base`

**第三方插件** 

你可以在维基上找到外部插件的列表。


---

## 项目结构

#### 初始化Gradle项目

`gradle init --type=java-application`  
支持的类型包括：`pom`/`java-applicatio`/`java-library`/`scala-library`/`groovy-library`/`basic`

`gradle init --type java-application --test-framework spock`  
`gradle init --type java-application --test-framework testng`

#### 典型项目目录结构
````
.gradle           --
------- gradle wrapper --------------------------------------------------
gradlew           -- 执行 Gradle 命令的包装器脚本（windows下）
gradlew.bat       -- 执行 Gradle 命令的包装器脚本 (*nix下)
gradle/wrapper/   -- 声明了gradle的目录与下载路径以及当前项目使用的gradle版本
  gradle-wrapper.properties
  gradle-wrapper.jar
-------------------------------------------------------------------------
build.gradle      -- Gradle 构建脚本
settings.gradle   -- 全局的项目配置文件，指定工程名。
gradle.properties -- 属性定义文件
-------------------------------------------------------------------------
src/              -- 项目源代码
  main
  src
-------------------------------------------------------------------------
build/            -- 构建运行输出
````

#### gradle.properties  

属性定义文件。 [Online docs](https://docs.gradle.org/current/userguide/build_environment.html)  

```
jspApiVersion = 2.1
jstlVersion = 1.2
junitVersion=4.12
servletApiVersion = 3.1.0
```
在项目根目录下建立名为`gradle.properties`文件，在该文件中定义需要的属性。这些属性在Gradle构建Gradle领域对象（即project对象实例）时会被自动加到project对象实例中作为其属性被直接调用。  
在命令行中通过`-D`或者`-P`给Gradle实时创建属性。  
`gradle.properties`加载顺序：
* from `gradle.properties` in project build dir.
* from `gradle.properties` in gradle user home.
* from system properties, e.g. when `-Dsome.property` is set on the command line.

#### build.gradle

```
// 插件
apply plugin: 'java'
apply plugin: 'application'
mainClassName = 'App'

// 仓库
repositories {
  mavenLocal()
  mavenCentral()
  jcenter()
}

//依赖关系
dependencies {
  compile 'com.google.guava:guava:20.0'
  testCompile 'junit:junit:4.12'
}
```
构建脚本内部结构
* `allprojects { }`:    配置项目和每个子项目.
* `artifacts { }`:      配置每个发布构件.
* `buildscript { }`:    配置构建脚本路径.
* `configurations { }`: 配置项目的依赖配置.
* `dependencies { }`:   配置依赖关系.
* `repositories { }`:   配置软件仓库.
* `sourceSets { }`:     配置源代码集.
* `subprojects { }`:    配置子项目.
* `publishing { }` :    配置发布插件.

#### settings.gradle

`settings.gradle` 文件示例
```
rootProject.name = 'myapp'
include 'sub-project1', 'sub-project2', 'sub-project3'
```
表示当前项目名为 `myapp`。 在当前的项目下建立三个子项目，分别为`sub-project1`, `sub-project2`,`sub-project3`。
默认情况下，每个子项目的名称对应着当前操作系统目录下的一个子目录。

在Gradle中，使用文件`settings.gradle`定义当前项目的子项目。
默认情况下，`settings.gradle` 文件和 `build.gradle` 文件的位置相同。 而在没有 `settings.gradle` 文件的项目中，如果执行构建，则Gradle按这个顺序查找 `settings.gradle`：
1. 从当前目录的master文件夹内寻找。
2. 如果master目录中也没有，则搜索父目录。
3. 如果父目录也没找到，则把构建当成单个项目构建。
4. 如果找到了，并且发现当前项目是多项目构建的一部分，则执行多项目构建。没找到，则执行单项目构建。


---
## 概念与原理

Gradle 是一个基于 Apache Ant 和 Apache Maven 概念的项目自动化建构工具。它使用一种基于 Groovy 的特定领域语言来声明项目设置，而不是传统的XML。 当前其支持的语言限于 Java、 Groovy 和 Scala，计划未来将支持更多的语言。

Gradle 是一种构建工具,它可以帮你管理项目中的差异,依赖,编译,打包,部署......,你可以定义满足自己需要的构建逻辑,写入到`build.gradle`中供日后复用.

Gradle的构建分两个阶段
* 第一阶段是设置阶段(configuration phase)，分析构建脚本，处理依赖关系和执行顺序等，脚本本身也需要依赖来完成自身的分析。
* 第二阶段是执行阶段(execution phase)，此阶段真正构建项目并执行项目下的各个任务。

和 Maven 一样，Gradle 只是提供了构建项目的一个框架，真正起作用的是`Plugin`。 Gradle 在默认情况下为我们提供了许多常用的 `Plugin`，其中便包括构建`Java`项目的`Plugin`，还有`War`，`Ear`等。 与 Maven 不同的是，Gradle 不提供内建的项目生命周期管理，只是`Java`的 Plugin 向 Project 中添加了许多Task，这些Task依次执行，为我们营造了一种如同Maven般项目构建周期。

现在我们都在谈领域驱动设计，而 Gradle 本身的领域对象主要有 `Project` 和 `Task`。 `Project` 为 `Task` 提供了执行上下文，所有的 `Plugin` 要么向 `Project` 中添加了用于配置的 `Property`，要么向 `Project` 中添加不同的`Task`。一个`Task`表示一个逻辑上较为独立的执行过程，比如编译Java源代码，拷贝文件，打包Jar文件，甚至可以是执行一个系统命令或者调用Ant。另外，一个 `Task` 可以读取和设置 `Project` 的 `Property` 以完成特定的操作。

#### Gradle特征  
Gradle让java项目在构建上有了跨越性发展。Gradle提供了：
* 很灵活的通用构建工具，就像ant。
* 使用可切换的，已经约定好的框架，就像maven。但是Gradle不会对你做任何限制。
* 支持多项目的构建
* 强大的依赖管理（基于Apache lvy）。
* 完美兼容maven或Ivy仓库
* 无需提供远程仓库、`pom.xml`、`ivy.xml`就支持依赖管理
* 支持Ant类的`task`和`builds`
* Groovy构建脚本
* 提供rich domain model来描述构建信息

#### 基本概念
* **projects(项目)**  
  每一个构建都是由一个或多个 projects 构成的.  
  一个 project 到底代表什么取决于你想用 Gradle 做什么.  
  举个例子, 一个 project 可以代表一个 JAR 或者一个网页应用. 它也可能代表一个发布的 ZIP 压缩包, 这个 ZIP 可能是由许多其他项目的 JARs 构成的.
  但是一个 project 不一定非要代表被构建的某个东西. 它可以代表一件**要做的事, 比如部署你的应用.
  不要担心现在看不懂这些说明. Gradle 的合约构建可以让你来具体定义一个 project 到底该做什么.
* **tasks(任务)**  
  每一个 project 是由一个或多个 tasks 构成的.
  一个 task 代表一些更加细化的构建.  
  可能是编译一些 classes, 创建一个 JAR, 生成 javadoc, 或者生成某个目录的压缩文件.
  目前, 我们先来看看定义构建里的一些简单的 task. 以后的章节会讲解多项目构建以及如何通过 projects 和 tasks 工作.

---
## 安装与配置

**安装**
* 需要Java环境。
* 下载安装，`GRADLE_HOME`环境变量为安装路径，`GRADLE_HOME/bin`添加到执行路径。
* `gradle -v`检查安装。
* JVM选项可以通过设置环境变量来更改. 您可以使用`GRADLE_OPTS`或者`JAVA_OPTS`.

**Gradle Eclipse配置**

**Gradle Wrapper**  
Gradle Wrapper (Gradle包装器)，Gradle的核心特征， 让机器在没有安装Gradle运行时环境的情况下运行Gradle构建。
* 可指定Gradle版本。通过自动从中心仓库下载Gradle运行时，解压和使用来实现的。
* 建议对每个Gradle项目使用包装器。

项目目录下的 Gradle Wrapper 内容：
```
gradlew           -- 执行 Gradle 命令的包装器脚本（windows下）
gradlew.bat       -- 执行 Gradle 命令的包装器脚本 (*nix下)
gradle/wrapper/   -- 声明了gradle的目录与下载路径以及当前项目使用的gradle版本
  gradle-wrapper.properties
  gradle-wrapper.jar
```


---
## Gradle命令

命令语法
```
gradle [option...] [task...]
```
`build.gradle` 默认项目构建文件

#### 命令简要说明
* 日志输出
  * `-q`(`--quiet`) 启用重要信息级别，只会输出错误信息。
  * `-i`(`--info`) 输出除debug以外的所有信息。
  * `-d`(`--debug`) 输出所有日志信息。
* 堆栈跟踪
  * `-s`(`--stacktrace`) 输出详细的错误堆栈。
  * `-S`(`--full-stacktrace`) 输出全部堆栈信息
* 跳过指定的测试: `-x`
  `gradle build -x test`
* 继续执行task而忽略前面失败的task: `--continue`
* 调用task时使用短名或缩写, 可通过全名调用、前缀调用或首字母调用
* 使用指定的gradle文件(*`myfile.gradle`*)调用task：`-b`  
  `gradle -b` *`myfile.gradle`*
* 使用指定的项目目录调用task：`-p`  
  `gradle -p` *`projectname`* *`taskname`*
* 显示task之间的依赖关系.  
  `gradle tasks --all`
* 查看指定阶段的依赖关系。  
  `gradle dependencies`
* 查看指定dependency的依赖情况: `dependecyInsight`  
  `gradle dependencyInsight --dependency junit --configuration testCompile`
* 产生build运行时间的报告: `--profile`  
  `gradle build --profile`
* 试运行build: `-m`  
  `gradle -m build`
* Gradle的图形界面: `--gui`  
* 重新编译Gradle脚本: `--recompile-scripts`  
* 添加 `apply plugin: 'application'`, 可以运行应用 `gradle run`


#### 命令详解
````
USAGE: gradle [option...] [task...]

-?, -h, --help          Shows this help message.
-a, --no-rebuild        Do not rebuild project dependencies.
-b, --build-file        Specifies the build file.
-c, --settings-file     Specifies the settings file.
--configure-on-demand   Only relevant projects are configured in this build run.
                        This means faster build for large multi-project builds. [incubating]
--console               Specifies which type of console output to generate.
                        Values are 'plain', 'auto' (default) or 'rich'.
--continue              Continues task execution after a task failure.
-D, --system-prop       Set system property of the JVM (e.g. -Dmyprop=myvalue).
-d, --debug             Log in debug mode (includes normal stacktrace).
--daemon                Uses the Gradle Daemon to run the build. Starts the Daemon if not running.
--foreground            Starts the Gradle Daemon in the foreground. [incubating]
-g, --gradle-user-home  Specifies the gradle user home directory.
--gui                   Launches the Gradle GUI.
-I, --init-script       Specifies an initialization script.
-i, --info              Set log level to info.
--include-build         Includes the specified build in the composite. [incubating]
-m, --dry-run           Runs the builds with all task actions disabled.
--max-workers           Configure the number of concurrent workers Gradle is allowed to use. [incubating]
--no-daemon             Do not use the Gradle Daemon to run the build.
--no-scan               Disables the creation of a build scan. [incubating]
--offline               The build should operate without accessing network resources.
-P, --project-prop      Set project property for the build script (e.g. -Pmyprop=myvalue).
-p, --project-dir       Specifies the start directory for Gradle. Defaults to current directory.
--parallel              Build projects in parallel. Gradle will attempt to determine the optimal
                        number of executor threads to use. [incubating]
--profile               Profiles build execution time and generates a report in the
                        <build_dir>/reports/profile directory.
--project-cache-dir     Specifies the project-specific cache directory.
                        Defaults to .gradle in the root project directory.
-q, --quiet             Log errors only.
--recompile-scripts     Force build script recompiling.
--refresh-dependencies  Refresh the state of dependencies.
--rerun-tasks           Ignore previously cached task results.
-S, --full-stacktrace   Print out the full (very verbose) stacktrace for all exceptions.
-s, --stacktrace        Print out the stacktrace for all exceptions.
--scan                  Creates a build scan. Gradle will fail the build if the build scan
                        plugin has not been applied. [incubating]
--status                Shows status of running and recently stopped Gradle Daemon(s).
--stop                  Stops the Gradle Daemon if it is running.
-t, --continuous        Enables continuous build. Gradle does not exit and will re-execute tasks
                        when task file inputs change. [incubating]
-u, --no-search-upward  Don't search in parent folders for a settings.gradle file.
-v, --version           Print version info.
-x, --exclude-task      Specify a task to be excluded from execution.
````





---
## Gradle 构建脚本基础

[Gradle docs 16. Build Script Basics](https://docs.gradle.org/3.4.1/userguide/tutorial_using_tasks.html)

#### 概念： projects 与 tasks
projects(项目)：每一个构建都是由一个或多个 projects 构成的. 一个 project 到底代表什么取决于你想用 Gradle 做什么.   
tasks(任务)： 每一个 project 是由一个或多个 tasks 构成的. 一个 task 代表一些更加细化的构建.  
>代码说明
>`guide`目录，`basic.gradle`、`basic02.gradle`文件

#### 构建脚本基础示例    

**Hello World**
```
task hello {
    group 'basic'
    description 'Produces a greeting'
    doLast {
        println 'Hello, World'
    }
}
```
运行 `gradle -q hello`
这个构建脚本定义了一个独立的 task, 叫做 `hello`, 并且加入了一个 `action`.

**构建脚本代码**  

使用Groovy脚本
```
task upper {
    doLast {
        String someString = 'mY_nAmE'
        println "Original: " + someString
        println "Upper case: " + someString.toUpperCase()
    }
}

task count {
    doLast {
        4.times { print "$it " }
    }
}
```

**任务依赖**
```
task intro(dependsOn: hello) {
    doLast { println "I'm Gradle" }
}

task taskX(dependsOn: 'taskY') {
    doLast {
        println 'taskX'
    }
}
task taskY {
    doLast {
        println 'taskY'
    }
}
```

**动态任务**  
动态创建了 task1, task2, task3, task4
```
4.times {
    counter ->
    task "task$counter" << {
        println "I'm task number $counter"
    }
}
```

**使用已存在task(任务)**  
加入依赖  
```
task0.dependsOn task2, task3
```
加入行为
```
hello.doFirst {
    println 'Hello Venus'
}
```

**短标记法**  
有一个短标记 $ 可以访问一个存在的任务. 也就是说每个任务都可以作为构建脚本的属性:
name 是任务的默认属性, 代表当前任务的名称
```
hello.doLast {
    println "Greetings from the $hello.name task."
}
```

**自定义属性**
```
task myTask {
    ext.myProperty = "myValue"
}

task printTaskProperties {
    doLast { println myTask.myProperty }
}
```

**调用 Ant 任务**  
Gradle 通过 Groovy 出色的集成了 Ant 任务. Groovy 自带了一个 `AntBuilder`. 相比于从一个 `build.xml` 文件中使用 Ant 任务, 在 Gradle 里使用 Ant 任务更为方便和强大.
```
task antloadfile {
    doLast {
        def files = file('../antLoadfileResources').listFiles().sort()
        files.each { File file ->
            if (file.isFile()) {
                ant.loadfile(srcFile: file, property: file.name)
                println " *** $file.name ***"
                println "${ant.properties[file.name]}"
            }
        }
    }
}
```

**使用方法**
```
task checksum << {
    fileList('../antLoadfileResources').each {File file ->
        ant.checksum(file: file, property: "cs_$file.name")
        println "$file.name Checksum: ${ant.properties["cs_$file.name"]}"
    }
}

task loadfile << {
    fileList('../antLoadfileResources').each {File file ->
        ant.loadfile(srcFile: file, property: file.name)
        println "I'm fond of $file.name"
    }
}

File[] fileList(String dir) {
    file(dir).listFiles({file -> file.isFile() } as FileFilter).sort()
}
```

**默认任务**
```
defaultTasks 'hello', 'count'
```

**通过 DAG 配置**  

Gradle 有一个配置阶段和执行阶段. 在配置阶段后, Gradle 将会知道应执行的所有任务.Gradle 为你提供一个"钩子", 以便利用这些信息.
```
task distribution {
    doLast { println "We build the zip with version=$version" }
}

task release(dependsOn: 'distribution') {
    doLast { println 'We release now' }
}

gradle.taskGraph.whenReady {taskGraph ->
    if (taskGraph.hasTask(release)) {
        version = '1.0'
    } else {
        version = '1.0-SNAPSHOT'
    }
}
```
分别运行命令 `gradle -q distribution` 和 `gradle -q release`



---
## Gradle 语法

Gradle脚本是使用Groovy语言来写的。Groovy的语法有点像Java，希望你能接受它。  
Groovy中有一个很重要的概念你必要要弄懂–Closure（闭包）  

**Closures**

Closure是一段单独的代码块，它可以接收参数，返回值，也可以被赋值给变量。和Java中的`Callable`接口，`Future`类似，也像函数指针，你自己怎么方便理解都好。

关键是这块代码会在你调用的时候执行，而不是在创建的时候。看一个Closure的例子：
```
def myClosure = { println 'Hello world!' }

//execute our closure
myClosure()

#output: Hello world!
```

下面是一个接收参数的Closure：
```
def myClosure = {String str -> println str }

//execute our closure
myClosure('Hello world!')

#output: Hello world!
```

接收多个参数的Closure：
```
def myClosure = {String str, int num -> println "$str : $num" }

//execute our closure
myClosure('my string', 21)

#output: my string : 21
```

另外，参数的类型是可选的，上面的例子可以简写成这样：
```
def myClosure = {str, num -> println "$str : $num" }

//execute our closure
myClosure('my string', 21)

#output: my string : 21
```

很酷的是Closure中可以使用当前上下文中的变量。默认情况下，当前的上下文就是closure被创建时所在的类：
```
def myVar = 'Hello World!'
def myClosure = {println myVar}
myClosure()

#output: Hello world!
```

如你所见，在创建closure的时候，`myVar`并不存在。这并没有什么问题，因为当我们执行closure的时候，在closure的上下文中，`myVar`是存在的。这个例子中。因为我在执行closure之前改变了它的上下文为m，因此`myVar`是存在的。

把closure当做参数传递

closure的好处就是可以传递给不同的方法，这样可以帮助我们解耦执行逻辑。前面的例子中我已经展示了如何把closure传递给一个类的实例。下面我们将看一下各种接收closure作为参数的方法：
1. 只接收一个参数，且参数是closure的方法： `myMethod(myClosure)`
2. 如果方法只接收一个参数，括号可以省略： `myMethod myClosure`
3. 可以使用内联的closure：` myMethod {println ‘Hello World’}`
4. 接收两个参数的方法： `myMethod(arg1, myClosure)`
5. 和4类似，单数closure是内联的： `myMethod(arg1, { println ‘Hello World’ })`
6. 如果最后一个参数是closure，它可以从小括号从拿出来：`myMethod(arg1) { println ‘Hello World’ }`

这里我只想提醒你一下，3和6的写法是不是看起来很眼熟？


**Gradle**

现在我们已经了解了基本的语法了，那么如何在Gradle脚本中使用呢？先看下面的例子吧：
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.2.3'
    }
}

allprojects {
    repositories {
        jcenter()
    }
```
知道了Groovy的语法，是不是上面的例子就很好理解了?   
首先就是一个buildscript方法，它接收一个closure：  
`def buildscript(Closure closure)`  

接着是allprojects方法，它也接收一个closure参数：  
`def allprojects(Closure closure)`  

其他的都类似。。。
现在看起来容易多了，但是还有一点不明白，那就是这些方法是在哪里定义的？答案就是Project.

**Project**

这是理解Gradle脚本的一个关键。
>构建脚本顶层的语句块都会被委托给Project的实例

这就说明Project正是我要找得地方。

在Project的文档页面搜索buildscript方法,会找到`buildscript{}` script block(脚本块).等等，script block是什么鬼？根据文档：
>script block就是只接收closure作为参数的方法

继续阅读buildscript的文档，文档上说Delegates to: `ScriptHandler from buildscript`。也就是说，我们传递给buildscript方法的closure，最终执行的上下文是ScriptHandler。在上面的例子中，我们的传递给buildscript的closure调用了`repositories(closure)`和`dependencies(closure)`方法。既然closure被委托给了ScriptHandler，那么我们就去ScriptHandler中寻找`dependencies`方法。

找到了`void dependencies(Closure configureClosure)`，根据文档，dependencies是用来配置脚本的依赖的。而dependencies最终又是委托到了DependencyHandler。

看到了Gradles是多么广泛的使用委托了吧。理解委托是很重要滴。

**Script blocks**

默认情况下，Project中预先定义了很多script block，但是Gradle插件允许我们自己定义新的script blocks！  
这就意味着，如果你在build脚本顶层发了一些{…}，但是你在Gradle的文档中却找不到这个script blocks或者方法，绝大多情况下，这是一些来自插件中定义的script block。

***android Script block***

我们来看看默认的Android app/build.gradle文件:
```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 22
    buildToolsVersion "22.0.1"

    defaultConfig {
        applicationId "com.trickyandroid.testapp"
        minSdkVersion 16
        targetSdkVersion 22
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

可以看到，这里有一个android方法，它接收一个closure参数。但是如果我们在Project的文档中搜索，是找不到这个方法的。原因很简单，这不是在Project中定义的方法。

仔细查看build脚本，可以看到在抵用android方法之前，我们使用了com.android.application插件。Android application插件扩展了Project对象，添加了android Script block。

哪里可以找到Android插件的文档呢？Android Tools website

如果我们打开AppExtension的文档，我们就可以找打build script中使用的方法了属性：
1. compileSdkVersion 22. 如果我们搜索compileSdkVersion，将会找到这个属性. 这里我们给这个属性赋值 “22”。
2. buildToolsVersion和1类似
3. defaultConfig - 是一个script block将会委托给ProductFlavor类来执行。
4. 其他

现在我们已经能够理解Gradle脚本的语法了，也知道如何查找文档了。


---
## Gradle in Action (Gradle实战)

**Gradle 介绍**
* 项目自动化介绍
* 下一代构建工具：Gradle
* 通过范例学习构建Gradle项目  

**掌握基本原理**
* 构建脚本概要
* 依赖管理
* 多项目构建
* Gradle测试
* 扩展Gradle
* 集成与迁移

**从构建到部署**
* IDE支持与工具
* 构建多语言项目
* 代码质量管理与监测
* 持续集成
* 打包与发布
* 基础环境准备与部署

**附录**
* 驾驭命令行
* Gradle用户所需要了解的Groovy

