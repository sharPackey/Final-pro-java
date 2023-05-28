<template>
  <div style="width:300px;height:30px;margin-left: 0">TOP 10 TAGS with JAVA：</div>
  <div ref="chartContainer" style="width: 1500px; height: 400px;"></div>
  <div style="width:300px;height:30px;margin-left: 0">TOP 10 TAGS in upvote：</div>
  <div ref="chartContainer2" style="width: 1500px; height: 400px;"></div>
  <div>完整TAGS组合名称：{{this.categories2.toString().replace("[","").replace("]","").replace(/"/g,"").replace(/,/g,', ')}}</div>
  <div style="width:300px;height:30px;margin-left: 0">TOP 10 TAGS in view：</div>
  <div ref="chartContainer3" style="width: 1500px; height: 400px;"></div>
  <div>完整TAGS组合名称：{{this.categories3.toString().replace("[","").replace("]","").replace(/"/g,"").replace(/,/g,', ')}}</div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'TT',
  data() {
    return {
      dd: '',
      categories1: [],
      categories2: [],
      categories3: [],
      value: [],
      max: '',
      avg: ''
    }
  },
  mounted() {
    this.$http.get("/most_tags").then(v => {
      console.log(v.data);
          this.value=v.data.b;
          this.categories1=v.data.a;
          this.renderChart();
        }
    );
    this.$http.get("/vote_tags").then(v => {
          console.log(v.data);
          this.value=v.data.b;
          this.categories2=v.data.a;
          this.renderChart2();
        }
    );
    this.$http.get("/view_tags").then(v => {
          console.log(v.data);
          this.value=v.data.b;
          this.categories3=v.data.a;
          this.renderChart3();
        }
    )
  },
  methods: {
    renderChart() {
      const chartContainer = this.$refs.chartContainer;
      const chart = echarts.init(chartContainer);

      // 柱状图数据
      const data = {
        categories: this.categories1,
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
    renderChart2() {
      const chartContainer = this.$refs.chartContainer2;
      const chart = echarts.init(chartContainer);

      // 柱状图数据
      const data = {
        categories: this.categories2,
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
    renderChart3() {
      const chartContainer = this.$refs.chartContainer3;
      const chart = echarts.init(chartContainer);

      // 柱状图数据
      const data = {
        categories: this.categories3,
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
