<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>水电费管理</el-breadcrumb-item>
      <el-breadcrumb-item>账单管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-select v-model="searchDormBuildId" placeholder="选择楼栋" clearable style="width: 150px; margin-right: 10px">
              <el-option v-for="build in buildList" :key="build.id" :label="build.name" :value="build.id"></el-option>
            </el-select>
            <el-select v-model="searchPaymentStatus" placeholder="缴费状态" clearable style="width: 150px; margin-right: 10px">
              <el-option label="全部" :value="null"></el-option>
              <el-option label="未缴费" value="未缴费"></el-option>
              <el-option label="已缴费" value="已缴费"></el-option>
            </el-select>
            <el-date-picker v-model="searchBillingMonth" type="month" placeholder="选择月份" value-format="YYYY-MM" style="width: 150px; margin-right: 10px"></el-date-picker>
            <el-button icon="Search" type="primary" @click="load">查询</el-button>
            <el-button icon="refresh-left" @click="reset">重置</el-button>
            <div style="float: right">
              <el-statistic title="未缴费金额" :value="unpaidTotal" :precision="2" style="margin-right: 30px">
                <template #suffix>元</template>
              </el-statistic>
              <el-statistic title="已缴费金额" :value="paidTotal" :precision="2">
                <template #suffix>元</template>
              </el-statistic>
            </div>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="520" style="width: 100%">
          <el-table-column label="#" type="index" width="60"/>
          <el-table-column label="楼栋号" prop="dormBuildId" width="100"/>
          <el-table-column label="房间号" prop="dormRoomId" width="100"/>
          <el-table-column label="计费月份" prop="billingMonth" width="120"/>
          <el-table-column label="用水量(吨)" prop="waterUsage" width="110"/>
          <el-table-column label="水费(元)" prop="waterAmount" width="100">
            <template #default="scope">
              <span style="color: #409EFF; font-weight: bold;">{{ scope.row.waterAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用电量(度)" prop="electricityUsage" width="110"/>
          <el-table-column label="电费(元)" prop="electricityAmount" width="100">
            <template #default="scope">
              <span style="color: #E6A23C; font-weight: bold;">{{ scope.row.electricityAmount }}</span>
            </template>
          </el-table-column>
          <el-table-column label="总金额(元)" prop="totalAmount" width="120">
            <template #default="scope">
              <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'danger'" size="large">{{ scope.row.totalAmount }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="缴费状态" prop="paymentStatus" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'warning'">{{ scope.row.paymentStatus }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="缴费时间" prop="paymentTime" width="180"/>
          <el-table-column label="操作" width="200">
            <template #default="scope">
              <el-button icon="View" type="primary" size="small" @click="showDetail(scope.row)">详情</el-button>
              <el-button v-if="scope.row.paymentStatus === '未缴费'" icon="Bell" type="warning" size="small" @click="sendReminder(scope.row)">催缴</el-button>
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
        
        <el-dialog v-model="detailDialog" title="账单详情" width="45%">
          <el-card>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="楼栋号">{{ detail.dormBuildId }}</el-descriptions-item>
              <el-descriptions-item label="房间号">{{ detail.dormRoomId }}</el-descriptions-item>
              <el-descriptions-item label="计费月份">{{ detail.billingMonth }}</el-descriptions-item>
              <el-descriptions-item label="缴费状态">
                <el-tag :type="detail.paymentStatus === '已缴费' ? 'success' : 'warning'">{{ detail.paymentStatus }}</el-tag>
              </el-descriptions-item>
            </el-descriptions>
            <el-divider>用量详情</el-divider>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="用水量(吨)">{{ detail.waterUsage }}</el-descriptions-item>
              <el-descriptions-item label="水费单价(元/吨)">{{ detail.waterPrice }}</el-descriptions-item>
              <el-descriptions-item label="用电量(度)">{{ detail.electricityUsage }}</el-descriptions-item>
              <el-descriptions-item label="电费单价(元/度)">{{ detail.electricityPrice }}</el-descriptions-item>
            </el-descriptions>
            <el-divider>费用明细</el-divider>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="水费金额">
                <span style="color: #409EFF; font-size: 18px; font-weight: bold;">{{ detail.waterAmount }} 元</span>
              </el-descriptions-item>
              <el-descriptions-item label="电费金额">
                <span style="color: #E6A23C; font-size: 18px; font-weight: bold;">{{ detail.electricityAmount }} 元</span>
              </el-descriptions-item>
              <el-descriptions-item label="总金额">
                <el-tag type="danger" size="large">{{ detail.totalAmount }} 元</el-tag>
              </el-descriptions-item>
            </el-descriptions>
            <el-divider v-if="detail.paymentStatus === '已缴费'">缴费信息</el-divider>
            <el-descriptions v-if="detail.paymentStatus === '已缴费'" :column="2" border>
              <el-descriptions-item label="缴费时间">{{ detail.paymentTime }}</el-descriptions-item>
              <el-descriptions-item label="缴费人ID">{{ detail.payerId }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
          <template #footer>
            <span class="dialog-footer">
              <el-button type="primary" @click="detailDialog = false">确 定</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/UtilityBillManage.js"></script>
