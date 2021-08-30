var loginFormVue = new Vue({
    el:'#loginForm',
    data:{
        userAccount:'',
        userPassword:'',
        checked:''
    },
    methods:{
        loginTest:function () {
            //this.checked选中是true 否则是空
            alert(this.userAccount+'||'+this.checked+"||"+this.userPassword)
        }
    }
})