import request from "@/utils/request";

const {ElMessage, ElMessageBox} = require("element-plus");

export default {
    name: "UtilityMeterReading",
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
            roomList: [],
            searchDormBuildId: null,
            searchDormRoomId: null,
            searchBillingMonth: null,
            user: null,
            dormBuildId: null,
            rules: {
                dormBuildId: [{required: true, message: "请选择楼栋", trigger: "change"}],
                dormRoomId: [{required: true, message: "请选择房间", trigger: "change"}],
                billingMonth: [{required: true, message: "请选择计费月份", trigger: "change"}],
                waterReading: [{required: true, message: "请输入本期水表读数", trigger: "blur"}],
                electricityReading: [{required: true, message: "请输入本期电表读数", trigger: "blur"}]
            }
        }
    },
    created() {
        this.init();
        this.load();
        this.loadBuilds();
    },
    methods: {
        init() {
            this.user = JSON.parse(window.sessionStorage.getItem('user'));
            const identity = window.sessionStorage.getItem('identity');
            if (identity && JSON.parse(identity) === 'dormManager') {
                this.dormBuildId = this.user.dormBuildId;
                this.searchDormBuildId = this.dormBuildId;
                this.loadRooms(this.dormBuildId);
            }
        },
        loadBuilds() {
            request.get("/build/findAll").then(res => {
                if (res.code === '0') {
                    this.buildList = res.data;
                }
            });
        },
        loadRooms(buildId) {
            if (!buildId) {
                this.roomList = [];
                return;
            }
            request.get("/room/find", {
                params: {
                    pageNum: 1,
                    pageSize: 1000,
                    dormBuildId: buildId
                }
            }).then(res => {
                if (res.code === '0' && res.data && res.data.records) {
                    this.roomList = res.data.records;
                }
            });
        },
        onBuildChange(buildId) {
            this.form.dormRoomId = null;
            this.loadRooms(buildId);
            this.loadLastReading();
        },
        loadLastReading() {
            if (!this.form.dormBuildId || !this.form.dormRoomId) {
                return;
            }
            request.get("/utilityMeterReading/getLastReading", {
                params: {
                    dormBuildId: this.form.dormBuildId,
                    dormRoomId: this.form.dormRoomId
                }
            }).then(res => {
                if (res.code === '0' && res.data) {
                    this.form.lastWaterReading = res.data.waterReading || 0;
                    this.form.lastElectricityReading = res.data.electricityReading || 0;
                } else {
                    this.form.lastWaterReading = 0;
                    this.form.lastElectricityReading = 0;
                }
                this.calculateUsage();
            });
        },
        calculateUsage() {
            if (this.form.waterReading != null && this.form.lastWaterReading != null) {
                this.form.waterUsage = (this.form.waterReading - this.form.lastWaterReading).toFixed(2);
            }
            if (this.form.electricityReading != null && this.form.lastElectricityReading != null) {
                this.form.electricityUsage = (this.form.electricityReading - this.form.lastElectricityReading).toFixed(2);
            }
        },
        load() {
            this.loading = true;
            request.get("/utilityMeterReading/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    dormBuildId: this.searchDormBuildId || this.dormBuildId,
                    billingMonth: this.searchBillingMonth
                }
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },
        reset() {
            this.searchDormBuildId = this.dormBuildId;
            this.searchDormRoomId = null;
            this.searchBillingMonth = null;
            this.currentPage = 1;
            this.load();
        },
        add() {
            this.judgeAddOrEdit = false;
            this.dialogVisible = true;
            this.form = {
                dormBuildId: this.dormBuildId,
                dormRoomId: null,
                billingMonth: null,
                lastWaterReading: 0,
                lastElectricityReading: 0,
                waterReading: null,
                electricityReading: null,
                waterUsage: 0,
                electricityUsage: 0
            };
            if (this.dormBuildId) {
                this.loadRooms(this.dormBuildId);
            }
        },
        handleEdit(row) {
            this.judgeAddOrEdit = true;
            this.form = {...row};
            this.dialogVisible = true;
            if (this.form.dormBuildId) {
                this.loadRooms(this.form.dormBuildId);
            }
        },
        cancel() {
            this.dialogVisible = false;
            this.form = {};
        },
        save() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    if (this.judgeAddOrEdit) {
                        request.put("/utilityMeterReading/update", this.form).then((res) => {
                            if (res.code === '0') {
                                ElMessage.success("修改成功");
                                this.dialogVisible = false;
                                this.load();
                            } else {
                                ElMessage.error(res.msg);
                            }
                        });
                    } else {
                        request.post("/utilityMeterReading/add", this.form).then((res) => {
                            if (res.code === '0') {
                                ElMessage.success("录入成功");
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
        generateBill(row) {
            ElMessageBox.confirm(
                `确认为 ${row.dormBuildId}号楼${row.dormRoomId}号房 ${row.billingMonth} 生成账单？`,
                '生成账单',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                request.post("/utilityBill/generate", row).then((res) => {
                    if (res.code === '0') {
                        ElMessage.success("账单生成成功！");
                    } else {
                        ElMessage.error(res.msg || "账单生成失败");
                    }
                });
            }).catch(() => {
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
