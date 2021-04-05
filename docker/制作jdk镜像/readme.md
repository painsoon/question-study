**有时候我们可能需要在jar环境/ext中添加第三方jar包为自己的项目服务，这时候可以定制个基础镜像**   

**假如需要修改配置文件，需要先找到文件位置然后用sed命令**   

## sed命令   
https://www.cnblogs.com/ginvip/p/6376049.html

**比如在/a/b.txt下aa开头的地方下面增加一行bb**
sed -i '/aa/a bb' /a/b.txt

## 写dockerfile    
FROM openjdk:8u181-jdk-stretch   

ADD ./aaa.jar /docker-java-home/jre/lib/ext   
ADD ./bbb.jar /docker-java-home/jre/lib/ext    

RUN sed -i '/aa/a bb' /a/b.txt     

## 创建镜像    
docker build -t myopenjdk:8 .    
