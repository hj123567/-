import request from "@/utils/request";

const {ElMessage, ElMessageBox} = require("element-plus");
export default {
    name: "AdjustRoomInfo",
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
                    {required: true, message: "请输入学号", trigger: "blur"},
                    {pattern: /^[a-zA-Z0-9]{4,9}$/, message: "必须由 2 到 5 个字母或数字组成", trigger: "blur",},
                ],
                name: [
                    {required: true, message: "请输入姓名", trigger: "blur"},
                    {pattern: /^(?:[\u4E00-\u9FA5·]{2,10})$/, message: "必须由 2 到 10 个汉字组成", trigger: "blur",},
                ],
                currentRoomId: [
                    {required: true, message: "请输入当前房间号", trigger: "blur"},
                ],
                currentBedId: [
                    {required: true, message: "请输入当前床位号", trigger: "blur"},
                ],
            },
        }
    },
    created() {
        this.load();
        this.loading = true;
        setTimeout(() => {
            //设置延迟执行
            this.loading = false;
        }, 1000);
    },
    methods: {
        async load() {
            const username = JSON.parse(sessionStorage.getItem("user")).username;
            request.get("/adjustRoom/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    search: username,
                },
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
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
                        // 自动设置当前时间，学生不能预约
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
                        //修改
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
                        //添加
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
            // 查看详情
            this.detailDialog = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
            });
        },
        handleEdit(row) {
            //修改
            // 生拷贝
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
            //改变每页个数
            this.pageSize = pageSize;
            this.load();
        },
        handleCurrentChange(pageNum) {
            //改变页码
            this.currentPage = pageNum;
            this.load();
        },
    },
}