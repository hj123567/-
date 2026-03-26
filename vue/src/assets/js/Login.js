import request from "@/utils/request";

const {ElMessage} = require("element-plus");
export default {
    name: "Login",
    data() {
        return {
            identity: "",
            form: {
                username: "",
                password: "",
                identity: "",
            },
            rules: {
                username: [
                    {required: true, message: "请输入用户名", trigger: "blur"},
                ],
                password: [{required: true, message: "请输入密码", trigger: "blur"}],
                identity: [{required: true, message: "请选择身份", trigger: "blur"}],
            },
        };
    },
    computed: {
        disabled() {
            const {username, password, identity} = this.form;
            return !!(username && password && identity);
        },
    },
    methods: {
        login() {
            this.$refs.form.validate((valid) => {
                if (valid) {
                    this.identity = this.form.identity;
                    console.log('登录请求:', "/" + this.identity + "/login");
                    console.log('登录数据:', this.form);
                    request.post("/" + this.identity + "/login", this.form).then((res) => {
                        console.log('登录响应:', res);
                        if (res.code === "0") {
                            ElMessage({
                                message: "登陆成功",
                                type: "success",
                            });
                            // 登陆成功跳转主页
                            window.sessionStorage.setItem("user", JSON.stringify(res.data));
                            window.sessionStorage.setItem("identity", JSON.stringify(this.form.identity));
                            // 如果是宿舍管理员，保存dormBuildId
                            if (this.form.identity === 'dormManager' && res.data.dormBuildId) {
                                window.sessionStorage.setItem("dormBuildId", res.data.dormBuildId);
                            }
                            this.$router.replace({path: "/home"});
                        } else {
                            ElMessage({
                                message: res.msg,
                                type: "error",
                            });
                        }
                    }).catch((error) => {
                        console.log('登录错误:', error);
                        ElMessage({
                            message: "网络错误，请检查后端服务是否运行",
                            type: "error",
                        });
                    });
                }
            });
        },
    },
};