## 拉取版本镜像   
docker pull mysql:5.8   

## 运行   
docker run --name mysql \  
 -p 3306:3306 \  
 --privileged=true \  
 -e MYSQL_ROOT_PASSWORD=123456 \  
 -e MYSQL_DATABASE=test \   
 -e TZ='Asia/Shanghai' \   
 -v /usr/local/docker/mysql/conf.d:/etc/mysql/conf.d \  
 -v /usr/local/docker/mysql/data:/var/lib/mysql \   
 --restart=always \  
 -d mysql:5.8 \   
 --lower_case_table_names=1   


```
#设置root权限
--privileged=true    

#设置表名不区分大小写; linux下默认是区分的，windows下默认不区分
 --lower_case_table_names=1

#挂载数据目录
-v /usr/local/docker/mysql/data:/var/lib/mysql

```
   

## mount  
```
-mount：绑定挂载，MySQL官方Docker搭建MySQL文档中，建议我们大家使用--mount

--mount type=bind,src=/home/docker/mysql/conf/my.cnf,dst=/etc/mysql/my.cnf --mount type=bind,src=/home/docker/mysql/data,dst=/var/lib/mysql
--mount type=bind,src=/home/docker/mysql/conf.d,dst=/etc/mysql/conf.d

```


## 修改权限   
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';   

flush privileges;   

## 创建个临时账号，并给予较大的权限
CREATE USER 'test'@'%' IDENTIFIED BY '123456';
GRANT ALL privileges ON *.* TO 'test'@'%' identified by 'password' with grant option;
grant all privileges on `[数据库名]`.* to '[用户名]'@'localhost'  identified by '[密码]';

flush privileges;


## 更改加密规则   
因为Navicat只支持旧版本的加密,8.0以上需要更改mysql的加密规则。   

ALTER USER 'root'@'localhost' IDENTIFIED BY 'password' PASSWORD EXPIRE NEVER;
flush privileges;

## 删除用户  
 drop user 'test'@'%'

## 导入sql   
mysql -u用户名 -p密码 数据库名 < 数据库名.sql   