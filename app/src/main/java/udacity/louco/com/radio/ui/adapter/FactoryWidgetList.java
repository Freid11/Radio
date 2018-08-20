package udacity.louco.com.radio.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import java.util.List;

import udacity.louco.com.radio.R;
import udacity.louco.com.radio.mvp.model.object.Radio;
import udacity.louco.com.radio.ui.widget.RadioWidget;

public class FactoryWidgetList implements RemoteViewsFactory {

    private Context context;
    private List<Radio> radioList;

    public FactoryWidgetList(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        radioList = RadioWidget.radioList;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return radioList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_view_adapter);
        remoteViews.setTextViewText(R.id.tv_name_radio_widget, radioList.get(position).getName());

        Bundle extras = new Bundle();
        extras.putParcelable(Radio.KEY_BUNDLE, radioList.get(position));
        Intent clickIntent = new Intent();
        clickIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.tv_name_radio_widget, clickIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
