<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统管理</el-breadcrumb-item>
      <el-breadcrumb-item>水电费配置</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-button icon="refresh-left" type="primary" @click="load">刷新</el-button>
            <div style="float: right">
              <el-tooltip content="添加配置" placement="top">
                <el-button icon="plus" type="primary" @click="add">添加配置</el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="600" style="width: 100%">
          <el-table-column label="#" type="index" width="60"/>
          <el-table-column label="水费单价(元/吨)" prop="waterPrice" width="150">
            <template #default="scope">
              <span style="font-weight: bold; color: #409EFF">{{ scope.row.waterPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="电费单价(元/度)" prop="electricityPrice" width="150">
            <template #default="scope">
              <span style="font-weight: bold; color: #E6A23C">{{ scope.row.electricityPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="计费周期日" prop="billingCycleDay" width="120">
            <template #default="scope">
              <span>每月{{ scope.row.billingCycleDay }}号</span>
            </template>
          </el-table-column>
          <el-table-column label="适用楼栋" prop="dormBuildId" width="120">
            <template #default="scope">
              <el-tag v-if="scope.row.dormBuildId" type="primary">{{ scope.row.dormBuildId }}号楼</el-tag>
              <el-tag v-else type="success">全部楼栋</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="createTime" width="180"/>
          <el-table-column label="更新时间" prop="updateTime" width="180"/>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button icon="Edit" type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-popconfirm title="确认删除此配置？" @confirm="handleDelete(scope.row.id)">
                <template #reference>
                  <el-button icon="Delete" type="danger" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin: 10px 0">
          <el-pagination
              v-model:currentPage="currentPage"
              :page-size="pageSize"
              :page-sizes="[10, 20]"
              :total="total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          >
          </el-pagination>
        </div>
        
        <el-dialog v-model="dialogVisible" :title="judgeAddOrEdit ? '编辑配置' : '添加配置'" width="40%">
          <el-form ref="form" :model="form" :rules="rules" label-width="140px">
            <el-form-item label="水费单价(元/吨)" prop="waterPrice">
              <el-input-number v-model="form.waterPrice" :min="0" :precision="2" :step="0.1" style="width: 100%"></el-input-number>
            </el-form-item>
            <el-form-item label="电费单价(元/度)" prop="electricityPrice">
              <el-input-number v-model="form.electricityPrice" :min="0" :precision="2" :step="0.1" style="width: 100%"></el-input-number>
            </el-form-item>
            <el-form-item label="计费周期日" prop="billingCycleDay">
              <el-input-number v-model="form.billingCycleDay" :min="1" :max="31" style="width: 100%">
                <template #append>号</template>
              </el-input-number>
            </el-form-item>
            <el-form-item label="适用楼栋" prop="dormBuildId">
              <el-select v-model="form.dormBuildId" placeholder="不选择则为全部楼栋" clearable style="width: 100%">
                <el-option label="全部楼栋" :value="null"></el-option>
                <el-option v-for="build in buildList" :key="build.id" :label="build.name" :value="build.id"></el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="cancel">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/UtilityConfigManage.js"></script>
