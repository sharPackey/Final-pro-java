<template>
  <div style="width:600px;height:10px;margin-left: 0">占比状况：</div>
  <div ref="chart" style="width: 500px; height: 350px; margin-left: 500px"></div>
  __________________________________________________________________________________________________________________
  <div style="width:300px;height:30px;margin-left: 0">详细数据与分布：

  </div>
  <div>MAX:{{ max }}</div>
  <div>AVG:{{ avg }}</div>
  <div ref="chartContainer" style="width: 1000px; height: 400px;"></div>
  <div></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'FT',
  data() {
    return {
      dd: '',
      categories: ["0", "1~2", "3~7", "8~20", "20+"],
      value: [],
      max: '',
      avg: ''
    }
  },
  mounted() {
    this.$http.get("/noAnsRate").then(v => {
          this.dd = v.data;
          this.initChart();
        }
    );
    this.$http.get("/MaxAvgAns").then(v => {
          this.avg = v.data.avg;
          this.max = v.data.max;
        }
    );
    this.$http.get("/ans_dis").then(v => {
          this.value.push(v.data.a);
          this.value.push(v.data.b);
          this.value.push(v.data.c);
          this.value.push(v.data.d);
          this.value.push(v.data.e);
          this.renderChart();
        }
    )
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
              {value: this.dd, name: "无答案问题:"+this.dd + "%"},
              {value: 100 - this.dd, name: "有答案问题:"+(100 - this.dd) + "%"},
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

      // 渲染图表
      chart.setOption(option);
    },
  },
};
</script>
