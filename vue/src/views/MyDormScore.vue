<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的宿舍</el-breadcrumb-item>
      <el-breadcrumb-item>卫生评分</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-button icon="refresh-left" type="primary" @click="load">刷新</el-button>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="705" style="width: 100%">
          <el-table-column label="#" type="index"/>
          <el-table-column label="楼栋号" prop="dormBuildId"/>
          <el-table-column label="房间号" prop="dormRoomId"/>
          <el-table-column label="评分">
            <template #default="scope">
              <el-tag :type="getScoreType(scope.row.score)">{{ scope.row.score }}分</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评语" prop="comment" :show-overflow-tooltip="true"/>
          <el-table-column label="评分人" prop="scorer"/>
          <el-table-column label="评分时间" prop="scoreTime" sortable/>
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
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/MyDormScore.js"></script>
