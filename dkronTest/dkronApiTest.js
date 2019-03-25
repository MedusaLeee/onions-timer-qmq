const axios = require('axios')
const moment = require('moment')
/*
{
  "executor": "http",
  "executor_config": {
      "method": "GET",
      "url": "http://example.com",
      "headers": "[]",
      "body": "",
      "timeout": "30",
      "expectCode": "200",
      "expectBody": "",
      "debug": "true"
  }
}

 */
// Asia/Shanghai
console.log(moment().utc().format())

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

// const reqBody = {
//     "name": "job-",
//     "schedule": "@at",
//     "timezone": "Asia/Shanghai",
//     "owner": "jianxun",
//     "owner_email": "jianxun@guanghe.tv",
//     "disabled": false,
//     "tags": {
//         "dkron_server": "true"
//     },
//     "retries": 3,
//     "processors": {
//         "log": {
//             "forward": true
//         }
//     },
//     "concurrency": "allow",
//     "executor": "shell",
//     "executor_config": {
//         "command": "echo 'Hello from Dkron'"
//     }
// }
const nowStr = moment().add(20 , 's').utc().format()

reqBody.name = reqBody.name + Date.now()
reqBody.schedule = reqBody.schedule + ' ' + nowStr
// reqBody.schedule = "@every 10s"
reqBody.executor_config.url = reqBody.executor_config.url + '?time=' + nowStr

axios({
    method: 'post',
    url: 'http://localhost:8080/v1/jobs',
    data: reqBody,
    headers: {
        'content-type': 'application/json;charset=utf-8'
    }
    })
    .then(function (response) {
        console.log(response);
    })
    .catch(function (error) {
        console.log(error);
    });
