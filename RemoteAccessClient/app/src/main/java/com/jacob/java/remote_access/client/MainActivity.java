package com.jacob.java.remote_access.client;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jacob.java.remote_access.client.entity.Client;
import com.jacob.java.remote_access.client.entity.MovementDirection;
import com.jacob.java.remote_access.client.entity.RemoteAccessPacket;
import com.jacob.java.remote_access.client.entity.scroll.InfiniteScrollLinearLayoutManager;
import com.jacob.java.remote_access.client.entity.scroll.ItemClickListener;
import com.jacob.java.remote_access.client.entity.scroll.MouseScrollAdapter;
import com.jacob.java.remote_access.client.entity.scroll.ScrollListener;
import com.jacob.java.remote_access.client.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * https://stackoverflow.com/questions/40584424/simple-android-recyclerview-example
 */
public class MainActivity extends AppCompatActivity implements ItemClickListener, SensorEventListener {
    private MouseScrollAdapter scrollAdapter;
    private static final int SCROLL_TEXT_NUM = 20;
    private RemoteAccessPacket packet;
    private Client client;
    private Thread initClientThread;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private double accelerateDX = 0;
    private double accelerateDY = 0;
//    private double accelerateDZ = 0;
    private long lastTimestamp = 0;
    private double vx = 0;    // Right: positive; Left: negative
    private double vy = 0;    // Forward: positive; Backward: negative
    public static final double METER_TO_PIXEL = 6193.548387;    // Number of pixels in 1 meter (measured by my computer)
    public static final int CALCULATION_ACCURACY = 5;    // Number of decimal places we round the double to before calculations
    /*
    In ms^-2
     */
    public static final double NO_MOVEMENT_THRESHOLD = 0.001;    // Threshold acceleration (sum of accelerations in axes) value for no movement of a device
    private int resetCounter = 0;
    public static final int RESET_COUNT = 2;    // Number of events needed to handle before resetting the velocities of this device
    public static final double MOVING_SPEED = 1;    // The speed of moving a mouse (not accurate to calculate the displacement of mouse) in ms^-1
    private MovementDirection directionX = MovementDirection.NEUTRAL;    // Right: positive; Left: negative
    private MovementDirection directionY = MovementDirection.NEUTRAL;    // Forward: positive; Backward: negative

    /*
    TODO: To be changed to configurable
     */
    public static final String SERVER_IP = "10.0.2.2" ;    // Localhost (Emulator)
    public static final int PORT = 5003;

    /**
     * Suppressed any user accessibility warnings
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Create RemoteAccessPacket
        packet = new RemoteAccessPacket(false, false, false, 0, 0, 0, false);

        try {
            initClient();

            initClientThread.join();
        } catch (InterruptedException e) {
            System.err.println("Unable to establish a connection to server: " + e.getMessage());
            System.exit(1);
        }

        System.out.println("Client after initClient = " + client);

        // Populate scroll
        List<String> scrollRowsData = new ArrayList<String>();
        for(int i = 0; i < SCROLL_TEXT_NUM; i++) {
            scrollRowsData.add(getString(R.string.scroll_text));
        }

        // Populate scroll adapter
        RecyclerView btnScroll = findViewById(R.id.scroll);
        btnScroll.setLayoutManager(new InfiniteScrollLinearLayoutManager(this));
        scrollAdapter = new MouseScrollAdapter(this, scrollRowsData);
        scrollAdapter.setClickListener(this);
        btnScroll.setAdapter(scrollAdapter);    // For clicking
        try {
            btnScroll.addOnScrollListener(new ScrollListener(packet, client));    // For scrolling
        } catch (Exception e) {
            System.err.println("Error in adding OnScrollListener: " + e.getMessage());
            System.exit(1);
        }
        btnScroll.scrollToPosition((int) (Integer.MAX_VALUE / 2));    // Initial position is middle

        // add divider between each scroll item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(btnScroll.getContext(),
                (new LinearLayoutManager(this)).getOrientation());
        btnScroll.addItemDecoration(dividerItemDecoration);

        // Left button
        AppCompatButton btnLeft = findViewById(R.id.btn_left);
        btnLeft.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                // Touch button
                packet.setIsButton1Down(true);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                // Release button
                packet.setIsButton1Down(false);
            } else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
                // Release button
                packet.setIsButton1Down(false);
            }

            // Send packet
            sendPacket();

            return true;
        });

        // Right button
        AppCompatButton btnRight = findViewById(R.id.btn_right);
        btnRight.setOnTouchListener((view, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                // Touch button
                packet.setIsButton3Down(true);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                // Release button
                packet.setIsButton3Down(false);
            } else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
                // Release button
                packet.setIsButton3Down(false);
            }

            // Send packet
            sendPacket();

            return true;
        });

        // Register accelerometer
        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get the accelerometer sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer == null) {
            // Device does not have an accelerometer
            Toast.makeText(this, "Device does not have an accelerometer", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update button 2
     * All function parameters below are dummy in this project
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(this, "You clicked " + scrollAdapter.getItem(position), Toast.LENGTH_SHORT).show();
        packet.setIsButton2Down(true);
        sendPacket();
        packet.setIsButton2Down(false);
        sendPacket();
    }

    /**
     * Establish a connection to server with a separate thread
     *
     * @throws InterruptedException
     */
    private void initClient() throws InterruptedException {
        initClientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Create Client
                try {
                    client = new Client(SERVER_IP, PORT, packet);
                } catch (IOException e) {
                    System.err.println("Cannot establish client connection: " + e.getMessage());
                    System.exit(1);
                }
            }
        });
        initClientThread.start();
    }

    /**
     * Send packet to server
     */
    private void sendPacket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(client == null) {
                    System.out.println("Null client");
                    return;
                }

                client.sendPacket();
            }
        }).start();
    }

    /**
     * Do something when we detect a change in the accelerometer sensor
     *
     * @param event the {@link android.hardware.SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // 3 linear accelerations in the axes [x, y, z]
            accelerateDX = Utils.roundToDecimal(event.values[0], CALCULATION_ACCURACY);    // Left: negative; Right: positive
            accelerateDY = Utils.roundToDecimal(event.values[1], CALCULATION_ACCURACY);    // Forward: positive; Backward: negative (need to reverse this value before sending to server)
//            accelerateDZ = Utils.roundToDecimal(event.values[2], CALCULATION_ACCURACY);    // We do not care about it


            // Calculate change in displacement
//            long currentTimestamp = System.currentTimeMillis();
            long currentTimestamp = event.timestamp;

            if(lastTimestamp != 0) {
                // Not the first event
                // We skip the first event
                double timeInterval = Utils.roundToDecimal((currentTimestamp - lastTimestamp) / Math.pow(10, 9), CALCULATION_ACCURACY);    // In seconds (currentTimestamp is in milliseconds)

                // Forward: positive; Backward: negative
                // Right: positive; Left: negative

                // Calculate the displacement in x-, y-coordinates
                // We will not calculate it in the Physics way
                double displacementX = 0;
                double displacementY = 0;
//                double displacementX = Utils.getDisplacement(vx, accelerateDX, timeInterval);    // In m
//                double displacementY = Utils.getDisplacement(vy, accelerateDY, timeInterval);    // In m

                // Calculate the updated velocity
                double finalVX = Utils.getFinalVelocity(vx, accelerateDX, timeInterval);    // In ms^-1
                double finalVY = Utils.getFinalVelocity(vy, accelerateDY, timeInterval);    // In ms^-1

                // Check if device is stationary
                if(directionX == MovementDirection.NEUTRAL) {
                    // The device was stationary
                    if(Math.abs(accelerateDX) > NO_MOVEMENT_THRESHOLD) {
                        if(accelerateDX < 0) {
                            // Left
                            directionX = MovementDirection.NEGATIVE;
                        } else {
                            // Right
                            directionX = MovementDirection.POSITIVE;
                        }
                    }
                    if(Math.abs(accelerateDY) > NO_MOVEMENT_THRESHOLD) {
                        if(accelerateDY < 0) {
                            // Backward
                            directionY = MovementDirection.NEGATIVE;
                        } else {
                            // Forward
                            directionX = MovementDirection.POSITIVE;
                        }
                    }
                } else {
                    // The device was moving
                    if(Math.abs(finalVX) <= MOVING_SPEED) {
                        directionX = MovementDirection.NEUTRAL;
                        finalVX = 0;
                    }
                    if(Math.abs(finalVY) < MOVING_SPEED) {
                        directionY = MovementDirection.NEUTRAL;
                        finalVY = 0;
                    }
                }


                if(directionX == MovementDirection.NEUTRAL && directionY == MovementDirection.NEUTRAL) {
                    // Update velocity for next timestamp
                    vx = 0;
                    vy = 0;
                    // Do not send packets to reduce workload of client and server
                    return;
                } else {
                    // Update velocity for next timestamp
                    vx = Utils.roundToDecimal(finalVX, CALCULATION_ACCURACY);
                    vy = Utils.roundToDecimal(finalVY, CALCULATION_ACCURACY);
                }


                // Send self-calculated displacement instead of real displacement to reduce calculation and measurement errors
                if(directionX == MovementDirection.NEGATIVE) {
                    // Left
                    displacementX = -1 * Math.abs(Math.tanh(finalVX)) * MOVING_SPEED;
                } else if(directionX == MovementDirection.POSITIVE) {
                    // Right
                    displacementX = Math.abs(Math.tanh(finalVX)) * MOVING_SPEED;
                } else {
                    // Do nothing
                }

                // Need to reverse number sign cause server has a reflected scale
                if(directionY == MovementDirection.NEGATIVE) {
                    // Backward
                    displacementY = Math.abs(Math.tanh(finalVY)) * MOVING_SPEED;
                } else if(directionX == MovementDirection.POSITIVE) {
                    // Forward
                    displacementY = -1 * Math.abs(Math.tanh(finalVY)) * MOVING_SPEED;
                } else {
                    // Do nothing
                }

                // Send to server
                packet.setDeltaMouseCoordinates(Utils.roundToDecimal(displacementX * METER_TO_PIXEL, 0), Utils.roundToDecimal(displacementY * METER_TO_PIXEL, 0));
                sendPacket();
            }

            lastTimestamp = currentTimestamp;

            if(resetCounter >= RESET_COUNT) {
                // Reset mouse status
                resetMouseStatus();
                resetCounter = 0;
            }

            resetCounter++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing here
    }

    /**
     * Register a listener for the accelerometer when the device becomes active, e.g. after pause/restart
     */
    @Override
    protected void onResume() {
        super.onResume();

        if(accelerometer != null) {
            // Register the sensor listeners when the current device becomes active
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }

    }

    /**
     * Unregister the sensor when paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listeners
        sensorManager.unregisterListener(this);
    }

    /**
     * Prevent velocities from building up
     */
    private void resetMouseStatus() {
        accelerateDX = 0;
        accelerateDY = 0;
        vx = 0;
        vy = 0;
        directionX = MovementDirection.NEUTRAL;
        directionY = MovementDirection.NEUTRAL;
    }
}