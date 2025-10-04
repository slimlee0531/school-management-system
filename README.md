# 学校管理系统

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue.svg">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.4-green.svg">
  <img src="https://img.shields.io/badge/Vue-3-orange.svg">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg">
  <img src="https://img.shields.io/badge/Element%20Plus-2.4.4-blue.svg">
</p>

## 📋 项目简介

一个基于前后端分离架构的学校管理系统，提供全面的学校运营和教学管理功能。系统采用Java Spring Boot作为后端框架，Vue 3作为前端框架，实现了员工管理、学生管理、班级管理、部门管理等核心功能模块。

## ✨ 核心功能

- **👥 员工管理**：员工信息的CRUD操作、分页查询、班主任信息管理
- **🎓 学生管理**：学生信息管理、违纪记录处理、分页查询
- **🏫 班级管理**：班级信息维护、学生分班管理
- **🏢 部门管理**：学校部门组织结构管理
- **🔐 权限认证**：基于JWT的用户认证与授权
- **📊 数据报表**：各类统计报表生成
- **📁 文件管理**：集成阿里云OSS实现文件存储

### 安全增强特性
- **JWT令牌认证**：采用Filter过滤器实现的JWT令牌验证机制
- **密码安全管理**：实现了完整的密码修改流程，包括旧密码验证、新密码强度校验和密码变更日志记录
- **敏感信息保护**：数据库密码和云服务密钥从环境变量读取，避免配置文件硬编码
- **ThreadLocal优化**：使用CurrentHolder工具类安全管理线程局部变量，防止内存泄漏

## 🔒 安全架构详解

本项目在安全方面进行了全面升级，采用了多层安全防护策略，以下是具体实现细节：

### JWT认证机制
- **过滤器实现**：使用`TokenFilter`过滤器替代原有的拦截器方案，更高效地处理身份验证
- **完整的Token生命周期管理**：
  - 登录时生成包含用户ID和姓名的JWT令牌
  - 请求时通过`Authorization`头传递Token
  - 过滤器验证Token有效性并解析用户信息
  - 使用ThreadLocal存储当前用户上下文
  - 请求结束后清理ThreadLocal资源，防止内存泄漏
- **Token配置**：在`application.yml`中可配置密钥和过期时间（默认30分钟）

### 密码安全管理
- **密码修改流程**：
  - 验证旧密码正确性
  - 新密码强度检查（长度6-20位）
  - 防止新密码与旧密码相同
  - 双重验证机制（前后端均进行验证）
- **事务管理**：密码修改和日志记录通过`@Transactional`注解确保原子性
- **操作日志**：每次密码变更都会记录详细日志，包括操作人、时间等信息

### 敏感信息保护
- **环境变量读取**：数据库密码(`DB_PASSWORD`)和阿里云OSS密钥(`ALIYUN_OSS_*`)均从环境变量读取
- **默认值处理**：开发环境提供安全的默认值，方便本地开发和测试
- **配置文件隔离**：敏感配置与代码分离，便于不同环境部署和管理

### ThreadLocal使用优化
- 使用`CurrentHolder`工具类统一管理线程局部变量
- 提供了完善的`remove()`方法，确保在请求结束时清理资源
- 采用`InheritableThreadLocal`支持线程池环境下的上下文传递

## 🛠 技术栈

### 后端
- **框架**：Spring Boot 3.5.4
- **ORM**：MyBatis
- **数据库**：MySQL 8.0
- **认证**：JWT (JSON Web Token)
- **文件存储**：阿里云OSS
- **分页**：PageHelper
- **日志**：SLF4J + Lombok

### 前端
- **框架**：Vue 3
- **UI组件库**：Element Plus 2.4.4
- **路由**：Vue Router 4.1.5
- **HTTP客户端**：Axios 1.7.2
- **图表**：ECharts 6.0.0
- **构建工具**：Vite 3.0.9
- **代码规范**：ESLint + Prettier

## 📁 项目结构

```
school-management-system/
├── backend/                # 后端代码目录
│   ├── tlias-management/   # 主项目模块
│   └── ...                 # 其他功能模块
├── frontend/               # 前端代码目录
│   ├── src/                # 前端源码
│   │   ├── api/            # API接口定义
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── views/          # 页面视图
│   │   └── utils/          # 工具函数
│   └── ...                 # 配置文件
└── README.md               # 项目说明文档
```

## 🚀 快速开始

### 环境要求
- JDK 17+ 
- Node.js 16+ 
- MySQL 8.0+
- Maven 3.8+

### 环境变量配置

为了确保系统安全，以下敏感信息**必须**通过环境变量进行配置，**禁止**在代码或配置文件中硬编码：

> ⚠️ **安全警告**：永远不要在公开的代码库、README或配置文件中暴露密码、密钥等敏感信息！

| 环境变量名称 | 描述 | 建议值 | 说明 |
|------------|------|-------|------|
| `DB_PASSWORD` | 数据库密码 | 不提供 | 开发环境可设置临时密码，生产环境必须使用强密码 |
| `ALIYUN_OSS_ACCESS_KEY_ID` | 阿里云OSS访问密钥ID | 不提供 | 生产环境必须设置，开发环境可选 |
| `ALIYUN_OSS_ACCESS_KEY_SECRET` | 阿里云OSS访问密钥Secret | 不提供 | 生产环境必须设置，开发环境可选 |
| `JWT_SECRET` | JWT签名密钥 | 不提供 | 必须使用至少32位的随机复杂字符串 |

**安全配置最佳实践**：

- **开发环境**：在IDE的运行配置中设置环境变量，不要提交到版本控制
- **测试环境**：使用CI/CD变量或密钥管理系统注入环境变量
- **生产环境**：使用云服务的密钥管理服务或容器平台的secret管理功能
- **密钥轮换**：定期更换敏感密钥，特别是在人员变动后

**查看配置文件中的实现**：

- 数据库密码：从环境变量读取，配置示例：`password: ${DB_PASSWORD:}`
- JWT密钥：当前为简化开发配置了默认值，生产环境应从环境变量读取
- 阿里云OSS密钥：从环境变量读取，无默认值

### 后端启动
1. 克隆项目代码
   ```bash
   git clone https://github.com/yourusername/school-management-system.git
   cd school-management-system
   ```

2. 配置数据库
   - 创建MySQL数据库：`tlias_management`
   - 修改`backend/tlias-management/src/main/resources/application.yml`中的数据库连接信息

3. 构建并启动后端服务
   ```bash
   cd backend/tlias-management
   mvn clean package
   java -jar target/tlias-management-0.0.1-SNAPSHOT.jar
   ```

### 前端启动
1. 安装依赖
   ```bash
   cd frontend
   npm install
   ```

2. 启动开发服务器
   ```bash
   npm run dev
   ```

3. 浏览器访问：[http://localhost:5173](http://localhost:5173)

## 🔑 关键API接口

### 员工管理
- **GET /emps** - 分页查询员工信息
- **POST /emps** - 新增员工
- **PUT /emps** - 修改员工信息
- **DELETE /emps** - 批量删除员工
- **GET /emps/{id}** - 查询单个员工详情
- **GET /emps/list** - 查询全部班主任信息

### 学生管理
- **GET /students** - 分页查询学生信息
- **POST /students** - 添加学生
- **PUT /students** - 修改学生信息
- **DELETE /students/{ids}** - 批量删除学生
- **GET /students/{id}** - 根据ID查询学生
- **PUT /students/violation/{id}/{score}** - 处理学生违纪信息

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 许可协议

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件

## 📧 联系我们

如有问题或建议，请联系项目维护者

---

⭐️ 如果你觉得这个项目有帮助，请给我们一个 star！⭐️
