##选择三台机器  
10.18.4.53，10.18.4.78，10.18.4.93  

##CASSANDRA_SEEDS
10.18.4.53，10.18.4.78


##copy配置文件
docker run -i --rm cassandra:3.0 cat /opt/cassandra/conf/cassandra.yaml > cassandra.yaml



##查看集群情况
###任一机器
docker exec -it tb-cassandra bash

nodetool -h 10.18.4.53 -u cassandra -pw cassandra status


