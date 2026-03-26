import request from "@/utils/request";

const {ElMessage, ElMessageBox} = require("element-plus");

export default {
    name: "MyUtilityBill",
    data() {
        return {
            loading: true,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            user: null,
            dormBuildId: null,
            dormRoomId: null
        }
    },
    created() {
        this.init();
    },
    methods: {
        init() {
            this.user = JSON.parse(window.sessionStorage.getItem('user'));
            if (this.user) {
                request.get("/room/getMyRoom/" + this.user.username).then((res) => {
                    if (res.code === "0" && res.data) {
                        this.dormBuildId = res.data.dormBuildId;
                        this.dormRoomId = res.data.dormRoomId;
                        this.load();
                    }
                });
            }
        },
        async load() {
            if (!this.dormBuildId || !this.dormRoomId) {
                this.loading = false;
                return;
            }
            request.get("/utilityBill/findByRoom", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    dormBuildId: this.dormBuildId,
                    dormRoomId: this.dormRoomId
                },
            }).then((res) => {
                this.tableData = res.data.records.map(item => ({
                    ...item,
                    month: item.billingMonth,
                    electricityFee: item.electricityAmount,
                    waterFee: item.waterAmount,
                    totalFee: item.totalAmount,
                    status: item.paymentStatus,
                    notifyTime: item.createTime,
                    payTime: item.paymentTime
                }));
                this.total = res.data.total;
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },
        async handlePay(row) {
            ElMessageBox.confirm(
                `确认缴纳 ${row.totalAmount} 元水电费？`,
                '确认缴纳',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                request.post("/utilityBill/pay", null, {
                    params: {
                        billId: row.id,
                        payerId: this.user.id,
                        payerName: this.user.name
                    }
                }).then((res) => {
                    if (res.code === "0") {
                        ElMessage({
                            message: "缴纳成功",
                            type: "success"
                        });
                        this.load();
                    } else {
                        ElMessage({
                            message: res.msg,
                            type: "error"
                        });
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
