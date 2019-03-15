const axios = require('axios')
const moment = require('moment')
const Promise = require('bluebird')

const main = async () => {
    const DIFF = 30
    const BATCH_NUM= 1000
    const format = 'YYYY-MM-DD HH:mm:ss'
    const nowStr = moment().add(DIFF, 's').format(format)
    const url = `http://localhost:9090/delay?time=${nowStr}`
    const arr = []
    for(let i = 0; i < BATCH_NUM; i += 1) {
        console.log(url)
        arr.push(url)
    }
    await Promise.map(arr, async u => axios.get(u), { concurrency: 200 })
}

main().then()