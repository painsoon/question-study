本地Git仓库和GitHub仓库之间的传输是通过SSH加密的  
### 创建SSH Key
ssh-keygen -t rsa -C "youremail@example.com"  

### 关联远程仓库    
git remote add origin git@server-name:path/repo-name.git    

### 关联后第一次推送    
git push -u origin master    


### 创建分支
git branch (branchname)

### 切换分支
git checkout (branchname)

### 创建新分支并立即切换到该分支下
git checkout -b newtest

### 合并分支
git merge

### 列出本地分支
git branch

### 列出远程分支
git branch -a

### 删除分支
git branch -d (branchname)

### 删除远程分支
git branch -d origin 分支

### 删除后依然能看到远程分支，彻底看不到
git remote prune origin

###  回退到某个版本
git reset --hard ahdhs1(commit_id)