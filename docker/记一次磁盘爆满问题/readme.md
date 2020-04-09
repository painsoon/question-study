# Docker 空间使用分析与清理

用户在使用 Docker 部署业务一段时间后，可能会发现宿主节点的磁盘容量持续增长，甚至将磁盘空间耗尽进而引发宿主机异常，进而对业务造成影响。   

## 通过du逐层分析  
du -sh *  
发现是/var/lib/docker/overlay2 和containers文件夹过大   

## 清理overlay2文件夹   

## 分析 Docker 空间分布  
docker system df   

-v:查看占用细节  

## 自动空间清理  
docker system prune   

**该指令默认会清除下面所有资源：**  
已停止的容器（container）  
未被任何容器所使用的卷（volume）   
未被任何容器所关联的网络（network）   
所有悬空镜像（image）。     

## 手工清理

### 镜像清理   
**删除所有悬空镜像，但不会删除未使用镜像**  
docker rmi $(docker images -f "dangling=true" -q)   

### 卷清理   
**删除所有未被任何容器关联引用的卷：**   
docker volume rm $(docker volume ls -qf dangling=true)   

### 容器清理   
**删除所有已退出的容器**  
docker rm -v $(docker ps -aq -f status=exited)  
**删除所有状态为 dead 的容器**   
docker rm -v $(docker ps -aq -f status=dead)    

## 清理和containers文件夹  

## 通过du定位到是哪个镜像文件夹过大  

## 查看完整的 containerId   
docker ps --no-trunc    

进入镜像发现有个json.log 结尾的文件很大，这个是部署的业务服务日志。   

**这个问题是业务系统把log4j往控制台打日志的发生在容器读写层，所以容器会不断增大。可以关闭控制台打印，利用日志驱动框架把日志写到容器外部文件。**  






