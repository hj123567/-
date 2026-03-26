<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>报修信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-input v-model="search" clearable placeholder="请输入标题" prefix-icon="Search" style="width: 20%"/>
            <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
            <el-button icon="refresh-left" style="margin-left: 10px" type="default" @click="reset"></el-button>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="600" style="width: 100%">
          <el-table-column label="#" type="index"/>
          <el-table-column label="标题" prop="title" sortable/>
          <el-table-column label="报修人" prop="repairer" width="100px"/>
          <el-table-column label="宿舍楼" prop="dormBuildId" width="90px">
            <template #default="scope">
              {{ scope.row.dormBuildId ? scope.row.dormBuildId + '号楼' : '-' }}
            </template>
          </el-table-column>
          <el-table-column label="房间号" prop="dormRoomId" width="90px">
            <template #default="scope">
              {{ scope.row.dormRoomId || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="报修时间" prop="orderBuildTime" sortable width="180px"/>
          <el-table-column
              :filter-method="filterTag"
              :filters="[
              { text: '待处理', value: '待处理' },
              { text: '处理中', value: '处理中' },
              { text: '已完成', value: '已完成' },
              { text: '已评价', value: '已评价' },
            ]"
              filter-placement="bottom-end"
              label="状态"
              prop="state"
              width="120px"
          >
            <template #default="scope">
              <el-tag :type="getStateType(scope.row.state)">{{ scope.row.state }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280px">
            <template #default="scope">
              <el-button icon="View" type="primary" @click="showDetail(scope.row)">查看</el-button>
              <el-button v-if="scope.row.state === '待处理'" icon="Tools" type="warning" @click="handleRepairing(scope.row)">正在维修</el-button>
              <el-button v-if="scope.row.state === '处理中'" icon="CircleCheck" type="success" @click="handleComplete(scope.row)">完成维修</el-button>
              <el-popconfirm title="确认删除？" @confirm="handleDelete(scope.row.id)">
                <template #reference>
                  <el-button icon="Delete" type="danger">删除</el-button>
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
        <div>
          <el-dialog v-model="detailDialog" title="报修详情" width="40%" @close="closeDetails">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="标题">{{ detail.title }}</el-descriptions-item>
              <el-descriptions-item label="报修人">{{ detail.repairer }}</el-descriptions-item>
              <el-descriptions-item label="宿舍楼">{{ detail.dormBuildId ? detail.dormBuildId + '号楼' : '-' }}</el-descriptions-item>
              <el-descriptions-item label="房间号">{{ detail.dormRoomId || '-' }}</el-descriptions-item>
              <el-descriptions-item label="内容">{{ detail.content }}</el-descriptions-item>
              <el-descriptions-item label="报修时间">{{ detail.orderBuildTime }}</el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="getStateType(detail.state)">{{ detail.state }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="完成时间">{{ detail.orderFinishTime || '未完成' }}</el-descriptions-item>
            </el-descriptions>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="closeDetails">关 闭</el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/RepairInfo.js"></script>
