package com.imstuding.www.handwyu.WidgetNotice;

import android.content.Intent;
import android.widget.RemoteViewsService;


/**
 * Created by yangkui on 2018/10/21.
 */

public class MyRemoteViewsServiceNotice extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactoryNotice(this.getApplicationContext(), intent);
    }
}
