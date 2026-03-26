<template>
  <div class="page-container">
    <el-breadcrumb separator-icon="ArrowRight" class="breadcrumb">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>申请管理</el-breadcrumb-item>
      <el-breadcrumb-item>调宿申请</el-breadcrumb-item>
    </el-breadcrumb>
    
    <el-card class="main-card" shadow="never">
      <div class="card-header">
        <h2 class="page-title">调宿申请记录</h2>
      </div>
      
      <div class="action-bar">
        <div class="search-group">
          <el-input 
            v-model="search" 
            clearable 
            placeholder="请输入学号搜索" 
            prefix-icon="Search" 
            class="search-input"
          />
          <el-button type="primary" class="search-btn" @click="load">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
        </div>
        <el-button type="primary" class="add-btn" @click="add">
          <el-icon><Plus /></el-icon>
          新增申请
        </el-button>
      </div>
      
      <div class="table-container">
        <el-table 
          v-loading="loading" 
          :data="tableData" 
          stripe 
          style="width: 100%"
          class="data-table"
        >
          <el-table-column type="index" label="#" width="60" align="center"/>
          <el-table-column label="学号" prop="username" sortable width="120"/>
          <el-table-column label="姓名" prop="name" width="100"/>
          <el-table-column label="当前房间号" prop="currentRoomId" sortable width="120"/>
          <el-table-column label="当前床位号" prop="currentBedId" sortable width="120"/>
          <el-table-column label="目标房间号" prop="towardsRoomId" sortable width="120"/>
          <el-table-column label="目标床位号" prop="towardsBedId" sortable width="120"/>
          <el-table-column
              :filter-method="filterTag"
              :filters="[
              { text: '未处理', value: '未处理' },
              { text: '通过', value: '通过' },
              { text: '驳回', value: '驳回' },
            ]"
              filter-placement="bottom-end"
              label="申请状态"
              prop="state"
              sortable
              width="130"
          >
            <template #default="scope">
              <el-tag :type="getStateType(scope.row.state)" size="small">
                {{ scope.row.state }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请时间" prop="applyTime" sortable width="180"/>
          <el-table-column label="处理时间" prop="finishTime" sortable width="180"/>
          <el-table-column label="操作" width="150" align="center" fixed="right">
            <template #default="scope">
              <el-tooltip content="查看详情" placement="top">
                <el-button 
                  icon="View" 
                  type="primary" 
                  size="small" 
                  circle 
                  @click="showDetail(scope.row)"
                />
              </el-tooltip>
              <el-tooltip v-if="scope.row.state==='未处理'" content="编辑" placement="top">
                <el-button 
                  icon="Edit" 
                  type="success" 
                  size="small" 
                  circle 
                  @click="handleEdit(scope.row)"
                />
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <div class="pagination-container">
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
      
      <div>
        <el-dialog v-model="dialogVisible" title="操作" width="30%" @close="cancel">
          <el-form ref="form" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="学号" prop="username">
              <el-input v-model="form.username" disabled style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" disabled style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item disabled label="当前房间号" prop="currentRoomId">
              <el-input v-model="form.currentRoomId" disabled style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="当前床位号" prop="currentBedId">
              <el-input v-model="form.currentBedId" disabled style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="申请时间" prop="applyTime" style="margin-top: 27px">
              <el-date-picker
                  v-model="form.applyTime"
                  disabled
                  clearable
                  placeholder="选择时间"
                  style="width: 50%"
                  type="datetime"
                  value-format="YYYY-MM-DD HH:mm:ss"
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
        <el-dialog v-model="detailDialog" title="学生信息" width="30%" @close="cancel">
          <el-form ref="form" :model="form" label-width="220px">
            <el-form-item label="学号：" prop="username">
              <span>{{ form.username }}</span>
            </el-form-item>
            <el-form-item label="姓名：" prop="name">
              <span>{{ form.name }}</span>
            </el-form-item>
            <el-form-item label="当前房间号：" prop="currentRoomId">
              <span>{{ form.currentRoomId }}</span>
            </el-form-item>
            <el-form-item label="当前床位号：" prop="currentBedId">
              <span>{{ form.currentBedId }}</span>
            </el-form-item>
            <el-form-item label="目标房间号：" prop="towardsRoomId">
              <span>{{ form.towardsRoomId }}</span>
            </el-form-item>
            <el-form-item label="目标床位号：" prop="towardsBedId">
              <span>{{ form.towardsBedId }}</span>
            </el-form-item>
            <el-form-item label="申请时间：" prop="applyTime">
              <span>{{ form.applyTime }}</span>
            </el-form-item>
            <el-form-item label="申请状态：" prop="state">
              <span>{{ form.state }}</span>
            </el-form-item>
            <el-form-item label="处理时间：" prop="finishTime">
              <span>{{ form.finishTime }}</span>
            </el-form-item>
          </el-form>
        </el-dialog>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Search, Plus, View, Edit, Delete } from '@element-plus/icons-vue'
import request from "@/utils/request";

const { ElMessage, ElMessageBox } = require("element-plus");

export default {
  name: "AdjustRoomInfo",
  components: { Search, Plus, View, Edit, Delete },
  data() {
    return {
      loading: true,
      dialogVisible: false,
      detailDialog: false,
      search: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      form: {},
      orderState: false,
      judgeOption: false,
      rules: {
        username: [
          { required: true, message: "请输入学号", trigger: "blur" },
          { pattern: /^[a-zA-Z0-9]{4,9}$/, message: "必须由 2 到 5 个字母或数字组成", trigger: "blur" },
        ],
        name: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          { pattern: /^(?:[\u4E00-\u9FA5·]{2,10})$/, message: "必须由 2 到 10 个汉字组成", trigger: "blur" },
        ],
        currentRoomId: [
          { required: true, message: "请输入当前房间号", trigger: "blur" },
        ],
        currentBedId: [
          { required: true, message: "请输入当前床位号", trigger: "blur" },
        ],
      },
    }
  },
  created() {
    this.load();
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
    }, 1000);
  },
  methods: {
    getStateType(state) {
      if (state === '通过') return 'success'
      if (state === '驳回') return 'danger'
      return 'info'
    },
    async load() {
      const username = JSON.parse(sessionStorage.getItem("user")).username;
      request.get("/adjustRoom/findPending/" + username, {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
        },
      }).then((res) => {
        this.tableData = res.data.records || [];
        this.total = res.data.total || 0;
        this.loading = false;
      });
    },
    filterTag(value, row) {
      return row.gender === value;
    },
    add() {
      const username = JSON.parse(sessionStorage.getItem("user")).username;
      request.get("/adjustRoom/canApply/" + username).then((res) => {
        if (res.code === "0") {
          this.dialogVisible = true;
          this.$nextTick(() => {
            this.$refs.form.resetFields();
            this.form.username = username;
            this.form.name = JSON.parse(sessionStorage.getItem("user")).name;
            request.get("/room/getMyRoom/" + this.form.username).then((res) => {
              this.form.currentRoomId = res.data.dormRoomId
              this.form.currentBedId = this.calBedNum(this.form.username, res.data)
            });
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            this.form.applyTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            this.form.state = "未处理";
            this.judgeOption = true;
          });
        } else {
          ElMessage({
            message: res.msg,
            type: "warning",
          });
        }
      }).catch((err) => {
        ElMessage({
          message: "检查申请状态失败，请重试",
          type: "error",
        });
      });
    },
    calBedNum(username, data) {
      if (data.firstBed === username) {
        return 1;
      } else if (data.secondBed === username) {
        return 2;
      } else if (data.thirdBed === username) {
        return 3;
      } else if (data.fourthBed === username) {
        return 4;
      }
    },
    judgeOrderState(state) {
      if (state === '通过') {
        this.orderState = true
      } else if (state === '驳回') {
        this.orderState = false
      } else if (state === '未处理') {
        this.orderState = false
      }
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.judgeOption === false) {
            this.judgeOrderState(this.form.state)
            request.put("/adjustRoom/update/" + this.orderState, this.form).then((res) => {
              if (res.code === "0") {
                ElMessage({
                  message: "修改成功",
                  type: "success",
                });
                this.search = "";
                this.load();
                this.dialogVisible = false;
              } else if (res.msg === "重复操作") {
                ElMessage({
                  message: res.msg,
                  type: "error",
                });
                this.search = "";
                this.load();
                this.dialogVisible = false;
              } else {
                ElMessage({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          } else if (this.judgeOption === true) {
            request.post("/adjustRoom/add", this.form).then((res) => {
              if (res.code === "0") {
                ElMessage({
                  message: "添加成功",
                  type: "success",
                });
                this.search = "";
                this.load();
                this.dialogVisible = false;
              } else {
                ElMessage({
                  message: res.msg,
                  type: "error",
                });
              }
            });
          }
        }
      });
    },
    cancel() {
      this.$refs.form.resetFields();
      this.dialogVisible = false;
      this.detailDialog = false;
    },
    showDetail(row) {
      this.detailDialog = true;
      this.$nextTick(() => {
        this.$refs.form.resetFields();
        this.form = JSON.parse(JSON.stringify(row));
      });
    },
    handleEdit(row) {
      this.dialogVisible = true;
      this.$nextTick(() => {
        this.$refs.form.resetFields();
        this.form = JSON.parse(JSON.stringify(row));
        this.judgeOption = false;
      });
    },
    handleDelete(row) {
      ElMessageBox.confirm(
        '确定要删除这条申请记录吗？',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        request.delete('/adjustRoom/delete/' + row.id).then(res => {
          if (res.code === '0') {
            ElMessage.success('删除成功');
            this.load();
          } else {
            ElMessage.error(res.msg || '删除失败');
          }
        }).catch(err => {
          ElMessage.error('网络错误，请重试');
        });
      }).catch(() => {
        ElMessage.info('已取消删除');
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum;
      this.load();
    },
  },
}
</script>

<style scoped>
.page-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
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

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 0 24px;
}

.search-group {
  display: flex;
  gap: 12px;
  align-items: center;
}

.search-input {
  width: 300px;
}

.search-btn,
.add-btn {
  border-radius: 8px;
  font-weight: 500;
  height: 40px;
  padding: 0 20px;
}

.table-container {
  padding: 0 24px;
}

.data-table {
  border-radius: 8px;
  overflow: hidden;
}

.data-table :deep(.el-table__header-wrapper) {
  background: #fafafa;
}

.data-table :deep(.el-table__header th) {
  background: #fafafa;
  color: #606266;
  font-weight: 600;
}

.pagination-container {
  padding: 24px;
  display: flex;
  justify-content: flex-end;
}

.pagination-container :deep(.el-pagination) {
  display: flex;
  align-items: center;
}
</style>
