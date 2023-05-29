<template>
  <div style="width:600px;height:10px;margin-left: 0">占比状况（蓝色为目标数据）(参与回答人数，参与评论人数，两者均有参与人数)：

    <div>总数：{{ this.total }}</div>
  </div>
  <div style="height:300px;display: flex;">
    <div ref="chart" style="width: 450px; height: 350px;margin-left: 50px"></div>
    <div ref="char" style="width: 420px; height: 350px;margin-left: 20px"></div>
    <div ref="cha" style="width: 420px; height: 350px;margin-left: 50px"></div>
  </div>
  __________________________________________________________________________________________________________________
  <div style="width:300px;height:30px;margin-left: 0">详细数据与分布：
  </div>
  <div ref="chartContainer" style="width: 1500px; height: 400px;"></div>
  <div ref="chartContainer2" style="width: 1500px; height: 400px;"></div>
  <div></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'FoT',
  data() {
    return {
      dd: '',
      dd2: '',
      dd3: '',
      categories: [],
      value: [],
      max: '',
      total: ''
    }
  },
  mounted() {
    this.$http.get("/Threads").then(v => {
          console.log(v.data);
          this.value = v.data.b;
          this.categories = v.data.a;
          console.log(v.data.b);
          this.total = this.value.map(Number).reduce((accumulator, currentValue) => accumulator + currentValue, 0);
          this.renderChart();
          this.$http.get("/Threads_ans").then(v => {
                this.dd = v.data.ans;
                this.dd2 = v.data.com;
                this.dd3 = v.data.both;
                console.log(this.total);
                this.initChart();
                this.initChart2();
                this.initChart3();
              }
          );
        }
    );

    this.$http.get("/Threads_most").then(v => {
          this.value = v.data.b;
          this.categories = v.data.a;
          this.renderChart2();
        }
    );

  },
  methods: {
    initChart() {
      const chartDom = this.$refs.chart;
      const myChart = echarts.init(chartDom);
      const option = {
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [
              {value: this.dd, name: "参与回答:"+this.dd},
              {value: this.total - this.dd, name: this.total - this.dd},
            ],
          },
        ],
      };
      myChart.setOption(option);
    },
    initChart2() {
      const chartDom = this.$refs.char;
      const myChart = echarts.init(chartDom);
      const option = {
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [
              {value: this.dd2, name: "参与评论:"+this.dd2},
              {value: this.total - this.dd2, name: this.total - this.dd2},
            ],
          },
        ],
      };
      myChart.setOption(option);
    },
    initChart3() {
      const chartDom = this.$refs.cha;
      const myChart = echarts.init(chartDom);
      const option = {
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [
              {value: this.dd3, name:"同时参与:"+ this.dd3},
              {value: this.total - this.dd3, name: this.total - this.dd3},
            ],
          },
        ],
      };
      myChart.setOption(option);
    },
    renderChart() {
      const chartContainer = this.$refs.chartContainer;
      const chart = echarts.init(chartContainer);

      // 柱状图数据
      const data = {
        categories: this.categories,
        values: this.value
      };

      const option = {
        xAxis: {
          type: 'category',
          data: data.categories
        },
        yAxis: {
          type: 'value',
          data: data.values
        },
        series: [
          {
            data: data.values,
            type: 'bar',
            label: {
              show: true,
              position: 'top' // 标签显示在柱状图的顶部
            }
          }
        ]
      };
      chart.setOption(option);
    },
    renderChart2() {
      const chartContainer = this.$refs.chartContainer2;
      const chart = echarts.init(chartContainer);

      // 柱状图数据
      const data = {
        categories: this.categories,
        values: this.value
      };

      const option = {
        xAxis: {
          type: 'category',
          data: data.categories
        },
        yAxis: {
          type: 'value',
          data: data.values
        },
        series: [
          {
            data: data.values,
            type: 'bar',
            label: {
              show: true,
              position: 'top' // 标签显示在柱状图的顶部
            }
          }
        ]
      };
      chart.setOption(option);
    },
  },
};
</script>
