<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍管理</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍分配</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>待处理申请</span>
                  <el-button type="primary" size="small" @click="loadData">刷新</el-button>
                </div>
              </template>
              <el-table :data="pendingApplications" border max-height="400" style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55"/>
                <el-table-column label="#" type="index" width="50"/>
                <el-table-column label="学号" prop="username" width="100"/>
                <el-table-column label="姓名" prop="name" width="80"/>
                <el-table-column label="当前房间" prop="currentRoomId" width="80"/>
                <el-table-column label="当前床位" prop="currentBedId" width="80"/>
                <el-table-column label="申请时间" prop="applyTime"/>
              </el-table>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>可用宿舍</span>
                </div>
              </template>
              <el-table :data="availableRooms" border max-height="400" style="width: 100%" @row-click="handleRoomSelect">
                <el-table-column label="#" type="index" width="50"/>
                <el-table-column label="房间号" prop="dormRoomId" width="80"/>
                <el-table-column label="楼栋号" prop="dormBuildId" width="80"/>
                <el-table-column label="已住人数" prop="currentCapacity" width="80"/>
                <el-table-column label="可住人数" prop="maxCapacity" width="80"/>
                <el-table-column label="空床位">
                  <template #default="scope">
                    <el-tag v-for="bed in scope.row.availableBeds" :key="bed" style="margin-right: 5px">{{ bed }}号床</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px">
          <el-col :span="24">
            <el-card>
              <template #header>
                <div class="card-header">
                  <span>分配操作</span>
                </div>
              </template>
              <el-form :inline="true">
                <el-form-item label="选中申请:">
                  <el-tag v-for="app in selectedApplications" :key="app.id" closable @close="removeSelection(app.id)">
                    {{ app.name }} ({{ app.username }})
                  </el-tag>
                </el-form-item>
                <el-form-item label="选中房间:">
                  <el-tag v-if="selectedRoom">{{ selectedRoom.dormBuildId }}号楼 {{ selectedRoom.dormRoomId }}室</el-tag>
                </el-form-item>
                <el-form-item label="选择床位:" v-if="selectedRoom">
                  <el-select v-model="selectedBed" placeholder="请选择床位">
                    <el-option v-for="bed in selectedRoom.availableBeds" :key="bed" :label="bed + '号床'" :value="bed"/>
                  </el-select>
                </el-form-item>
              </el-form>
              <div style="margin-top: 20px">
                <el-button type="primary" :disabled="!canAllocateToEmpty" @click="allocateToEmptyBed">
                  分配到空床位
                </el-button>
                <el-button type="warning" :disabled="!canSwap" @click="swapStudents">
                  互换宿舍
                </el-button>
                <el-button @click="clearSelection">清除选择</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/DormAllocation.js"></script>
<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
