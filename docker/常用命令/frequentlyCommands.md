
# 容器生命周期管理  

## run  创建一个新的容器并运行一个命令  
docker run [options] image [command] [arg...]  

-a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项；  
-d: 后台运行容器，并返回容器ID；  
-i: 以交互模式运行容器，通常与 -t 同时使用；  
-t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用；  
-P: 随机端口映射，容器内部端口随机映射到主机的高端口  
-p: 指定端口映射，格式为：主机(宿主)端口:容器端口  
--name="nginx-lb": 为容器指定一个名称；  
--dns 8.8.8.8: 指定容器使用的DNS服务器，默认和宿主一致；  
-e username="ritchie": 设置环境变量；   
--expose=[]: 开放一个端口或一组端口；  
--volume , -v: 绑定一个卷。 主机目录：容器目录  

## start/stop/restart  
docker start/stop/restart containerName  

## kill  杀掉一个容器  
docker kill [options] container   

-s :向容器发送一个信号   
 
**例：docker kill -s KILL mynginx**   

## rm   删除一个或多个容器   
docker rm [OPTIONS] CONTAINER [CONTAINER...]  

-f :通过SIGKILL信号强制删除一个运行中的容器  
-l :移除容器间的网络连接，而非容器本身  
-v :-v 删除与容器关联的卷   

**例：移除容器nginx01对容器db01的连接，连接名db**   
docker rm -l db  

**例：删除容器nginx01,并删除容器挂载的数据卷**   
docker rm -v nginx01  

## exec  在运行的容器中执行命令  
docker exec [OPTIONS] CONTAINER COMMAND [ARG...]  

-d :分离模式: 在后台运行  
-i :即使没有附加也保持STDIN 打开  
-t :分配一个伪终端  
   
**例：docker exec -it nginx bash**   
 


# 容器操作  

## ps  查看容器  
docker ps [OPTIONS]   

-a:显示所有容器  
-l:最近创建的  

## 查看完整的 containerId   
docker ps --no-trunc    


## inspect   



## top    



## attach  



## logs   



## export  
 

## port    




# 容器rootfs命令  

## cp 用于容器与主机之间的数据拷贝
docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|-   
docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH   

**将主机/www/runoob目录拷贝到容器96f7f14e99ab的/www目录下**  
docker cp /www/runoob 96f7f14e99ab:/www/   

**将容器96f7f14e99ab的/www目录拷贝到主机的/tmp目录中。**   
docker cp 96f7f14e99ab:/www /tmp  




# 镜像仓库  


## docker search : 从Docker Hub查找镜像  

## pull  从镜像仓库中拉取或者更新指定镜像  
docker pull [OPTIONS] NAME[:TAG|@DIGEST]  

## docker push myapache:v1    将本地的镜像上传到镜像仓库,要先登陆到镜像仓库  


# 本地镜像管理  

## docker images : 列出本地镜像   




