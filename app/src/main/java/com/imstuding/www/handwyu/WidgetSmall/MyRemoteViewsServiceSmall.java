package com.imstuding.www.handwyu.WidgetSmall;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class MyRemoteViewsServiceSmall extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactorySmall(this.getApplicationContext(), intent);
    }
}
