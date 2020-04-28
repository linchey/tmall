# FastDFS的安装(linux)



## 1、 安装依赖软件和类库（安装前准备）

```
yum install gcc-c++ -y

yum -y install zlib zlib-devel pcre pcre-devel gcc gcc-c++ openssl openssl-devel libevent libevent-devel perl unzip net-tools wget

yum -y install libevent

yum install perl*

```

压缩包：

fast依赖库:     		     libfastcommonV1.0.7.tar.gz

fastdfs包：                         FastDFS_v5.05.tar.gz

Fdfs整合nginx的插件：fastdfs-nginx-module_v1.16.tar.gz

nginx软件包:                       nginx-1.12.2.tar.gz

## 2、安装Libfastcommon

1. 解压Libfastcommon压缩包

```
[root@localhost opt]# tar -zxvf libfastcommonV1.0.7.tar.gz
```

2. 编译

```
[root@localhost opt]#./make.sh
```

3. 计入目录安装

```
[root@localhost libfastcommon-1.0.7]# ./make.sh install
```

4. 将类库拷贝到/usr/lib下

```
[root@localhost libfastcommon-1.0.7]# cp /usr/lib64/libfastcommon.so /usr/lib/
```

# 3、安装fastdfs

### 3.1、配置tracker

1. 新建目录

```
[root@localhost /]# mkdir /opt/fastdfs

```

2.  解压

```
[root@localhost opt]# tar -zxvf FastDFS_v5.05.tar.gz
```

3. 编译、安装

```
[root@localhost opt]# cd FastDFS
[root@localhost FastDFS]# ./make.sh
[root@localhost FastDFS]# ./make.sh install
```

4.进入conf配置目录将文件都拷贝到/etc/fdfs下

```
[root@localhost conf]# cp  *  /etc/fdfs/

```

5. 配置tracker.conf

```
[root@localhost /]# cd /etc/fdfs
[root@localhost fdfs]# vim /etc/fdfs/tracker.conf
设置软件数据和日志目录 :base_path=/opt/fastdfs

```

### 3.2、配置storage

 storage不需要安装，因为安装tracker时已经同时安装

1. 创建Storage存储文件的目录

   ```
   [root@localhost /]#mkdir /opt/fastdfs/fdfs_storaged
   ```

2. 设置软件数据和日志目录,存储文件的目录,Storage的trackerip

```
[root@localhost fdfs]# vim /etc/fdfs/storage.conf
base_path=/opt/fastdfs
store_path0=/opt/fastdfs/fdfs_storaged
tracker_server=192.168.159.130:22122(本机ip)

```

### 3.3、配置tracker和storage的启动服务

1. 因为启动脚本还在安装目录下，所以我们新建/usr/local/fdfs目录，并且将启动脚本cp到该目录）

```
[root@localhost /]#mkdir /usr/local/fdfs
```

2. 进入安装目录,将启动脚本cp到该目录

```
[root@localhost opt]# cd /opt/FastDFS
root@localhost FastDFS]# cp restart.sh  /usr/local/fdfs/
[root@localhost FastDFS]# cp stop.sh  /usr/local/fdfs/
```

3. 配置tracker启动服务

```
[root@localhost FastDFS]# vim fdfs_trackerd
PRG=/usr/bin/fdfs_trackerd
if [ ! -f /usr/local/fdfs/stop.sh ]; then
if [ ! -f /usr/local/fdfs/restart.sh ]; then
/usr/local/fdfs/stop.sh $CMD
/usr/local/fdfs/restart.sh $CMD &

```



4. 配置storaged启动服务

配置storage启动服务(restart和stop脚本已经拷贝到/usr/local/fdfs下，所以storage只需要配置/etc/init.d/fdfs_storage脚本就可以了)

```
[root@localhost init.d]# vim fdfs_storaged
PRG=/usr/bin/fdfs_storaged
if [ ! -f /usr/local/fdfs/stop.sh ]; then
if [ ! -f /usr/local/fdfs/restart.sh ]; then
/usr/local/fdfs/stop.sh $CMD
/usr/local/fdfs/restart.sh $CMD &
```

5. 将启动脚本加入linux服务

```
[root@localhost init.d]# chkconfig --add fdfs_trackerd
[root@localhost init.d]# chkconfig --add fdfs_storaged
```

6. 测试上传

   修改/etc/fdfs/client.conf

```
[root@localhost ~]# vim /etc/fdfs/client.conf

base_path=/opt/fastdfs

tracker_server=192.168.159.130
```

​	上传图片

```
[root@localhost ~]# /usr/bin/fdfs_test  /etc/fdfs/client.conf  upload  /root/0c2f7068894ecdc8.jpg

```

对应的上传路径：

/opt/fastdfs/fdfs_storaged/data /00/00

## 4、FastDFS-nginx-module插件

1. 解压

   ```
   [root@localhost opt]# tar -zxvf fastdfs-nginx-module_v1.16.tar.gz
   ```

2. 修改插件读取fdfs的目录

```
[root@localhost src]# vim config
将/usr/local/路径改为/usr/
有三处修改
```

3. 将FastDFS-nginx-module/src下的mod_fastdfs.conf拷贝至/etc/fdfs/下

   ```
   [root@localhost src]# cp mod_fastdfs.conf /etc/fdfs/
   ```

4. 修改mod_fastdfs.conf的内容：

```
[root@localhost src]# vim /etc/fdfs/mod_fastdfs.conf
base_path=/opt/fastdfs
tracker_server=192.168.159.130:22122
url_have_group_name = true
store_path0=/opt/fastdfs/fdfs_storaged
```

5. 将libfdfsclient.so拷贝至/usr/lib下

```
[root@localhost src]# cp /usr/lib64/libfdfsclient.so /usr/lib/
```

## 5、nginx

做web服务器，提供http请求服务

(依赖于：pcre-devel、zlib-devel)

1. #### 创建nginx/client目录

```
[root@localhost src]# mkdir -p /var/temp/nginx/client
```

2. 解压

   ```
   [root@localhost opt]# tar -zxvf nginx-1.12.2.tar.gz
   ```

3. 添加fastdfs-nginx-module模块

```
./configure \
--prefix=/usr/local/nginx \
--pid-path=/var/run/nginx/nginx.pid \
--lock-path=/var/lock/nginx.lock \
--error-log-path=/var/log/nginx/error.log \
--http-log-path=/var/log/nginx/access.log \
--with-http_gzip_static_module \
--http-client-body-temp-path=/var/temp/nginx/client \
--http-proxy-temp-path=/var/temp/nginx/proxy \
--http-fastcgi-temp-path=/var/temp/nginx/fastcgi \
--http-uwsgi-temp-path=/var/temp/nginx/uwsgi \
--http-scgi-temp-path=/var/temp/nginx/scgi \
--add-module=/opt/fastdfs-nginx-module/src


```

4. 编译、安装

```
[root@localhost nginx-1.12.2]# make
[root@localhost nginx-1.12.2]# make install
```

5. 编辑nginx.conf

```
[root@localhost nginx-1.12.2]# vim /usr/local/nginx/conf/nginx.conf
server {
        listen       80;
       # server_name  localhost;
        server_name 192.168.159.130;
        #charset koi8-r;

        #access_log  logs/host.access.log  main;
 location /group1/M00/ {
            #root   html;
            #index  index.html index.htm;
            ngx_fastdfs_module;
        }


```

5. 启动

```
[root@localhost conf]# /usr/local/nginx/sbin/nginx

```

6. 设置开机启动

## 4. fdsf 整合springboot

1.  通过git下载fdfs的客户端

```
git clone https://github.com/happyfish100/fastdfs-client-java
```

2. 将fdfs的客户端打包到本地maven仓库中

3. 添加依赖

   ```
    <dependency>
               <groupId>org.csource</groupId>
               <artifactId>fastdfs-client-java</artifactId>
               <version>1.27-SNAPSHOT</version>
     </dependency>
   ```







重启后出现

nginx: [emerg] open() "/var/run/nginx/nginx.pid" failed (2: No such file or directory)

解决方法

```
[root@localhost /]# cd /var/run
[root@localhost run]# mkdir nginx
[root@localhost run]# cd nginx
[root@localhost nginx]# touch nginx.pid
[root@localhost nginx]# /usr/local/nginx/sbin/nginx

```

