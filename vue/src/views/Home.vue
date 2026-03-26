<template>
    <el-card class="main-card" style="margin: 15px; min-height: calc(100vh - 80px)">
        <!-- 公告详情对话框 -->
        <el-dialog
            v-model="noticeDialogVisible"
            :title="currentNotice.title"
            width="60%"
            destroy-on-close
            custom-class="notice-dialog"
        >
            <div class="dialog-content">
                <div class="dialog-header">
                    <span class="publish-time">发布时间：{{ currentNotice.releaseTime }}</span>
                    <span class="author">发布人：{{ currentNotice.author }}</span>
                </div>
                <div class="dialog-body" v-html="currentNotice.content"></div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button class="close-button" @click="noticeDialogVisible = false">关闭</el-button>
                </span>
            </template>
        </el-dialog>
        
        <!-- 学生端首页 -->
        <div v-if="identity === 'stu'" class="student-home">
            <div class="student-content">
                <!-- 左侧 通告 -->
                <div class="notice-section">
                    <div class="section-title">
                        <el-icon class="title-icon"><Bell /></el-icon>
                        <h2>通告</h2>
                    </div>
                    <el-timeline class="timeline">
                        <el-timeline-item 
                            v-for="(activity, index) in activities.slice(0, 8)" 
                            :key="index"
                            :timestamp="activity.releaseTime"
                            class="timeline-item"
                        >
                            <div class="timeline-content">
                                <span 
                                    class="notice-title" 
                                    @click="viewNoticeDetail(activity)"
                                >
                                    {{ activity.title }}
                                </span>
                            </div>
                        </el-timeline-item>
                    </el-timeline>
                </div>
                
                <!-- 右侧 天气和日历 -->
                <div class="weather-calendar-section">
                    <div class="section-title">
                        <el-icon class="title-icon"><Sunny /></el-icon>
                        <h2>天气与日历</h2>
                    </div>
                    <div class="weather-card" v-if="showWeatherCalendar">
                        <weather/>
                    </div>
                    <div class="calendar-card" v-if="showWeatherCalendar">
                        <Calender/>
                    </div>
                    <div class="loading-container" v-else>
                        <el-icon class="loading-icon"><Loading /></el-icon>
                        <span>加载中...</span>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- 管理员端首页 -->
        <div v-else class="admin-home">
            <!-- 头部数据 -->
            <div class="stats-section">
                <div class="section-title">
                    <el-icon class="title-icon"><DataAnalysis /></el-icon>
                    <h2>数据统计</h2>
                </div>
                <el-row :gutter="20" class="stats-grid">
                    <el-col :span="6">
                        <el-card class="stat-card student-stat">
                            <div class="stat-content">
                                <div class="stat-label">学生统计</div>
                                <div class="stat-value">{{ studentNum }}</div>
                                <div class="stat-desc">当前分类总记录数</div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card class="stat-card room-stat">
                            <div class="stat-content">
                                <div class="stat-label">住宿人数</div>
                                <div class="stat-value">{{ haveRoomStudentNum }}</div>
                                <div class="stat-desc">当前分类总记录数</div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card class="stat-card repair-stat">
                            <div class="stat-content">
                                <div class="stat-label">报修统计</div>
                                <div class="stat-value">{{ repairOrderNum }}</div>
                                <div class="stat-desc">当前分类总记录数</div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="6">
                        <el-card class="stat-card empty-stat">
                            <div class="stat-content">
                                <div class="stat-label">空宿舍统计</div>
                                <div class="stat-value">{{ noFullRoomNum }}</div>
                                <div class="stat-desc">当前分类总记录数</div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </div>
            
            <!-- 下部 -->
            <div class="bottom-section">
                <!-- 左侧 通告 -->
                <div class="notice-section">
                    <div class="section-title">
                        <el-icon class="title-icon"><Bell /></el-icon>
                        <h2>通告</h2>
                    </div>
                    <el-timeline class="timeline">
                        <el-timeline-item 
                            v-for="(activity, index) in activities.slice(0, 8)" 
                            :key="index"
                            :timestamp="activity.releaseTime"
                            class="timeline-item"
                        >
                            <div class="timeline-content">
                                <span 
                                    class="notice-title" 
                                    @click="viewNoticeDetail(activity)"
                                >
                                    {{ activity.title }}
                                </span>
                            </div>
                        </el-timeline-item>
                    </el-timeline>
                </div>
                
                <!-- 中部 -->
                <div class="chart-section">
                    <div class="section-title">
                        <el-icon class="title-icon"><PieChart /></el-icon>
                        <h2>宿舍学生人数分布</h2>
                    </div>
                    <div class="chart-container">
                        <home_echarts/>
                    </div>
                </div>
                
                <!-- 右侧 -->
                <div class="weather-calendar-section">
                    <div class="section-title">
                        <el-icon class="title-icon"><Sunny /></el-icon>
                        <h2>天气与日历</h2>
                    </div>
                    <div class="weather-card" v-if="showWeatherCalendar">
                        <weather/>
                    </div>
                    <div class="calendar-card" v-if="showWeatherCalendar">
                        <Calender/>
                    </div>
                    <div class="loading-container" v-else>
                        <el-icon class="loading-icon"><Loading /></el-icon>
                        <span>加载中...</span>
                    </div>
                </div>
            </div>
        </div>
    </el-card>
</template>

<script src="@/assets/js/Home.js"></script>
<style scoped>@import '../assets/css/Home.css';</style>