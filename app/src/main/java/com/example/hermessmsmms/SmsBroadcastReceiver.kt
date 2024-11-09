package com.example.hermessmsmms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage

class SmsBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            return
        }
        val rawSmsChunks: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val smsInfos: List<SmsInfo> = groupSmsBySender(rawSmsChunks)
        for (sms in smsInfos) {
            Intent().also { i ->
                i.setAction("hermes.sms.received")
                i.setPackage("hermes.sms.")
                i.putExtra("SMS_INFO", sms)
                i.setClass(context, MainActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // println("broadcasting $sms")
                // context.sendBroadcast(i)
                context.startActivity(i)
            }
        }
    }

    private fun groupSmsBySender(rawSmsChunks: Array<SmsMessage>) = rawSmsChunks
        .groupBy(
            keySelector = { it.displayOriginatingAddress },
            valueTransform = { it.displayMessageBody })
        .mapValues { it.value.joinToString("") }
        .map { entry -> SmsInfo(entry.key, entry.value) }
}