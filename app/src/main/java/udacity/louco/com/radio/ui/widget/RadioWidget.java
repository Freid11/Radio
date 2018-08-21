package udacity.louco.com.radio.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import udacity.louco.com.radio.R;
import udacity.louco.com.radio.component.WidgetService;
import udacity.louco.com.radio.mvp.model.db.ContentDao;
import udacity.louco.com.radio.mvp.model.db.RadioDatabase;
import udacity.louco.com.radio.mvp.model.object.Radio;

/**
 * Implementation of App Widget functionality.
 */
public class RadioWidget extends AppWidgetProvider {

    public static String ACTION_KEY = "android.appwidget.action.APPWIDGET_UPDATE";

    public static List<Radio> radioList = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.radio_widget);

        fillList(context, views);

        Intent intent = new Intent(context , RadioWidget.class);
        intent.setAction("Radio");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        views.setPendingIntentTemplate(R.id.lv_radio, pendingIntent);

        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, RadioWidget.class));

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_radio);

    }

    private static void fillList(Context context, RemoteViews views) {
        Intent active = new Intent(context, WidgetService.class);
        views.setRemoteAdapter(R.id.lv_radio, active);
    }

    private class getListRadio extends AsyncTask<Context, Void, Void>{

        @Override
        protected Void doInBackground(Context... contexts) {
            Context context = contexts[0];
            RadioDatabase mRadioDatabase;
            ContentDao mContentDao;

            mRadioDatabase = RadioDatabase.getInstance(context);
            mContentDao = mRadioDatabase.getContent();

            radioList = mContentDao.getFavoriteNotLiveData();

            Intent intent = new Intent(RadioWidget.ACTION_KEY);
            intent.setAction(RadioWidget.ACTION_KEY);
            context.sendBroadcast(intent);
            return null;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Louco", "Receive");

        String action = intent.getAction();
        Radio radio = intent.getParcelableExtra(Radio.KEY_BUNDLE);
        if(radio !=null) {
            Log.d("Louco", radio.getName());
            Radio.sRadio.onNext(radio);
        }

        if(action.equals(ACTION_KEY)){

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RadioWidget.class));
            for(int appWdgepid : appWidgetIds){
                updateAppWidget(context, appWidgetManager, appWdgepid);
            }

        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            new getListRadio().execute(context);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }
}

