import request from "@/utils/request";

const {ElMessage, ElMessageBox} = require("element-plus");

export default {
    name: "UtilityBillManage",
    data() {
        return {
            loading: true,
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            detailDialog: false,
            detail: {},
            buildList: [],
            searchDormBuildId: null,
            searchPaymentStatus: null,
            searchBillingMonth: null,
            user: null,
            dormBuildId: null,
            unpaidTotal: 0,
            paidTotal: 0
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
            }
        },
        loadBuilds() {
            request.get("/build/findAll").then(res => {
                if (res.code === '0') {
                    this.buildList = res.data;
                }
            });
        },
        load() {
            this.loading = true;
            request.get("/utilityBill/find", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    dormBuildId: this.searchDormBuildId || this.dormBuildId,
                    billingMonth: this.searchBillingMonth,
                    paymentStatus: this.searchPaymentStatus
                }
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.calculateTotals();
                this.loading = false;
            }).catch(() => {
                this.loading = false;
            });
        },
        calculateTotals() {
            this.unpaidTotal = 0;
            this.paidTotal = 0;
            this.tableData.forEach(item => {
                if (item.paymentStatus === '未缴费' && item.totalAmount) {
                    this.unpaidTotal += parseFloat(item.totalAmount);
                }
                if (item.paymentStatus === '已缴费' && item.totalAmount) {
                    this.paidTotal += parseFloat(item.totalAmount);
                }
            });
        },
        reset() {
            this.searchDormBuildId = this.dormBuildId;
            this.searchPaymentStatus = null;
            this.searchBillingMonth = null;
            this.currentPage = 1;
            this.load();
        },
        showDetail(row) {
            this.detail = {...row};
            this.detailDialog = true;
        },
        sendReminder(row) {
            ElMessageBox.confirm(
                `确定向 ${row.dormBuildId}号楼${row.dormRoomId}号房 发送催缴提醒？`,
                '催缴提醒',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }
            ).then(() => {
                ElMessage.success("催缴提醒已发送！");
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
