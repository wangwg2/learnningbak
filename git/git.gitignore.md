### .gitignore

`.gitignore` 配置文件用于配置不需要加入版本管理的文件，配置好该文件可以为我们的版本管理带来很大的便利。

[.gitignore templates](https://github.com/github/gitignore)  
A collection of useful .gitignore templates


#### 配置语法
* 以斜杠 `/` 开头表示目录；
* 以星号 `*` 通配多个字符；
* 以问号 `?` 通配单个字符
* 以方括号 `[]` 包含单个字符的匹配列表；
* 以叹号 `!` 表示不忽略(跟踪)匹配到的文件或目录；

把已经track的文件，过滤也补上来了
````
git update-index --assume-unchanged file 		#忽略跟踪
git update-index --no-assume-unchanged file 	# 恢复跟踪
````

#### 示例
````
fd1/*       忽略目录 fd1 下的全部内容；
            注意，不管是根目录下的 /fd1/ 目录，还是某个子目录 /child/fd1/ 目录，都会被忽略；
/fd1/*      忽略根目录下的 /fd1/ 目录的全部内容；

/*
!.gitignore
!/fw/bin/
!/fw/sf/
            说明：忽略全部内容，但是不忽略 .gitignore 文件、根目录下的 /fw/bin/ 和 /fw/sf/ 目录；
````

**参考 简版**
````shell
## customize

## RESERVED ##
*.sw?
.#*
*#
*~
*.jar
*.war
*.ear
*.zip
*.tar.gz
*.rar
*.class
*.log
*.tmp
*.swp
Thumbs.db
.DS_Store


.classpath
.project
.metadata
.settings
.springBeans
.gradle
bin
build
target
.idea/
*.iml
*.ipr
*.iws
*.sublime-*
````


**参考**
````shell
## customize

#### RESERVED #####
# ## temp file
*.sw?
.#*
*#
*~
#.exe
.prefs
Thumbs.db
.DS_Store
.factorypath
dump.rdb
transaction-logs

#local.properties
#.*.md.html
#manifest.yml
#MANIFEST.MF
#settings.xml


# ### Java.gitignore ###
*.class
*.log
*.ctxt
.mtj.tmp/
*.jar
*.war
*.ear
*.zip
*.tar.gz
*.rar
hs_err_pid*


# ### Gradle.gitignore ###
.gradle
/build/
gradle-app.setting
.gradletasknamecache
#!gradle-wrapper.jar
# gradle/wrapper/gradle-wrapper.properties

# ### Maven.gitignore ###
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
#!/.mvn/wrapper/maven-wrapper.jar


# ### Eclipse.gitignore ###
.metadata
bin/
tmp/
*.tmp
*.bak
*.swp
*~.nib
local.properties
.settings/
.loadpath
.recommenders

# Eclipse
.project
.externalToolBuilders/
*.launch

*.pydevproject
.cproject
.classpath
.factorypath
.buildpath
.target
.tern-project
.texlipse
.springBeans
.recommenders/
.cache-main
.scala_dependencies
.worksheet

# ### Intellij IDEA ###
.idea/workspace.xml
.idea/tasks.xml
.idea/datasources.xml
.idea/dataSources.ids
*.iml
*.ipr
*.iws

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans

### NetBeans ###
nbproject/private/
build/
nbbuild/
dist/
nbdist/
.nb-gradle/

````
