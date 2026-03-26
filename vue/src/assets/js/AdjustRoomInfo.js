import request from "@/utils/request";

const {ElMessage} = require("element-plus");
export default {
    name: "AdjustRoomInfo",
    data() {
        const checkRoomState = (rule, value, callback) => {
            this.dormRoomId = value
            request.get("/room/checkRoomState/" + value).then((res) => {
                if (res.code === "0") {
                    callback();
                } else {
                    callback(new Error(res.msg));
                }
            });
        };
        const checkBedState = (rule, value, callback) => {
            request.get("/room/checkBedState/" + this.dormRoomId + '/' + value).then((res) => {
                if (res.code === "0") {
                    callback();
                } else {
                    callback(new Error(res.msg));
                }
            });
        };
        const checkApplyState = (rule, value, callback) => {
            console.log(this.form.finishTime)
            if (value === "通过" && this.form.finishTime !== null) {
                callback();
            } else if (value === "驳回" && this.form.finishTime !== null) {
                callback();
            } else {
                callback(new Error("请检查订单完成状态与选择时间是否匹配"));
            }
        };
        return {
            loading: true,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            identity: '',
            dormBuildId: null,
            form: {},
            dormRoomId: 0,
            orderState: false,
            emptyBeds: [],
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
                state: [{validator: checkApplyState, trigger: "blur"},],
                towardsRoomId: [{validator: checkRoomState, trigger: "blur"}],
                towardsBedId: [{validator: checkBedState, trigger: "blur"}],
            },
        }
    },
    created() {
        this.identity = JSON.parse(sessionStorage.getItem('identity'));
        if (this.identity === 'dormManager') {
            const dormBuildIdStr = sessionStorage.getItem('dormBuildId');
            this.dormBuildId = dormBuildIdStr ? parseInt(dormBuildIdStr) : null;
        }
        this.load();
        this.loading = true;
        setTimeout(() => {
            //设置延迟执行
            this.loading = false;
        }, 1000);
    },
    methods: {
        async load() {
            const params = {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            };
            if (this.identity === 'dormManager' && this.dormBuildId) {
                params.dormBuildId = this.dormBuildId;
            }
            request.get("/adjustRoom/find", {
                params: params,
            }).then((res) => {
                console.log(res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        reset() {
            this.search = ''
            const params = {
                pageNum: 1,
                pageSize: this.pageSize,
                search: this.search,
            };
            if (this.identity === 'dormManager' && this.dormBuildId) {
                params.dormBuildId = this.dormBuildId;
            }
            request.get("/adjustRoom/find", {
                params: params,
            }).then((res) => {
                console.log(res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        filterTag(value, row) {
            return row.gender === value;
        },
        judgeOrderState(state) {
            if (state === '通过') {
                this.orderState = true
            } else if (state === '驳回') {
                this.orderState = false
            }
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.judgeOrderState(this.form.state)
                    //修改
                    request.put("/adjustRoom/update/" + this.orderState, this.form).then((res) => {
                        console.log(res);
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
                this.emptyBeds = [];
                // 自动加载可换宿舍列表
                this.loadAllEmptyBeds();
            });
        },
        loadAllEmptyBeds() {
            // 加载所有可换宿舍的空床位（宿管管理的楼栋，排除当前房间）
            request.get("/room/getAllEmptyBeds", {
                params: {
                    dormBuildId: this.dormBuildId,
                    currentRoomId: this.form.currentRoomId
                }
            }).then((res) => {
                if (res.code === "0") {
                    this.emptyBeds = res.data || [];
                } else {
                    this.emptyBeds = [];
                }
            }).catch(() => {
                this.emptyBeds = [];
            });
        },
        loadEmptyBeds() {
            // 加载空床位信息（保留原有方法，但不再使用）
            if (!this.form.towardsRoomId) {
                this.emptyBeds = [];
                return;
            }
            request.get("/room/getEmptyBeds/" + this.form.towardsRoomId).then((res) => {
                if (res.code === "0") {
                    this.emptyBeds = res.data || [];
                } else {
                    this.emptyBeds = [];
                }
            }).catch(() => {
                this.emptyBeds = [];
            });
        },
        selectBed(row) {
            // 选择床位
            this.form.towardsRoomId = row.roomId;
            this.form.towardsBedId = row.bedId;
            ElMessage.success(`已选择 ${row.roomId}房间 ${row.bedLabel}`);
        },
        async handleDelete(id) {
            //删除
            request.delete("/adjustRoom/delete/" + id).then((res) => {
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