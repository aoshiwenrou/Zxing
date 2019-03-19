package com.alwaysnb.scan;

/**
 * 功能开关
 */
public class FuncSwitcher {

    /**
     * 一维码：商品
     */
    public static final boolean DECODE_1D_PRODUCT = true;
    /**
     * 一维码：工业
     */
    public static final boolean DECODE_1D_INDUSTRIAL = true;
    /**
     * 二维码
     */
    public static final boolean DECODE_QR = true;
    /**
     * Data Matrix
     */
    public static final boolean DECODE_DATA_MATRIX = true;
    /**
     * Aztec
     */
    public static final boolean DECODE_AZTEC = false;
    /**
     * PDF417(测试)
     */
    public static final boolean DECODE_PDF417 = false;
    /**
     * 震动
     */
    public static final boolean VIBRATE = false;
    /**
     * 播放提示音
     */
    public static final boolean PLAY_BEEP = false;
    /**
     * 自动对焦
     */
    public static final boolean AUTO_FOCUS = true;
    /**
     * 不持续对焦，使用标准对焦模式
     */
    public static final boolean DISABLE_CONTINUOUS_FOCUS = false;
    /**
     * 反色：扫描黑色背景上的白色条码，仅适用于部分设备
     */
    public static final boolean INVERT_SCAN = false;
    /**
     * 不使用距离检测
     */
    public static final boolean DISABLE_METERING = true;
    /**
     * 不进行条形码场景匹配
     */
    public static final boolean DISABLE_BARCODE_SCENE_MODE = true;

}
