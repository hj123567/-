# 学生宿舍管理系统 - 学生端用例图（UML标准样式）

```mermaid
graph LR
    classDef actor fill:#fff,stroke:#333,stroke-width:2px,text-align:center;
    classDef usecase fill:#fff,stroke:#333,stroke-width:1px,text-align:center;
    
    Student((学生)):::actor
    
    U1(我的宿舍):::usecase
    U2(查看宿舍信息):::usecase
    U3(查看室友信息):::usecase
    U4(查看床位安排):::usecase
    U5(查看报修记录):::usecase
    U6(查看卫生评分记录):::usecase
    U7(查看水电费账单):::usecase
    U8(一键申请调宿):::usecase
    
    U9(卫生评分):::usecase
    U10(查看评分记录):::usecase
    U11(查看评分详情):::usecase
    
    U12(水电费管理):::usecase
    U13(查看账单列表):::usecase
    U14(筛选账单):::usecase
    U15(在线缴费):::usecase
    U16(查看缴费状态):::usecase
    
    U17(申请调宿):::usecase
    U18(填写调宿申请):::usecase
    U19(提交调宿申请):::usecase
    U20(查看申请状态):::usecase
    
    U21(报修申请):::usecase
    U22(填写报修信息):::usecase
    U23(提交报修申请):::usecase
    U24(查看报修状态):::usecase
    
    U25(个人信息):::usecase
    U26(查看个人信息):::usecase
    U27(编辑个人信息):::usecase
    
    Student --> U1
    Student --> U9
    Student --> U12
    Student --> U17
    Student --> U21
    Student --> U25
    
    U1 --> U2
    U1 --> U3
    U1 --> U4
    U1 --> U5
    U1 --> U6
    U1 --> U7
    U1 --> U8
    
    U9 --> U10
    U9 --> U11
    
    U12 --> U13
    U12 --> U14
    U12 --> U15
    U12 --> U16
    
    U17 --> U18
    U17 --> U19
    U17 --> U20
    
    U21 --> U22
    U21 --> U23
    U21 --> U24
    
    U25 --> U26
    U25 --> U27
    
    linkStyle default stroke:#333,stroke-width:1px,fill:none;
```

## 用例图说明

### 参与者
- **学生**：系统的主要用户，使用学生端功能

### 主要用例模块

#### 1. 我的宿舍
- 查看宿舍信息
- 查看室友信息
- 查看床位安排
- 查看报修记录
- 查看卫生评分记录
- 查看水电费账单
- 一键申请调宿

#### 2. 卫生评分
- 查看评分记录
- 查看评分详情

#### 3. 水电费管理
- 查看账单列表
- 筛选账单
- 在线缴费
- 查看缴费状态

#### 4. 申请调宿
- 填写调宿申请
- 提交调宿申请
- 查看申请状态

#### 5. 报修申请
- 填写报修信息
- 提交报修申请
- 查看报修状态

#### 6. 个人信息
- 查看个人信息
- 编辑个人信息

## 用例图特点

- **标准UML样式**：使用传统的用例图表示方法
- **白色背景**：所有节点使用白色背景
- **半开箭头**：使用实线箭头表示关联关系
- **层次清晰**：主用例与子用例关系明确
- **功能完整**：覆盖了学生端的所有核心功能
- **交互明确**：清晰展示了学生与系统功能的交互关系

此用例图可以帮助开发团队和用户理解学生端系统的功能范围和操作流程，为系统设计和测试提供参考。
