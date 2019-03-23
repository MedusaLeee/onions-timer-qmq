const AMQP = require('amqplib')
const config = require('config')

const mqUrl = config.get('mqUrl')

const getMqChannel = async () => {
    const conn = await AMQP.connect(mqUrl)
    conn.on('error', (err) => {
        console.error(err)
        throw new Error('timer consumer connection error')
    })
    conn.on('close', () => {
        throw new Error('timer consumer connection close')
    })
    const channel = await conn.createChannel()
    return channel
}

module.exports = {
    getMqChannel
}