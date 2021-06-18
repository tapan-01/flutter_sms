package com.babariviere.sms.status;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;

import com.babariviere.sms.permisions.Permissions;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

/**
 * Created by Joan Pablo on 4/17/2018.
 */

public class SmsStateHandler implements EventChannel.StreamHandler, PluginRegistry.RequestPermissionsResultListener {

    private BroadcastReceiver smsStateChangeReceiver;
    final private PluginRegistry.Registrar registrar;
    //private Permissions permissions;
    EventChannel.EventSink eventSink;

    public SmsStateHandler(PluginRegistry.Registrar registrar) {
        this.registrar = registrar;
        this.permissions = new Permissions(registrar.activity());
        registrar.addRequestPermissionsResultListener(this);
    }

    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        this.eventSink = eventSink;
        smsStateChangeReceiver = new SmsStateChangeReceiver(eventSink);
        registerDeliveredReceiver();
        registerSentReceiver();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void registerDeliveredReceiver() {
        registrar.context().registerReceiver(
                smsStateChangeReceiver,
                new IntentFilter("SMS_DELIVERED"));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void registerSentReceiver() {
        registrar.context().registerReceiver(
                smsStateChangeReceiver,
                new IntentFilter("SMS_SENT"));
    }

    @Override
    public void onCancel(Object o) {
        registrar.context().unregisterReceiver(smsStateChangeReceiver);
        smsStateChangeReceiver = null;
    }


}
