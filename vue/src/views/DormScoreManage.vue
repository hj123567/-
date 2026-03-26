<template>
  <div>
    <el-breadcrumb separator-icon="ArrowRight" style="margin: 16px">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>宿舍管理</el-breadcrumb-item>
      <el-breadcrumb-item>卫生评分</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="margin: 15px; min-height: calc(100vh - 111px)">
      <div>
        <div style="margin: 10px 0">
          <div style="margin: 10px 0">
            <el-input v-model="search" clearable placeholder="请输入房间号" prefix-icon="Search" style="width: 20%"/>
            <el-button icon="Search" style="margin-left: 5px" type="primary" @click="load"></el-button>
            <el-button icon="refresh-left" style="margin-left: 10px" type="default" @click="reset"></el-button>
            <div style="float: right">
              <el-tooltip content="添加评分" placement="top">
                <el-button icon="plus" style="width: 50px" type="primary" @click="add"></el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <el-table v-loading="loading" :data="tableData" border max-height="705" style="width: 100%">
          <el-table-column label="#" type="index"/>
          <el-table-column label="楼栋号" prop="dormBuildId" sortable/>
          <el-table-column label="房间号" prop="dormRoomId" sortable/>
          <el-table-column label="评分">
            <template #default="scope">
              <el-tag :type="getScoreType(scope.row.score)">{{ scope.row.score }}分</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="评语" prop="comment" :show-overflow-tooltip="true"/>
          <el-table-column label="评分人" prop="scorer"/>
          <el-table-column label="评分时间" prop="scoreTime" sortable/>
          <el-table-column label="操作" width="190px">
            <template #default="scope">
              <el-button icon="View" type="primary" @click="showDetail(scope.row)">详情</el-button>
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
          <el-dialog v-model="dialogVisible" :title="judgeAddOrEdit ? '编辑评分' : '添加评分'" width="30%" @close="cancel">
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
              <el-form-item label="楼栋号" prop="dormBuildId">
                <el-input-number v-model="form.dormBuildId" :min="1" style="width: 80%" :disabled="dormBuildId !== null"></el-input-number>
              </el-form-item>
              <el-form-item label="房间号" prop="dormRoomId">
                <div style="display: flex; gap: 10px;">
                  <el-input-number v-model="form.dormRoomId" :min="1" style="width: 120px" placeholder="直接输入房间号"></el-input-number>
                  <span style="line-height: 32px;">或</span>
                  <el-select v-model="form.dormRoomId" placeholder="从列表选择" style="width: 250px" filterable>
                    <el-option
                      v-for="room in roomList"
                      :key="room.dormRoomId"
                      :label="room.dormBuildId + '-' + room.floorNum + '楼' + (room.dormRoomId % 100) + '号'"
                      :value="room.dormRoomId"
                    >
                      <span style="float: left">{{ room.dormBuildId }}栋{{ room.floorNum }}楼{{ room.dormRoomId % 100 }}号</span>
                      <span style="float: right; color: #8492a6; font-size: 13px; margin-left: 20px;">
                        {{ room.currentCapacity }}/{{ room.maxCapacity }}人
                      </span>
                    </el-option>
                  </el-select>
                </div>
              </el-form-item>
              <el-form-item label="评分" prop="score">
                <el-slider v-model="form.score" :min="0" :max="100" :marks="{0: '0', 60: '及格', 80: '良好', 100: '优秀'}"></el-slider>
                <div style="text-align: center; margin-top: 10px;">
                  <el-tag :type="getScoreType(form.score)">{{ form.score }}分</el-tag>
                </div>
              </el-form-item>
              <el-form-item label="评语" prop="comment">
                <el-input v-model="form.comment" type="textarea" :rows="3" style="width: 80%"></el-input>
              </el-form-item>
            </el-form>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
              </span>
            </template>
          </el-dialog>
          
          <el-dialog v-model="detailDialog" title="评分详情" width="30%">
            <el-card>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">楼栋号：</span>
                <span>{{ detail.dormBuildId }}</span>
              </div>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">房间号：</span>
                <span>{{ detail.dormRoomId }}</span>
              </div>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">评分：</span>
                <el-tag :type="getScoreType(detail.score)">{{ detail.score }}分</el-tag>
              </div>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">评语：</span>
                <div style="margin-top: 8px; padding: 12px; background: #f5f7fa; border-radius: 4px;">
                  {{ detail.comment }}
                </div>
              </div>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">评分人：</span>
                <span>{{ detail.scorer }}</span>
              </div>
              <div style="margin-bottom: 16px;">
                <span style="font-weight: bold;">评分时间：</span>
                <span>{{ detail.scoreTime }}</span>
              </div>
            </el-card>
            <template #footer>
              <span class="dialog-footer">
                <el-button type="primary" @click="closeDetails">确 定</el-button>
              </span>
            </template>
          </el-dialog>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script src="@/assets/js/DormScoreManage.js"></script>
