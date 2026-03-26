<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>申请报修</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin-bottom: 20px;">
          <el-alert :title="isRoomLeader === 1 ? '您是寝室长，可以申请报修和确认完成' : '您可以查看报修进度，只有寝室长可以申请报修'" :type="isRoomLeader === 1 ? 'success' : 'info'" :closable="false" show-icon />
        </div>
        <div>
          <div style="margin: 10px 0">
            <div style="margin: 10px 0">
              <el-input v-model="search" clearable placeholder="请输入标题" prefix-icon="Search" style="width: 20%"/>
              <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
              <div style="float: right">
                <el-tooltip v-if="isRoomLeader === 1" content="申请报修" placement="top">
                  <el-button icon="plus" style="width: 100px" type="primary" @click="add">申请报修</el-button>
                </el-tooltip>
              </div>
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
                <el-button v-if="isRoomLeader === 1 && scope.row.state === '已完成'" icon="InfoFilled" type="success" @click="handleConfirmComplete(scope.row)">确认完成</el-button>
                <el-button v-if="isRoomLeader === 1 && scope.row.state === '待处理'" icon="Delete" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
            <el-dialog v-model="dialogVisible" title="申请报修" width="35%" @close="cancel">
              <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-form-item label="标题" prop="title">
                  <el-input v-model="form.title" clearable style="width: 80%"></el-input>
                </el-form-item>
                <el-form-item label="报修人" prop="repairer">
                  <el-input v-model="form.repairer" disabled clearable style="width: 80%"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                  <el-input
                      v-model="form.content"
                      :autosize="{ minRows: 4, maxRows: 10 }"
                      autosize
                      clearable
                      style="width: 80%"
                      type="textarea"
                  ></el-input>
                </el-form-item>
                <el-form-item label="时间" prop="orderBuildTime">
                  <el-date-picker
                      v-model="form.orderBuildTime"
                      clearable
                      placeholder="选择时间"
                      style="width: 78%"
                      type="datetime"
                      value-format="YYYY-MM-DD HH:mm:ss"
                      :disabled="true"
                  ></el-date-picker>
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
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/ApplyRepairInfo.js"></script>
