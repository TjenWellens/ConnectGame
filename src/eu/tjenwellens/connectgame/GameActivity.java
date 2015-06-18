package eu.tjenwellens.connectgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.*;
import android.widget.Toast;

public class GameActivity extends Activity implements SensorEventListener
{

    private SensorManager sensorManager;
    private ChooseOptionsGame game;
    private int prevOrientation;
    private long lastShakeTimestamp = 0;
    private long gap = 500;
    private double threshold = 2.00d * 2.00d * SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        preInit();

        // check which gamemode to start
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            int cols = extras.getInt("cols", 3);
            int rows = extras.getInt("rows", 3);
            int connect = extras.getInt("connect", 3);
            boolean gravity = extras.getBoolean("gravity", false);
            boolean enclose = extras.getBoolean("enclose", false);
            boolean rotationGravity = extras.getBoolean("rotationGravity", false);

            game = new ChooseOptionsGame(this, cols, rows, connect, gravity, enclose, rotationGravity);

        } else
        {
            game = new ChooseOptionsGame(this, 3, 3, 3, false, false, false);
        }
        postInit();
    }

    private void preInit()
    {
        // set all rotations possible: 0, 90, 180, 270
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        fullscreen();
        // set listeners
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    private void fullscreen()
    {
        // hide titlebar --> fullscreen
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    private void unfullscreen()
    {
        // hide titlebar --> not fullscreen
//        requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    private void postInit()
    {
        setContentView(game);
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        prevOrientation = display.getRotation();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is killed and restarted.
        int rows = game.getRows();
        savedInstanceState.putInt("rows", rows);
        int cols = game.getCols();
        savedInstanceState.putInt("cols", cols);
        Cell[][] cells = game.getCells();
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                savedInstanceState.putInt("cells[" + row + "][" + col + "]", cells[row][col].getSort());
            }
        }
        int connect = game.getConnect();
        savedInstanceState.putInt("connect", connect);
        boolean gravity = game.isGravity();
        savedInstanceState.putBoolean("gravity", gravity);
        boolean enclose = game.isEnclose();
        savedInstanceState.putBoolean("enclose", enclose);
        boolean rotateGravity = game.isRotateGravity();
        savedInstanceState.putBoolean("rotateGravity", rotateGravity);
        savedInstanceState.putInt("prevRotation", prevOrientation);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        preInit();
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        int rows = savedInstanceState.getInt("rows");
        int cols = savedInstanceState.getInt("cols");
        Cell[][] cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                int sort = savedInstanceState.getInt("cells[" + row + "][" + col + "]");
                switch (sort)
                {
                    case 1:
                        cells[row][col] = Cross.getInstance();
                        break;
                    case 2:
                        cells[row][col] = Circle.getInstance();
                        break;
                    default:
                        cells[row][col] = Empty.getInstance();
                }
                cells[row][col] = Circle.getInstance();
            }
        }
        int connect = savedInstanceState.getInt("cols");
        boolean gravity = savedInstanceState.getBoolean("gravity");
        boolean enclose = savedInstanceState.getBoolean("enclose");
        boolean rotateGravity = savedInstanceState.getBoolean("rotateGravity");
        this.prevOrientation = savedInstanceState.getInt("prevRotation");
        game = new ChooseOptionsGame(this, cells, cols, rows, connect, gravity, enclose, rotateGravity);
        postInit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
//        setContentView(R.layout.myLayout);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        unfullscreen();
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu)
    {
        fullscreen();
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.resetItem:
                Toast.makeText(this, "You pressed reset!", Toast.LENGTH_LONG).show();
                game.resetGameBoard();
                break;
            case R.id.settings:
                openSettings();
                break;
            case R.id.infoItem:
                Toast.makeText(this, R.string.infoMessage, Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void openSettings()
    {
        Intent intent = new Intent(this, eu.tjenwellens.connectgame.SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            checkShake(sensorEvent);
//            checkOrientation();
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            checkOrientation();
        }
    }

    private void checkShake(SensorEvent e)
    {
        double netForce = e.values[0] * e.values[0];

        netForce += e.values[1] * e.values[1];
        netForce += e.values[2] * e.values[2];
        long now = SystemClock.uptimeMillis();
        if (threshold < netForce)
        {
            lastShakeTimestamp = now;
        } else
        {
            if (lastShakeTimestamp > 0 && now - lastShakeTimestamp > gap)
            {
                lastShakeTimestamp = 0;
                game.resetGameBoard();
            }
        }
    }

    private void checkOrientation()
    {
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int newOrientation = display.getRotation();

        if (newOrientation != prevOrientation)
        {
//            /*
            // <editor-fold>
            switch (newOrientation)
            {
                case Surface.ROTATION_0:
                    switch (prevOrientation)
                    {
                        case Surface.ROTATION_0:
                            // do nothing
                            break;
                        case Surface.ROTATION_90:
                            game.rotateClockwise();
                            break;
                        case Surface.ROTATION_180:
                            game.rotate180(newOrientation);
                            break;
                        case Surface.ROTATION_270:
                            game.rotateCounterClockwise();
                            break;
                    }
                    break;
                case Surface.ROTATION_90:
                    switch (prevOrientation)
                    {
                        case Surface.ROTATION_0:
                            game.rotateCounterClockwise();
                            break;
                        case Surface.ROTATION_90:
                            // do nothing
                            break;
                        case Surface.ROTATION_180:
                            game.rotateClockwise();
                            break;
                        case Surface.ROTATION_270:
                            game.rotate180(newOrientation);
                            break;
                    }
                    break;
                case Surface.ROTATION_180:
                    switch (prevOrientation)
                    {
                        case Surface.ROTATION_0:
                            game.rotate180(newOrientation);
                            break;
                        case Surface.ROTATION_90:
                            game.rotateCounterClockwise();
                            break;
                        case Surface.ROTATION_180:
                            // do nothing
                            break;
                        case Surface.ROTATION_270:
                            game.rotateClockwise();
                            break;
                    }
                    break;
                case Surface.ROTATION_270:
                    switch (prevOrientation)
                    {
                        case Surface.ROTATION_0:
                            game.rotateClockwise();
                            break;
                        case Surface.ROTATION_90:
                            game.rotate180(newOrientation);
                            break;
                        case Surface.ROTATION_180:
                            game.rotateCounterClockwise();
                            break;
                        case Surface.ROTATION_270:
                            // do nothing
                            break;
                    }
                    break;
            }

            //</editor-fold>
//            */
        }
        prevOrientation = newOrientation;
    }

    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // ignore
    }
}