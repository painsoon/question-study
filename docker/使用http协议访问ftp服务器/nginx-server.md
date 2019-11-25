## 下载镜像   
docker pull nginx  

## 创建容器  

### 1.简单启动  

docker run --name nginx -p 80:80 -d nginx  

docker exec -it nginx bash  

自定义配置：vi /etc/nginx/nginx.conf   

重启： docker restart nginx   

**这样不是很方便，还有第二种方式，挂载配置文件，  
就是把装有docker宿主机上面的nginx.conf配置文件映射到启动的nginx容器里面，   
这需要你首先准备好nginx.con配置文件**   

### 2.映射启动  

docker run --name nginx \  
-p 8088:80 \  
-v /home/docker-nginx/nginx.conf:/etc/nginx/nginx.conf \  
-v /home/docker-nginx/log:/var/log/nginx \  
-v /home/docker-nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf  \  
-d nginx  

### 参数解析  

-p 映射端口，将docker宿主机的8088端口和容器的80端口进行绑定  
-v 挂载文件用的，第一个-v 表示将你本地的nginx.conf覆盖你要起启动的容器的nginx.conf文件，  
	（宿主机：容器）
    第二个表示将日志文件进行挂载，就是把nginx服务器的日志写到你docker宿主机的/home/docker-nginx/log/下面   
-d 后台启动哪个镜像   


## nginx + ftp  

### 思考  

http访问ftp目录只需把路径location到ftp的目录即可。  
所以只需把ftp的目录和location的目录映射到同一个宿主机的目录即可。    

docker run --name nginx \  
-p 8088:80 \  
-v /home/docker-nginx/nginx.conf:/etc/nginx/nginx.conf \  
-v /home/docker-nginx/log:/var/log/nginx \  
-v /home/docker-nginx/conf.d/default.conf:/etc/nginx/conf.d/default.conf  \   
-v /home/ftp:/home/ftp \   //把/home/ftp挂载出去映射到同ftp映射出去的目录
--restart=always \  
-d nginx


## 配置nginx.conf   

**把自己的server写在conf.d文件夹中，在nginx.conf中include**  
不同的server独立出来，这样的好处是比较清爽。  

```
server {  
        listen       8088;  
        server_name  localhost;  
  
        #charset utf-8;  
  
        #access_log  logs/host.access.log  main;  
  
        #默认请求  
        location /ftp { 
            alias  /home/ftp/;    //精致匹配或指定开始字符时，不能用root，要alias 

     	 	autoindex on;
      		autoindex_exact_size off;
       		autoindex_localtime on;
           
        }
}

```   

## 重启  

docker restart nginx