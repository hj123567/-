<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>水电费</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div style="margin: 10px 0">
        <div style="margin: 10px 0">
          <el-select v-model="searchPaymentStatus" placeholder="缴费状态" clearable style="width: 150px; margin-right: 10px">
            <el-option label="全部" value=""></el-option>
            <el-option label="未缴费" value="未缴费"></el-option>
            <el-option label="已缴费" value="已缴费"></el-option>
          </el-select>
          <el-button icon="Search" type="primary" @click="load">查询</el-button>
          <el-button icon="refresh-left" @click="reset">重置</el-button>
        </div>
        
        <el-card style="margin-bottom: 20px">
          <div style="display: flex; justify-content: space-around; text-align: center">
            <div>
              <div style="font-size: 14px; color: #909399">待缴费金额</div>
              <div style="font-size: 24px; font-weight: bold; color: #f56c6c; margin-top: 8px">
                ¥ {{ (unpaidAmount || 0).toFixed(2) }}
              </div>
            </div>
            <div>
              <div style="font-size: 14px; color: #909399">已缴费金额</div>
              <div style="font-size: 24px; font-weight: bold; color: #67c23a; margin-top: 8px">
                ¥ {{ (paidAmount || 0).toFixed(2) }}
              </div>
            </div>
          </div>
        </el-card>
        
        <el-table v-loading="loading" :data="tableData" border max-height="550" style="width: 100%">
          <el-table-column label="#" type="index" width="60"/>
          <el-table-column label="楼栋号" prop="dormBuildId" width="100"/>
          <el-table-column label="房间号" prop="dormRoomId" width="100"/>
          <el-table-column label="计费月份" prop="billingMonth" width="120"/>
          <el-table-column label="用电量(度)" prop="electricityUsage" width="120"/>
          <el-table-column label="电费(元)" width="120">
            <template #default="scope">
              <span style="color: #f56c6c">{{ (scope.row.electricityAmount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="用水量(吨)" prop="waterUsage" width="120"/>
          <el-table-column label="水费(元)" width="120">
            <template #default="scope">
              <span style="color: #409eff">{{ (scope.row.waterAmount || 0).toFixed(2) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="总计(元)" width="120">
            <template #default="scope">
              <strong style="color: #e6a23c">{{ (scope.row.totalAmount || 0).toFixed(2) }}</strong>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="paymentStatus" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'danger'">
                {{ scope.row.paymentStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="缴费时间" prop="paymentTime" width="180">
            <template #default="scope">
              {{ scope.row.paymentTime || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button 
                v-if="scope.row.paymentStatus === '未缴费'" 
                type="primary" 
                size="small" 
                @click="payBill(scope.row)">
                <el-icon><Coin /></el-icon>
                立即缴费
              </el-button>
              <el-tag v-else type="success" size="small">
                <el-icon><CircleCheck /></el-icon>
                已缴费
              </el-tag>
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
      </div>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Coin, CircleCheck } from '@element-plus/icons-vue'

export default {
  name: 'MyUtilityBill',
  components: { Coin, CircleCheck },
  data() {
    return {
      loading: true,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      searchPaymentStatus: '',
      user: null,
      room: null,
      unpaidAmount: 0,
      paidAmount: 0
    }
  },
  created() {
    this.init()
    this.load()
  },
  methods: {
    init() {
      this.user = JSON.parse(sessionStorage.getItem('user'))
      this.getMyRoom()
    },
    getMyRoom() {
      const name = this.user.username
      request.get('/room/getMyRoom/' + name).then(res => {
        if (res.code === '0' && res.data) {
          this.room = res.data
          this.load()
        }
      })
    },
    load() {
      if (!this.room || !this.room.dormBuildId || !this.room.dormRoomId) {
        this.tableData = []
        this.loading = false
        return
      }
      
      this.loading = true
      request.get('/utilityBill/findByRoom', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          dormBuildId: this.room.dormBuildId,
          dormRoomId: this.room.dormRoomId
        }
      }).then(res => {
        if (res.code === '0' && res.data) {
          this.tableData = res.data.records || []
          this.total = res.data.total || 0
          this.calculateAmounts()
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    calculateAmounts() {
      this.unpaidAmount = 0
      this.paidAmount = 0
      this.tableData.forEach(bill => {
        if (bill.paymentStatus === '未缴费') {
          this.unpaidAmount += bill.totalAmount || 0
        } else {
          this.paidAmount += bill.totalAmount || 0
        }
      })
    },
    reset() {
      this.searchPaymentStatus = ''
      this.currentPage = 1
      this.load()
    },
    payBill(bill) {
      ElMessageBox.confirm(
        `确认缴纳 ${bill.billingMonth} 的水电费 ¥${(bill.totalAmount || 0).toFixed(2)} 元？`,
        '在线缴费',
        {
          confirmButtonText: '确认支付',
          cancelButtonText: '取消',
          type: 'info'
        }
      ).then(() => {
        request.post('/utilityBill/pay', null, {
          params: {
            billId: bill.id,
            payerId: this.user.id || 1,
            payerName: this.user.name
          }
        }).then(res => {
          if (res.code === '0') {
            ElMessage.success('缴费成功！')
            this.load()
          } else {
            ElMessage.error(res.msg || '缴费失败')
          }
        }).catch(error => {
          ElMessage.error('缴费失败，请稍后重试')
        })
      }).catch(() => {
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
</style>
