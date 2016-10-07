/**
 * Created by helly on 2016/10/6.
 */
var pageData = {
    pid: "AUTD"
};
var vm = new Vue({
    el: '#marketMatch',
    data: {
        pName: "Au(T+D)",
        buyLines: [],
        sellLines: []
    },
    ready: function () {
        pageData.intervalId = setInterval(function () {
            $.get('/viewData', {pid: pageData.pid}, function (data) {
                vm.buyLines = data.buyLines;
                vm.sellLines = data.sellLines;
            }, 'json');
        }, 1000);
    },
    methods: {
        requestGenRandomOrder: function () {
            $.post('/postData', {pid: pageData.pid}, function (data) {
                vm.buyLines = data.buyLines;
                vm.sellLines = data.sellLines;
            }, 'json');
        }
    }
});