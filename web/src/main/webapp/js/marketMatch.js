/**
 * Created by helly on 2016/10/6.
 */
var pageData = {
    pid: "AUTD",
    sellLineNames: ['卖五', '卖四', '卖三', '卖二', '卖一'],
    buyLineNames: ['买一', '买二', '买三', '买四', '买五'],
    readyChart: function () {
        // 基于准备好的dom，初始化echarts实例
        pageData.chart = echarts.init(document.getElementById('orderBookChart'));

        // 指定图表的配置项和数据

        var option = {
            title: {
                text: 'Au(T+D)挂单队列'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                top: 80,
                bottom: 30,
                left: 100
            },
            xAxis: {
                type: 'value',
                position: 'top',
                splitLine: {lineStyle: {type: 'dashed'}},
            },
            yAxis: {
                type: 'category',
                axisLine: {show: false},
                axisLabel: {show: false},
                axisTick: {show: false},
                splitLine: {show: false},
                data: []
            },
            series: [{
                name: 'volume',
                type: 'bar',
                stack: 'total volume',
                label: {
                    normal: {
                        show: true,
                        formatter: '{b}'
                    }
                },
                data: []
            }]
        };

        pageData.chart.setOption(option);
    },
    refreshData: function (data) {
        vm.buyLines = data.buyLines;
        vm.sellLines = data.sellLines;
        vm.order = data.order;
        vm.hisOrder = data.hisOrder;
        var newData = {names: [], data: []};
        var labelLeft = {
            normal: {
                position: 'left'
            }
        };
        for (var i = 0; i < 5; i++) {
            if (i >= data.sellLines.length) {
                newData.names.unshift(pageData.sellLineNames[5 - i - 1] + "@0");
                newData.data.unshift({value: 0, label: labelLeft});
            } else {
                newData.names.push(data.sellLines[i].name + "@" + data.sellLines[i].limitPrice);
                newData.data.push({value: data.sellLines[i].volume, label: labelLeft});
            }
        }
        for (var j = 0; j < 5; j++) {
            if (j >= data.buyLines.length) {
                newData.names.push(pageData.buyLineNames[j] + "@0");
                newData.data.push({value: 0, label: labelLeft});
            } else {
                newData.names.push(data.buyLines[j].name + "@" + data.buyLines[j].limitPrice);
                newData.data.push({value: data.buyLines[j].volume, label: labelLeft});
            }
        }
        var option = {
            yAxis: {
                type: 'category',
                axisLine: {show: false},
                axisLabel: {show: false},
                axisTick: {show: false},
                splitLine: {show: false},
                data: newData.names.reverse()
            },
            series: [{
                name: 'volume',
                type: 'bar',
                stack: 'total volume',
                label: {
                    normal: {
                        show: true,
                        formatter: '{b}'
                    }
                },
                data: newData.data.reverse()
            }]
        };
        pageData.chart.setOption(option);
    }
};

var vm = new Vue({
    el: '#marketMatch',
    data: {
        pName: "Au(T+D)",
        buyLines: [],
        sellLines: [],
        order: {},
        hisOrder: []
    },
    ready: function () {
        pageData.intervalId = setInterval(function () {
            $.get('/viewData', {pid: pageData.pid}, function (data) {
                pageData.refreshData(data);
            }, 'json');
        }, 1000);
        pageData.readyChart();
    },
    methods: {
        requestGenRandomOrder: function () {
            $.post('/postData', {pid: pageData.pid}, function (data) {
                pageData.refreshData(data);
            }, 'json');
        }
    }
});