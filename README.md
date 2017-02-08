<h2>项目结构</h2>
*	jdk1.8 版本构建：maven3.0 数据库：mysql 5.1+ springMVC + mybatis + redis


<h2>描述</h2>
*	应用服务可直接运行 JettyServer 启动时先导入根目录下的sql脚本文件。
*	set default project for first register user


<h2>Windows下部署</h2>

<h3>部署前准备</h3>
*	从服务器拉取最新代码做部署准备
*	执行 _**`mvn eclipse:eclipse`**_ 下载项目所需要的 jar 包
*	执行 _**`mvn clean`**_ 清理项目
*	执行 _**`mvn compile`**_ 编译项目
*	执行 _**`mvn package -Dmaven.test.skip=true`**_ 将项目打包
*	打包完毕将项目中 _**`target`**_ 目录下相应的 war 包上传服务器准备部署

<h3>数据库同步</h3>
*	将每个人修改数据库的记录同步到服务器中的数据库中，_**`在同步完成之后，需要在每个记录文	件后添加本次更新的标记`**_，方便下次同步数据库的时候查看


<h3>开始部署</h3>
*	部署之前_**`以当前的日期为名称新建文件夹`**_，_**`将正在运行的项目拷贝到当	前文件夹备份`**_，并将需要部署的 war 包上传到该目录下
*	拷贝 war 文件到 tomcat 的 _**`webapps`**_ 目录下，tomcat 自动解压 	war 文件
*	当 war 文件解压完毕，关闭 tomcat，进行相应文件的替换
*	首先是项目根目录下的 _**`signing`**_ 文件夹 和 _**`uediter`**_ 两个文件夹	用先前拷贝出来的工程里的对应文件夹替换
*	然后根据需要替换相应的 _**`WEB-INF/classess`**_ 和 _**`WEB-INF/	classess/spring`**_ 下的配置文件

<h3>部署完毕</h3>
*	配置文件替换完毕，使用 tomcat 的启动脚本 _**`startup.bat`**_ 启	动tomcat
