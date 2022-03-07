package cn.weli.mediaplayer.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BroadcastHelper {

    /**
     * 发送广播
     * @param action
     */
    public static void sendBroadcast(Context context, String action){
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    /**
     * 发送广播
     * @param action
     */
    public static void sendBroadcast(Context context, int position,int type ,String action){
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putInt("type",type);
        Intent intent = new Intent(action);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }



    /**
     * 注册接收者
     * @param context
     * @param receiver
     * @param actions
     */
    public static void registReceiver(Context context, BroadcastReceiver receiver, String... actions){
        IntentFilter filter = new IntentFilter();
        for(String action : actions){
            filter.addAction(action);
        }
        context.registerReceiver(receiver,filter);
    }

}
