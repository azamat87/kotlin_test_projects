package mars.studio.findphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by Asus on 11.03.2018.
 */
class StartWithOS: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action == "android.intent.action.BOOT_COMPLETED"){
            val inte = Intent(context, MyService::class.java)
            context!!.startService(inte)
        }
    }
}