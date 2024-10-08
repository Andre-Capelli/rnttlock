package com.reactnativettlock;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.reactnativettlock.util.Utils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactnativettlock.model.RNControlAction;
import com.reactnativettlock.model.TTBaseFieldConstant;
import com.reactnativettlock.model.TTGatewayEvent;
import com.reactnativettlock.model.TTKeypadConstant;
import com.reactnativettlock.model.TTKeypadEvent;
import com.reactnativettlock.model.TTLockConfigConverter;
import com.reactnativettlock.model.TTLockErrorConverter;
import com.reactnativettlock.model.TTLockEvent;
import com.reactnativettlock.model.TTLockFieldConstant;
import com.reactnativettlock.model.TTRemoteEvent;
import com.reactnativettlock.util.PermissionUtils;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ActivateLiftFloorsCallback;
import com.ttlock.bl.sdk.callback.AddFingerprintCallback;
import com.ttlock.bl.sdk.callback.AddICCardCallback;
import com.ttlock.bl.sdk.callback.AddRemoteCallback;
import com.ttlock.bl.sdk.callback.ClearAllFingerprintCallback;
import com.ttlock.bl.sdk.callback.ClearAllICCardCallback;
import com.ttlock.bl.sdk.callback.ClearPassageModeCallback;
import com.ttlock.bl.sdk.callback.ClearRemoteCallback;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.callback.CreateCustomPasscodeCallback;
import com.ttlock.bl.sdk.callback.DeleteFingerprintCallback;
import com.ttlock.bl.sdk.callback.DeleteICCardCallback;
import com.ttlock.bl.sdk.callback.DeletePasscodeCallback;
import com.ttlock.bl.sdk.callback.DeleteRemoteCallback;
import com.ttlock.bl.sdk.callback.GetAccessoryBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.GetBatteryLevelCallback;
import com.ttlock.bl.sdk.callback.GetLockConfigCallback;
import com.ttlock.bl.sdk.callback.GetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.GetLockStatusCallback;
import com.ttlock.bl.sdk.callback.GetLockTimeCallback;
import com.ttlock.bl.sdk.callback.RecoverLockDataCallback;
import com.ttlock.bl.sdk.callback.SetLiftControlableFloorsCallback;
import com.ttlock.bl.sdk.callback.SetLiftWorkModeCallback;
import com.ttlock.bl.sdk.callback.VerifyLockCallback;
import com.ttlock.bl.sdk.callback.GetLockVersionCallback;
import com.ttlock.bl.sdk.callback.GetLockSystemInfoCallback;
import com.ttlock.bl.sdk.callback.GetOperationLogCallback;
import com.ttlock.bl.sdk.callback.GetRemoteUnlockStateCallback;
import com.ttlock.bl.sdk.callback.GetUnlockDirectionCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ModifyAdminPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyFingerprintPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyICCardPeriodCallback;
import com.ttlock.bl.sdk.callback.ModifyPasscodeCallback;
import com.ttlock.bl.sdk.callback.ModifyRemoteValidityPeriodCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ResetPasscodeCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.SetAutoLockingPeriodCallback;
import com.ttlock.bl.sdk.callback.SetHotelCardSectorCallback;
import com.ttlock.bl.sdk.callback.SetHotelDataCallback;
import com.ttlock.bl.sdk.callback.SetLightTimeCallback;
import com.ttlock.bl.sdk.callback.SetLockConfigCallback;
import com.ttlock.bl.sdk.callback.SetLockSoundWithSoundVolumeCallback;
import com.ttlock.bl.sdk.callback.SetLockTimeCallback;
import com.ttlock.bl.sdk.callback.SetPassageModeCallback;
import com.ttlock.bl.sdk.callback.SetRemoteUnlockSwitchCallback;
import com.ttlock.bl.sdk.callback.SetUnlockDirectionCallback;
import com.ttlock.bl.sdk.constant.LogType;
import com.ttlock.bl.sdk.device.Remote;
import com.ttlock.bl.sdk.device.WirelessDoorSensor;
import com.ttlock.bl.sdk.device.WirelessKeypad;
import com.ttlock.bl.sdk.entity.AccessoryInfo;
import com.ttlock.bl.sdk.entity.AccessoryType;
import com.ttlock.bl.sdk.entity.ActivateLiftFloorsResult;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.HotelData;
import com.ttlock.bl.sdk.entity.IpSetting;
import com.ttlock.bl.sdk.entity.LockError;
import com.ttlock.bl.sdk.entity.PassageModeConfig;
import com.ttlock.bl.sdk.entity.PassageModeType;
import com.ttlock.bl.sdk.entity.SoundVolume;
import com.ttlock.bl.sdk.entity.TTLiftWorkMode;
import com.ttlock.bl.sdk.entity.TTLockConfigType;
import com.ttlock.bl.sdk.entity.UnlockDirection;
import com.ttlock.bl.sdk.entity.ValidityInfo;
import com.ttlock.bl.sdk.gateway.api.GatewayClient;
import com.ttlock.bl.sdk.gateway.callback.ConfigIpCallback;
import com.ttlock.bl.sdk.gateway.callback.ConnectCallback;
import com.ttlock.bl.sdk.gateway.callback.InitGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanGatewayCallback;
import com.ttlock.bl.sdk.gateway.callback.ScanWiFiByGatewayCallback;
import com.ttlock.bl.sdk.gateway.model.ConfigureGatewayInfo;
import com.ttlock.bl.sdk.gateway.model.DeviceInfo;
import com.ttlock.bl.sdk.gateway.model.GatewayError;
import com.ttlock.bl.sdk.gateway.model.GatewayType;
import com.ttlock.bl.sdk.gateway.model.WiFi;
import com.ttlock.bl.sdk.keypad.InitKeypadCallback;
import com.ttlock.bl.sdk.keypad.ScanKeypadCallback;
import com.ttlock.bl.sdk.keypad.WirelessKeypadClient;
import com.ttlock.bl.sdk.keypad.model.InitKeypadResult;
import com.ttlock.bl.sdk.keypad.model.KeypadError;
import com.ttlock.bl.sdk.remote.api.RemoteClient;
import com.ttlock.bl.sdk.remote.callback.GetRemoteSystemInfoCallback;
import com.ttlock.bl.sdk.remote.callback.InitRemoteCallback;
import com.ttlock.bl.sdk.remote.callback.ScanRemoteCallback;
import com.ttlock.bl.sdk.remote.model.InitRemoteResult;
import com.ttlock.bl.sdk.remote.model.RemoteError;
import com.ttlock.bl.sdk.remote.model.SystemInfo;
import com.ttlock.bl.sdk.util.FeatureValueUtil;
import com.ttlock.bl.sdk.util.GsonUtil;
import com.ttlock.bl.sdk.util.LogUtil;
import com.reactnativettlock.model.TTRemoteFieldConstant;
import com.reactnativettlock.model.TTGatewayFieldConstant;

import java.util.HashMap;
import java.util.List;

@ReactModule(name = "Ttlock")
public class TtlockModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    public static final int TYPE_LOCK = 1;
    public static final int TYPE_PLUG = 2;
    public static final int TYPE_REMOTE = 3;
    public static final int TYPE_DOOR_SENSOR = 4;
    public static final int TYPE_WIRELESS_KEYPAD = 5;
//    private boolean scanGateway;
    private int scanType;
    private static HashMap<String, ExtendedBluetoothDevice> mCachedDevice = new HashMap<>();
    private static HashMap<String, Remote> mCachedRemote = new HashMap<>();
    private HashMap<String, WirelessDoorSensor> mCachedDoorSensor = new HashMap<>();
    private HashMap<String, WirelessKeypad> mCachedKeypad = new HashMap<>();
    private String mac;
    private int totalCnt;
    private boolean flag;

    public TtlockModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        LogUtil.setDBG(true);
    }

    @Override
    public String getName() {
        return "Ttlock";
    }

    /**
     * android 6.0
     */
//    private boolean initPermission() {
//        String permissions[] = {
//                Manifest.permission.ACCESS_FINE_LOCATION
//        };
//        ArrayList<String> toApplyList = new ArrayList<String>();
//        for (String perm : permissions) {
//            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getCurrentActivity(), perm)) {
//                toApplyList.add(perm);
//            }
//        }
//        String tmpList[] = new String[toApplyList.size()];
//        if (!toApplyList.isEmpty()) {
//            ActivityCompat.requestPermissions(getCurrentActivity(), toApplyList.toArray(tmpList), PERMISSIONS_REQUEST_CODE);
//            return false;
//        }
//        return true;
//    }

    @ReactMethod
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    for (int i=0;i<permissions.length;i++) {
                        if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[i]) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // permission was granted, yay! Do the
                            // contacts-related task you need to do.
                          switch (scanType) {
                            case TYPE_LOCK:
                              startScan();
                              break;
                            case TYPE_PLUG:
                              startScanGateway();
                              break;
                            case TYPE_REMOTE:
                              startScanRemoteKey();
                              break;
                          }
                        } else {
                            // permission denied, boo! Disable the
                            // functionality that depends on this permission.
                        }
                    }
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private ExtendedBluetoothDevice cacheAndFilterScanDevice(ExtendedBluetoothDevice btDevice){
        ExtendedBluetoothDevice newAddDevice = btDevice;
        String lockMac = btDevice.getAddress();
        if(mCachedDevice.isEmpty()){
            mCachedDevice.put(lockMac,btDevice);
        }else {
            ExtendedBluetoothDevice child = mCachedDevice.get(lockMac);
            if(child == null){
                mCachedDevice.put(lockMac,btDevice);
            }else {
                if(newAddDevice.isSettingMode() != child.isSettingMode()){
                    mCachedDevice.remove(lockMac);
                    mCachedDevice.put(lockMac,btDevice);
                }else {
                    newAddDevice = null;
                }
            }
        }

        return newAddDevice;
    }


  //-------------wireless keypad---------------------------
  @ReactMethod
  public void startScanWirelessKeypad() {
    scanType = TYPE_WIRELESS_KEYPAD;
    PermissionUtils.doWithScanPermission(getCurrentActivity(), success -> {
      if (success) {
        mCachedKeypad.clear();
        WirelessKeypadClient.getDefault().startScanKeyboard(new ScanKeypadCallback() {
          @Override
          public void onScanKeyboardSuccess(WirelessKeypad wirelessKeypad) {
            mCachedKeypad.put(wirelessKeypad.getAddress(), wirelessKeypad);
            WritableMap map = Arguments.createMap();
            map.putString(TTKeypadConstant.KEYPAD_NAME, wirelessKeypad.getName());
            map.putString(TTKeypadConstant.KEYPAD_MAC, wirelessKeypad.getAddress());
            map.putInt(TTBaseFieldConstant.RSSI, wirelessKeypad.getRssi());
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTKeypadEvent.ScanKeypad, map);
          }

          @Override
          public void onScanFailed(int error) {

          }
        });
      } else {
        LogUtil.d("no scan permission");
      }
    });
  }

  @ReactMethod
  public void stopScanWirelessKeypad() {
    WirelessKeypadClient.getDefault().stopScanKeyboard();
  }

  @ReactMethod
  public void initWirelessKeypad(String keypadMac, String lockMac, Callback success, Callback fail) {
    WirelessKeypadClient.getDefault().initializeKeypad(mCachedKeypad.get(keypadMac), lockMac,  new InitKeypadCallback() {
      @Override
      public void onInitKeypadSuccess(InitKeypadResult initKeypadResult) {
        WritableArray writableArray = Arguments.createArray();
        writableArray.pushInt(initKeypadResult.getBatteryLevel());
        writableArray.pushString(initKeypadResult.getFeatureValue());
        success.invoke(writableArray);
      }

      @Override
      public void onFail(KeypadError error) {
        fail.invoke(error.getErrorCode(), error.getDescription());
      }
    });
  }


  //---------------remote------------------------------
  @ReactMethod
  public void startScanRemoteKey() {
    scanType = TYPE_REMOTE;
    PermissionUtils.doWithScanPermission(getCurrentActivity(), success -> {
      if (success) {
        mCachedRemote.clear();
        RemoteClient.getDefault().prepareBTService(getCurrentActivity());
        RemoteClient.getDefault().startScan(new ScanRemoteCallback() {
          @Override
          public void onScanRemote(Remote remote) {
            mCachedRemote.put(remote.getAddress(), remote);
            WritableMap map = Arguments.createMap();
            map.putString(TTRemoteFieldConstant.REMOTE_KEY_NAME, remote.getName());
            map.putString(TTRemoteFieldConstant.REMOTE_KEY_MAC, remote.getAddress());
            map.putInt(TTRemoteFieldConstant.RSSI, remote.getRssi());
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTRemoteEvent.ScanRemoteKey, map);
          }
        });
      } else {
        LogUtil.d("no scan permission");
      }
    });
  }

  @ReactMethod
  public void stopScanRemoteKey() {
      RemoteClient.getDefault().stopScan();
  }

  @ReactMethod
  public void initRemoteKey(String remoteMac, String lockData, Callback success, Callback fail) {
      RemoteClient.getDefault().initialize(mCachedRemote.get(remoteMac), lockData, new InitRemoteCallback() {
        @Override
        public void onInitSuccess(InitRemoteResult initRemoteResult) {
          WritableArray writableArray = Arguments.createArray();
          writableArray.pushInt(initRemoteResult.getBatteryLevel());
          writableArray.pushString(GsonUtil.toJson(initRemoteResult.getSystemInfo()));
          success.invoke(writableArray);

        }

        @Override
        public void onFail(RemoteError remoteError) {
          fail.invoke(remoteError.getErrorCode(), remoteError.getDescription());
        }
      });
  }

  @ReactMethod
  public void getRemoteKeySystemInfo(String remoteMac, Callback success, Callback fail) {
      RemoteClient.getDefault().getRemoteSystemInfo(remoteMac, new GetRemoteSystemInfoCallback() {
        @Override
        public void onGetRemoteSystemInfoSuccess(SystemInfo systemInfo) {
          WritableMap map = Arguments.createMap();
          map.putString(TTRemoteFieldConstant.MODEL_NUM, systemInfo.getModelNum());
          map.putString(TTRemoteFieldConstant.HARDWARE_REVISION, systemInfo.getHardwareRevision());
          map.putString(TTRemoteFieldConstant.FIRMWARE_REVISION, systemInfo.getFirmwareRevision());
          success.invoke(map);
        }

        @Override
        public void onFail(RemoteError remoteError) {
          fail.invoke(remoteError.getErrorCode(), remoteError.getDescription());
        }
      });
  }

    //-------------gateway---------------
    @ReactMethod
    public void startScanGateway() {
//        scanGateway = true;
      scanType = TYPE_PLUG;
      PermissionUtils.doWithScanPermission(getCurrentActivity(), success -> {
        if (success) {
          GatewayClient.getDefault().prepareBTService(getCurrentActivity());
          GatewayClient.getDefault().startScanGateway(new ScanGatewayCallback() {
            @Override
            public void onScanGatewaySuccess(ExtendedBluetoothDevice device) {
              WritableMap map = Arguments.createMap();
              map.putString(TTGatewayFieldConstant.GATEWAY_NAME, device.getName());
              map.putString(TTGatewayFieldConstant.GATEWAY_MAC, device.getAddress());
              map.putBoolean(TTGatewayFieldConstant.IS_DFU_MODE, device.isDfuMode());
              map.putInt(TTGatewayFieldConstant.RSSI, device.getRssi());
              map.putInt(TTGatewayFieldConstant.TYPE, device.getGatewayType());
              getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTGatewayEvent.scanGateway, map);
            }

            @Override
            public void onScanFailed(int errorCode) {
              LogUtil.d("errorCode:" + errorCode);
            }
          });
        } else {
          LogUtil.d("no scan permission");
        }
      });

    }

    @ReactMethod
    public void stopScanGateway() {
        GatewayClient.getDefault().stopScanGateway();
    }

    @ReactMethod
    public void connect(String mac, Callback callback) {
//        ExtendedBluetoothDevice device = mCachedDevice.get(mac);
        if (mac == null) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, callback);
            LogUtil.d("mac is null");
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            this.mac = mac;
            //{ code: 0, description: "The bluetooth connect timeout" },
            //      { code: 1, description: "The bluetooth connect success" },
            //      { code: 2, description: "The bluetooth connect fail" }

            //是否已经回调
            flag = false;
            GatewayClient.getDefault().connectGateway(mac, new ConnectCallback() {
              @Override
              public void onConnectSuccess(ExtendedBluetoothDevice device) {
                LogUtil.d("connected:" + device);
                flag = true;
                callback.invoke(1);
              }

              @Override
              public void onDisconnected() {
                try {//过滤掉断开连接的回调
                  if (!flag) {
                    flag = true;
                    callback.invoke(0);
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
          } else {
            if (!flag) {
              flag = true;
              callback.invoke(0);
            }
          }
        });

    }

    @ReactMethod
    public void getNearbyWifi(Callback callback) {//0:完成 1:失败
      if (TextUtils.isEmpty(mac)) {
        LogUtil.d("mac is null");
        return;
      }
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          GatewayClient.getDefault().scanWiFiByGateway(mac, new ScanWiFiByGatewayCallback() {
            @Override
            public void onScanWiFiByGateway(List<WiFi> wiFis) {
              if (wiFis != null) {
                WritableArray writableArray = Arguments.createArray();
                for (WiFi wiFi : wiFis) {
                  WritableMap map = Arguments.createMap();
                  map.putString(TTGatewayFieldConstant.WIFI, wiFi.getSsid());
                  map.putInt(TTGatewayFieldConstant.RSSI, wiFi.getRssi());
                  writableArray.pushMap(map);
                }
                //锁开关状态跟oneMeterRSSI android不需要
                getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTGatewayEvent.scanWifi, writableArray);
              }
            }

            @Override
            public void onScanWiFiByGatewaySuccess() {
              callback.invoke(0);
            }

            @Override
            public void onFail(GatewayError error) {
              callback.invoke(1);
            }
          });
        } else {//未授权
          callback.invoke(1);
        }
      });
    }

    private void configIp(ConfigureGatewayInfo gatewayInfo, IpSetting ipSetting, Callback success, Callback fail) {
      GatewayClient.getDefault().configIp(mac, ipSetting, new ConfigIpCallback() {
        @Override
        public void onConfigIpSuccess() {
          doInitGateway(gatewayInfo, success, fail);
        }

        @Override
        public void onFail(GatewayError gatewayError) {
          fail.invoke(gatewayError.getErrorCode());
        }
      });
    }

    private void doInitGateway(ConfigureGatewayInfo gatewayInfo, Callback success, Callback fail) {
      GatewayClient.getDefault().initGateway(gatewayInfo, new InitGatewayCallback() {
        @Override
        public void onInitGatewaySuccess(DeviceInfo deviceInfo) {
          WritableMap map = Arguments.createMap();
          map.putString(TTGatewayFieldConstant.MODEL_NUM, deviceInfo.getModelNum());
          map.putString(TTGatewayFieldConstant.HARDWARE_REVISION, deviceInfo.getHardwareRevision());
          map.putString(TTGatewayFieldConstant.FIRMWARE_REVISION, deviceInfo.getFirmwareRevision());
          success.invoke(map);
        }

        @Override
        public void onFail(GatewayError error) {
          LogUtil.d("error:" + error.getDescription());
          fail.invoke(error.getErrorCode());
        }
      });
    }

    @ReactMethod
    public void initGateway(ReadableMap readableMap, Callback success, Callback fail) {

        if (readableMap != null) {
            ConfigureGatewayInfo gatewayInfo = new ConfigureGatewayInfo();
            gatewayInfo.plugName = readableMap.getString(TTGatewayFieldConstant.GATEWAY_NAME);
            gatewayInfo.plugVersion = readableMap.getInt(TTGatewayFieldConstant.TYPE);
            if (gatewayInfo.plugVersion == GatewayType.G2) {//G2网关才有WIFI跟WIFI 密码
                gatewayInfo.ssid = readableMap.getString(TTGatewayFieldConstant.WIFI);
                gatewayInfo.wifiPwd = readableMap.getString(TTGatewayFieldConstant.WIFI_PASSWORD);
            }
            gatewayInfo.uid = readableMap.getInt(TTGatewayFieldConstant.TTLOCK_UID);
            gatewayInfo.userPwd = readableMap.getString(TTGatewayFieldConstant.TTLOCK_LOGIN_PASSWORD);
            String serverIp = readableMap.getString(TTGatewayFieldConstant.SERVER_IP);
            String serverPort = readableMap.getString(TTGatewayFieldConstant.SERVER_PORT);
            if (!TextUtils.isEmpty(serverIp)) {
              gatewayInfo.setServer(serverIp);
            }
            if (!TextUtils.isEmpty(serverPort)) {
              gatewayInfo.setPort(Integer.valueOf(serverPort));
            }
            if (readableMap.hasKey(TTGatewayFieldConstant.IP_SETTING_TYPE)) {
              int ipSettingType = readableMap.getInt(TTGatewayFieldConstant.IP_SETTING_TYPE);
              if (ipSettingType == IpSetting.STATIC_IP) {//静态ip
                IpSetting ipSetting = new IpSetting();
                ipSetting.setType(IpSetting.STATIC_IP);

                if (readableMap.hasKey(TTGatewayFieldConstant.IP_ADDRESS)) {
                  ipSetting.setIpAddress(readableMap.getString(TTGatewayFieldConstant.IP_ADDRESS));
                }
                if (readableMap.hasKey(TTGatewayFieldConstant.SUBNET_MASK)) {
                  ipSetting.setSubnetMask(readableMap.getString(TTGatewayFieldConstant.SUBNET_MASK));
                }
                if (readableMap.hasKey(TTGatewayFieldConstant.ROUTER)) {
                  ipSetting.setRouter(readableMap.getString(TTGatewayFieldConstant.ROUTER));
                }
                if (readableMap.hasKey(TTGatewayFieldConstant.PREFERRED_DNS)) {
                  ipSetting.setPreferredDns(readableMap.getString(TTGatewayFieldConstant.PREFERRED_DNS));
                }
                if (readableMap.hasKey(TTGatewayFieldConstant.ALTERNATE_DNS)) {
                  ipSetting.setAlternateDns(readableMap.getString(TTGatewayFieldConstant.ALTERNATE_DNS));
                }

                configIp(gatewayInfo, ipSetting, success, fail);
              } else {
                doInitGateway(gatewayInfo, success, fail);
              }
            } else {
              doInitGateway(gatewayInfo, success, fail);
            }
        }

    }

    //-----------lock--------------
    @ReactMethod
    public void startScan() {
        LogUtil.d("start scan");
//        scanGateway = false;
        scanType = TYPE_LOCK;
        PermissionUtils.doWithScanPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().startScanLock(new ScanLockCallback() {
              @Override
              public void onScanLockSuccess(ExtendedBluetoothDevice extendedBluetoothDevice) {
//                LogUtil.d("extendedBluetoothDevice:" + extendedBluetoothDevice);
                if(extendedBluetoothDevice != null){
                  cacheAndFilterScanDevice(extendedBluetoothDevice);
                  WritableMap map = Arguments.createMap();
                  map.putString(TTLockFieldConstant.LOCK_NAME,extendedBluetoothDevice.getName());
                  map.putString(TTLockFieldConstant.LOCK_MAC,extendedBluetoothDevice.getAddress());
                  map.putBoolean(TTLockFieldConstant.IS_INITED, !extendedBluetoothDevice.isSettingMode());
                  map.putBoolean(TTLockFieldConstant.IS_KEYBOARD_ACTIVATED,extendedBluetoothDevice.isTouch());
                  map.putInt(TTLockFieldConstant.ELECTRIC_QUANTITY,extendedBluetoothDevice.getBatteryCapacity());
                  map.putString(TTLockFieldConstant.LOCK_VERSION, extendedBluetoothDevice.getLockVersionJson());
                  map.putInt(TTLockFieldConstant.RSSI,extendedBluetoothDevice.getRssi());
                  map.putInt(TTLockFieldConstant.LOCK_SWITCH_STATE, extendedBluetoothDevice.getParkStatus());
                  //锁开关状态跟oneMeterRSSI android不需要
                  getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTLockEvent.scanLock, map);
                }
              }

              @Override
              public void onFail(LockError error) {
                LogUtil.d("error:" + error);
              }
            });
          }
        });
    }

    @ReactMethod
    public void stopScan() {
        TTLockClient.getDefault().stopScanLock();
    }

    @ReactMethod
    public void initLock(ReadableMap readableMap, Callback success, Callback fail) {
        String lockmac = readableMap.getString(TTLockFieldConstant.LOCK_MAC);
        if (TextUtils.isEmpty(lockmac)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            LogUtil.d("lockmac is null");
            return;
        }
        ExtendedBluetoothDevice device = mCachedDevice.get(lockmac);
        if (device == null) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            LogUtil.d("device is null");
            return;
        }
        if (readableMap.hasKey(TTLockFieldConstant.CLIENT_PARA)) {
            String clientPara = readableMap.getString(TTLockFieldConstant.CLIENT_PARA);
            if (!TextUtils.isEmpty(clientPara)) {
                device.setManufacturerId(clientPara);
            }
        }
        TTLockClient.getDefault().initLock(device, new InitLockCallback() {
            @Override
            public void onInitLockSuccess(String lockData) {
                success.invoke(lockData);
            }

            @Override
            public void onFail(LockError error) {
                lockErrorCallback(error, fail);
            }
        });
    }

    @ReactMethod
    public void resetLock(String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().resetLock(lockData, new ResetLockCallback() {
              @Override
              public void onResetLockSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void resetEkey(String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().resetEkey(lockData, new ResetKeyCallback() {
              @Override
              public void onResetKeySuccess(String lockData) {
                successCallback.invoke(lockData);
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void controlLock(int controlAction, String lockData, Callback successCallback, Callback fail) {
        if (!RNControlAction.isValidAction(controlAction) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().controlLock(RNControlAction.RN2Native(controlAction), lockData, new ControlLockCallback() {
              @Override
              public void onControlLockSuccess(ControlLockResult controlLockResult) {
                WritableArray writableArray = Arguments.createArray();
                writableArray.pushDouble(controlLockResult.getLockTime());
                writableArray.pushInt(controlLockResult.getBattery());
                writableArray.pushInt(controlLockResult.getUniqueid());
                successCallback.invoke(writableArray);
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void setHotelData(ReadableMap hotelDataMap, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        HotelData hotelData = convertMapToHotelData(hotelDataMap);

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().setHotelData(hotelData, lockData, new SetHotelDataCallback() {
                    @Override
                    public void onSetHotelDataSuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void setHotelCardSector(String cardSector, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(cardSector) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().setHotelCardSector(cardSector, lockData, new SetHotelCardSectorCallback() {
                    @Override
                    public void onSetHotelCardSectorSuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }


    private HotelData convertMapToHotelData(ReadableMap map) {
        HotelData hotelData = new HotelData();
        if (map.hasKey("icKey")) {
            hotelData.setIcKey(map.getString("icKey"));
        }
        if (map.hasKey("aesKey")) {
            hotelData.setAesKey(map.getString("aesKey"));
        }
        if (map.hasKey("hotelNumber")) {
            hotelData.setHotelNumber(map.getInt("hotelNumber"));
        }
        if (map.hasKey("buildingNumber")) {
            hotelData.setBuildingNumber(map.getInt("buildingNumber"));
        }
        if (map.hasKey("floorNumber")) {
            hotelData.setFloorNumber(map.getInt("floorNumber"));
        }
        if (map.hasKey("hotelInfo")) {
            hotelData.setHotelInfo(map.getString("hotelInfo"));
        }
        if (map.hasKey("sector")) {
            hotelData.setSector((short) map.getInt("sector"));
        }
        if (map.hasKey("controlableFloors")) {
            // Assuming the controlableFloors is a ReadableArray
            ReadableArray array = map.getArray("controlableFloors");
            byte[] bytes = new byte[array.size()];
            for (int i = 0; i < array.size(); i++) {
                bytes[i] = (byte) array.getInt(i);
            }
            hotelData.setControlableFloors(bytes);
        }
        if (map.hasKey("ttLiftWorkMode")) {
            // Assuming ttLiftWorkMode is passed as an ordinal integer
            hotelData.setTtLiftWorkMode(TTLiftWorkMode.values()[map.getInt("ttLiftWorkMode")]);
        }
        if (map.hasKey("controlableLockMac")) {
            hotelData.setControlableLockMac(map.getString("controlableLockMac"));
        }
        if (map.hasKey("powerWorkModeValue")) {
            hotelData.setPowerWorkModeValue((byte) map.getInt("powerWorkModeValue"));
        }
        if (map.hasKey("paraType")) {
            hotelData.setParaType((byte) map.getInt("paraType"));
        }

        return hotelData;
    }

    @ReactMethod
    public void setLightTime(int light, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().setLightTime(light, lockData, new SetLightTimeCallback() {
                    @Override
                    public void onSetLightTimeSuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void getLightTime(String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().getLightTime(lockData, new GetLockTimeCallback() {
                    @Override
                    public void onGetLockTimeSuccess(long lockTimestamp) {
                        successCallback.invoke(String.valueOf(lockTimestamp));
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void setLiftControlableFloors(String floors, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(floors) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().setLiftControlableFloors(floors, lockData, new SetLiftControlableFloorsCallback() {
                    @Override
                    public void onSetLiftControlableFloorsSuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void setLiftWorkMode(TTLiftWorkMode floors, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().setLiftWorkMode(floors, lockData, new SetLiftWorkModeCallback() {
                    @Override
                    public void onSetLiftWorkModeSuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void activateLiftFloors(List<Integer> floors, long var, String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().activateLiftFloors(floors, var, lockData, new ActivateLiftFloorsCallback() {
                    @Override
                    public void onActivateLiftFloorsSuccess(ActivateLiftFloorsResult var1) {
                        successCallback.invoke(var1);
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void recoverLockData(String lockData, int paramInt, String lockData2, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData) || TextUtils.isEmpty(lockData2)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().recoverLockData(lockData, paramInt, lockData2, new RecoverLockDataCallback() {
                    @Override
                    public void onRecoveryDataSuccess(int lock) {
                        successCallback.invoke(String.valueOf(lock));
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }

    @ReactMethod
    public void verifyLock(String lockData, Callback successCallback, Callback fail) {

        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
            if (success) {
                TTLockClient.getDefault().verifyLock(lockData, new VerifyLockCallback() {
                    @Override
                    public void onVerifySuccess() {
                        successCallback.invoke();
                    }

                    @Override
                    public void onFail(LockError error) {
                        lockErrorCallback(error, fail);
                    }
                });
            } else {
                noPermissionCallback(fail);
            }
        });
    }


    @ReactMethod
    public void createCustomPasscode(String passcode, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
        LogUtil.d("passcode:" + passcode);
        if (TextUtils.isEmpty(passcode) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        LogUtil.d("startDate:" + startDate);
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().createCustomPasscode(passcode, (long)startDate, (long)endDate, lockData,  new CreateCustomPasscodeCallback() {
              @Override
              public void onCreateCustomPasscodeSuccess(String passcode) {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void modifyPasscode(String passcodeOrigin, String passcodeNew, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(passcodeOrigin) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().modifyPasscode(passcodeOrigin, passcodeNew, (long)startDate, (long)endDate, lockData,  new ModifyPasscodeCallback() {
              @Override
              public void onModifyPasscodeSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void deletePasscode(String passcode, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(passcode) || TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().deletePasscode(passcode, lockData,  new DeletePasscodeCallback() {
              @Override
              public void onDeletePasscodeSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void resetPasscode(String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().resetPasscode(lockData,  new ResetPasscodeCallback() {
              @Override
              public void onResetPasscodeSuccess(String lockData) {
                successCallback.invoke(lockData);
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void getLockSwitchState(String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(lockData)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().getLockStatus(lockData,  new GetLockStatusCallback() {
              @Override
              public void onGetLockStatusSuccess(int status) {
                successCallback.invoke(status);
              }

              @Override
              public void onGetDoorSensorStatusSuccess(int status) {
                //todo:
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void addCard(ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
        //todo:测试循环IC卡数据
        LogUtil.d("cycleList:" + cycleList);
        ValidityInfo validityInfo = new ValidityInfo();
        validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
        validityInfo.setStartDate((long) startDate);
        validityInfo.setEndDate((long) endDate);
        validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().addICCard(validityInfo, lockData, new AddICCardCallback() {
              @Override
              public void onEnterAddMode() {
                //前端目前不接收任何数据
                getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTLockEvent.addCardProgrress, null);
              }

              @Override
              public void onAddICCardSuccess(long cardNum) {
                successCallback.invoke(String.valueOf(cardNum));
              }

              @Override
              public void onFail(LockError lockError) {
                lockErrorCallback(lockError, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void modifyCardValidityPeriod(String cardNumber, ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(cardNumber)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            ValidityInfo validityInfo = new ValidityInfo();
            validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
            validityInfo.setStartDate((long) startDate);
            validityInfo.setEndDate((long) endDate);
            validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));

            TTLockClient.getDefault().modifyICCardValidityPeriod(validityInfo, cardNumber, lockData, new ModifyICCardPeriodCallback() {
              @Override
              public void onModifyICCardPeriodSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError lockError) {
                lockErrorCallback(lockError, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void deleteCard(String cardNumber, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(cardNumber)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().deleteICCard(cardNumber, lockData,  new DeleteICCardCallback() {
              @Override
              public void onDeleteICCardSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void clearAllCards(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().clearAllICCard(lockData,  new ClearAllICCardCallback() {
            @Override
            public void onClearAllICCardSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void addFingerprint(ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {

      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          ValidityInfo validityInfo = new ValidityInfo();
          validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
          validityInfo.setStartDate((long) startDate);
          validityInfo.setEndDate((long) endDate);
          validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));

          //todo:
          TTLockClient.getDefault().addFingerprint(validityInfo, lockData, new AddFingerprintCallback() {
            @Override
            public void onEnterAddMode(int totalCount) {
              totalCnt = totalCount;
              WritableArray writableArray = Arguments.createArray();
              writableArray.pushInt(0);
              writableArray.pushInt(totalCnt);
              getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTLockEvent.addFingerprintProgress, writableArray);
            }

            @Override
            public void onCollectFingerprint(int currentCount) {
              WritableArray writableArray = Arguments.createArray();
              writableArray.pushInt(currentCount);
              writableArray.pushInt(totalCnt);
              getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(TTLockEvent.addFingerprintProgress, writableArray);
            }

            @Override
            public void onAddFingerpintFinished(long fingerprintNum) {
              successCallback.invoke(String.valueOf(fingerprintNum));
            }

            @Override
            public void onFail(LockError lockError) {
              lockErrorCallback(lockError, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void modifyFingerprintValidityPeriod(String fingerprintNumber, ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(fingerprintNumber)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }

        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            ValidityInfo validityInfo = new ValidityInfo();
            validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
            validityInfo.setStartDate((long) startDate);
            validityInfo.setEndDate((long) endDate);
            validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));

            TTLockClient.getDefault().modifyFingerprintValidityPeriod(validityInfo, fingerprintNumber, lockData, new ModifyFingerprintPeriodCallback() {
              @Override
              public void onModifyPeriodSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError lockError) {
                lockErrorCallback(lockError, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void deleteFingerprint(String fingerprintNumber, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(fingerprintNumber)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().deleteFingerprint(fingerprintNumber, lockData,  new DeleteFingerprintCallback() {
              @Override
              public void onDeleteFingerprintSuccess() {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void clearAllFingerprints(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().clearAllFingerprints(lockData,  new ClearAllFingerprintCallback() {
            @Override
            public void onClearAllFingerprintSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError lockError) {
              lockErrorCallback(lockError, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void modifyAdminPasscode(String adminPasscode, String lockData, Callback successCallback, Callback fail) {
        if (TextUtils.isEmpty(adminPasscode)) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().modifyAdminPasscode(adminPasscode, lockData,  new ModifyAdminPasscodeCallback() {
              @Override
              public void onModifyAdminPasscodeSuccess(String passcode) {
                successCallback.invoke(passcode);
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void setLockTime(double timestamp, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().setLockTime((long) timestamp, lockData,  new SetLockTimeCallback() {
            @Override
            public void onSetTimeSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockTime(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getLockTime(lockData,  new GetLockTimeCallback() {
            @Override
            public void onGetLockTimeSuccess(long lockTimestamp) {
              successCallback.invoke(String.valueOf(lockTimestamp));
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockOperationRecord(int type, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getOperationLog(type == 0 ? LogType.NEW : LogType.ALL, lockData,  new GetOperationLogCallback() {
            @Override
            public void onGetLogSuccess(String log) {
              successCallback.invoke(log);
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockAutomaticLockingPeriodicTime(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getAutomaticLockingPeriod(lockData, new GetAutoLockingPeriodCallback() {
            @Override
            public void onGetAutoLockingPeriodSuccess(int currtentTime, int minTime, int maxTime) {
              WritableArray writableArray = Arguments.createArray();
              writableArray.pushInt(currtentTime);
              writableArray.pushInt(maxTime);
              writableArray.pushInt(minTime);
              successCallback.invoke(writableArray);
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void setLockAutomaticLockingPeriodicTime(int seconds, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().setAutomaticLockingPeriod(seconds, lockData,  new SetAutoLockingPeriodCallback() {
            @Override
            public void onSetAutoLockingPeriodSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockRemoteUnlockSwitchState(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getRemoteUnlockSwitchState(lockData,  new GetRemoteUnlockStateCallback() {
            @Override
            public void onGetRemoteUnlockSwitchStateSuccess(boolean enabled) {
              successCallback.invoke(enabled);
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void setLockRemoteUnlockSwitchState(boolean isOn, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().setRemoteUnlockSwitchState(isOn, lockData,  new SetRemoteUnlockSwitchCallback() {
            @Override
            public void onSetRemoteUnlockSwitchSuccess(String lockData) {
              successCallback.invoke(lockData);
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockConfig(int config, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getLockConfig(TTLockConfigConverter.RN2Native(config), lockData, new GetLockConfigCallback() {
            @Override
            public void onGetLockConfigSuccess(TTLockConfigType ttLockConfigType, boolean switchOn) {
              LogUtil.d("ttLockConfigType:" + switchOn);
              WritableArray writableArray = Arguments.createArray();
              writableArray.pushInt(TTLockConfigConverter.native2RN(ttLockConfigType));
              writableArray.pushBoolean(switchOn);
              successCallback.invoke(writableArray);
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void setLockConfig(int config, boolean isOn, String lockData, Callback successCallback, Callback fail) {
        TTLockConfigType ttLockConfigType = TTLockConfigConverter.RN2Native(config);
        if (ttLockConfigType == null) {
            lockErrorCallback(LockError.DATA_FORMAT_ERROR, fail);
            return;
        }
        LogUtil.d("ttLockConfigType:" + isOn);
        PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
          if (success) {
            TTLockClient.getDefault().setLockConfig(ttLockConfigType, isOn, lockData, new SetLockConfigCallback() {
              @Override
              public void onSetLockConfigSuccess(TTLockConfigType ttLockConfigType) {
                successCallback.invoke();
              }

              @Override
              public void onFail(LockError error) {
                lockErrorCallback(error, fail);
              }
            });
          } else {
            noPermissionCallback(fail);
          }
        });
    }

    @ReactMethod
    public void addPassageMode(int type, ReadableArray weekly, ReadableArray monthly, int startDate, int endDate, String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          PassageModeConfig passageModeConfig = new PassageModeConfig();
          passageModeConfig.setModeType(type == 0 ? PassageModeType.Weekly : PassageModeType.Monthly);
          if (passageModeConfig.getModeType() == PassageModeType.Weekly) {
            passageModeConfig.setRepeatWeekOrDays(Utils.readableArray2IntJson(weekly));
          } else {
            passageModeConfig.setRepeatWeekOrDays(Utils.readableArray2IntJson(monthly));
          }
          passageModeConfig.setStartDate(startDate);
          passageModeConfig.setEndDate(endDate);

          LogUtil.d("weekdays:" + passageModeConfig.getRepeatWeekOrDays());

          TTLockClient.getDefault().setPassageMode(passageModeConfig, lockData,  new SetPassageModeCallback() {
            @Override
            public void onSetPassageModeSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void clearAllPassageModes(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().clearPassageMode(lockData,  new ClearPassageModeCallback() {
            @Override
            public void onClearPassageModeSuccess() {
              successCallback.invoke();
            }

            @Override
            public void onFail(LockError error) {
              lockErrorCallback(error, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

    @ReactMethod
    public void getLockVersionWithLockMac(String lockData, Callback successCallback, Callback fail) {
      PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
        if (success) {
          TTLockClient.getDefault().getLockVersion(lockData, new GetLockVersionCallback() {
            @Override
            public void onGetLockVersionSuccess(String lockVersion) {
              successCallback.invoke(lockVersion);
            }

            @Override
            public void onFail(LockError lockError) {
              lockErrorCallback(lockError, fail);
            }
          });
        } else {
          noPermissionCallback(fail);
        }
      });
    }

  /**
   *
   * @param soundVolumeValue
   * On = -1,
   *   Off = 0,
   *   Livel_1 = 1,
   *   Livel_2 = 2,
   *   Livel_3 = 3,
   *   Livel_4 = 4,
   *   Livel_5 = 5
   * @param lockData
   * @param successCallback
   * @param fail
   */
  @ReactMethod
  public void setLockSoundVolume(int soundVolumeValue, String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        SoundVolume soundVolume = SoundVolume.OFF;
        switch (soundVolumeValue) {
          case -1://On = -1,
            soundVolume = SoundVolume.ON;
            break;
          case 0://Off = 0,
            soundVolume = SoundVolume.OFF;
            break;
          case 1:
          case 2:
          case 3:
          case 4:
          case 5:
            soundVolume = SoundVolume.getInstance(soundVolumeValue);
            break;
        }
        TTLockClient.getDefault().setLockSoundWithSoundVolume(soundVolume, lockData, new SetLockSoundWithSoundVolumeCallback() {
          @Override
          public void onSetLockSoundSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void getLockSoundVolume(String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().getLockSoundWithSoundVolume(lockData, new GetLockSoundWithSoundVolumeCallback() {
          @Override
          public void onGetLockSoundSuccess(boolean enable, SoundVolume soundVolume) {
//            On = -1,
//              Off = 0,
//              Livel_1 = 1,
//              Livel_2 = 2,
//              Livel_3 = 3,
//              Livel_4 = 4,
//              Livel_5 = 5
            int soundVolumeValue = 0;
            if (enable) {
              switch (soundVolume) {
                case ON:
                  soundVolumeValue = -1;
                  break;
                case OFF:
                  soundVolumeValue = 0;
                  break;
                default:
                  soundVolumeValue = soundVolume.getValue();
                  break;
              }
            }
            successCallback.invoke(soundVolumeValue);
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  /**
   *
   * @param direction
   * Left = 1,
   *   Right = 2
   * @param lockData
   * @param successCallback
   * @param fail
   */
  @ReactMethod
  public void setUnlockDirection(int direction, String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().setUnlockDirection(direction == 1 ? UnlockDirection.LEFT : UnlockDirection.RIGHT, lockData, new SetUnlockDirectionCallback() {
          @Override
          public void onSetUnlockDirectionSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void getUnlockDirection(String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().getUnlockDirection(lockData, new GetUnlockDirectionCallback() {
          @Override
          public void onGetUnlockDirectionSuccess(UnlockDirection unlockDirection) {
//             Left = 1,
//             Right = 2
            successCallback.invoke(unlockDirection.getValue() == 1 ? 1 : 2);
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void addRemoteKey(String remoteMac, ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
    ValidityInfo validityInfo = new ValidityInfo();
    validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
    validityInfo.setStartDate((long) startDate);
    validityInfo.setEndDate((long) endDate);
    validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().addRemote(remoteMac, validityInfo, lockData, new AddRemoteCallback() {
          @Override
          public void onAddSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void modifyRemoteKey(String remoteMac, ReadableArray cycleList, double startDate, double endDate, String lockData, Callback successCallback, Callback fail) {
    ValidityInfo validityInfo = new ValidityInfo();
    validityInfo.setModeType(cycleList == null || cycleList.size() == 0 ? ValidityInfo.TIMED : ValidityInfo.CYCLIC);
    validityInfo.setStartDate((long) startDate);
    validityInfo.setEndDate((long) endDate);
    validityInfo.setCyclicConfigs(Utils.readableArray2CyclicList(cycleList));

    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().modifyRemoteValidityPeriod(remoteMac, validityInfo, lockData, new ModifyRemoteValidityPeriodCallback() {
          @Override
          public void onModifySuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void deleteRemoteKey(String remoteMac, String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().deleteRemote(remoteMac, lockData, new DeleteRemoteCallback() {
          @Override
          public void onDeleteSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void getLockElectricQuantity(String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().getBatteryLevel(lockData, new GetBatteryLevelCallback() {
          @Override
          public void onGetBatteryLevelSuccess(int battery) {
            successCallback.invoke(battery);
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void clearAllRemoteKey(String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().clearRemote(lockData, new ClearRemoteCallback() {
          @Override
          public void onClearSuccess() {
            successCallback.invoke();
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

  @ReactMethod
  public void getAccessoryElectricQuantity(int type, String mac, String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        AccessoryInfo accessoryInfo = new AccessoryInfo();
        accessoryInfo.setAccessoryMac(mac);
        AccessoryType accessoryType = null;
        switch (type) {
          case 1:
            accessoryType = AccessoryType.WIRELESS_KEYPAD;
            break;
          case 2:
            accessoryType = AccessoryType.REMOTE;
            break;
        }
        accessoryInfo.setAccessoryType(accessoryType);
        TTLockClient.getDefault().getAccessoryBatteryLevel(accessoryInfo, lockData, new GetAccessoryBatteryLevelCallback() {
          @Override
          public void onGetAccessoryBatteryLevelSuccess(AccessoryInfo accessoryInfo) {
            WritableArray writableArray = Arguments.createArray();
            writableArray.pushInt(accessoryInfo.getAccessoryBattery());
            writableArray.pushDouble(accessoryInfo.getBatteryDate());
            successCallback.invoke(writableArray);
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

@ReactMethod
  public void getLockSystem(String lockData, Callback successCallback, Callback fail) {
    PermissionUtils.doWithConnectPermission(getCurrentActivity(), success -> {
      if (success) {
        TTLockClient.getDefault().getLockSystemInfo(lockData, new GetLockSystemInfoCallback() {
          @Override
          public void onGetLockSystemInfoSuccess(com.ttlock.bl.sdk.entity.DeviceInfo deviceInfo) {
            WritableMap map = Arguments.createMap();
            map.putString(TTBaseFieldConstant.MODEL_NUM, deviceInfo.getModelNum());
            map.putString(TTBaseFieldConstant.HARDWARE_REVISION, deviceInfo.getHardwareRevision());
            map.putString(TTBaseFieldConstant.FIRMWARE_REVISION, deviceInfo.getFirmwareRevision());
            map.putString(TTLockFieldConstant.LOCK_DATA, deviceInfo.getLockData());
            successCallback.invoke(map);
          }

          @Override
          public void onFail(LockError lockError) {
            lockErrorCallback(lockError, fail);
          }
        });
      } else {
        noPermissionCallback(fail);
      }
    });
  }

    @ReactMethod
    public void getBluetoothState(Callback callback) {
        boolean enable = TTLockClient.getDefault().isBLEEnabled(getCurrentActivity());
        //4-on, 5-off
        callback.invoke(enable ? 4 : 5);
    }

    @ReactMethod
    public void supportFunction(int function, String lockData, Callback callback) {
        boolean support = FeatureValueUtil.isSupportFeature(lockData, function);
        callback.invoke(support);
    }

    private void lockErrorCallback(LockError lockError, Callback fail) {
        if (fail != null) {
            fail.invoke(TTLockErrorConverter.native2RN(lockError), lockError.getErrorMsg());
        }
    }

    private void noPermissionCallback(Callback fail) {
      lockErrorCallback(LockError.LOCK_NO_PERMISSION, fail);
    }



}
