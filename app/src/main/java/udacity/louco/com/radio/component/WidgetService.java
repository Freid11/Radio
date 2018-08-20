package udacity.louco.com.radio.component;

import android.content.Intent;
import android.widget.RemoteViewsService;

import udacity.louco.com.radio.ui.adapter.FactoryWidgetList;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FactoryWidgetList(getApplicationContext());
    }
}
