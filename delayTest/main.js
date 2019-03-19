const axios = require('axios')
const moment = require('moment')
const Promise = require('bluebird')
// http://localhost:9090/async?time=2019-03-15 14:06:43&index=9999
const main = async () => {
    const DIFF = 30
    // 并行数量
    const CONCURRENCY = 450
    // 总数量
    const BATCH_NUM= 10000
    const format = 'YYYY-MM-DD HH:mm:ss'
    const nowStr = moment().add(DIFF, 's').format(format)
    const url = `http://localhost:9090/async?time=${nowStr}`
    const arr = []
    for(let i = 0; i < BATCH_NUM; i += 1) {
        const newUrl = url + "&index=" + i
        console.log(newUrl)
        arr.push(newUrl)
    }
    await Promise.map(arr, async u => axios.get(u), { concurrency: CONCURRENCY })
}

main().then()
