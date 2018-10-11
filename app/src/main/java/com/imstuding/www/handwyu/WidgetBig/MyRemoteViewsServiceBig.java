package com.imstuding.www.handwyu.WidgetBig;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MyRemoteViewsServiceBig extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactoryBig(this.getApplicationContext(), intent);
    }
}
