import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "DormAllocation",
    data() {
        return {
            pendingApplications: [],
            availableRooms: [],
            selectedApplications: [],
            selectedRoom: null,
            selectedBed: null,
            dormBuildId: null
        }
    },
    computed: {
        canAllocateToEmpty() {
            return this.selectedApplications.length === 1 && this.selectedRoom !== null && this.selectedBed !== null;
        },
        canSwap() {
            return this.selectedApplications.length === 2;
        }
    },
    created() {
        const dormBuildIdStr = sessionStorage.getItem('dormBuildId');
        this.dormBuildId = dormBuildIdStr ? parseInt(dormBuildIdStr) : null;
        this.loadData();
    },
    methods: {
        async loadData() {
            const params = {};
            if (this.dormBuildId) {
                params.dormBuildId = this.dormBuildId;
            }
            request.get("/dormAllocation/getPendingApplications", { params }).then((res) => {
                if (res.code === "0") {
                    this.pendingApplications = res.data;
                }
            });
            request.get("/dormAllocation/getAvailableRooms", { params }).then((res) => {
                if (res.code === "0") {
                    this.availableRooms = res.data;
                }
            });
        },
        handleSelectionChange(selection) {
            this.selectedApplications = selection;
        },
        handleRoomSelect(row) {
            this.selectedRoom = row;
            this.selectedBed = null;
        },
        removeSelection(id) {
            this.selectedApplications = this.selectedApplications.filter(app => app.id !== id);
        },
        clearSelection() {
            this.selectedApplications = [];
            this.selectedRoom = null;
            this.selectedBed = null;
        },
        async allocateToEmptyBed() {
            const requestData = {
                applicationId: this.selectedApplications[0].id,
                targetRoomId: this.selectedRoom.dormRoomId,
                targetBedId: this.selectedBed
            };

            request.post("/dormAllocation/allocateToEmptyBed", requestData).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "分配成功",
                        type: "success"
                    });
                    this.clearSelection();
                    this.loadData();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error"
                    });
                }
            });
        },
        async swapStudents() {
            const requestData = {
                applicationId1: this.selectedApplications[0].id,
                applicationId2: this.selectedApplications[1].id
            };

            request.post("/dormAllocation/swapStudents", requestData).then((res) => {
                if (res.code === "0") {
                    ElMessage({
                        message: "互换成功",
                        type: "success"
                    });
                    this.clearSelection();
                    this.loadData();
                } else {
                    ElMessage({
                        message: res.msg,
                        type: "error"
                    });
                }
            });
        }
    }
}
