package ru.itis.android.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SampleReceiver: BroadcastReceiver() {

    override fun onReceive(ctx: Context?, intent: Intent?) {
        println("TEST TAG - Receiver called")
    }

}