const _ = require('lodash')
const mq = require('./mq')
const config = require('config')
const httpUtil = require('./httpUtil')

const httpUrl = config.get('timerHttpServerUrl')
const httpAgent = httpUtil.build(httpUrl)

const CHECK_APP_ID_PATH = '/timers/check'
const PUT_MESSAGE_PATH = '/timers'

class OnionTimer {
    constructor(appId) {
        this.appId = appId
        this.mqName = `timer-${appId}`
        this.init()
    }
    // 实例化时检查 appId
    init () {
        this.checkAppId().then()
    }
    // message为字符串最大 32K
    async sendMessage (message, jobAt) {
        const body = {
            appId: this.appId,
            jobAt,
            data: JSON.stringify(message)
        }
        try {
            const resp = await httpAgent.post(PUT_MESSAGE_PATH, body)
            if (resp.status === 200) {
                return true
            }
            return false
        } catch (error) {
            if (error.response) {
                console.error(error.response);
                return false
            }
            if (error.request) {
                console.error(error.request);
                return false
            }
            console.error('Error', error.message);
            return false
        }

    }
    async checkAppId () {
        // try {
        //     const { data } = await httpAgent.get(`${CHECK_APP_ID_PATH}?r=${Math.random()}`)
        //     // TODO 请传入正确的AppId
        // } catch (e) {
        //     throw new Error('OnionTimer server error...')
        // }
    }
    consume (handler, prefetch = 5) {
        if (typeof handler !== 'function') {
            throw new Error('handler is not a function')
        }
        if (!_.isInteger(prefetch)) {
            throw new Error('prefetch type is not int')
        }
        const mqName = this.mqName
        mq.getMqChannel().then(channel => {
            channel.prefetch(prefetch)
            channel.consume(mqName, async (msg) => {
                await handler(msg, channel.ack.bind(channel))
            }, { noAck: false })
        })
    }
}

module.exports = OnionTimer