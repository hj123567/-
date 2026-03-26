import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "MyDormScore",
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
        this.load();
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
        }, 1000);
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
        getScoreType(score) {
            if (score >= 90) return 'success';
            if (score >= 80) return '';
            if (score >= 60) return 'warning';
            return 'danger';
        },
        async load() {
            request.get("/dormScore/findByRoom", {
                params: {
                    pageNum: this.currentPage,
                    pageSize: this.pageSize,
                    dormBuildId: this.dormBuildId,
                    dormRoomId: this.dormRoomId
                },
            }).then((res) => {
                this.tableData = res.data.records;
                this.total = res.data.total;
                this.loading = false;
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
