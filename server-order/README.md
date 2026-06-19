# Server-Order 模块

## 概述

Server-Order 模块是游戏代练平台的核心业务模块，主要负责订单管理和游戏管理功能。

## 功能模块

### 游戏管理 (Games Management)

游戏管理模块提供了完整的游戏CRUD操作，包括：

- 游戏列表查询（支持分页、搜索、筛选）
- 游戏创建（支持图标文件上传）
- 游戏更新（支持图标文件更新）
- 游戏删除
- 游戏状态切换

#### API 接口

**基础路径**: `/games`

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| GET | `/games` | 获取游戏列表 | GameQueryDTO |
| POST | `/games` | 创建游戏 | gameData (JSON), icon (文件) |
| PUT | `/games/{id}` | 更新游戏 | id, gameData (JSON), icon (文件) |
| DELETE | `/games/{id}` | 删除游戏 | id |
| PATCH | `/games/{id}/status` | 切换游戏状态 | id, status |

#### 请求参数说明

**GameQueryDTO (查询参数)**
```json
{
  "pageNum": 1,           // 页码，默认1
  "pageSize": 10,         // 每页数量，默认10
  "gameName": "王者荣耀",  // 游戏名称（模糊查询）
  "status": 1,            // 游戏状态 (0: 禁用, 1: 启用)
  "startTime": "2024-01-01 00:00:00", // 开始时间
  "endTime": "2024-12-31 23:59:59"    // 结束时间
}
```

**GameCreateDTO (创建参数)**
```json
{
  "gameName": "王者荣耀",     // 游戏名称（必填）
  "status": 1,              // 游戏状态 (0: 禁用, 1: 启用)
  "sortOrder": 0            // 排序权重
}
```

**GameUpdateDTO (更新参数)**
```json
{
  "gameName": "王者荣耀-更新版", // 游戏名称
  "status": 1,                 // 游戏状态 (0: 禁用, 1: 启用)
  "sortOrder": 1               // 排序权重
}
```

#### 文件上传说明

**创建游戏时上传图标：**
- 使用 `multipart/form-data` 格式
- `gameData` 参数：JSON格式的游戏数据
- `icon` 参数：图片文件（可选）

**更新游戏时更新图标：**
- 使用 `multipart/form-data` 格式
- `gameData` 参数：JSON格式的游戏数据
- `icon` 参数：新的图片文件（可选，不传则不更新图标）

#### 响应格式

所有接口都返回统一的响应格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体数据
  }
}
```

#### 使用示例

**1. 获取游戏列表**
```bash
GET /games?pageNum=1&pageSize=10&gameName=王者荣耀&status=1
```

**2. 创建游戏（带图标）**
```bash
POST /games
Content-Type: multipart/form-data

gameData: {
  "gameName": "王者荣耀",
  "status": 1,
  "sortOrder": 0
}
icon: [图片文件]
```

**3. 创建游戏（不带图标）**
```bash
POST /games
Content-Type: multipart/form-data

gameData: {
  "gameName": "王者荣耀",
  "status": 1
}
```

**4. 更新游戏（更新图标）**
```bash
PUT /games/1
Content-Type: multipart/form-data

gameData: {
  "gameName": "王者荣耀-更新版",
  "sortOrder": 1
}
icon: [新的图片文件]
```

**5. 更新游戏（不更新图标）**
```bash
PUT /games/1
Content-Type: multipart/form-data

gameData: {
  "gameName": "王者荣耀-更新版"
}
```

**6. 删除游戏**
```bash
DELETE /games/1
```

**7. 切换游戏状态**
```bash
PATCH /games/1/status?status=0
```

## 数据库表结构

### tb_jmz_games (游戏表)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | int | 主键ID |
| name | varchar(100) | 游戏名称 |
| icon | varchar(255) | 游戏图标（OSS对象Key） |
| status | tinyint | 状态：1-启用，0-禁用 |
| sort_order | int | 排序 |
| created_at | timestamp | 创建时间 |
| updated_at | timestamp | 更新时间 |

## 技术栈

- Spring Boot
- MyBatis-Plus
- MySQL
- Nacos (服务注册与配置中心)
- Spring Security (安全认证)
- Spring Cloud OpenFeign (服务间调用)
- 阿里云OSS (文件存储)

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/game_boosting?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### OSS配置
```yaml
oss:
  endpoint: https://oss-cn-shanghai.aliyuncs.com
  accessKeyId: your-access-key-id
  accessKeySecret: your-access-key-secret
  bucketName: your-bucket-name
  region: cn-shanghai
```

### Nacos配置
```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
      config:
        server-addr: localhost:8848
        file-extension: yml
```

## 文件存储说明

- 游戏图标文件通过 Feign 客户端调用 `jmz-file` 服务上传到阿里云OSS
- 文件上传成功后返回OSS对象Key，存储在数据库的 `icon` 字段中
- 删除游戏时会自动删除对应的OSS文件
- 更新游戏图标时会先删除旧图标再上传新图标

## 注意事项

1. 文件上传需要确保 `jmz-file` 服务正常运行
2. 图片文件建议使用常见格式（jpg、png、gif等）
3. 文件大小建议控制在合理范围内（如5MB以内）
4. 更新游戏图标时，如果上传失败，不会影响其他字段的更新 