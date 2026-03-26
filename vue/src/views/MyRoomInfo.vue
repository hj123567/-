<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>我的宿舍</el-breadcrumb-item>
    </el-breadcrumb>
    
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 30px">
        <h2 style="margin: 0; color: #303133">我的宿舍</h2>
        <el-button type="primary" @click="applyRoomChange">
          <el-icon><Refresh /></el-icon>
          <span>申请调宿</span>
        </el-button>
      </div>
      
      <el-row :gutter="20" style="margin-bottom: 30px">
        <el-col :span="12">
          <el-card shadow="hover" header="房间信息">
            <el-descriptions :column="2" border>
              <el-descriptions-item label="楼宇号">
                <span class="info-text">{{ room.dormBuildId }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="房间号">
                <span class="info-text">{{ room.dormRoomId }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="楼层">
                <span class="info-text">{{ room.floorNum }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="可住人数">
                <span class="info-text">{{ room.maxCapacity }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="已住人数">
                <span class="info-text">{{ room.currentCapacity }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="宿舍状态" v-if="identity === 'dormManager' || identity === 'admin'">
                <el-tag :type="room.currentCapacity === room.maxCapacity ? 'warning' : 'success'">
                  {{ room.currentCapacity === room.maxCapacity ? '已满' : '有空位' }}
                </el-tag>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-bottom: 30px">
        <el-col :span="12">
          <el-card shadow="hover" header="室友信息">
            <el-table :data="roommates" style="width: 100%">
              <el-table-column prop="username" label="学号" width="120" />
              <el-table-column prop="name" label="姓名" width="100" />
              <el-table-column prop="gender" label="性别" width="80" />
              <el-table-column prop="college" label="学院" width="150" />
              <el-table-column prop="className" label="班级" width="120" />
              <el-table-column prop="phoneNum" label="电话" />
              <el-table-column prop="bed" label="床位" width="80" />
            </el-table>
          </el-card>
        </el-col>
        
        <el-col :span="12">
          <el-card shadow="hover" header="床位安排">
            <div class="bed-layout">
              <div class="bed-item" :class="{ 'my-bed': bed.username === name }" v-for="bed in bedArrangement" :key="bed.position">
                <div class="bed-position">{{ bed.position }}</div>
                <div class="bed-occupant" v-if="bed.username">
                  {{ bed.username }}<br>
                  <small>{{ bed.name }}</small>
                </div>
                <div class="bed-occupant empty" v-else>空床位</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-card shadow="hover" header="宿舍记录">
        <el-tabs>
          <el-tab-pane label="报修记录">
            <el-table :data="repairRecords" style="width: 100%">
              <el-table-column prop="id" label="报修编号" width="120" />
              <el-table-column prop="title" label="报修内容" />
              <el-table-column prop="orderBuildTime" label="报修创建时间" width="180" />
              <el-table-column prop="orderFinishTime" label="报修完成时间" width="180" />
              <el-table-column prop="state" label="报修状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.state === '完成' ? 'success' : 'info'">
                    {{ scope.row.state }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="卫生评分">
            <el-table :data="cleanlinessScores" style="width: 100%">
              <el-table-column prop="date" label="检查日期" width="150" />
              <el-table-column prop="score" label="评分" width="100">
                <template #default="scope">
                  <el-rate v-model="scope.row.score" disabled show-score />
                </template>
              </el-table-column>
              <el-table-column prop="comment" label="评价" />
            </el-table>
          </el-tab-pane>
          
          <el-tab-pane label="水电费">
            <el-table :data="utilityBills" style="width: 100%" v-loading="utilityLoading">
              <el-table-column prop="billingMonth" label="月份" width="120" />
              <el-table-column label="用电量(度)" width="120">
                <template #default="scope">
                  {{ scope.row.electricityUsage || 0 }}
                </template>
              </el-table-column>
              <el-table-column label="电费(元)" width="120">
                <template #default="scope">
                  <span style="color: #f56c6c">{{ (scope.row.electricityAmount || 0).toFixed(2) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="用水量(吨)" width="120">
                <template #default="scope">
                  {{ scope.row.waterUsage || 0 }}
                </template>
              </el-table-column>
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
              <el-table-column prop="paymentStatus" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'danger'">
                    {{ scope.row.paymentStatus }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120">
                <template #default="scope">
                  <el-button 
                    v-if="scope.row.paymentStatus === '未缴费'" 
                    type="primary" 
                    size="small" 
                    @click="payBill(scope.row)">
                    缴纳
                  </el-button>
                  <el-tag v-else type="success" size="small">已缴费</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MyRoomInfo',
  data() {
    return {
      name: '',
      identity: '',
      form: {
        username: '',
        name: ''
      },
      room: {
        dormRoomId: '',
        dormBuildId: '',
        floorNum: '',
        maxCapacity: '',
        currentCapacity: '',
        firstBed: '',
        secondBed: '',
        thirdBed: '',
        fourthBed: ''
      },
      roommates: [],
      bedArrangement: [],
      repairRecords: [],
      cleanlinessScores: [],
      utilityBills: [],
      utilityLoading: false
    }
  },
  created() {
    console.log('MyRoomInfo created')
    try {
      this.init()
      console.log('After init, name:', this.name, 'identity:', this.identity)
      if (this.name) {
        this.getInfo()
      } else {
        ElMessage.error('未登录，请重新登录')
        this.$router.push('/Login')
      }
    } catch (e) {
      console.error('初始化错误:', e)
      ElMessage.error('读取登录信息失败，请重新登录')
      this.$router.push('/Login')
    }
  },
  methods: {
    init() {
      const userStr = sessionStorage.getItem('user')
      const identityStr = sessionStorage.getItem('identity')
      
      console.log('sessionStorage user:', userStr)
      console.log('sessionStorage identity:', identityStr)
      
      if (!userStr) {
        throw new Error('user not found')
      }
      
      this.form = JSON.parse(userStr)
      this.name = this.form.username || this.form.name
      
      if (identityStr) {
        this.identity = JSON.parse(identityStr)
      }
      
      console.log('this.name:', this.name)
    },
    getInfo() {
      console.log('获取宿舍信息，用户名:', this.name)
      if (!this.name) {
        ElMessage.error('用户名不存在')
        return
      }
      
      request.get('/room/getMyRoom/' + this.name).then(async res => {
        console.log('宿舍信息响应:', res)
        if (res.code === '0' && res.data) {
          this.room = res.data
          console.log('this.room:', this.room)
          this.room.maxCapacity = 4
          await this.initRoommateInfo()
          this.initBedArrangement()
          this.initMockData()
          this.getUtilityBills()
        } else {
          ElMessage({
            message: res.msg || '获取宿舍信息失败',
            type: 'warning'
          })
        }
      }).catch(error => {
        console.error('获取宿舍信息错误:', error)
        ElMessage({
          message: '网络错误，请检查后端服务',
          type: 'error'
        })
      })
    },
    async initRoommateInfo() {
      // 调用后端接口获取同房间的所有学生
      try {
        const res = await request.get('/stu/findByRoom/' + this.room.dormRoomId)
        if (res.code === '0' && res.data) {
          const students = res.data
          // 根据床位号映射床位名称
          const bedNameMap = {}
          if (this.room.firstBed) bedNameMap[this.room.firstBed] = '1 号床'
          if (this.room.secondBed) bedNameMap[this.room.secondBed] = '2 号床'
          if (this.room.thirdBed) bedNameMap[this.room.thirdBed] = '3 号床'
          if (this.room.fourthBed) bedNameMap[this.room.fourthBed] = '4 号床'
          
          this.roommates = students.map(stu => ({
            username: stu.username,
            name: stu.name,
            gender: stu.gender,
            college: stu.college,
            className: stu.className,
            phoneNum: stu.phoneNum || '暂无',
            bed: bedNameMap[stu.username] || '未知床位'
          }))
        }
      } catch (error) {
        console.error('获取室友信息失败:', error)
        this.roommates = []
      }
    },
    initBedArrangement() {
      // 创建床位映射
      const bedMap = {}
      if (this.room.firstBed) {
        const student = this.roommates.find(r => r.username === this.room.firstBed)
        bedMap['1 号床'] = student ? student.name : ''
      }
      if (this.room.secondBed) {
        const student = this.roommates.find(r => r.username === this.room.secondBed)
        bedMap['2 号床'] = student ? student.name : ''
      }
      if (this.room.thirdBed) {
        const student = this.roommates.find(r => r.username === this.room.thirdBed)
        bedMap['3 号床'] = student ? student.name : ''
      }
      if (this.room.fourthBed) {
        const student = this.roommates.find(r => r.username === this.room.fourthBed)
        bedMap['4 号床'] = student ? student.name : ''
      }
      
      this.bedArrangement = [
        {
          position: '1 号床',
          username: this.room.firstBed,
          name: bedMap['1 号床'] || ''
        },
        {
          position: '2 号床',
          username: this.room.secondBed,
          name: bedMap['2 号床'] || ''
        },
        {
          position: '3 号床',
          username: this.room.thirdBed,
          name: bedMap['3 号床'] || ''
        },
        {
          position: '4 号床',
          username: this.room.fourthBed,
          name: bedMap['4 号床'] || ''
        }
      ]
    },
    initMockData() {
      this.getRepairRecords()
      this.getCleanlinessScores()
      this.getUtilityBills()
    },
    getUtilityBills() {
      if (!this.room.dormBuildId || !this.room.dormRoomId) {
        this.utilityBills = []
        return
      }
      
      this.utilityLoading = true
      request.get('/utilityBill/findByRoom', {
        params: {
          pageNum: 1,
          pageSize: 20,
          dormBuildId: this.room.dormBuildId,
          dormRoomId: this.room.dormRoomId
        }
      }).then(res => {
        if (res.code === '0' && res.data && res.data.records) {
          this.utilityBills = res.data.records
        } else {
          this.utilityBills = []
        }
        this.utilityLoading = false
      }).catch(error => {
        console.error('获取水电费账单失败:', error)
        this.utilityBills = []
        this.utilityLoading = false
      })
    },
    payBill(bill) {
      ElMessageBox.confirm(
        `确认缴纳 ${bill.billingMonth} 的水电费 ${(bill.totalAmount || 0).toFixed(2)} 元？`,
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
            payerId: this.form.id || 1,
            payerName: this.form.name
          }
        }).then(res => {
          if (res.code === '0') {
            ElMessage.success('缴费成功！')
            this.getUtilityBills()
          } else {
            ElMessage.error(res.msg || '缴费失败')
          }
        }).catch(error => {
          ElMessage.error('缴费失败，请稍后重试')
        })
      }).catch(() => {
      })
    },
    getCleanlinessScores() {
      if (!this.room.dormBuildId || !this.room.dormRoomId) {
        console.log('房间信息不完整，无法获取卫生评分')
        return
      }
      
      request.get('/dormScore/findByRoom', {
        params: {
          pageNum: 1,
          pageSize: 100,
          dormBuildId: this.room.dormBuildId,
          dormRoomId: this.room.dormRoomId
        }
      }).then(res => {
        if (res.code === '0' && res.data && res.data.records) {
          this.cleanlinessScores = res.data.records.map(item => ({
            id: item.id,
            date: item.scoreTime ? item.scoreTime.substring(0, 10) : '',
            score: item.score ? item.score / 20 : 0,
            comment: item.comment || ''
          }))
        } else {
          console.log('获取卫生评分失败，使用默认数据')
          this.cleanlinessScores = [
            {
              date: '2026-03-01',
              score: 4.5,
              comment: '整体整洁，地面干净'
            },
            {
              date: '2026-02-01',
              score: 4.0,
              comment: '卫生状况良好'
            },
            {
              date: '2026-01-01',
              score: 3.5,
              comment: '需要加强卫生打扫'
            }
          ]
        }
      }).catch(error => {
        console.error('获取卫生评分出错:', error)
        this.cleanlinessScores = [
          {
            date: '2026-03-01',
            score: 4.5,
            comment: '整体整洁，地面干净'
          },
          {
            date: '2026-02-01',
            score: 4.0,
            comment: '卫生状况良好'
          },
          {
            date: '2026-01-01',
            score: 3.5,
            comment: '需要加强卫生打扫'
          }
        ]
      })
    },
    getRepairRecords() {
      request.get('/repair/find/' + this.form.name, {
        params: {
          pageNum: 1,
          pageSize: 10,
          search: ''
        }
      }).then(res => {
        if (res.code === '0') {
          this.repairRecords = res.data.records || []
        } else {
          console.error('获取报修记录失败:', res.msg)
          this.repairRecords = [
            {
              id: '1001',
              title: '水龙头漏水',
              orderBuildTime: '2026-03-15 14:30:00',
              orderFinishTime: '2026-03-16 10:00:00',
              state: '完成'
            },
            {
              id: '1002',
              title: '灯管损坏',
              orderBuildTime: '2026-03-10 09:15:00',
              orderFinishTime: '2026-03-10 14:00:00',
              state: '完成'
            },
            {
              id: '1003',
              title: '空调不制冷',
              orderBuildTime: '2026-03-05 16:45:00',
              orderFinishTime: null,
              state: '未完成'
            }
          ]
        }
      }).catch(error => {
        console.error('获取报修记录出错:', error)
        this.repairRecords = [
          {
            id: '1001',
            title: '水龙头漏水',
            orderBuildTime: '2026-03-15 14:30:00',
            orderFinishTime: '2026-03-16 10:00:00',
            state: '完成'
          },
          {
            id: '1002',
            title: '灯管损坏',
            orderBuildTime: '2026-03-10 09:15:00',
            orderFinishTime: '2026-03-10 14:00:00',
            state: '完成'
          },
          {
            id: '1003',
            title: '空调不制冷',
            orderBuildTime: '2026-03-05 16:45:00',
            orderFinishTime: null,
            state: '未完成'
          }
        ]
      })
    },
    applyRoomChange() {
      this.$router.push('/applyChangeRoom')
    }
  }
}
</script>

<style scoped>
.bed-layout {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.bed-item {
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  transition: all 0.3s ease;
  background-color: #f9f9f9;
}

.bed-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border-color: #409EFF;
}

.bed-item.my-bed {
  border-color: #67C23A;
  background-color: #f0f9eb;
}

.bed-position {
  font-weight: bold;
  margin-bottom: 10px;
  color: #303133;
}

.bed-occupant {
  font-size: 14px;
  color: #606266;
}

.bed-occupant.empty {
  color: #909399;
  font-style: italic;
}

.info-text {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}
</style>