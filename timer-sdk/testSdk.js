const Timer = require('./index')
const moment = require('moment')

const timer = new Timer('test01')

console.log(timer)

const message = {
    id: 1,
    name: 'tom'
}
const DIFF = 30
const format = 'YYYY-MM-DD HH:mm:ss'
const jobAt = moment().add(DIFF, 's').format(format)
timer.sendMessage(message, jobAt).then()