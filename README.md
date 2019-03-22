# Zxing简介

根据Zxing的官方源码，剥离出单纯的扫码功能，并做了以下一点：
- 支持竖屏页面
- 扫码速度优化
- 增加权限申请
- 灵活的功能配置

你可以一行代码依赖我的库，直接使用扫码功能；也可以自定义UI完成你的需求。事实上我更倾向于后者，我已经放开了UI配置方法。或者你有更深的定制需求，你也可以下载我的代码作为module集成到你的项目中。

如果你觉得我的代码可以对你有一点点帮助，请给我一个star，谢谢支持。

版本说明：https://github.com/aoshiwenrou/Zxing/releases



# 目录结构说明

<table>
    <tr>
        <th>模块名称</th>
        <th>说明</th>
    </tr>
    <tr>
        <th>app</th>
        <th>Zxing官方源码，可直接运行，体验强大的扫码功能</th>
    </tr>
    <tr>
        <th>zxingCore</th>
        <th>Zxing官方扫码核心库，我已经生成了jar包</th>
    </tr>
    <tr>
        <th>jscan</th>
        <th>基于zxing进行功能剥离之后的库，我已经对一些功能进行了优化</th>
    </tr>
    <tr>
        <th>demo</th>
        <th>jscan的demo模块，我写了一些简单的demo，你可以作为参考</th>
    </tr>
</table>



# 使用方法

1.添加依赖
--------------------
先在 build.gradle(Project:XXXX) 的 repositories 添加
``` 
maven { url 'https://jitpack.io' }
```
一定要加上这个，否则会提示依赖失败

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

然后在需要依赖的module的 build.gradle 的 dependencies 添加:

 ```
 dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:26.1.0'
    
    /*添加依赖*/
    implementation 'com.github.aoshiwenrou:Zxing:0.2.0'
}
```
最新版本：https://github.com/aoshiwenrou/Zxing/releases
 
 2.权限
 --------------
 
 demo使用的是RxPermissions
 
 需要申请的权限有：
 
   ```
   Manifest.permission.CAMERA
   ```
   
 项目中用到的所有权限（与Zxing官方保持一致）
   
   ```
   <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.FLASHLIGHT" />
    <!-- unavailable in API 23 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

    <uses-feature android:name="android.hardware.camera.any" />
    <uses-feature
        android:name="android.hardware.camera.autofocus"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.camera.flash"
        android:required="false" />
    <uses-feature android:name="android.hardware.screen.landscape" />
    <uses-feature
        android:name="android.hardware.wifi"
        android:required="false" />
   ```

 
3.跳转到扫一扫界面：
--------------

1.使用默认配置项，两行代码即可

```
Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
startActivity(intent);
```
或者你使用的是fragment，只需要new一下就好
```
CaptureFragment fragment = new CaptureFragment();
```

2.自定义配置项，使用CaptureSwitcher类。配置项都要在初始化相机之前调用，否则可能无效，例如
```
    CaptureSwitcher.get().resetDefault();//重要，使所有参数重置为默认值
    CaptureSwitcher.get().setDisableContinuousFocus(false);
    CaptureSwitcher.get().setDecode1dIndustrial(true);
    CaptureSwitcher.get().setDecode1dProduct(true);

    getSupportFragmentManager().beginTransaction()
            .replace(com.jcking.scan.R.id.layout_content, new CaptureFragment())
            .commitAllowingStateLoss();
```

下面的每一个属性都有相关的set和get方法。
```
    // 一维码：商品
    private boolean decode1dProduct = DECODE_1D_PRODUCT;
    // 一维码：工业
    private boolean decode1dIndustrial = DECODE_1D_INDUSTRIAL;
    // 二维码
    private boolean decodeQr = DECODE_QR;
    // Data Matrix
    private boolean decodeDataMatrix = DECODE_DATA_MATRIX;
    // Aztec
    private boolean decodeAztec = DECODE_AZTEC;
    // PDF417(测试)
    private boolean decodePdf417 = DECODE_PDF417;
    // 震动
    private boolean vibrate = VIBRATE;
    // 播放提示音
    private boolean playBeep = PLAY_BEEP;
    // 自动对焦
    private boolean autoFocus = AUTO_FOCUS;
    // 不持续对焦，使用标准对焦模式
    private boolean disableContinuousFocus = DISABLE_CONTINUOUS_FOCUS;
    // 反色：扫描黑色背景上的白色条码，仅适用于部分设备
    private boolean invertScan = INVERT_SCAN;
    // 不使用距离检测
    private boolean disableMetering = DISABLE_METERING;
    // 不进行条形码场景匹配
    private boolean disableBarcodeSceneMode = DISABLE_BARCODE_SCENE_MODE;
    // 不进行bitmap转换输出。如果为true，则在handleDecode方法中无法拿到bitmap对象，但是能提高一点点效率
    private boolean disableDecodeBitmap = DISABLE_DECODE_BITMAP;

```

4.自定义UI
-------------------------------------------
1.自定义Activity
- 继承CaptureActivity
- 覆写getLayoutResource()
- 覆写getViewfinderView() ,如果你的id为R.id.viewfinder_view，则不需要覆写
- 覆写getSurfaceView()，如果你的id为R.id.preview_view，则不需要覆写
- 覆写handleDecode(Result rawResult, Bitmap barcode, float scaleFactor)方法，处理扫码结果

```
public class CustomActivity extends CaptureActivity {

    @Override
    protected int getLayoutResource() {
        return super.getLayoutResource();
    }

    @Override
    public SurfaceView getSurfaceView() {
        return super.getSurfaceView();
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return super.getViewfinderView();
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        super.handleDecode(rawResult, barcode, scaleFactor);
    }
}
```

2.自定义Fragment
- 继承CaptureFragment
- 覆写onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
- 覆写getViewfinderView() ,如果你的id为R.id.viewfinder_view，则不需要覆写
- 覆写getSurfaceView()，如果你的id为R.id.preview_view，则不需要覆写
- 覆写handleDecode(Result rawResult, Bitmap barcode, float scaleFactor)方法，处理扫码结果

```
public class CustomFragment extends CaptureFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public SurfaceView getSurfaceView() {
        return super.getSurfaceView();
    }

    @Override
    public ViewfinderView getViewfinderView() {
        return super.getViewfinderView();
    }

    @Override
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        super.handleDecode(rawResult, barcode, scaleFactor);
    }
}
```


# 参考

- zxing/zxing : https://github.com/zxing/zxing
- yuzhiqiang1993/zxing ： https://github.com/yuzhiqiang1993/zxing
- tbruyelle/RxPermissions : https://github.com/tbruyelle/RxPermissions

