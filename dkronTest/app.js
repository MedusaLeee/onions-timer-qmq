const moment = require('moment')
const Koa = require('koa')
const http = require('http')
const app = new Koa()



app.use(ctx => {
    if (!ctx.query.time) {
        ctx.body = '时间差: ' + 0
        return
    }
    console.log(ctx.query.time)
    const msgTime = moment(ctx.query.time).unix()
    let currentTime = moment().unix()
    console.log('时间差 s: ', currentTime - msgTime)
    ctx.body = '时间差 s: ' + (currentTime - msgTime)
});

const server = http.createServer(app.callback())
server.on('error', error => {
    console.error('server start error: ', error)
})
server.on('listening', () => {
    console.log('server start success')
})

server.listen(9090)
