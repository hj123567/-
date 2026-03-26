# 学生宿舍管理系统

一个功能完善的学生宿舍管理系统，支持学生信息管理、宿舍分配、卫生评分、报修管理、水电费管理等功能。

---

## 系统功能

### 用户角色
- **系统管理员（admin）**：系统全局管理、用户管理、参数配置
- **宿舍管理员（dormManager）**：楼栋和学生宿舍管理、卫生评分、报修处理
- **学生（stu）**：个人宿舍信息查询、服务申请、在线缴费

### 功能模块

| 模块 | 功能描述 |
|------|---------|
| 用户管理模块 | 学生信息、宿管信息、新生入学管理 |
| 宿舍管理模块 | 楼宇信息、房间信息、床位信息管理 |
| 宿舍分配模块 | 新生宿舍分配、智能推荐房间 |
| 卫生评分模块 | 宿管评分、学生查看、每天每房间一次评分约束 |
| 信息管理模块 | 公告信息、报修信息 |
| 申请管理模块 | 调宿申请、报修申请、申请审批 |
| 访客管理模块 | 访客信息管理 |
| 水电费管理模块 | 参数配置、读数录入、账单生成、在线缴费 |
| 个人中心模块 | 我的宿舍、卫生评分、水电费、个人信息 |

---

## 技术栈

### 前端
- Vue.js 3.x
- Element Plus
- Vue Router
- Axios

### 后端
- Spring Boot 2.6.x
- MyBatis Plus
- MySQL 8.0
- Maven

---

## 快速开始

### 前置要求
- JDK 1.8+
- Node.js 14+
- MySQL 8.0+
- Maven 3.6+

### 数据库配置

1. 创建数据库
```sql
CREATE DATABASE dormitory DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 执行数据库脚本
```
database/数据库脚本.sql
database/create_utility_tables.sql
```

3. 修改数据库配置
编辑 `Dormitory_business/src/main/resources/application.properties`，修改数据库连接信息：
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dormitory?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
```

### 后端启动

```bash
cd Dormitory_business
mvn spring-boot:run
```

后端服务将在 http://localhost:9090 启动

### 前端启动

```bash
cd vue
npm install
npm run serve
```

前端服务将在 http://localhost:8080 启动

---

## 默认账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 系统管理员 | admin | 123456 |
| 宿舍管理员 | dorm1 | 123456 |
| 学生 | s001 | 123456 |

---

## 项目结构

```
学生宿舍管理系统/
├── Dormitory_business/          # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/springboot/
│   │   │   │       ├── common/      # 公共类
│   │   │   │       ├── controller/  # 控制器
│   │   │   │       ├── entity/      # 实体类
│   │   │   │       ├── mapper/      # Mapper接口
│   │   │   │       ├── service/     # 服务层
│   │   │   │       └── util/        # 工具类
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
├── vue/                             # 前端项目
│   ├── public/
│   ├── src/
│   │   ├── assets/
│   │   │   ├── css/
│   │   │   └── js/
│   │   ├── components/
│   │   ├── layout/
│   │   ├── router/
│   │   ├── store/
│   │   ├── utils/
│   │   ├── views/
│   │   ├── App.vue
│   │   └── main.js
│   └── package.json
├── database/                        # 数据库脚本
└── README.md
```

---

## 系统特色

1. **卫生评分约束**：每天每个房间只能评分一次，只显示当天的评分
2. **新生宿舍智能分配**：根据性别、楼栋智能推荐房间
3. **完整的水电费管理**：自动计算用量、自动生成账单、学生在线缴费
4. **精细化权限控制**：不同角色拥有不同的功能权限

---

## 许可证

本项目仅供学习使用。

---

## 联系方式

如有问题，请提交Issue。
