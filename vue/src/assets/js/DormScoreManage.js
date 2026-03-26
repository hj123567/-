import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "DormScoreManage",
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
            dormBuildId: null,
            form: {},
            detail: {},
            judgeAddOrEdit: false,
            roomList: [], // 房间列表
            rules: {
                dormBuildId: [
                    {required: true, message: "请输入楼栋号", trigger: "blur"},
                ],
                dormRoomId: [
                    {required: true, message: "请选择房间", trigger: "change"},
                ],
                score: [
                    {required: true, message: "请输入评分", trigger: "blur"},
                ],
            },
        }
    },
    created() {
        const dormBuildIdStr = sessionStorage.getItem('dormBuildId');
        this.dormBuildId = dormBuildIdStr ? parseInt(dormBuildIdStr) : null;
        this.load();
        this.loadRoomList(); // 加载房间列表
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 1000);
    },
    methods: {
        getScoreType(score) {
            if (score >= 90) return 'success';
            if (score >= 80) return '';
            if (score >= 60) return 'warning';
            return 'danger';
        },
        async load() {
            const params = {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            };
            if (this.dormBuildId) {
                params.dormBuildId = this.dormBuildId;
            }
            request.get("/dormScore/find", {
                params: params,
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        reset() {
            this.search = ''
            this.load();
        },
        add() {
            this.judgeAddOrEdit = false;
            this.dialogVisible = true;
            // 确保 dormBuildId 有值，如果没有则默认为 1
            const buildId = this.dormBuildId || 1;
            this.form = {
                score: 80,
                dormBuildId: buildId,
                dormRoomId: null,
                comment: ''
            };
            console.log('初始化表单，dormBuildId:', buildId);
            // 打开对话框后加载房间列表
            this.$nextTick(() => {
                this.loadRoomList();
            });
        },
        async loadRoomList() {
            try {
                const buildId = this.dormBuildId || 1;
                const res = await request.get("/room/find", {
                    params: {
                        dormBuildId: buildId,
                        pageSize: 100
                    }
                });
                if (res.code === "0") {
                    this.roomList = res.data.records || [];
                    console.log('房间列表:', this.roomList);
                }
            } catch (error) {
                console.error('加载房间列表失败:', error);
                this.roomList = [];
            }
        },
        showDetail(row) {
            this.detailDialog = true;
            this.$nextTick(() => {
                this.detail = row;
            });
        },
        closeDetails() {
            this.detailDialog = false;
        },
        save() {
            console.log('保存按钮被点击，表单数据:', this.form);
            
            // 检查房间号是否存在
            if (!this.form.dormRoomId || this.form.dormRoomId <= 0) {
                ElMessage({
                    message: '请输入房间号',
                    type: "warning",
                });
                return;
            }
            
            // 获取用户信息
            const userStr = window.sessionStorage.getItem('user');
            console.log('用户信息:', userStr);
            
            if (!userStr) {
                ElMessage({
                    message: '用户未登录，请重新登录',
                    type: "error",
                });
                return;
            }
            
            const user = JSON.parse(userStr);
            this.form.scorer = user.name || '管理员';
            console.log('评分人:', this.form.scorer);

            // 如果没有设置 dormBuildId，使用表单中的楼栋号
            if (this.dormBuildId === null || this.dormBuildId === undefined) {
                this.dormBuildId = this.form.dormBuildId;
            }
            
            // 检查权限
            if (this.dormBuildId !== null && this.form.dormBuildId !== this.dormBuildId) {
                ElMessage({
                    message: "您只能管理自己楼栋的评分！",
                    type: "error",
                });
                return;
            }

            // 设置评分时间
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            this.form.scoreTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            
            // 确保 managerDormBuildId 有值
            const managerBuildId = this.dormBuildId || this.form.dormBuildId;
            console.log('准备发送请求，数据:', JSON.stringify(this.form));
            console.log('managerDormBuildId:', managerBuildId);
            
            // 发送请求
            request.post("/dormScore/add", this.form, {
                params: {
                    managerDormBuildId: managerBuildId
                }
            }).then((res) => {
                console.log('收到响应:', res);
                if (res.code === "0") {
                    ElMessage({
                        message: "添加成功",
                        type: "success",
                    });
                    this.load();
                    this.dialogVisible = false;
                } else {
                    ElMessage({
                        message: res.msg || '添加失败',
                        type: "error",
                    });
                }
            }).catch((err) => {
                console.error('请求失败:', err);
                console.error('错误详情:', err.response || err.message);
                ElMessage({
                    message: '添加失败，请稍后重试',
                    type: "error",
                });
            });
        },
        cancel() {
            this.$refs.form.resetFields();
            this.dialogVisible = false;
        },
        async handleDelete(id) {
            request.delete("/dormScore/delete/" + id, {
                params: {
                    managerDormBuildId: this.dormBuildId
                }
            }).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "删除成功",
                        type: "success",
                    });
                    this.search = "";
                    this.load();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
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
