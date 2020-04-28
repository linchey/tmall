#Zookeeper
进入Apache ZooKeeper官方网站进行下载，https://zookeeper.apache.org/releases.html
 版本 zookeeper-3.4.11.tar.gz
 拷贝zookeeper-3.4.11.tar.gz到/opt下，并解压缩
 改名叫zookeeper
 ##制作开机启动的脚本
 vim/etc/init.d/zookeeper
 脚本：
 ```
 #!/bin/bash
 #chkconfig:2345 20 90
 #description:zookeeper
 #processname:zookeeper
 ZK_PATH=/opt/zookeeper
 export JAVA_HOME=/opt/jdk1.8.0_152
 case $1 in
          start) sh  $ZK_PATH/bin/zkServer.sh start;;
          stop)  sh  $ZK_PATH/bin/zkServer.sh stop;;
          status) sh  $ZK_PATH/bin/zkServer.sh status;;
          restart) sh $ZK_PATH/bin/zkServer.sh restart;;
          *)  echo "require start|stop|status|restart"  ;;
 esac
```
然后把脚本注册为Service
```chkonconfig --add zookeeper```
增加权限
```chmod +x /etc/init.d/zookeeper```
##初始化zookeeper配置文件
拷贝/opt/zookeeper/conf/zoo_sample.cfg   
到同一个目录下改个名字叫zoo.cfg
```cp /opt/zookeeper/conf/zoo_sample.cfg zoo.cfg  ```
启动zookeeper
```service zookeeper start```

#dubbo
tomcat8 +  dubbo-admin
拷贝tomcat8和dubbo-admin到/opt目录下
dubbo-admin-2.6.0.war拷贝到tomcat的webapps目录下
##设置开机启动tomcat
```vim /etc/init.d/dubbo-admin```
复制如下脚本
```
#!/bin/bash
#chkconfig:2345 20 90
#description:dubbo-admin
#processname:dubbo-admin
CATALANA_HOME=/opt/tomcat4dubbo
export JAVA_HOME=/opt/jdk1.8.0_152
case $1 in
start)  
    echo "Starting Tomcat..."  
    $CATALANA_HOME/bin/startup.sh  
    ;;  
  
stop)  
    echo "Stopping Tomcat..."  
    $CATALANA_HOME/bin/shutdown.sh  
    ;;  
  
restart)  
    echo "Stopping Tomcat..."  
    $CATALANA_HOME/bin/shutdown.sh  
    sleep 2  
    echo  
    echo "Starting Tomcat..."  
    $CATALANA_HOME/bin/startup.sh  
    ;;  
*)  
    echo "Usage: tomcat {start|stop|restart}"  
    ;; esac

```
 同样的注册进入到服务中
 ```chkconfig --add dubbo-admin```
 加入权限
 ```chmod +x dubbo-admin```
 ## 启动服务
 ```service dubbo-admin start```
 启动后用浏览器访问http://192.168.159.130:8080/dubbo/(安装软件所在的主机的IP，虚拟机最好设置成静态IP，如何设置自己百度)
 可以看到要提示用户名密码，默认是root/root
 dubbo的整合所需依赖
 ```    <properties>
            <dubbo-starter.version>1.0.10</dubbo-starter.version>
            <dubbo.version>2.6.0</dubbo.version>
            <zkclient.version>0.10</zkclient.version>

        </properties>
        <dependency>
                    <groupId>com.alibaba</groupId>
                    <artifactId>dubbo</artifactId>
                    <version>${dubbo.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>com.101tec</groupId>
                    <artifactId>zkclient</artifactId>
                    <version>${zkclient.version}</version>
                </dependency>
    
                <dependency>
                    <groupId>com.gitee.reger</groupId>
                    <artifactId>spring-boot-starter-dubbo</artifactId>
                    <version>${dubbo-starter.version}</version>
        </dependency>
```