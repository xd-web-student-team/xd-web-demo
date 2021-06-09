let proxyObj = {};
proxyObj['/ws'] = {
  ws: true,
  target: "ws://39.107.142.199:8082"
};
proxyObj['/'] = {
  ws: false,
  target: 'http://39.107.142.199:8082',
  changeOrigin: true,
  pathRewrite: {
    '^/': ''
  }
}
module.exports = {
  devServer: {
    host: 'localhost',
    port: 8080,
    proxy: proxyObj
  }
}
