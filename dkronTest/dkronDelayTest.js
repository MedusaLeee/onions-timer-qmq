const axios = require('axios')
const Promoise = require('bluebird')
const _ = require('lodash')
const moment = require('moment')

const reqBody = {
    "name": "job-",
    "schedule": "@at",
    "timezone": "Asia/Shanghai",
    "owner": "jianxun",
    "owner_email": "jianxun@guanghe.tv",
    "disabled": false,
    "tags": {
        "dkron_server": "true"
    },
    "retries": 3,
    "processors": {
        "log": {
            "forward": true
        }
    },
    "concurrency": "allow",
    "executor": "http",
    "executor_config": {
        "method": "GET",
        "url": "http://koa:9090",
        "headers": "[]",
        "body": "",
        "timeout": "30",
        "expectCode": "200",
        "expectBody": "",
        "debug": "true"
    }
}

const nowStr = moment().add(20 , 's').utc().format()

let idPrefix = Math.random().toString() + '-'  + 1000 + '-'

const reqArr = []

for(let a = 1; a <= 1000; a ++) {
    const reqCopy = _.cloneDeep(reqBody)
    reqCopy.name = reqCopy.name + idPrefix + a
    reqCopy.schedule = reqCopy.schedule + ' ' + nowStr
    reqCopy.executor_config.url = reqCopy.executor_config.url + '?time=' + nowStr
    reqArr.push(reqCopy)
}


Promoise.map(reqArr, (body) => {
    return axios({
        method: 'post',
        url: 'http://localhost:8080/v1/jobs',
        data: body,
        headers: {
            'content-type': 'application/json;charset=utf-8'
        }
    })
}, { concurrency: 200 }).then(function (respArr) {
    console.log(respArr.length);
})
.catch(function (error) {
    console.log(error);
});


