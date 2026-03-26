<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>学生信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <!--    功能区-->
        <div style="margin: 10px 0">
          <!--    搜索区-->
          <div style="margin: 10px 0">
            <el-input v-model="search" clearable placeholder="请输入姓名" prefix-icon="Search"
                      style="width: 20%"/>
            <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
            <el-button icon="refresh-left" style="margin-left: 10px" type="default" @click="reset"></el-button>
            <div style="float: right" v-if="this.identity === 'admin'">
              <el-tooltip content="添加" placement="top">
                <el-button icon="plus" style="width: 50px" type="primary" @click="add"></el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <!--    表格-->
        <el-table v-loading="loading" :data="tableData" border max-height="705" style="width: 100%">
          <el-table-column label="#" type="index"/>
          <el-table-column label="学号" prop="username" sortable/>
          <el-table-column label="姓名" prop="name"/>
          <el-table-column label="学院" prop="college"/>
          <el-table-column label="班级" prop="className"/>
          <el-table-column label="宿舍楼号" prop="dormBuildId"/>
          <el-table-column label="房间号" prop="dormRoomId"/>
          <el-table-column
              :filter-method="filterTag"
              :filters="[
              { text: '男', value: '男' },
              { text: '女', value: '女' },
            ]"
              filter-placement="bottom-end"
              label="性别"
              prop="gender"
          />
          <el-table-column label="年龄" prop="age" sortable/>
          <el-table-column label="学生类型" prop="studentType" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.studentType === '新生' ? 'success' : ''" size="small">
                {{ scope.row.studentType }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="报到状态" prop="registrationStatus" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.registrationStatus === '已报到' ? 'success' : 'warning'" size="small">
                {{ scope.row.registrationStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="住宿状态" prop="accommodationStatus" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.accommodationStatus === '已分配' ? 'success' : 'info'" size="small">
                {{ scope.row.accommodationStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="入学年份" prop="admissionYear" width="100"/>
          <el-table-column label="手机号" prop="phoneNum" v-if="this.identity !== 'admin'"/>
          <el-table-column :show-overflow-tooltip="true" label="邮箱" prop="email" v-if="this.identity !== 'admin'"/>
          <!--      操作栏-->
          <el-table-column label="操作" width="130px" v-if="this.identity === 'admin'">
            <template #default="scope">
              <el-button icon="Edit" type="primary" @click="handleEdit(scope.row)"></el-button>
              <el-popconfirm title="确认删除？" @confirm="handleDelete(scope.row.username)">
                <template #reference>
                  <el-button icon="Delete" type="danger"></el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="130px" v-if="this.identity === 'dormManager'">
            <template #default="scope">
              <el-button icon="Edit" type="primary" @click="handleEdit(scope.row)"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <!--分页-->
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
          <!--      弹窗-系统管理员编辑-->
          <el-dialog v-model="dialogVisible" title="操作" width="30%" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
              <el-form-item label="学号" prop="username">
                <el-input v-model="form.username" :disabled="judgeAddOrEdit" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="form.password" :disabled="disabled" :show-password="showpassword"
                          style="width: 80%"></el-input>
                <el-tooltip content="修改密码" placement="right">
                  <el-icon :style="editDisplay" size="large" style="margin-left: 5px; cursor: pointer"
                           @click="EditPass">
                    <edit/>
                  </el-icon>
                </el-tooltip>
              </el-form-item>
              <el-form-item :style="display" label="确认密码" prop="checkPass">
                <el-input v-model="form.checkPass" :show-password="showpassword" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="姓名" prop="name">
                <el-input v-model="form.name" style="width: 80%"></el-input>
              </el-form-item>
              <el-form-item label="年龄" prop="age">
                <el-input v-model.number="form.age" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="性别" prop="gender">
                <el-radio v-model="form.gender" label="男">男</el-radio>
                <el-radio v-model="form.gender" label="女">女</el-radio>
              </el-form-item>
              <el-form-item label="手机号" prop="phoneNum">
                <el-input v-model.number="form.phoneNum" style="width: 80%"
                ></el-input>
              </el-form-item>
              <el-form-item label="邮箱地址" prop="email">
                <el-input v-model="form.email" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="学院" prop="college">
                <el-input v-model="form.college" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="班级" prop="className">
                <el-input v-model="form.className" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="宿舍楼号" prop="dormBuildId">
                <el-input-number v-model="form.dormBuildId" :min="1" :disabled="this.identity === 'admin'" style="width: 80%"></el-input-number>
            </el-form-item>
            <el-form-item label="房间号" prop="dormRoomId">
                <el-input-number v-model="form.dormRoomId" :min="1" :disabled="this.identity === 'admin'" style="width: 80%"></el-input-number>
            </el-form-item>
            <el-form-item label="学生类型" prop="studentType" v-if="this.identity === 'admin'">
                <el-select v-model="form.studentType" style="width: 80%">
                    <el-option label="新生" value="新生"></el-option>
                    <el-option label="老生" value="老生"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="报到状态" prop="registrationStatus" v-if="this.identity === 'admin'">
                <el-select v-model="form.registrationStatus" style="width: 80%">
                    <el-option label="未报到" value="未报到"></el-option>
                    <el-option label="已报到" value="已报到"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="住宿状态" prop="accommodationStatus" v-if="this.identity === 'admin'">
                <el-select v-model="form.accommodationStatus" style="width: 80%">
                    <el-option label="未分配" value="未分配"></el-option>
                    <el-option label="已分配" value="已分配"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="入学年份" prop="admissionYear" v-if="this.identity === 'admin'">
                <el-input-number v-model="form.admissionYear" :min="2020" :max="2030" style="width: 80%"></el-input-number>
            </el-form-item>
            <el-form-item label="预先分配楼栋" prop="assignedDormBuildId" v-if="this.identity === 'admin'">
                <el-input-number v-model="form.assignedDormBuildId" :min="1" style="width: 80%" placeholder="不填则不预先分配"></el-input-number>
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
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/StuInfo.js"></script>