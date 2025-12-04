### Personal Service
JDK 21
MySql 8
Spring-boot 4

com.HeZhizhu.PersonalServer
├── PersonalServerApplication.java  # 项目启动类（自动生成）
├── config/                      # 配置类（跨域、拦截器等）
├── controller/                  # 控制器（接收前端请求，返回响应）
├── service/                     # 服务层（业务逻辑处理）
│   └── impl/                    # 服务实现类
├── repository/                  # 数据访问层（JPA 接口，操作数据库）
├── entity/                      # 实体类（对应数据库表）
├── dto/                         # 数据传输对象（前后端数据交互格式）
├── exception/                   # 异常处理（全局异常、自定义异常）
└── util/                        # 工具类（通用工具方法）