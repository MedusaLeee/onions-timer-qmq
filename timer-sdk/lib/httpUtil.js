const axios = require('axios')
const httpHelper = require('http')
const https = require('https')
const axiosRetry = require('axios-retry')

const build = (baseUrl) => {
    const instance = axios.create({
        baseURL: baseUrl,
        timeout: 8000,
        httpAgent: new httpHelper.Agent({ keepAlive: true }),
        httpsAgent: new https.Agent({ keepAlive: true })
    })
    axiosRetry(instance, { retries: 3, retryDelay: () => 1000 })
    const get = async (path, headers = {}) => {
        if (!headers['Content-type']) {
            headers['Content-type'] = 'application/json;charset=utf-8'
        }
        return instance.get(path, { headers })
    }
    const post = async (path, body, headers = {}) => {
        if (!headers['Content-type']) {
            headers['Content-type'] = 'application/json;charset=utf-8'
        }
        return instance.post(path, body, { headers })
    }

    return {
        get,
        post
    }
}

module.exports = {
    build
}
