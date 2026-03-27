# 学生宿舍管理系统 - 学生端用例图

```mermaid
flowchart TD
    subgraph 学生端系统
        A[学生] --> B[我的宿舍]
        A --> C[卫生评分]
        A --> D[水电费管理]
        A --> E[申请调宿]
        A --> F[报修申请]
        A --> G[个人信息]
        
        B --> B1[查看宿舍信息]
        B --> B2[查看室友信息]
        B --> B3[查看床位安排]
        B --> B4[查看报修记录]
        B --> B5[查看卫生评分记录]
        B --> B6[查看水电费账单]
        B --> B7[一键申请调宿]
        
        C --> C1[查看评分记录]
        C --> C2[查看评分详情]
        
        D --> D1[查看账单列表]
        D --> D2[筛选账单]
        D --> D3[在线缴费]
        D --> D4[查看缴费状态]
        
        E --> E1[填写调宿申请]
        E --> E2[提交调宿申请]
        E --> E3[查看申请状态]
        
        F --> F1[填写报修信息]
        F --> F2[提交报修申请]
        F --> F3[查看报修状态]
        
        G --> G1[查看个人信息]
        G --> G2[编辑个人信息]
    end
    
    style A fill:#f9f,stroke:#333,stroke-width:2px
    style B fill:#bbf,stroke:#333,stroke-width:1px
    style C fill:#bbf,stroke:#333,stroke-width:1px
    style D fill:#bbf,stroke:#333,stroke-width:1px
    style E fill:#bbf,stroke:#333,stroke-width:1px
    style F fill:#bbf,stroke:#333,stroke-width:1px
    style G fill:#bbf,stroke:#333,stroke-width:1px
    style B1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B3 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B4 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B5 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B6 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style B7 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style C1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style C2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style D1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style D2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style D3 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style D4 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style E1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style E2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style E3 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style F1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style F2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style F3 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style G1 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
    style G2 fill:#f0f0f0,stroke:#333,stroke-width:0.5px
```

## 用例图说明

### 参与者
- **学生**：系统的主要用户，使用学生端功能

### 主要用例模块

1. **我的宿舍**
   - 查看宿舍基本信息（楼栋号、房间号、楼层等）
   - 查看室友详细信息
   - 查看床位分配情况
   - 查看宿舍相关记录（报修、卫生评分、水电费）
   - 一键跳转到调宿申请

2. **卫生评分**
   - 查看宿舍历史卫生评分记录
   - 查看评分详细信息（评分、评语、评分人、时间）

3. **水电费管理**
   - 查看水电费账单列表
   - 按缴费状态筛选账单
   - 在线缴纳水电费
   - 查看缴费状态

4. **申请调宿**
   - 填写调宿申请信息
   - 提交调宿申请
   - 查看申请审批状态

5. **报修申请**
   - 填写报修信息
   - 提交报修申请
   - 查看报修处理状态

6. **个人信息**
   - 查看个人详细信息
   - 编辑个人信息

## 用例图特点

- **层次清晰**：主用例模块下包含具体的子用例
- **功能完整**：覆盖了学生端的所有核心功能
- **交互明确**：清晰展示了学生与系统功能的交互关系
- **结构合理**：符合学生使用系统的实际操作流程

此用例图可以帮助开发团队和用户理解学生端系统的功能范围和操作流程，为系统设计和测试提供参考。