# 简介

Docker Compose 是一种通过单个命令创建和启动Docker应用程序的工具。   
它提供以下命令来管理应用程序的整个生命周期：  
docker-compose up -d  
docker-compose start / docker-compose stop    
启动，停止和重建服务   
查看运行服务的状态   
流式运行服务的日志输出docker-compose logs -f name     
在服务上运行一次性命令  

# 使用  

创建一个 docker-compose.yml 配置文件：  

```
version: '3'
services:
  webapp:
    restart: always
    image: training/webapp
    container_name: webapp
    ports:
      - 5000:5000

```

**参数说明：**   
version：指定脚本语法解释器版本   
services：要启动的服务列表   
webapp：服务名称，可以随便起名，不重复即可    
restart：启动方式，这里的 always 表示总是启动，即使服务器重启了也会立即启动服务    
image：镜像的名称，默认从 Docker Hub 下载    
container_name：容器名称，可以随便起名，不重复即可   
ports：端口映射列列表，左边为宿主机端口，右边为容器端口   

# 例子

## 安装mysql   

```
version: '3'
services:
  mysql:
    restart: always
    image: mysql
    container_name: mysql
    ports:
      - 3306:3306
    environment:
      TZ: Asia/Shanghai
      MYSQL_ROOT_PASSWORD: 123456
    command:
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_general_ci
      --explicit_defaults_for_timestamp=true
      --lower_case_table_names=1
      --max_allowed_packet=128M
      --sql-mode="STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO"
    volumes:
      - mysql-data:/var/lib/mysql

volumes:
  mysql-data:

```

mysql-data不指定目录，统一配置，在docker安装目录/var/lib/docker/volumes下

