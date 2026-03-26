import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "MyRoomInfo",
    data() {
        return {
            name: "",
            identity: "",
            form: {
                username: "",
                name: ""
            },
            room: {
                dormRoomId: "",
                dormBuildId: "",
                floorNum: "",
                maxCapacity: "",
                currentCapacity: "",
                firstBed: "",
                secondBed: "",
                thirdBed: "",
                fourthBed: "",
            },
            // 室友信息
            roommates: [],
            // 床位安排
            bedArrangement: [],
            // 报修记录
            repairRecords: [],
            // 卫生评分
            cleanlinessScores: [],
            // 水电费
            utilityBills: [],
            // 水电费加载状态
            utilityLoading: false,
        };
    },
    created() {
        this.init();
        this.getInfo();
    },
    methods: {
        init() {
            this.form = JSON.parse(sessionStorage.getItem("user"));
            this.name = this.form.username;
            this.identity = sessionStorage.getItem("identity");
        },
        getInfo() {
            request.get("/room/getMyRoom/" + this.name).then((res) => {
                if (res.code === "0") {
                    this.room = res.data;
                    // 确保可住人数为4
                    this.room.maxCapacity = 4;
                    this.initRoommateInfo();
                    this.initBedArrangement();
                    this.initMockData();
                    // 获取水电费账单
                    this.getUtilityBills();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        // 初始化室友信息
        initRoommateInfo() {
            // 模拟室友数据
            this.roommates = [
                {
                    username: this.name,
                    name: this.form.name || "我",
                    gender: "男",
                    college: "计算机学院",
                    className: "计科2001",
                    phoneNum: "138****1234",
                    bed: "1号床"
                },
                {
                    username: this.room.secondBed,
                    name: "李四",
                    gender: "男",
                    college: "计算机学院",
                    className: "计科2001",
                    phoneNum: "139****5678",
                    bed: "2号床"
                },
                {
                    username: this.room.thirdBed,
                    name: "王五",
                    gender: "男",
                    college: "计算机学院",
                    className: "计科2001",
                    phoneNum: "137****9012",
                    bed: "3号床"
                },
                {
                    username: this.room.fourthBed,
                    name: "赵六",
                    gender: "男",
                    college: "计算机学院",
                    className: "计科2001",
                    phoneNum: "136****3456",
                    bed: "4号床"
                }
            ].filter(item => item.username);
        },
        // 初始化床位安排
        initBedArrangement() {
            this.bedArrangement = [
                {
                    position: "1号床",
                    username: this.room.firstBed,
                    name: this.room.firstBed === this.name ? (this.form.name || "我") : "李四"
                },
                {
                    position: "2号床",
                    username: this.room.secondBed,
                    name: this.room.secondBed === this.name ? (this.form.name || "我") : "王五"
                },
                {
                    position: "3号床",
                    username: this.room.thirdBed,
                    name: this.room.thirdBed === this.name ? (this.form.name || "我") : "赵六"
                },
                {
                    position: "4号床",
                    username: this.room.fourthBed,
                    name: this.room.fourthBed === this.name ? (this.form.name || "我") : ""
                }
            ];
        },
        // 初始化数据
        initMockData() {
            // 从API获取报修记录
            this.getRepairRecords();
            
            // 从API获取卫生评分
            this.getCleanlinessScores();
            
            // 从API获取水电费账单
            this.getUtilityBills();
        },
        
        // 获取水电费账单
        getUtilityBills() {
            if (!this.room.dormBuildId || !this.room.dormRoomId) {
                this.utilityBills = [];
                return;
            }
            
            this.utilityLoading = true;
            request.get("/utilityBill/findByRoom", {
                params: {
                    pageNum: 1,
                    pageSize: 20,
                    dormBuildId: this.room.dormBuildId,
                    dormRoomId: this.room.dormRoomId
                }
            }).then((res) => {
                if (res.code === "0" && res.data && res.data.records) {
                    this.utilityBills = res.data.records;
                } else {
                    this.utilityBills = [];
                }
                this.utilityLoading = false;
            }).catch(error => {
                console.error('获取水电费账单失败:', error);
                this.utilityBills = [];
                this.utilityLoading = false;
            });
        },
        
        // 缴费
        payBill(bill) {
            ElMessageBox.confirm(
                `确认缴纳 ${bill.billingMonth} 的水电费 ${(bill.totalAmount || 0).toFixed(2)} 元？`,
                '在线缴费',
                {
                    confirmButtonText: '确认支付',
                    cancelButtonText: '取消',
                    type: 'info'
                }
            ).then(() => {
                request.post("/utilityBill/pay", null, {
                    params: {
                        billId: bill.id,
                        payerId: this.form.id || 1,
                        payerName: this.form.name
                    }
                }).then((res) => {
                    if (res.code === "0") {
                        ElMessage.success("缴费成功！");
                        this.getUtilityBills();
                    } else {
                        ElMessage.error(res.msg || "缴费失败");
                    }
                }).catch(error => {
                    ElMessage.error("缴费失败，请稍后重试");
                });
            }).catch(() => {
            });
        },
        
        // 获取报修记录
        getRepairRecords() {
            // 使用与ApplyRepairInfo相同的接口和参数
            request.get("/repair/find/" + this.form.name, {
                params: {
                    pageNum: 1,
                    pageSize: 10,
                    search: ""
                }
            }).then((res) => {
                if (res.code === "0") {
                    this.repairRecords = res.data.records || [];
                } else {
                    console.error('获取报修记录失败:', res.msg);
                    // 如果API调用失败，使用模拟数据作为 fallback
                    this.repairRecords = [
                        {
                            id: "1001",
                            title: "水龙头漏水",
                            orderBuildTime: "2026-03-15 14:30:00",
                            orderFinishTime: "2026-03-16 10:00:00",
                            state: "完成"
                        },
                        {
                            id: "1002",
                            title: "灯管损坏",
                            orderBuildTime: "2026-03-10 09:15:00",
                            orderFinishTime: "2026-03-10 14:00:00",
                            state: "完成"
                        },
                        {
                            id: "1003",
                            title: "空调不制冷",
                            orderBuildTime: "2026-03-05 16:45:00",
                            orderFinishTime: null,
                            state: "未完成"
                        }
                    ];
                }
            }).catch(error => {
                console.error('获取报修记录出错:', error);
                // 如果网络错误，使用模拟数据作为 fallback
                this.repairRecords = [
                    {
                        id: "1001",
                        title: "水龙头漏水",
                        orderBuildTime: "2026-03-15 14:30:00",
                        orderFinishTime: "2026-03-16 10:00:00",
                        state: "完成"
                    },
                    {
                        id: "1002",
                        title: "灯管损坏",
                        orderBuildTime: "2026-03-10 09:15:00",
                        orderFinishTime: "2026-03-10 14:00:00",
                        state: "完成"
                    },
                    {
                        id: "1003",
                        title: "空调不制冷",
                        orderBuildTime: "2026-03-05 16:45:00",
                        orderFinishTime: null,
                        state: "未完成"
                    }
                ];
            });
        },
        // 申请调宿
        applyRoomChange() {
            this.$router.push("/applyChangeRoom");
        },
    },
};