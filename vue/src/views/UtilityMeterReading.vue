<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>水电费管理</el-breadcrumb-item>
      <el-breadcrumb-item>读数录入</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-select v-model="searchDormBuildId" placeholder="选择楼栋" clearable style="width: 150px; margin-right: 10px">
              <el-option v-for="build in buildList" :key="build.id" :label="build.name" :value="build.id"></el-option>
            </el-select>
            <el-select v-model="searchDormRoomId" placeholder="选择房间" clearable style="width: 150px; margin-right: 10px">
              <el-option v-for="room in roomList" :key="room.dormRoomId" :label="room.dormRoomId + '号房'" :value="room.dormRoomId"></el-option>
            </el-select>
            <el-date-picker v-model="searchBillingMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 150px; margin-right: 10px"></el-date-picker>
            <el-button icon="Search" type="primary" @click="load">查询</el-button>
            <el-button icon="refresh-left" @click="reset">重置</el-button>
            <div style="float: right">
              <el-tooltip content="录入读数" placement="top">
                <el-button icon="plus" type="primary" @click="add">录入读数</el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="550" style="width: 100%">
          <el-table-column label="#" type="index" width="60"/>
          <el-table-column label="楼栋号" prop="dormBuildId" width="100"/>
          <el-table-column label="房间号" prop="dormRoomId" width="100"/>
          <el-table-column label="计费月份" prop="billingMonth" width="120"/>
          <el-table-column label="上期水表(吨)" prop="lastWaterReading" width="120"/>
          <el-table-column label="本期水表(吨)" prop="waterReading" width="120"/>
          <el-table-column label="用水量(吨)" prop="waterUsage" width="110">
            <template #default="scope">
              <el-tag v-if="scope.row.waterUsage" type="info">{{ scope.row.waterUsage }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="上期电表(度)" prop="lastElectricityReading" width="120"/>
          <el-table-column label="本期电表(度)" prop="electricityReading" width="120"/>
          <el-table-column label="用电量(度)" prop="electricityUsage" width="110">
            <template #default="scope">
              <el-tag v-if="scope.row.electricityUsage" type="warning">{{ scope.row.electricityUsage }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="录入时间" prop="recordTime" width="180"/>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button icon="Edit" type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button icon="Document" type="success" size="small" @click="generateBill(scope.row)">生成账单</el-button>
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
        
        <el-dialog v-model="dialogVisible" :title="judgeAddOrEdit ? '编辑读数' : '录入读数'" width="45%">
          <el-form ref="form" :model="form" :rules="rules" label-width="140px">
            <el-form-item label="楼栋号" prop="dormBuildId">
              <el-select v-model="form.dormBuildId" placeholder="请选择楼栋" style="width: 100%" @change="onBuildChange">
                <el-option v-for="build in buildList" :key="build.id" :label="build.name" :value="build.id"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="房间号" prop="dormRoomId">
              <el-select v-model="form.dormRoomId" placeholder="请选择房间" style="width: 100%" filterable>
                <el-option v-for="room in roomList" :key="room.dormRoomId" :label="room.dormRoomId + '号房'" :value="room.dormRoomId"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="计费月份" prop="billingMonth">
              <el-date-picker v-model="form.billingMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 100%"></el-date-picker>
            </el-form-item>
            <el-divider content-position="left">上期读数</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="上期水表(吨)">
                  <el-input-number v-model="form.lastWaterReading" :min="0" :precision="2" disabled style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上期电表(度)">
                  <el-input-number v-model="form.lastElectricityReading" :min="0" :precision="2" disabled style="width: 100%"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-divider content-position="left">本期读数</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="本期水表(吨)" prop="waterReading">
                  <el-input-number v-model="form.waterReading" :min="0" :precision="2" style="width: 100%" @change="calculateUsage"></el-input-number>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="本期电表(度)" prop="electricityReading">
                  <el-input-number v-model="form.electricityReading" :min="0" :precision="2" style="width: 100%" @change="calculateUsage"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            <el-divider content-position="left">用量计算</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用水量(吨)">
                  <el-tag type="info" size="large" style="width: 100%; text-align: center;">{{ form.waterUsage || 0 }}</el-tag>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="用电量(度)">
                  <el-tag type="warning" size="large" style="width: 100%; text-align: center;">{{ form.electricityUsage || 0 }}</el-tag>
                </el-form-item>
              </el-col>
            </el-row>
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
<script src="@/assets/js/UtilityMeterReading.js"></script>
