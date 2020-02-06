# 概况

## 代码 = 数据 + 模板 

- 数据：mysql

- 模板：freemarker

- 代码：controller、service、dao、model、mapper...

## 功能点

## vg

解析的命令识别符

### 格式
```shell script
vg [cmd] [param1] [param2] [...]
```

### 初始化项目

```shell script
vg init
```

生成如下目录及文件：

- /pom.xml
- /src/main/java/com/samples/demo/common
- /src/main/java/com/samples/demo/common/DataSourceConfig.java
- /src/main/java/com/samples/demo/common/MybatisMapper.java
- /src/main/java/com/samples/demo/controller
- /src/main/java/com/samples/demo/dao
- /src/main/java/com/samples/demo/mapper
- /src/main/java/com/samples/demo/model
- /src/main/java/com/samples/demo/service
- /src/main/java/com/samples/demo/Application.java
- /src/main/resources
- /src/main/resources/application.properties
- /src/main/resources/logback.xml
- /src/main/resources/mybatis-config.xml

### 生成文件

```shell script
vg model [table_name] [model_name]
```

参数：

- table_name : 原始数据表名，必选
- model_name : 映射的模型类名，可选，缺省时同table_name