## 展示git

**开始**
* 创建 `README` 文件
* `git add README` 并 `git commit -m "add README"`
* `git log`
````
root@DESKTOP# git commit -m "add README"
[master 07aee8d] add README
 1 file changed, 3 insertions(+), 2 deletions(-)
root@DESKTOP# git log
commit 07aee8dd617c25a06c60df20bd11782f12c9c91e
````

**改名**
* `git mv README testfile`
* `git add *`
* `git commit -m "update README.md & rename README to testfile"`
````
[master b105ded]
commit b105ded0eca5dba1b1a583451aa6b62bc97c50d8
````

**editor**
`git config core.editor vi`

**fetch**
git fetch

**pull**
`git pull --rebase origin master`
