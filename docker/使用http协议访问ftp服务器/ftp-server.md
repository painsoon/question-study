## 寻找vsftpd的镜像
docker search vsftpd

## 把镜像pull到本地
docker pull fauria/vsftpd

## 创建vsftpd的container
```
docker run -d \
-v /home/ftp:/home/vsftpd \
-p 20:20 \
-p 21:21 \
-p 21100-21110:21100-21110 \
-e FTP_USER=test \
-e FTP_PASS=test \
-e PASV_ADDRESS=192.168.10.86 \
-e PASV_MIN_PORT=21100 \
-e PASV_MAX_PORT=21110 \
--name vsftpd \
--restart=always \
fauria/vsftpd
```   

## 参数解析
-v  :映射 docker 容器 ftp 文件根目录（冒号前面是宿主机的目录）
-p  :映射 docker 端口（冒号前面是宿主机的端口）
-e PASV_ADDRESS :宿主机 ip，当需要使用被动模式时必须设置。


## 开启防火墙
firewall-cmd --permanent --add-port=20/tcp  
firewall-cmd --permanent --add-port=21/tcp   
firewall-cmd --permanent --add-port=21100/tcp   
firewall-cmd --permanent --add-port=21101/tcp    
firewall-cmd --permanent --add-port=21102/tcp   
firewall-cmd --permanent --add-port=21103/tcp   
firewall-cmd --permanent --add-port=21104/tcp   
firewall-cmd --permanent --add-port=21105/tcp   
firewall-cmd --permanent --add-port=21106/tcp   
firewall-cmd --permanent --add-port=21107/tcp    
firewall-cmd --permanent --add-port=21108/tcp   
firewall-cmd --permanent --add-port=21109/tcp    
firewall-cmd --permanent --add-port=21110/tcp     
firewall-cmd --reload

##  修改、完善vsftpd的设置  

docker exec -it vsftpd bash   

**修改并生成虚拟用户模式下的用户db文件**   
vi /etc/vsftpd/virtual_users.txt   

**添加用户密码**   
user    //name  
user    //passwd   

**建立新用户文件夹**  
mkdir /home/vsftpd/user

**hash处理登录的验证信息并写入数据库**   
/usr/bin/db_load -T -t hash -f /etc/vsftpd/virtual_users.txt /etc/vsftpd/virtual_users.db   

**重启**  
docker restart vsftpd  


## 文件夹权限   

会有一个问题，即使设置了chmod -R 777 /home/ftp (-R 传递到子目录)，  
但是新建或新上传的文件夹不会自动继承父文件夹的权限，就会造成403.  

**解决**  

vim /etc/vsftpd/vsftpd.conf

找到Local_umask=000  // FTP上本地的文件权限，默认是077,设为000   

**问题**   
每次重启文件中会增加local_umask,而默认又是077，上传的文件又是没有权限。   

**思考？**   
为什么每次重启/etc/vsftpd/vsftpd.conf中会增加东西？  

查看vsftpd进程：  
ps -ef | grep vsftpd   

root      2604  0.0  0.0  11692  1420 ?        Ss   16:06   0:00 /bin/bash /usr/sbin/run-vsftpd.sh   

会发现它是以/usr/sbin/run-vsftpd.sh脚本启动的。   
修改它：   
vi /usr/sbin/run-vsftpd.sh    
发现有这样一行echo "local_umask=${LOCAL_UMASK}" >> /etc/vsftpd/vsftpd.conf  
修改：echo "local_umask=000" >> /etc/vsftpd/vsftpd.conf   




 








