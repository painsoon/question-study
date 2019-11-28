# rpm    

### RPM包命名原则   
　　httpd-2.2.15-15.el6.centos.l.i686.rpm    
　　　　httpd 软件包名    
　　　　2.2.15 软件版本    
　　　　15 软件发布的次数    
　　　　el6.centos 适合的Linux平台   
　　　　i686 适合的硬件平台   
　　　　rpm rpm包扩展名   
　　　　如果名字里有noarch,则表示所有平台都可以。   

### rpm安装：    
　　rpm-ivh 包全名    
　　　　-i(install) 安装    
　　　　-v(verbose) 显示详细信息    
　　　　-h(hash) 显示进度     
　　　　--nodeps 不检测依赖性 一般都必须要检测   

### rpm 包升级    
rpm -Uvh 包全名（U升级）   


###  rpm -e 包名   
　　-e(erase) 卸载   
　　--nodeps 不检查依赖性    


### 查询软件包是否安装
rpm -q 包名 :查询包是否安装   
　　　　-q(query) 查询   
　　　　-a(all) 所有   
　　　　-i(information) 查询软件信息   
　　　　-p(package) 查询未安装包信息    

　　rpm -ql 包名：查询包中文件安装位置(list) 注：包的安装路径在包生成的时候就确定了   

　　rpm -qlp 包全名：查询未安装包安装时会安装在哪里。    
　　rpm -qf 系统文件名 ：查询系统文件属于哪个rpm包 注：系统文件名必须是通过安装哪个包生成的文件    
　　　　-f:查询系统文件属于哪个包   
　　rpm -qR 包名 查询已安装软件包的依赖性   
　　　　-r: 查询软件包的依赖性(requires)   
　　rpm -qRp:查询未安装包的依赖性   
　　　　-p: 查询未安装包的依赖性    

　　　　例如：   
　　　　　　rpm -qa | grep httpd 查询所有Apache的包    



