import request from "@/utils/request";

const {ElMessage} = require("element-plus");

export default {
    name: "StuInfo",
    data() {
        // 手机号验证
        const checkPhone = (rule, value, callback) => {
            const phoneReg = /^1[3|4|5|6|7|8][0-9]{9}$/;
            if (!value) {
                return callback(new Error("电话号码不能为空"));
            }
            setTimeout(() => {
                if (!Number.isInteger(+value)) {
                    callback(new Error("请输入数字值"));
                } else {
                    if (phoneReg.test(value)) {
                        callback();
                    } else {
                        callback(new Error("电话号码格式不正确"));
                    }
                }
            }, 100);
        };
        const checkPass = (rule, value, callback) => {
            if (!this.editJudge) {
                if (value == "") {
                    callback(new Error("请再次输入密码"));
                } else if (value !== this.form.password) {
                    callback(new Error("两次输入密码不一致!"));
                } else {
                    callback();
                }
            } else {
                callback();
            }
        };
        return {
            showpassword: true,
            judgeAddOrEdit: true,
            loading: true,
            editJudge: true,
            disabled: false,
            judge: false,
            dialogVisible: false,
            search: "",
            currentPage: 1,
            pageSize: 10,
            total: 0,
            tableData: [],
            identity: '',
            dormBuildId: null,
            form: {
                username: "",
                name: "",
                age: "",
                gender: "",
                phoneNum: "",
                email: "",
                college: "",
                className: "",
                dormBuildId: null,
                dormRoomId: null,
                studentType: "",
                registrationStatus: "",
                accommodationStatus: "",
                admissionYear: null,
                assignedDormBuildId: null,
            },
            rules: {
                username: [
                    {required: true, message: "请输入学号", trigger: "blur"},
                    {
                        pattern: /^[a-zA-Z0-9]{2,20}$/,
                        message: "必须由 2 到 20 个字母或数字组成",
                        trigger: "blur",
                    },
                ],
                name: [
                    {required: true, message: "请输入姓名", trigger: "blur"},
                    {
                        pattern: /^(?:[\u4E00-\u9FA5·]{2,10})$/,
                        message: "必须由 2 到 10 个汉字组成",
                        trigger: "blur",
                    },
                ],
                age: [
                    {required: true, message: "请输入年龄", trigger: "blur"},
                    {type: "number", message: "年龄必须为数字值", trigger: "blur"},
                    {
                        pattern: /^(1|[1-9]\d?|100)$/,
                        message: "范围：1-100",
                        trigger: "blur",
                    },
                ],
                gender: [{required: true, message: "请选择性别", trigger: "change"}],
                phoneNum: [{required: true, validator: checkPhone, trigger: "blur"}],
                email: [
                    {type: "email", message: "请输入正确的邮箱地址", trigger: "blur"},
                ],
                college: [
                    {required: false, message: "请输入学院", trigger: "blur"},
                ],
                className: [
                    {required: false, message: "请输入班级", trigger: "blur"},
                ],
                password: [
                    {required: true, message: "请输入密码", trigger: "blur"},
                    {
                        min: 6,
                        max: 32,
                        message: "长度在 6 到 16 个字符",
                        trigger: "blur",
                    },
                ],
                checkPass: [{validator: checkPass, trigger: "blur"}],
            },
            editDisplay: {
                display: "block",
            },
            display: {
                display: "none",
            },
        };
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
            request.get("/stu/find", {
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
            request.get("/stu/find", {
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
        add() {
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.judgeAddOrEdit = false;
                this.editDisplay = {display: "none"};
                this.disabled = false;
                this.form = {
                    username: "",
                    name: "",
                    age: "",
                    gender: "",
                    phoneNum: "",
                    email: "",
                    college: "",
                    className: "",
                    dormBuildId: null,
                    dormRoomId: null,
                    studentType: "",
                    registrationStatus: "",
                    accommodationStatus: "",
                    admissionYear: null,
                    assignedDormBuildId: null,
                };
                this.judge = false;
            });
        },
        save() {
            console.log('保存按钮被点击，表单数据:', this.form);
            this.$refs.form.validate((valid) => {
                console.log('表单验证结果:', valid);
                if (valid) {
                    if (this.judge === false) {
                        //新增
                        console.log('执行新增操作');
                        console.log('请求URL:', '/api/stu/add');
                        console.log('请求数据:', this.form);
                        request.post("/stu/add", this.form).then((res) => {
                            console.log('新增响应:', res);
                            if (res.code === "0") {
                                ElMessage({
                                    message: "新增成功",
                                    type: "success",
                                });
                                this.search = "";
                                this.loading = true;
                                this.load();
                                this.dialogVisible = false;
                            } else {
                                ElMessage({
                                    message: res.msg,
                                    type: "error",
                                });
                            }
                        }).catch((error) => {
                            console.error('新增失败:', error);
                            console.error('错误消息:', error.message);
                            console.error('错误状态:', error.status);
                            ElMessage({
                                message: '新增失败，请检查网络连接',
                                type: "error",
                            });
                        });
                    } else {
                        //修改
                        console.log('执行修改操作');
                        console.log('请求URL:', '/api/stu/update');
                        console.log('请求数据:', this.form);
                        request.put("/stu/update", this.form).then((res) => {
                            console.log('修改响应:', res);
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
                        }).catch((error) => {
                            console.error('修改失败:', error);
                            console.error('错误消息:', error.message);
                            console.error('错误状态:', error.status);
                            ElMessage({
                                message: '修改失败，请检查网络连接',
                                type: "error",
                            });
                        });
                    }
                } else {
                    console.log('表单验证失败');
                    ElMessage({
                        message: '请检查表单数据是否正确',
                        type: "warning",
                    });
                }
            });
        },
        cancel() {
            this.$refs.form.resetFields();
            this.display = {display: "none"};
            this.editJudge = true;
            this.disabled = true;
            this.showpassword = true;
            this.dialogVisible = false;
        },
        EditPass() {
            if (this.editJudge) {
                this.showpassword = false;
                this.display = {display: "flex"};
                this.disabled = false;
                this.editJudge = false;
            } else {
                this.showpassword = true;
                this.display = {display: "none"};
                this.editJudge = true;
                this.disabled = true;
            }
        },
        handleEdit(row) {
            //修改
            //判断操作类型
            this.judge = true;
            // 生拷贝
            this.dialogVisible = true;
            this.$nextTick(() => {
                this.$refs.form.resetFields();
                this.form = JSON.parse(JSON.stringify(row));
                // 确保所有字段被正确复制
                this.form.college = row.college || "";
                this.form.className = row.className || "";
                this.form.dormBuildId = row.dormBuildId || null;
                this.form.dormRoomId = row.dormRoomId || null;
                this.form.studentType = row.studentType || "";
                this.form.registrationStatus = row.registrationStatus || "";
                this.form.accommodationStatus = row.accommodationStatus || "";
                this.form.admissionYear = row.admissionYear || null;
                this.form.assignedDormBuildId = row.assignedDormBuildId || null;
                this.judgeAddOrEdit = true;
                this.editDisplay = {display: "block"};
                this.disabled = true;
            });
        },
        async handleDelete(username) {
            //删除
            console.log(username);
            request.delete("/stu/delete/" + username).then((res) => {
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
};