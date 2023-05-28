module.exports = {
  lintOnSave: false,
  devServer: {
    host: "0.0.0.0", // ip
    port: 8080, // 设置端口号
    https: false, // https:{type:Boolean}
    open: false, //配置自动启动浏览器
    proxy: {
      //设置代理
      "/api": {
        target: "http://localhost:9090",
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      },
    },
  },
};