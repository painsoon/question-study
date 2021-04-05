# 安装 docker-compose  
## 获取脚本  
$ curl -L https://github.com/docker/compose/releases/  download/1.25.0-rc2/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/  docker-compose
## 赋予执行权限  
$ chmod +x /usr/local/bin/docker-compose  

# 下载  
zookeerper docker-compose.yml    
$ wget https://raw.githubusercontent.com/JacianLiu/docker-compose/master/zookeeper/docker-compose.yml   

# 新建 kafka 网络  
docker network create kafka-net  

# 创建目录  
/opt/docker_service/zookeeper/  

# 创建文件   
docker-compose.yml    

```
version: '3'
services:
 zoo1:
  image: zookeeper:3.4 # 镜像名称
  restart: always # 当发生错误时自动重启
  hostname: zoo1
  container_name: zoo1
  privileged: true
  ports: # 端口
   - 2181:2181
  volumes: # 挂载数据卷
   - ./zoo1/data:/data
   - ./zoo1/datalog:/datalog 
  environment:
   TZ: Asia/Shanghai
   ZOO_MY_ID: 1 # 节点ID
   ZOO_PORT: 2181 # zookeeper端口号
   ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888 # zookeeper节点列表
#  networks:
#   default:
#    ipv4_address: 172.23.0.11
 
 zoo2:
  image: zookeeper:3.4
  restart: always
  hostname: zoo2
  container_name: zoo2
  privileged: true
  ports:
   - 2182:2181
  volumes:
   - ./zoo2/data:/data
   - ./zoo2/datalog:/datalog
  environment:
   TZ: Asia/Shanghai
   ZOO_MY_ID: 2
   ZOO_PORT: 2181
   ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888
#  networks:
#   default:
#    ipv4_address: 172.23.0.12
 
 zoo3:
  image: zookeeper:3.4
  restart: always
  hostname: zoo3
  container_name: zoo3
  privileged: true
  ports:
   - 2183:2181
  volumes:
   - ./zoo3/data:/data
   - ./zoo3/datalog:/datalog
  environment:
   TZ: Asia/Shanghai
   ZOO_MY_ID: 3
   ZOO_PORT: 2181
   ZOO_SERVERS: server.1=zoo1:2888:3888 server.2=zoo2:2888:3888 server.3=zoo3:2888:3888
#  networks:
#   default:
#    ipv4_address: 172.23.0.13
 
networks:
 default:
  external:
   name: kafka-net

```


# 启动  
docker-compose up -d  

|命令|解释|
|-|-|
|docker-compose up|启动所有容器|
|docker-compose up -d|后台启动并运行所有容器|
|docker-compose up --no-recreate -d|不重新创建已经停止的容器|
|docker-compose up -d test2|只启动test2这个容器|
|docker-compose stop|停止容器|
|docker-compose start|启动容器|
|docker-compose down|停止并销毁容器|   


