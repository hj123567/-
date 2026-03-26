import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "RepairInfo",
    components: {},
    data() {
        const checkRepairState = (rule, value, callback) => {
            if (this.judge) {
                if (value === "未完成" && this.form.orderFinishTime === null) {
                    callback();
                } else if (value === "完成" && this.form.orderFinishTime !== null) {
                    callback();
                } else {
                    callback(new Error("请检查报修完成状态与选择时间是否匹配"));
                }
            } else {
                callback();
            }
        };
        return {
            buildTimeDisabled: true,
            loading: true,
            disabled: false,
            judge: false,
            dialogVisible: false,
            detailDialog: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            identity: '',
            dormBuildId: null,
            detail: {},
            form: {},
            rules: {
                title: [{required: true, message: "请输入标题", trigger: "blur"}],
                content: [{required: true, message: "请输入内容", trigger: "blur"}],
                repairer: [
                    {required: true, message: "请输入申请人", trigger: "blur"},
                ],
                orderBuildTime: [
                    {required: true, message: "请选择时间", trigger: "blur"},
                ],
                state: [{validator: checkRepairState, trigger: "blur"}],
            },
            finishTime: {
                display: "none",
            },
        };
    },
    created() {
        console.log('RepairInfo created() - sessionStorage:', {
            identity: sessionStorage.getItem('identity'),
            dormBuildId: sessionStorage.getItem('dormBuildId'),
            user: sessionStorage.getItem('user')
        });
        this.identity = JSON.parse(sessionStorage.getItem('identity'));
        if (this.identity === 'dormManager') {
            const dormBuildIdStr = sessionStorage.getItem('dormBuildId');
            this.dormBuildId = dormBuildIdStr ? parseInt(dormBuildIdStr) : null;
            console.log('RepairInfo created() - dormManager, dormBuildId:', this.dormBuildId);
        }
        this.load();
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 1000);
    },
    methods: {
        getStateType(state) {
            if (state === '待处理') return 'danger';
            if (state === '处理中') return 'warning';
            if (state === '已完成') return 'info';
            if (state === '已评价') return 'success';
            return 'info';
        },
        handleRepairing(row) {
            const updateData = {
                id: row.id,
                state: '处理中'
            };
            request.put("/repair/update", updateData).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "已标记为正在维修中！",
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
        handleComplete(row) {
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const seconds = String(now.getSeconds()).padStart(2, '0');
            const currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

            const updateData = {
                id: row.id,
                state: '已完成',
                orderFinishTime: currentTime
            };
            request.put("/repair/update", updateData).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "已标记完成！等待学生确认",
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
        async load() {
            const params = {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                search: this.search,
            };
            console.log('load() - identity:', this.identity, 'dormBuildId:', this.dormBuildId);
            if (this.identity === 'dormManager' && this.dormBuildId && this.dormBuildId > 0) {
                params.dormBuildId = this.dormBuildId;
            }
            console.log('load() - params:', params);
            request.get("/repair/find", {
                params: params,
            }).then((res) => {
                console.log('load() - response:', res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            });
        },
        reset() {
            this.search = '';
            const params = {
                pageNum: 1,
                pageSize: this.pageSize,
                search: this.search,
            };
            if (this.identity === 'dormManager' && this.dormBuildId) {
                params.dormBuildId = this.dormBuildId;
            }
            request.get("/repair/find", {
                params: params,
            }).then((res) => {
                console.log(res);
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
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
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.buildTimeDisabled = false;
                this.finishTime = {display: "none"};
                this.disabled = false;
                this.form = {};
                this.judge = false;
            });
        },
        save() {
            this.$refs.form.validate(async (valid) => {
                if (valid) {
                    if (this.judge === false) {
                        await request.post("/repair/add", this.form).then((res) => {
                            console.log(res);
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
                    } else {
                        await request.put("/repair/update", this.form).then((res) => {
                            console.log(res);
                            if (res.code === "0") {
                                ElMessage({
                                    message: "修改成功",
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
        },
        handleEdit(row) {
            this.judge = true;
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
                this.disabled = true;
                this.buildTimeDisabled = true;
                this.finishTime = {display: "flex"};
            });
        },
        handleDelete(id) {
            console.log(id);
            request.delete("/repair/delete/" + id).then((res) => {
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
};
