package cs446.mindme;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class WidgetService extends Service {

    private static WidgetService widgetService;
    private WindowManager windowManager;
    private View chatHead;
    private TextView numberWidget;
    private int number = 0;
    private WindowManager.LayoutParams params;

    public static WidgetService getWidgetService() { return widgetService; }

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        widgetService = this;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new View(this);
        //chatHead.setImageResource(R.drawable.widget);
        LayoutInflater inflater = LayoutInflater.from(this);
        chatHead = inflater.inflate(R.layout.widget_layout, null, false);
        numberWidget = (TextView) chatHead.findViewById(R.id.numberWidget);
        //chatHead.inflate(this, R.layout.widget_layout, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 50;
        params.y = 100;

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });

        windowManager.addView(chatHead, params);

        toggleVisibility(false);
    }

    public void toggleVisibility(final boolean visible) {
        MainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatHead.setVisibility(visible ? View.VISIBLE : View.GONE);
                if (!visible) {
                    number = 0;
                }
            }
        });
    }

    public void incrementNumber() {
        if (chatHead == null) {
            return;
        }
        MainActivity.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleVisibility(true);
                number = number + 1;
                numberWidget.setText("" + number);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) windowManager.removeView(chatHead);
    }
}
