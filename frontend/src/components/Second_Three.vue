<template>
  <div style="width:600px;height:10px;margin-left: 0">占比状况（左右蓝色为目标数据）：</div>
  <div style="display: flex;">
    <div ref="chart" style="width: 350px; height: 350px;margin-left: 300px"></div>
    <div ref="char" style="width: 350px; height: 350px;margin-left: 100px"></div>
  </div>
  __________________________________________________________________________________________________________________
  <div style="width:300px;height:30px;margin-left: 0">详细数据与分布：
  </div>
  <div ref="chartContainer" style="width: 1000px; height: 400px;"></div>
  <div></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'ST',
  data() {
    return {
      dd: '',
      categories: ["<12h", "12h~180d", "180d~360d", "1~3y", "3y~6y", "6y+"],
      value: [],
      max: '',
      avg: ''
    }
  },
  mounted() {
    this.$http.get("/AcAns").then(v => {
          this.dd = v.data;
          this.initChart();
        }
    );
    this.$http.get("/Time_dist").then(v => {
          console.log(v.data);
          this.value.push(v.data.a);
          this.value.push(v.data.b);
          this.value.push(v.data.c);
          this.value.push(v.data.d);
          this.value.push(v.data.e);
          this.value.push(v.data.f);
          this.renderChart();
        }
    );
    this.$http.get("/Percen").then(v => {
          this.dd = v.data;
          this.initChart2();
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
              {value: this.dd, name: this.dd + "%"},
              {value: 100 - this.dd, name: 100 - this.dd + "%"},
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
              {value: this.dd, name: this.dd + "%"},
              {value: 100 - this.dd, name: 100 - this.dd + "%"},
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
  },
};
</script>
