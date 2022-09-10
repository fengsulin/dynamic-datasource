<a name="wOnLK"></a>
# 一、前言
在实际开发中，经常可能会遇到一个应用中需要放访问多个数据库的情况，以下是两种定型的场景：

- 业务复杂或数据量大
- 读写分离：为解决数据库读写性能瓶颈（读比写性能更高，写锁会影响读阻塞），写主库，读从库。
<a name="sXJjd"></a>
# 二、自定义数据源
> 对于大多数java应用都使用spring框架，通过spring-jdbc来获取数据源`dataSource.getConnection()`为此我们可以自定义数据源，然后通过实现`DataSource`来获取我们自定义的数据源。

<a name="dT1vC"></a>
## 2.1 创建spring-boot项目
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.6.11</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.lin</groupId>
    <artifactId>dynamic-datasource</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dynamic-datasource</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
<!--        mybatis-plus框架-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.2</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
<!--        druid数据源连接池-->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.2.11</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>

```
<a name="O5jYC"></a>
## 2.2 配置文件
> 这里以datasource1作为写库，datasource2作为读库。

```yaml
server:
  port: 8080
spring:
  application:
    name: @project.artifactId@
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    datasource1:
      druid:
        initial-size: 1
        min-idle: 1
        max-active: 20
  #      是否检测连接池里连接的可用性
        test-on-borrow: true
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: 123456
      url: jdbc:mysql://192.168.2.131:3306/datasource1?serverTimezone=Asia/Shanghai&useUnicode=true&useSSL=false
    datasource2:
      type: com.alibaba.druid.pool.DruidDataSource
      druid:
        initial-size: 1
        min-idle: 1
        max-active: 20
        #      是否检测连接池里连接的可用性
        test-on-borrow: true
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: root
      password: 123456
      url: jdbc:mysql://192.168.2.131:3306/datasource2?serverTimezone=Asia/Shanghai&useUnicode=true&useSSL=false


```
<a name="Au9VI"></a>
## 2.3 自定义数据源
> 自定义DataSource，将bean注入到容器。

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.datasource1")
    public DataSource dataSource1(){
        // 底层会自动拿到spring.datasource中的配置，创建DruidDataSource数据源
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.datasource2")
    public DataSource dataSource2(){
        return DruidDataSourceBuilder.create().build();
    }
}

```
<a name="EK3Sg"></a>
# 三、动态获取数据源
<a name="O3qcx"></a>
## 3.1 方式一：自定义获取数据源
> 该种方式并不稳定，这里只是以学习为主尝试。
> 自定义实现DataSource，需要重写DataSource里的很多方法，有些方法需要熟悉源码才能很好的写出，增加了使用难度，这里只实现`getConnect()`.

```java
@Component
@Primary    // 将该bean设置为主要注入bean，防止容器中存在多个相同类型的bean时spring不知道取那个而报错。
public class DynamicDataSource implements DataSource, InitializingBean {

    //  当前使用的数据源标识，为了动态判断使用那个数据源
    public static ThreadLocal<String> flag = new ThreadLocal<>();

    @Autowired
    private DataSource dataSource1;
    @Autowired
    private DataSource dataSource2;

    @Override
    public Connection getConnection() throws SQLException {
        if (flag.get().equals("W")){
            return dataSource1.getConnection();
        }else {
            return dataSource2.getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化bean，bean注入到容器时的初始化操作
        flag.set("W");  //  这里默认设置标识为W
    }
}

```
<a name="njmh2"></a>
## 3.2 方式二：继承AbstracRoutingDataSource
> `AbstracRoutingDataSource`类时spring提供的，为的是方便动态切换数据源，该种方式比方式一更加稳定。

```java
@Component
@Primary    // 将该bean设置为主要注入bean，防止容器中存在多个相同类型的bean时spring不知道取那个而报错。
public class DynamicDataSource extends AbstractRoutingDataSource {

    //  当前使用的数据源标识，为了动态判断使用那个数据源
    public static ThreadLocal<Object> flag = new ThreadLocal<>();

    @Autowired
    private DataSource dataSource1;
    @Autowired
    private DataSource dataSource2;


    @Override
    public void afterPropertiesSet() {
        // 初始化bean，bean注入到容器时的初始化操作
        // 为targetDataSources初始化所有数据源
        Map<Object,Object> targetDataSource = new HashMap<>();
        targetDataSource.put(DataSourceFlagEnum.W,dataSource1);
        targetDataSource.put(DataSourceFlagEnum.R,dataSource2);
        super.setTargetDataSources(targetDataSource);

        // 为defaultTargetDataSource 设置默认的数据源
        super.setDefaultTargetDataSource(dataSource1);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {  // 获取数据源的标识
        return flag.get();
    }


}

```
<a name="DPw5f"></a>
## 3.3 解决设置数据源标识
常见的有两种：

- mybatis插件：适合读写分离，通过自定义类实现`Interceptor`mybatis拦截器，获取每一次sql的类型时读还是写。
- Aop：适用范围比较广
<a name="Rg3ug"></a>
### 3.3.1 基于mybatis

- 自定义拦截器
```java
@Intercepts({
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class,Object.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class,method = "query",args = {MappedStatement.class,Object.class,RowBounds.class,ResultHandler.class, CacheKey.class, BoundSql.class})
})
public class DynamicDataSourcePlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取当前方法（update，query）所有参数
        Object[] objects = invocation.getArgs();
        // MappedStatement封装了SQL和SQL属性
        MappedStatement ms = (MappedStatement) objects[0];
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)){
            DynamicDataSource.flag.set(DataSourceFlagEnum.R);
        }else {
            DynamicDataSource.flag.set(DataSourceFlagEnum.W);
        }
        return invocation.proceed();
    }

}
```

- 将拦截器注入到容器
```java
@Configuration
public class DynamicPluginConfig {

//    @Autowired
//    private List<SqlSessionFactory> sqlSessionFactoryList;
//
//    @PostConstruct
//    public void addDataSourcePlugin(){
//        DynamicDataSourcePlugin dynamicDataSourcePlugin = new DynamicDataSourcePlugin();
//        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList){
//            sqlSessionFactory.getConfiguration().addInterceptor(dynamicDataSourcePlugin);
//        }
//    }

    /**
     * 将自定义的Interceptor注入到spring容器，MybatisPlusAutoConfiguration 插件自定配置类在程序启动时会到容器中找到注入的拦截器
     */
    @Bean
    public Interceptor dynamicDataSourcePlugin(){
        return new DynamicDataSourcePlugin();
    }
}
```
> 注意：我们自定义的拦截器会先执行

<a name="owzdn"></a>
### 3.3.2 基于AOP
> 适合不同业务数据源

- 自定义注解
```java
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceAnnotation {
    DataSourceFlagEnum value() default DataSourceFlagEnum.R;
}

```

- 切面类
```java
@Component
@Aspect
public class DynamicDataSourceAspect {

    //前置或环绕
    @Before("within(com.lin.dynamic.service.imp.*) && @annotation(dataSourceAnnotation)")
    public void before(JoinPoint joinPoint, DataSourceAnnotation dataSourceAnnotation){
        DataSourceFlagEnum value = dataSourceAnnotation.value();
        DynamicDataSource.flag.set(value);
    }
}

```

- 实现类或方法添加注解
```java
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Override
    @DataSourceAnnotation(DataSourceFlagEnum.R)
    public List<Person> list() {
        return personMapper.list();
    }

    @Override
    @DataSourceAnnotation(DataSourceFlagEnum.W)
    public void save(Person person) {
        personMapper.save(person);
    }
}

```
<a name="a9zFe"></a>
# 四、Spring集成多个Mybatis框架实现多数据源
> 这里有多少个数据源就设置多个配置类，我这里只设置其中一个，其他类似。

```java
/**
 * 这里指定mapper接口和引用的sqlSessionFactory名称
 */
@Configuration
@MapperScan(
        basePackages = "com.lin.dynamic.mapper",
        sqlSessionFactoryRef = "rSqlSessionFactory"
)
public class DynamicConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.datasource1")
    public DataSource dataSource1(){
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean
    @Primary
    public SqlSessionFactory rSqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        // 指定主库
        sessionFactoryBean.setDataSource(dataSource1());
        // 指定主库对应的mapper.xml文件
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResource("classpath:mapper/r/*.xml"));
        return sessionFactoryBean.getObject();
    }
}

```
<a name="SpReC"></a>
# 五、多数源事务控制
> 涉及到多个数据源的读写，一旦发生异常会导致数据不一致，在这种情况下进行回退，但是spring的声明式事务在一次请求线程中只能使用一个数据源进行控制。

<a name="W6fZo"></a>
## 5.1 针对一个事务管理器进行控制
> 这里以集成多个Mybatis框架实现多数据源为例，如果时前面的两种会更难实现。

```java
    @Bean
    @Primary
    public DataSourceTransactionManager wTransactionManager(){
        // spring一次只能使用一个事务管理器，所有这里用primary注解，也在注解Transactional("事务名称")
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource1());
        return dataSourceTransactionManager;
    }

```
<a name="rAJEP"></a>
## 5.2 一个方法开启两个事务：spring编程式事务

- 数据源1
```java
    @Bean
//    @Primary
    public DataSourceTransactionManager rTransactionManager(){
        // spring一次只能使用一个事务管理器，所有这里用primary注解，也在注解Transactional("事务名称")
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource1());
        return dataSourceTransactionManager;
    }
    
    @Bean
    public TransactionTemplate rTransactionTemplate(){
        return new TransactionTemplate(rTransactionManager());
    }

```

- 数据源2
```java
    @Bean
//    @Primary
    public DataSourceTransactionManager wTransactionManager(){
        // spring一次只能使用一个事务管理器，所有这里用primary注解，也在注解Transactional("事务名称")
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource2());
        return dataSourceTransactionManager;
    }
    
    @Bean
    public TransactionTemplate wTransactionTemplate(){
        return new TransactionTemplate(wTransactionManager());
    }

```
<a name="c8XS2"></a>
### 5.2.1 spring编程式事务
> 伪代码

```java
# 注入两个TransactionTemplate

@Override
public void saveAll(Person person){
    wTransactionTemplate.execute((wstatus) ->{
        rTransactionTemplate.execute((rstatus) ->{
            try{
                saveW(person);
                saveR(person);
            }catch (Exception e){
                e.printStackTrace();
                wstatus.setRollbackOnly();
                rstatus.setRollbackOnly();
                return false;
            }
        })
    }
    return true;
);
}
```
<a name="EBzjx"></a>
## 5.3 通过spring声明式事务
> 一个方法里只能有一个数据源的事务操作

```java
@Transactional(transactionManager = "wTransactionManager")
public void saveAll(){
    saveW();
}

@Transactional(transactionManager = "rTransactionManager")
public void saveAllR(){
    saveR();
}
```
<a name="cGrx8"></a>
# 六、基于dynamic-datasource多数据源组件
> 基于spring-boot的多数据源组件，功能强悍，支持Seata分布式事务。
> 官网文档：`[https://github.com/baomidou/dynamic-datasource-spring-boot-starter](https://github.com/baomidou/dynamic-datasource-spring-boot-starter)`
> 该组件适合数据源比较多，分布式事务的情景。
> 该组件的原理是继承`AbstractRoutingDataSource`，基于注解实现数据源切换，但其内部还有其他更丰富的功能。

