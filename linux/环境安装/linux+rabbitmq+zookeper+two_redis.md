## 一. vmWare 中安装centos7
1.网络连接桥接模式（centos7取消了ifconfig改为IP addr）  
**2.启动网络服务**  
 vi /etc/sysconfig/network-scripts/ifcfg-ens33  
   将ONBOOT后面的状态改为yes，保存退出。  
   重启网络服务service network restart  
   （配置静态ip）  
  TYPE="Ethernet"   # 网络类型为以太网  
  BOOTPROTO="static"  # 手动分配ip  
  NAME="ens33"  # 网卡设备名，设备名一定要跟文件名一致  
  DEVICE="ens33"  # 网卡设备名，设备名一定要跟文件名一致  
  ONBOOT="yes"  # 该网卡是否随网络服务启动  
  IPADDR="192.168.220.101"  #该网卡ip地址就是你要配置的固定IP，如果你要用xshell等工  具连接，220这个网段最好和你自己的电脑网段一致，否则有可能用xshell连接失败  
  GATEWAY="192.168.220.2"   # 网关  
  NETMASK="255.255.255.0"   # 子网掩码  
  DNS1="8.8.8.8"    # DNS，8.8.8.8为Google提供的免费DNS服务器的IP地址  

**3.关闭防火墙**    
   systemctl stop firewalld # 临时关闭防火墙  
   systemctl disable firewalld # 禁止开机启动  

## 二.安装redis并配置多个实例
**源文件安装**  
**1.添加epel仓库，更新yum源**  
    yum install epel-release  
    yum update  

**2.安装redis数据库**  
    yum -y install redis  

**3.启动redis服务**  
    systemctl start redis   

**4.为了可以使Redis能被远程连接，需要修改配置文件**  
    路径为/etc/redis.conf  
    需要修改的地方：  
    首先，注释这一行：#bind 127.0.0.1  
    默认为保护模式，把 protected-mode yes 改为 protected-mode no  
    默认为不守护进程模式，把daemonize no 改为daemonize yes  
    requirepass foobared前的“#”去掉，密码改为你想要设置的密码  

**补充：常用命令**  

systemctl start redis.service #启动redis服务器  
systemctl stop redis.service #停止redis服务器  
systemctl restart redis.service #重新启动redis服务器  
systemctl status redis.service #获取redis服务器的运行状态  
systemctl enable redis.service #开机启动redis服务器  
systemctl disable redis.service #开机禁用redis服务器  

**5.编辑redis开机启动redis脚本**
```
#!/bin/sh
# chkconfig: 2345 10 90  
# description: Start and Stop redis   
#端口号
REDISPORT=6379
EXEC=/usr/local/redis/redis-server
CLIEXEC=/usr/local/redis/redis-cli

PIDFILE=/var/run/redis_${REDISPORT}.pid
#conf路径
CONF="/usr/local/redis/redis.conf"
#密码
AUTH="123456"

case "$1" in
    start)
        if [ -f $PIDFILE ]
        then
                echo "$PIDFILE exists, process is already running or crashed"
        else
                echo "Starting Redis server..."
                $EXEC $CONF &
        fi
        ;;
    stop)
        if [ ! -f $PIDFILE ]
        then
                echo "$PIDFILE does not exist, process is not running"
        else
                PID=$(cat $PIDFILE)
                echo "Stopping ..."
                $CLIEXEC -p $REDISPORT shutdown
                while [ -x /proc/${PID} ]
                do
                    echo "Waiting for Redis to shutdown ..."
                    sleep 1
                done
                echo "Redis stopped"
        fi
        ;;
    restart)
        "$0" stop
        sleep 3
        "$0" start
        ;;
    *)
        echo "Please use start or stop or restart as first argument"
        ;;
esac
```

**6.配置安装多个redis实例**  

给新redis实例创建redis配置（复制redis.conf）  
    
    #修改pidfile
    #pidfile /var/run/redis/redis.pid
    pidfile /var/run/redis/redis-xxx.pid

    #dir /var/lib/redis/
    dir /var/lib/redis-xxx/
    ...
    #修改端口 port
    #port 6379
    port 6380
    ...
    #修改日志文件路径 logfile
    #logfile /var/log/redis/redis.log
    logfile /var/log/redis/redis-xxx.log

    #配置6380的数据库文件位置
    dbfilename dump_6380.rdb

    创建目录 /var/lib/redis-xxx
    mkdir -p /var/lib/redis-xx

    开启端口为6380的redis服务端进程
    redis-server /etc/redis-6380.conf

    1.port    使用的端口号
    2.daemonize   守护进程
    3.pidfile   进程开启时存储临时数据的文件
    4.logfile  进程开启时的日志文件
    5.databases   数据分区  即：分区为多少个redis数据库
    6.save  900  1
      save   600  10
      save   60  10000

    Redis的三大缓存机制 :15分钟内有1次修改则缓存一次，10分钟内有10次修改则缓存一次，1分钟内有10000次修改则缓存一次。
    7.dbfilename   存储持久化数据的文件（数据库文件）
    8.dir  /var/lib/redis   redis的目录文件

##三. 安装rabbitmq

**1.# 准备**   
```
yum update

yum install epel-release

yum install gcc gcc-c++ glibc-devel make ncurses-devel openssl-devel autoconf java-1.8.0-openjdk-devel git wget wxBase.x86_64
```

    #安装erlang
    yum install -y erlang

    # 安装rabbitmq
    wget https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.15/rabbitmq-server-3.7.15-1.el7.noarch.rpm

    # 导入秘钥
    rpm --import https://www.rabbitmq.com/rabbitmq-release-signing-key.asc
    # 安装
    yum install rabbitmq-server-3.7.15-1.el7.noarch.rpm

**2.通过yum等软件仓库都可以直接安装RabbitMQ，但版本一般都较为保守。**  

    RabbitMQ官网提供了新版的rpm包（http://www.rabbitmq.com/download.html），但是安装的时候会提示需要erlang版本>=20.3，然而默认yum仓库中的版本较低。
    其实RabbitMQ在github上有提供新的erlang包（https://github.com/rabbitmq/erlang-rpm）
    也可以直接加到yum源中

    #vim /etc/yum.repos.d/rabbitmq-erlang.repo
    [rabbitmq-erlang]
    name=rabbitmq-erlang
    baseurl=https://dl.bintray.com/rabbitmq/rpm/erlang/20/el/7
    gpgcheck=1
    gpgkey=https://dl.bintray.com/rabbitmq/Keys/rabbitmq-release-signing-key.asc
    repo_gpgcheck=0
    enabled=1

    #yum clean all         //清除源缓存
    #yum makecache    //就是把服务器的包信息下载到本地电脑缓存起来，makecache建立一个缓存，以后用install时就在缓存中搜索，提高了速度。
    #rpm -qa                //查看安装了哪些软件
    #rpm -e PACKAGE_FILE   //卸载软件

**3.    # 启用web管理界面**  

    rabbitmq-plugins enable rabbitmq_management

    启动,如果不成功,可以把 -detached去掉看看什么原因,这个是后台启动
    ./rabbitmq-server -detached

    默认用户名密码都是guest,具有所有权限,但是在3.3.0版本之后只能在localhost登陆,所以要新添加一个用户在windons登陆即可

    注意添加用户需要启动之后添加
    ./rabbitmqctl add_user admin admin
    设置权限
    ./rabbitmqctl set_user_tags admin administrator

    ./rabbitmqctl  set_permissions -p / admin '.*' '.*' '.*'
    浏览器就能访问了,默认端口是15672,所以还需要开放下端口:


    firewall-cmd --zone=public --add-port=15672/tcp --permanent
    firewall-cmd --reload
    访问,guest是无法登陆的,需要创建新用户
    ip:15672

##四、安装zookeeper  

**1.zookeeper依赖jdk环境，所以先安装jdk，这里采用yum方式安装**  

    #检查是否安装JDK
    rpm -qa|grep -E '^open[jre|jdk]|j[re|dk]'
    #卸载已安装JDK
    rpm -qa|grep java|xargs rpm -e --nodeps

    #yum安装jdk,查看jdk列表
    yum list *jdk*
    #安装jdk1.8
    yum install -y java-1.8.0-openjdk*

**2.配置环境变量**  

    #JDK默认安装路径/usr/lib/jvm

    #在/etc/profile文件添加如下命令
    JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.181-3.b13.el7_5.x86_64
    PATH=$PATH:$JAVA_HOME/bin  
    CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar  
    export JAVA_HOME  CLASSPATH  PATH 

    #保存关闭profile文件，执行如下命令生效
    source  /etc/profile

**3.下载zookeeper**  

    wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.14/zookeeper-3.4.14.tar.gz

    tar -xvf zookeeper-3.4.14.tar.gz
    mv ./zookeeper-3.4.14 /usr/local/zookeeper
    mkdir /usr/local/zookeeper/var
    mkdir /usr/local/zookeeper/var/log
    echo 1 > /usr/local/zookeeper/var/log/myid
    cp /usr/local/zookeeper/conf/zoo_sample.cfg /usr/local/zookeeper/conf/zoo.cfg

**4.修改配置**  

    vim /usr/local/zookeeper/zookeeper-3.4.14/conf/zoo.cfg

    tickTime=2000
    initLimit=5
    syncLimit=2
    #数据存储和日志目录
    dataDir=/usr/local/zookeeper/var/log
    dataLogDir=/usr/local/zookeeper/var/log
    clientPort=2181
    # 多台server在下面配置即可,如果单台服务器构建多个server,则每个server用过的端口不能重复使用
    # 格式: server.[n]=[server_ip]:[server与leader交互端口]:[server选举leader端口]
    # server.1=192.168.0.123:2182:2183
    # server.2=192.168.0.123:2182:2183
    # server.3=192.168.0.123:2182:2183
    maxClientCnxns=60
    minSessionTimeout=60
    maxSessionTimeout=120
    # purgeInterval含义: 0-禁用自动清除 1-使用自动清除
    autopurge.purgeInterval=1

**5.配置zookeeper环境变量**  

    #vim /etc/profile在尾部加入或修改以下
    JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.222.b10-1.el7_7.x86_64
    PATH=$PATH:$JAVA_HOME/bin
    CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
    export JAVA_HOME  CLASSPATH  PATH
    KEEPER_HOME=/usr/local/zookeeper/zookeeper-3.4.14
    PATH=$PATH:$ZOOKEEPER_HOME/bin
    export PATH ZOOKEEPER_HOME PATH

    #生效
    source /etc/profile

**6.防火墙开启**  

    systemctl enable firewalld
    systemctl start firewalld
    firewall-cmd --zone=public --permanent --add-port=2181/tcp
    firewall-cmd --reload

**7.开机启动**  

    vim /usr/lib/systemd/system/zookeeper.service 
    [Unit]
    Description=zookeeper
    After=network.target remote-fs.target nss-lookup.target
    [Service]
    Type=forking
    ExecStart=/usr/local/zookeeper/zookeeper-3.4.14/bin/zkServer.sh start
    ExecReload=/usr/local/zookeeper/zookeeper-3.4.14/bin/zkServer.sh restart
    ExecStop=/usr/local/zookeeper/zookeeper-3.4.14/bin/zkServer.sh stop
    [Install]
    WantedBy=multi-user.target

**生效**  

    systemctl daemon-reload
    #systemctl开机启动zookeeper
    systemctl enable /usr/lib/systemd/system/zookeeper
    #systemctl取消开机启动
    systemctl disable zookeeper.service

**8.测试**   

    #启动
    /usr/local/zookeeper/zookeeper-3.4.14/bin/zkServer.sh start
    #连接
    /usr/local/zookeeper/zookeeper-3.4.14/bin/zkCli.sh