import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "UtilityConfigManage",
    data() {
        return {
            loading: true,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            dialogVisible: false,
            judgeAddOrEdit: false,
            form: {},
            buildList: [],
            rules: {
                waterPrice: [{required: true, message: "请输入水费单价", trigger: "blur"}],
                electricityPrice: [{required: true, message: "请输入电费单价", trigger: "blur"}],
                billingCycleDay: [{required: true, message: "请选择计费周期日", trigger: "blur"}]
            }
        }
    },
    created() {
        this.load();
        this.loadBuilds();
    },
    methods: {
        loadBuilds() {
            request.get("/build/findAll").then(res => {
                if (res.code === '0') {
                    this.buildList = res.data;
                }
            });
        },
        load() {
            this.loading = true;
            request.get("/utilityConfig/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize
                }
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },
        add() {
            this.judgeAddOrEdit = false;
            this.dialogVisible = true;
            this.form = {
                waterPrice: 5.00,
                electricityPrice: 0.80,
                billingCycleDay: 1,
                dormBuildId: null
            };
        },
        handleEdit(row) {
            this.judgeAddOrEdit = true;
            this.form = {...row};
            this.dialogVisible = true;
        },
        cancel() {
            this.dialogVisible = false;
            this.form = {};
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.judgeAddOrEdit) {
                        request.put("/utilityConfig/update", this.form).then((res) => {
                            if (res.code === '0') {
                                ElMessage.success("修改成功");
                                this.dialogVisible = false;
                                this.load();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    } else {
                        request.post("/utilityConfig/add", this.form).then((res) => {
                            if (res.code === '0') {
                                ElMessage.success("添加成功");
                                this.dialogVisible = false;
                                this.load();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    }
                }
            });
        },
        handleDelete(id) {
            request.delete("/utilityConfig/delete/" + id).then((res) => {
                if (res.code === '0') {
                    ElMessage.success("删除成功");
                    this.load();
                } else {
                    ElMessage.error(res.msg);
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
