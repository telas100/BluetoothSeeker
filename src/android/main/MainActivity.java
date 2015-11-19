package android.main;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static ListView mListBluetooth = null;
    private static ArrayAdapter mArrayAdapter = null;
    private static List<String> mBluetoothDevices = null;
    private static BluetoothAdapter mBluetoothAdapter = null;
    private static final IntentFilter bluetoothFilter 
            = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    private static final BroadcastReceiver mBluetoothReceiver 
            = new BroadcastReceiver(){
                @Override
                public void onReceive(Context context, Intent intent) {
                    String  action = intent.getAction();
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Toast.makeText(context,device.getName(),Toast.LENGTH_LONG).show();
                        if(!mBluetoothDevices.contains(device.getName())){
                            mBluetoothDevices.add(device.getName());
                            mArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            };
  
    private void bluetoothDeviceLookout(){
        if(mBluetoothAdapter == null) {
            Toast.makeText(this,"Impossible d'activer le Bluetooth",Toast.LENGTH_LONG).show();
            this.finish();
        }
        if (mBluetoothAdapter != null){
            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 100);
            }
            if(mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.startDiscovery();
                registerReceiver(mBluetoothReceiver, bluetoothFilter);
            }
            else
                this.finish();
        }
        else
            this.finish();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mListBluetooth = (ListView) findViewById(R.id.listBluetooth);
        mBluetoothDevices = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mBluetoothDevices);
        mListBluetooth.setAdapter(mArrayAdapter);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();        
    }
    
    @Override
    public void onStart() {
        super.onStart();
        mBluetoothDevices.clear();
        this.bluetoothDeviceLookout();        
    }
  
    @Override
    protected void onResume() {
        super.onResume();
        mBluetoothDevices.clear();
        this.bluetoothDeviceLookout();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(mBluetoothReceiver);
    }
  
    @Override
    public void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mBluetoothReceiver);
    }
}
