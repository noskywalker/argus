# 模块说明：

## 1、监控系统，对应argus-monitor模块，main方法由storm来触发执行，main方法为：TopoLogyPortal
## 2、报警系统，对应argus-alarm模块，是一个executable jar，main方法为：RunAlarmService
## 3、mis管理平台，对应argus-mis系统，是一个war包，部署到tomcat。
附带docker-compose文件：
```java
docker-compose up
```
### compose启动后，管理后台登录
    
    http://localhost:8080
    
    admin@test.com/123qwe