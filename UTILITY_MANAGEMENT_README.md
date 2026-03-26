# 水电费管理系统 - 使用说明

## 功能概述
完整的学生宿舍水电费管理系统，包含参数配置、读数录入、账单生成、在线缴费等功能。

## 数据库表
1. `utility_config` - 水电费参数配置
2. `utility_meter_reading` - 水电表读数记录
3. `utility_bill` - 水电费账单
4. `utility_payment_record` - 缴费记录
5. `utility_reminder` - 催缴提醒

## 后端API接口

### 水电费参数配置 (`/utilityConfig`)
- `POST /add` - 添加配置
- `PUT /update` - 更新配置
- `DELETE /delete/{id}` - 删除配置
- `GET /find` - 分页查询配置
- `GET /getDefault` - 获取默认配置
- `GET /getByBuildId` - 按楼栋获取配置

### 水电表读数 (`/utilityMeterReading`)
- `POST /add` - 录入读数（自动计算用量）
- `PUT /update` - 更新读数
- `DELETE /delete/{id}` - 删除读数
- `GET /find` - 分页查询读数
- `GET /getLastReading` - 获取上期读数

### 水电费账单 (`/utilityBill`)
- `POST /generate` - 生成账单
- `POST /pay` - 在线缴费
- `PUT /update` - 更新账单
- `DELETE /delete/{id}` - 删除账单
- `GET /find` - 分页查询账单
- `GET /findByRoom` - 按房间查询账单

## 前端页面路由

### 系统管理员
- `/utilityConfigManage` - 水电费参数配置

### 宿舍管理员
- `/utilityMeterReading` - 水电表读数录入
- `/utilityBillManage` - 水电费账单管理

### 学生
- `/myUtilityBill` - 我的水电费

## 使用流程

1. **系统管理员**设置水电费单价和计费周期
2. **宿管**每月录入各房间的水电表读数
3. **宿管**根据读数生成水电费账单
4. **学生**查看自己的水电费账单
5. **学生**在线缴费
6. **宿管**查看缴费状态，可对未缴费房间进行催缴

## 权限控制
- 系统管理员：只能访问参数配置页面
- 宿管：只能访问读数录入和账单管理页面
- 学生：只能访问我的水电费页面
