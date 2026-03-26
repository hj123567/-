import { Search, Plus, View, Delete, InfoFilled } from '@element-plus/icons-vue'
import request from "@/utils/request";

const { ElMessage, ElMessageBox } = require("element-plus");

export default {
    name: "ApplyRepairInfo",
    components: { Search, Plus, View, Delete, InfoFilled },
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
            detail: {},
            name: '',
            username: '',
            isRoomLeader: 0,
            form: {},
            room: {
                dormRoomId: '',
                dormBuildId: '',
            },
            rules: {
                title: [{ required: true, message: "请输入标题", trigger: "blur" }],
                content: [{ required: true, message: "请输入内容", trigger: "blur" }],
                orderBuildTime: [{ required: true, message: "请选择时间", trigger: "blur" }],
            },
        }
    },
    created() {
        this.init();
        this.getInfo();
        this.load();
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 1000);
    },
    methods: {
        init() {
            this.form = JSON.parse(sessionStorage.getItem("user"));
            this.name = this.form.name;
            this.username = this.form.username;
            this.isRoomLeader = this.form.isRoomLeader || 0;
            console.log('init() - isRoomLeader:', this.isRoomLeader);
        },
        async load() {
            const params = {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            };
            if (this.room && this.room.dormBuildId && this.room.dormRoomId) {
                params.dormBuildId = this.room.dormBuildId;
                params.dormRoomId = this.room.dormRoomId;
            }
            request.get("/repair/find/" + this.name, {
                params: params,
            }).then((res) => {
                console.log(res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        getInfo() {
            console.log('getInfo() - username:', this.username);
            request.get("/room/getMyRoom/" + this.username).then((res) => {
                if (res.code === "0") {
                    this.room = res.data;
                    console.log('getInfo() - room data:', this.room);
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        getStateType(state) {
            if (state === '待处理') return 'danger';
            if (state === '处理中') return 'warning';
            if (state === '已完成') return 'info';
            if (state === '已评价') return 'success';
            return 'info';
        },
        handleConfirmComplete(row) {
            const updateData = {
                id: row.id,
                state: '已评价'
            };
            request.put("/repair/update", updateData).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "确认完成！感谢您的评价",
                        type: "success",
                    });
                    this.load();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        filterTag(value, row) {
            return row.state === value;
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
        add() {
            console.log('add() - room data:', this.room);
            if (!this.room) {
                ElMessage.error('无法获取您的房间信息，请先分配宿舍！');
                return;
            }
            if (!this.room.dormBuildId || !this.room.dormRoomId) {
                ElMessage.error('房间信息不完整，请重新登录！');
                return;
            }
            request.get(`/repair/checkPending/${this.room.dormBuildId}/${this.room.dormRoomId}`).then((res) => {
                if (res.code === "0") {
                    this.dialogVisible = true;
                    this.$nextTick(() => {
                        this.$refs.form.resetFields();
                        this.form.repairer = this.name;
                        this.form.dormBuildId = this.room.dormBuildId;
                        this.form.dormRoomId = this.room.dormRoomId;
                        console.log('add() - form data:', this.form);
                        const now = new Date();
                        const year = now.getFullYear();
                        const month = String(now.getMonth() + 1).padStart(2, '0');
                        const day = String(now.getDate()).padStart(2, '0');
                        const hours = String(now.getHours()).padStart(2, '0');
                        const minutes = String(now.getMinutes()).padStart(2, '0');
                        const seconds = String(now.getSeconds()).padStart(2, '0');
                        const currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
                        this.form.orderBuildTime = currentTime;
                        this.form.state = '待处理';
                    });
                } else {
                    ElMessage.error(res.msg || '该寝室已有未完成的报修');
                }
            }).catch((err) => {
                console.error('检查报修状态失败:', err);
                ElMessage.error('网络错误，请重试');
            });
        },
        save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    console.log('save() - form data:', this.form);
                    await request.post("/repair/add", this.form).then((res) => {
                        if (res.code === "0") {
                            ElMessage({
                                message: "新增成功",
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
            });
        },
        cancel() {
            this.$refs.form.resetFields();
            this.dialogVisible = false;
        },
        handleSizeChange(pageSize) {
            this.pageSize = pageSize;
            this.load();
        },
        handleCurrentChange(pageNum) {
            this.currentPage = pageNum;
            this.load();
        },
        handleDelete(row) {
            ElMessageBox.confirm(
                '确定要删除这条报修记录吗？',
                '提示',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                request.delete('/repair/delete/' + row.id).then(res => {
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
    },
};
