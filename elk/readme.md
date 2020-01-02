# 目的  

收集容器中的日志，查询分析   

# 架构elk  
elasticsearch:全文搜索引擎，搜索查询日志  
kibana:es的操作UI，专门用于可视化elasticsearch数据   
logstash：读取日志文件，并对其过滤，然后转发给其他组件   
filebeat:同为日志抓取工具，可以直接将日志发给logstash或elasticsearch   

logstash很占内存，filebeat较为轻量级，这里使用filebeat代替logstash，当然配合使用能设计出更好的架构。每台服务器上部署filebeat收集日志发送到消息队列，Logstash单独部署在一台服务器消费队列用于解析过滤日志并发送到elasticsearh，kibana查看。比如：
filebeat --> 消息中间件（redis,kafaka,rabbitmq）--> logstash --> elasticsearch <-- kibana    

# 镜像准备    

docker pull elasticsearch:7.1.1   
docker pull kibana:7.1.1   
docker pull docker.elastic.co/beats/filebeat:7.5.1        

# 运行elasticsearch      

## 创建网络  

如果需要安装kibana等其他，需要创建一个网络，名字任意取，让他们在同一个网络，使得es和kibana通信。      
docker network create esnet

## 创建容器   

官方镜像里面ES的配置文件保存在/usr/share/elasticsearch/config，容器默认对外提供9200端口，用作API交互。 9200是供htpp访问端口，9300是供tcp访问的端口，如果不做端口映射，浏览器就不能访问elasticsearch的服务。       

```
docker run \
-p 9200:9200 -p 9300:9300 \
--network esnet \
-e "discovery.type=single-node" \
--name elasticsearch \
--restart=always \
-d elasticsearch:7.1.1

```

# 运行kibana   

Kibana作为ES操作的UI，对外提供5601端口做页面交互。需要跟ES容器通信，所以这里要将ES的容器link一下，(注意：要和elasticsearch在同一网络)。   
```
docker run -p 5601:5601 \
--network esnet \
--name kibana \
--link elasticsearch:7.1.1 \
--restart=always \
-d kibana:7.1.1

```

# 运行filebeat  

## 安装filebeat   

Filebeat 最新的安装文档 https://www.elastic.co/guide/en/beats/filebeat/current/filebeat-installation.html    
把配置文件挂载到宿主机方便修改    
**注意：要和elasticsearch，kibana在同一网络**  

```
docker run \
--name=filebeat \
--network esnet \
-v /usr/share/filebeat/filebeat.yml:/usr/share/filebeat/filebeat.yml \
--restart=always \
-d docker.elastic.co/beats/filebeat:7.5.1

```

## 配置filebeat   

两个问题：  
1.监控哪些日志文件？   
2.将日志发送到哪里？   

在当前目录创建modules.d文件夹，避免启动报错  

```
filebeat.config.modules:
  path: ${path.config}/modules.d/*.yml

filebeat.inputs:
- type: log
  enabled: true
  paths:
    #需要读取日志的目录,Docker 会将容器日志记录到 /var/lib/docker/containers/<contariner ID>/<contariner ID>-json.log
    - /var/lib/docker/containers/*/*.log
  #  - /app/logs/*.log
     
  # 因为docker使用的log driver是json-file，因此采集到的日志格式是json格式，设置为true之后，filebeat会将日志进行json_decode处理
  json.keys_under_root: true

  #如果启用此设置，则在出现JSON解组错误或配置中定义了message_key但无法使用的情况下，Filebeat将添加“error.message”和“error.type：json”键。
  json.add_error_key: true

  #一个可选的配置设置，用于指定应用行筛选和多行设置的JSON密钥。 如果指定，键必须位于JSON对象的顶层，且与键关联的值必须是字符串，否则不会发生过滤或多行聚合。
  json.message_key: log
  tail_files: true
  # 将error日志合并到一行
  multiline.pattern: '^([0-9]{4}|[0-9]{2})-[0-9]{2}' 
  multiline.negate: true
  multiline.match: after
  multiline.timeout: 10s


# 过滤掉一些不必要字段
processors:
 - drop_fields:
     fields: ["input_type", "offset", "stream", "beat"]

output.elasticsearch:
  hosts: ["elasticsearch:9200"]
#  username: '${ELASTICSEARCH_USERNAME:}'
#  password: '${ELASTICSEARCH_PASSWORD:}'


setup.kibana:
  host: "kibana:5601"
```


