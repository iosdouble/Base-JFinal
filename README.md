## 配置文件说明

src/main/resource 目录下

1、修改项目启动端口号

在 undertow.properties 文件中修改对应的配置端口配置即可

2、修改数据库配置

在 demo-config-dev.txt 配置文件中修改对应的数据库配置即可


## 视图文件说明

配置地址 webapp/wx-app目录


&emsp;&emsp;如果在开发过程中，需要添加扩展的HTML页面或者其他页面操作，只需要在wx-app
目录中添加对应的HTML页面即可，将Css样式表以及JS文件分别通过相对路径放入到对应的文件夹中即可