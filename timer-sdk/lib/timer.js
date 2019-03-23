const config = require('config')
const httpUtil = require('./httpUtil')

const httpUrl = config.get('timerHttpServerUrl')
const httpAgent = httpUtil.build(httpUrl)

const CHECK_APP_ID_PATH = '/timers/check'
const PUT_MESSAGE_PATH = '/timers'

class OnionTimer {
    constructor() {
        this.appId = null
    }
    // 实例化时检查 appId
    async init (appId) {
        this.appId = appId
        await this.checkAppId()
    }
    // message为字符串最大 32K
    async sendMessage (message, jobAt) {
        const body = {
            appId: this.appId,
            jobAt,
            data: message
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
}