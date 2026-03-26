import weather from "@/components/weather";
import Calender from "@/components/Calendar";
import request from "@/utils/request";
import home_echarts from "@/components/home_echarts";

export default {
    name: "Home",
    components: {
        weather,
        Calender,
        home_echarts,
    },
    data() {
        return {
            studentNum: "",
            haveRoomStudentNum: "",
            detailDialog: false,
            repairOrderNum: "",
            noFullRoomNum: "",
            activities: [],
            identity: '',
            noticeDialogVisible: false,
            currentNotice: {
                title: '',
                content: '',
                author: '',
                releaseTime: ''
            },
            showWeatherCalendar: false
        };
    },
    created() {
        this.identity = JSON.parse(window.sessionStorage.getItem('identity'));
        this.getHomePageNotice();
        if (this.identity !== 'stu') {
            this.getStuNum();
            this.getHaveRoomNum();
            this.getNoFullRoom();
            this.getOrderNum();
        }
        // 延迟加载天气和日历，提高页面加载速度
        setTimeout(() => {
            this.showWeatherCalendar = true;
        }, 500);
    },
    methods: {
        async getStuNum() {
            request.get("/stu/stuNum").then((res) => {
                if (res.code === "0") {
                    this.studentNum = res.data;
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        async getHaveRoomNum() {
            request.get("/room/selectHaveRoomStuNum").then((res) => {
                if (res.code === "0") {
                    this.haveRoomStudentNum = res.data;
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        async getOrderNum() {
            request.get("/repair/orderNum").then((res) => {
                if (res.code === "0") {
                    this.repairOrderNum = res.data;
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        async getNoFullRoom() {
            request.get("/room/noFullRoom").then((res) => {
                if (res.code === "0") {
                    this.noFullRoomNum = res.data;
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        async getHomePageNotice() {
            request.get("/notice/homePageNotice").then((res) => {
                if (res.code === "0") {
                    this.activities = res.data;
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error",
                    });
                }
            });
        },
        // 查看公告详情
        viewNoticeDetail(notice) {
            this.currentNotice = notice;
            this.noticeDialogVisible = true;
        },

    },
};