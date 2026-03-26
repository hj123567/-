<template>
  <div class="page-container">
    <el-breadcrumb separator-icon="ArrowRight" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍管理</el-breadcrumb-item>
      <el-breadcrumb-item>新生宿舍分配</el-breadcrumb-item>
    </el-breadcrumb>
    
    <el-card class="main-card" shadow="never">
      <div class="card-header">
        <h2 class="page-title">新生宿舍分配</h2>
        <div class="page-desc">
          用于新生入学时分配宿舍，老生搬离后可直接为新生分配
        </div>
      </div>
      
      <div class="content-grid">
        <div class="left-panel">
          <div class="panel-header">
            <h3 class="panel-title">选择新生</h3>
            <div class="search-box">
              <el-select v-model="filterStudentType" placeholder="学生类型" size="small" clearable style="width: 120px;">
                <el-option label="全部" value=""></el-option>
                <el-option label="新生" value="新生"></el-option>
                <el-option label="老生" value="老生"></el-option>
              </el-select>
              <el-select v-model="filterAccommodationStatus" placeholder="住宿状态" size="small" clearable style="width: 120px;">
                <el-option label="全部" value=""></el-option>
                <el-option label="未分配" value="未分配"></el-option>
                <el-option label="已分配" value="已分配"></el-option>
              </el-select>
              <el-input 
                v-model="search" 
                clearable 
                placeholder="请输入学号或姓名搜索" 
                prefix-icon="Search" 
                size="small"
              />
              <el-button type="primary" size="small" @click="loadStudents">
                <el-icon><Search /></el-icon>
                搜索
              </el-button>
            </div>
          </div>
          
          <div class="student-list">
            <el-table 
              v-loading="studentLoading" 
              :data="studentList" 
              stripe 
              height="500"
              @row-click="selectStudent"
              :highlight-current-row="true"
              :row-class-name="tableRowClassName"
            >
              <el-table-column type="index" label="#" width="50" align="center"/>
              <el-table-column label="学号" prop="username" width="110"/>
              <el-table-column label="姓名" prop="name" width="90"/>
              <el-table-column label="性别" prop="gender" width="70">
                <template #default="scope">
                  <el-tag :type="scope.row.gender === '男' ? 'primary' : 'danger'" size="small">
                    {{ scope.row.gender }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="学院" prop="college" min-width="100" show-overflow-tooltip/>
              <el-table-column label="班级" prop="className" width="90"/>
              <el-table-column label="学生类型" prop="studentType" width="80">
                <template #default="scope">
                  <el-tag :type="scope.row.studentType === '新生' ? 'success' : ''" size="small">
                    {{ scope.row.studentType || '老生' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="报到状态" prop="registrationStatus" width="80">
                <template #default="scope">
                  <el-tag :type="scope.row.registrationStatus === '已报到' ? 'success' : 'warning'" size="small">
                    {{ scope.row.registrationStatus || '已报到' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="住宿状态" prop="accommodationStatus" width="80">
                <template #default="scope">
                  <el-tag :type="scope.row.accommodationStatus === '已分配' ? 'success' : 'info'" size="small">
                    {{ scope.row.accommodationStatus || '未分配' }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <div class="pagination-box">
            <el-pagination
                v-model:currentPage="currentPage"
                :page-size="pageSize"
                :page-sizes="[10, 20, 50]"
                :total="total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
          </div>
        </div>
        
        <div class="right-panel">
          <div class="selected-student-info" v-if="selectedStudent">
            <div class="panel-header">
              <h3 class="panel-title">新生信息</h3>
            </div>
            <div class="info-grid">
              <div class="info-item">
                <span class="info-label">学号：</span>
                <span class="info-value">{{ selectedStudent.username }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">姓名：</span>
                <span class="info-value">{{ selectedStudent.name }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">性别：</span>
                <span class="info-value">
                  <el-tag :type="selectedStudent.gender === '男' ? 'primary' : 'danger'" size="small">
                    {{ selectedStudent.gender }}
                  </el-tag>
                </span>
              </div>
              <div class="info-item">
                <span class="info-label">学院：</span>
                <span class="info-value">{{ selectedStudent.college }}</span>
              </div>
              <div class="info-item">
                <span class="info-label">班级：</span>
                <span class="info-value">{{ selectedStudent.className }}</span>
              </div>
            </div>
          </div>
          
          <div class="recommend-section" v-if="selectedStudent && !selectedStudent.dormRoomId">
            <div class="panel-header">
              <h3 class="panel-title">智能推荐</h3>
              <el-button type="primary" size="small" @click="loadRecommendRooms">
                <el-icon><Refresh /></el-icon>
                刷新推荐
              </el-button>
            </div>
            
            <div class="recommend-tips" v-if="recommendTips">
              <el-alert :title="recommendTips" type="info" :closable="false" show-icon />
            </div>
            
            <div class="room-list" v-loading="roomLoading">
              <el-empty v-if="recommendRooms.length === 0" description="暂无推荐房间" />
              <el-table 
                v-else
                :data="recommendRooms" 
                stripe 
                height="250"
                @row-click="selectRoom"
                :highlight-current-row="true"
              >
                <el-table-column type="index" label="#" width="50" align="center"/>
                <el-table-column label="楼栋" prop="dormBuildId" width="70"/>
                <el-table-column label="房间" prop="dormRoomId" width="70"/>
                <el-table-column label="楼层" prop="floorNum" width="70"/>
                <el-table-column label="已住" prop="currentCapacity" width="60" align="center"/>
                <el-table-column label="可住" prop="maxCapacity" width="60" align="center"/>
                <el-table-column label="空床位" width="130">
                  <template #default="scope">
                    <el-tag 
                      v-for="bed in scope.row.availableBeds" 
                      :key="bed" 
                      size="small"
                      style="margin-right: 5px; margin-bottom: 5px;"
                    >
                      {{ bed }}号床
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
          
          <div class="allocate-section" v-if="selectedRoom && selectedStudent && !selectedStudent.dormRoomId">
            <div class="panel-header">
              <h3 class="panel-title">分配宿舍</h3>
            </div>
            
            <div class="room-info">
              <div class="info-row">
                <span class="info-label">选择楼栋：</span>
                <span class="info-value">{{ selectedRoom.dormBuildId }}号楼</span>
              </div>
              <div class="info-row">
                <span class="info-label">选择房间：</span>
                <span class="info-value">{{ selectedRoom.dormRoomId }}室</span>
              </div>
              <div class="info-row">
                <span class="info-label">选择床位：</span>
                <div class="bed-selector">
                  <el-radio-group v-model="selectedBed" size="default">
                    <el-radio-button 
                      v-for="bed in selectedRoom.availableBeds" 
                      :key="bed"
                      :value="bed"
                    >
                      {{ bed }}号床
                    </el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </div>
            
            <div class="allocate-actions">
              <el-button type="primary" size="large" :disabled="!selectedBed" @click="allocateDorm">
                <el-icon><Check /></el-icon>
                分配宿舍
              </el-button>
              <el-button size="large" @click="clearSelection">清除选择</el-button>
            </div>
          </div>
          
          <div class="success-section" v-if="allocateSuccess">
            <el-result icon="success" title="分配成功">
              <template #sub-title>
                <div class="success-info">
                  <p>请告知新生以下入住信息：</p>
                  <div class="success-details">
                    <div class="detail-item">
                      <span class="detail-label">楼栋号：</span>
                      <span class="detail-value">{{ successInfo.dormBuildId }}号楼</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">房间号：</span>
                      <span class="detail-value">{{ successInfo.dormRoomId }}室</span>
                    </div>
                    <div class="detail-item">
                      <span class="detail-label">床位号：</span>
                      <span class="detail-value">{{ successInfo.bedNum }}号床</span>
                    </div>
                  </div>
                </div>
              </template>
              <template #extra>
                <el-button type="primary" @click="handleSuccessConfirm">完成</el-button>
              </template>
            </el-result>
          </div>
          
          <div class="empty-state" v-if="!selectedStudent">
            <el-empty description="请在左侧选择需要分配宿舍的新生" />
          </div>
          
          <div class="already-allocated" v-if="selectedStudent && selectedStudent.dormRoomId">
            <el-result icon="info" title="该新生已分配宿舍">
              <template #sub-title>
                <div class="allocated-info">
                  <div class="allocated-item">
                    <span class="allocated-label">楼栋号：</span>
                    <span class="allocated-value">{{ selectedStudent.dormBuildId }}号楼</span>
                  </div>
                  <div class="allocated-item">
                    <span class="allocated-label">房间号：</span>
                    <span class="allocated-value">{{ selectedStudent.dormRoomId }}室</span>
                  </div>
                </div>
              </template>
            </el-result>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Search, Refresh, Check } from '@element-plus/icons-vue'
import request from "@/utils/request";
import { ElMessage, ElMessageBox } from "element-plus";

export default {
  name: "DormAssign",
  components: { Search, Refresh, Check },
  data() {
    return {
      studentLoading: true,
      roomLoading: false,
      search: "",
      filterStudentType: "",
      filterAccommodationStatus: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      studentList: [],
      selectedStudent: null,
      selectedRoom: null,
      selectedBed: null,
      recommendRooms: [],
      recommendTips: "",
      allocateSuccess: false,
      successInfo: {}
    }
  },
  created() {
    this.loadStudents();
  },
  methods: {
    tableRowClassName({ row }) {
      if (this.selectedStudent && row.username === this.selectedStudent.username) {
        return 'current-row';
      }
      return '';
    },
    async loadStudents() {
      this.studentLoading = true;
      request.get("/stu/find", {
        params: {
          pageNum: 1,
          pageSize: 1000,
          search: this.search,
        },
      }).then((res) => {
        let allStudents = res.data.records || [];
        
        allStudents = allStudents.filter(student => {
          if (this.filterStudentType && this.filterStudentType !== '') {
            let studentType = student.studentType || '老生';
            if (studentType !== this.filterStudentType) {
              return false;
            }
          }
          if (this.filterAccommodationStatus && this.filterAccommodationStatus !== '') {
            let accommodationStatus = student.accommodationStatus;
            if (!accommodationStatus) {
              accommodationStatus = student.dormRoomId ? '已分配' : '未分配';
            }
            if (accommodationStatus !== this.filterAccommodationStatus) {
              return false;
            }
          }
          return true;
        });
        
        const startIndex = (this.currentPage - 1) * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.studentList = allStudents.slice(startIndex, endIndex);
        this.total = allStudents.length;
        this.studentLoading = false;
      }).catch(() => {
        this.studentLoading = false;
      });
    },
    selectStudent(row) {
      this.selectedStudent = row;
      this.selectedRoom = null;
      this.selectedBed = null;
      this.allocateSuccess = false;
      
      if (!row.dormRoomId) {
        this.loadRecommendRooms();
      }
    },
    async loadRecommendRooms() {
      if (!this.selectedStudent) return;
      
      this.roomLoading = true;
      this.recommendRooms = [];
      this.recommendTips = "";
      
      try {
        let targetDormBuildId = this.selectedStudent.assignedDormBuildId;
        
        let genderTip = this.selectedStudent.gender === '男' ? '男生（单数楼栋）' : '女生（双数楼栋）';
        if (targetDormBuildId) {
          this.recommendTips = `根据预先分配的${targetDormBuildId}号楼、${genderTip}、${this.selectedStudent.college}、${this.selectedStudent.className} 智能推荐房间`;
        } else {
          this.recommendTips = `根据 ${genderTip}、${this.selectedStudent.college}、${this.selectedStudent.className} 智能推荐房间`;
        }
        
        let allRooms = [];
        
        if (targetDormBuildId) {
          const res = await request.get("/room/getRoomsWithEmptyBeds", {
            params: {
              dormBuildId: targetDormBuildId
            }
          });
          if (res.code === "0" && res.data) {
            allRooms = res.data || [];
          }
        } else {
          for (let buildId = 1; buildId <= 4; buildId++) {
            const res = await request.get("/room/getRoomsWithEmptyBeds", {
              params: {
                dormBuildId: buildId
              }
            });
            if (res.code === "0" && res.data) {
              allRooms = allRooms.concat(res.data || []);
            }
          }
        }
        
        if (allRooms.length > 0) {
          let rooms = allRooms.filter(room => {
            if (this.selectedStudent.gender === '男') {
              return room.dormBuildId % 2 === 1;
            } else {
              return room.dormBuildId % 2 === 0;
            }
          });
          
          rooms.sort((a, b) => {
            if (a.currentCapacity !== b.currentCapacity) {
              return a.currentCapacity - b.currentCapacity;
            }
            if (a.dormBuildId !== b.dormBuildId) {
              return a.dormBuildId - b.dormBuildId;
            }
            return a.dormRoomId - b.dormRoomId;
          });
          
          this.recommendRooms = rooms.slice(0, 20);
        }
      } catch (error) {
        console.error('加载推荐房间失败:', error);
        ElMessage.error('加载推荐房间失败');
      } finally {
        this.roomLoading = false;
      }
    },
    selectRoom(row) {
      this.selectedRoom = row;
      this.selectedBed = row.availableBeds && row.availableBeds.length > 0 ? row.availableBeds[0] : null;
    },
    async allocateDorm() {
      if (!this.selectedStudent || !this.selectedRoom || !this.selectedBed) {
        ElMessage.warning('请选择学生、房间和床位');
        return;
      }
      
      try {
        await ElMessageBox.confirm(
          `确定要为 ${this.selectedStudent.name} 分配 ${this.selectedRoom.dormBuildId}号楼 ${this.selectedRoom.dormRoomId}室 ${this.selectedBed}号床吗？`,
          '确认分配',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        );
        
        const res = await request.put("/stu/updateWithBed", null, {
          params: {
            username: this.selectedStudent.username,
            dormBuildId: this.selectedRoom.dormBuildId,
            dormRoomId: this.selectedRoom.dormRoomId,
            bedNum: this.selectedBed
          }
        });
        
        if (res.code === "0") {
          this.allocateSuccess = true;
          this.successInfo = {
            dormBuildId: this.selectedRoom.dormBuildId,
            dormRoomId: this.selectedRoom.dormRoomId,
            bedNum: this.selectedBed
          };
          
          this.loadStudents();
        } else {
          ElMessage.error(res.msg || '分配失败');
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('分配失败:', error);
          ElMessage.error('分配失败，请重试');
        }
      }
    },
    handleSuccessConfirm() {
      this.allocateSuccess = false;
      this.selectedStudent = null;
      this.selectedRoom = null;
      this.selectedBed = null;
    },
    clearSelection() {
      this.selectedRoom = null;
      this.selectedBed = null;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.loadStudents();
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum;
      this.loadStudents();
    }
  }
}
</script>

<style scoped>
.page-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
  box-sizing: border-box;
}

.breadcrumb {
  margin-bottom: 20px;
  font-size: 14px;
}

.main-card {
  border-radius: 12px;
  border: none;
  overflow: hidden;
}

.card-header {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.page-desc {
  margin: 8px 0 0 0;
  font-size: 14px;
  color: #909399;
}

.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  padding: 0 24px 24px 24px;
}

.left-panel,
.right-panel {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
  max-height: calc(100vh - 200px);
}

.right-panel {
  overflow-y: auto;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
  background: #fafafa;
}

.panel-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.search-box {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.student-list {
  padding: 16px;
  flex: 1;
  overflow: auto;
}

.pagination-box {
  padding: 16px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
}

.selected-student-info,
.recommend-section,
.allocate-section,
.success-section,
.empty-state,
.already-allocated {
  padding: 16px;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 13px;
}

.info-label {
  font-weight: 500;
  color: #606266;
  min-width: 55px;
}

.info-value {
  color: #303133;
}

.recommend-tips {
  margin: 16px 0;
}

.room-list {
  margin-top: 16px;
}

.room-info {
  margin: 16px 0;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.bed-selector {
  flex: 1;
}

.allocate-actions {
  margin-top: 24px;
  display: flex;
  gap: 12px;
}

.success-info {
  text-align: left;
}

.success-info p {
  margin: 0 0 16px 0;
  font-size: 14px;
}

.success-details {
  background: #f0f9eb;
  padding: 16px;
  border-radius: 8px;
}

.detail-item {
  display: flex;
  margin-bottom: 8px;
}

.detail-item:last-child {
  margin-bottom: 0;
}

.detail-label {
  font-weight: 500;
  color: #67c23a;
  min-width: 70px;
}

.detail-value {
  color: #67c23a;
  font-weight: 600;
}

.allocated-info {
  text-align: left;
}

.allocated-item {
  display: flex;
  margin-bottom: 8px;
}

.allocated-item:last-child {
  margin-bottom: 0;
}

.allocated-label {
  font-weight: 500;
  color: #909399;
  min-width: 70px;
}

.allocated-value {
  color: #606266;
}

:deep(.current-row) {
  background-color: #ecf5ff !important;
}
</style>
