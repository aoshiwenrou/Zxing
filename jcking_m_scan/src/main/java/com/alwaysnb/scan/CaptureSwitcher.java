package com.alwaysnb.scan;

/**
 * 功能开关。本功能的设置应该在初始化Camera之前，否则可能无效
 */
public class CaptureSwitcher {

    /**
     * 一维码：商品
     */
    private static final boolean DECODE_1D_PRODUCT = true;
    /**
     * 一维码：工业
     */
    private static final boolean DECODE_1D_INDUSTRIAL = true;
    /**
     * 二维码
     */
    private static final boolean DECODE_QR = true;
    /**
     * Data Matrix
     */
    private static final boolean DECODE_DATA_MATRIX = true;
    /**
     * Aztec
     */
    private static final boolean DECODE_AZTEC = false;
    /**
     * PDF417(测试)
     */
    private static final boolean DECODE_PDF417 = false;
    /**
     * 震动
     */
    private static final boolean VIBRATE = false;
    /**
     * 播放提示音
     */
    private static final boolean PLAY_BEEP = false;
    /**
     * 自动对焦
     */
    private static final boolean AUTO_FOCUS = true;
    /**
     * 不持续对焦，使用标准对焦模式
     */
    private static final boolean DISABLE_CONTINUOUS_FOCUS = true;
    /**
     * 反色：扫描黑色背景上的白色条码，仅适用于部分设备
     */
    private static final boolean INVERT_SCAN = false;
    /**
     * 不使用距离检测
     */
    private static final boolean DISABLE_METERING = true;
    /**
     * 不进行条形码场景匹配
     */
    private static final boolean DISABLE_BARCODE_SCENE_MODE = true;

    /////////////////////////////////////////////////////////////////////////////////

    private static class Holder{
        private static CaptureSwitcher instance = new CaptureSwitcher();
    }

    private CaptureSwitcher(){}

    public static CaptureSwitcher get(){
        return Holder.instance;
    }

    /**
     * 重置为默认设置
     */
    public void resetDefault(){
        decode1dProduct = DECODE_1D_PRODUCT;
        decode1dIndustrial = DECODE_1D_INDUSTRIAL;
        decodeQr = DECODE_QR;
        decodeDataMatrix = DECODE_DATA_MATRIX;
        decodeAztec = DECODE_AZTEC;
        decodePdf417 = DECODE_PDF417;
        vibrate = VIBRATE;
        playBeep = PLAY_BEEP;
        autoFocus = AUTO_FOCUS;
        disableContinuousFocus = DISABLE_CONTINUOUS_FOCUS;
        invertScan = INVERT_SCAN;
        disableMetering = DISABLE_METERING;
        disableBarcodeSceneMode = DISABLE_BARCODE_SCENE_MODE;
    }

    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 一维码：商品
     */
    private boolean decode1dProduct = DECODE_1D_PRODUCT;
    /**
     * 一维码：工业
     */
    private boolean decode1dIndustrial = DECODE_1D_INDUSTRIAL;
    /**
     * 二维码
     */
    private boolean decodeQr = DECODE_QR;
    /**
     * Data Matrix
     */
    private boolean decodeDataMatrix = DECODE_DATA_MATRIX;
    /**
     * Aztec
     */
    private boolean decodeAztec = DECODE_AZTEC;
    /**
     * PDF417(测试)
     */
    private boolean decodePdf417 = DECODE_PDF417;
    /**
     * 震动
     */
    private boolean vibrate = VIBRATE;
    /**
     * 播放提示音
     */
    private boolean playBeep = PLAY_BEEP;
    /**
     * 自动对焦
     */
    private boolean autoFocus = AUTO_FOCUS;
    /**
     * 不持续对焦，使用标准对焦模式
     */
    private boolean disableContinuousFocus = DISABLE_CONTINUOUS_FOCUS;
    /**
     * 反色：扫描黑色背景上的白色条码，仅适用于部分设备
     */
    private boolean invertScan = INVERT_SCAN;
    /**
     * 不使用距离检测
     */
    private boolean disableMetering = DISABLE_METERING;
    /**
     * 不进行条形码场景匹配
     */
    private boolean disableBarcodeSceneMode = DISABLE_BARCODE_SCENE_MODE;

    public boolean isDecode1dProduct() {
        return decode1dProduct;
    }

    public void setDecode1dProduct(boolean decode1dProduct) {
        this.decode1dProduct = decode1dProduct;
    }

    public boolean isDecode1dIndustrial() {
        return decode1dIndustrial;
    }

    public void setDecode1dIndustrial(boolean decode1dIndustrial) {
        this.decode1dIndustrial = decode1dIndustrial;
    }

    public boolean isDecodeQr() {
        return decodeQr;
    }

    public void setDecodeQr(boolean decodeQr) {
        this.decodeQr = decodeQr;
    }

    public boolean isDecodeDataMatrix() {
        return decodeDataMatrix;
    }

    public void setDecodeDataMatrix(boolean decodeDataMatrix) {
        this.decodeDataMatrix = decodeDataMatrix;
    }

    public boolean isDecodeAztec() {
        return decodeAztec;
    }

    public void setDecodeAztec(boolean decodeAztec) {
        this.decodeAztec = decodeAztec;
    }

    public boolean isDecodePdf417() {
        return decodePdf417;
    }

    public void setDecodePdf417(boolean decodePdf417) {
        this.decodePdf417 = decodePdf417;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isPlayBeep() {
        return playBeep;
    }

    public void setPlayBeep(boolean playBeep) {
        this.playBeep = playBeep;
    }

    public boolean isAutoFocus() {
        return autoFocus;
    }

    public void setAutoFocus(boolean autoFocus) {
        this.autoFocus = autoFocus;
    }

    public boolean isDisableContinuousFocus() {
        return disableContinuousFocus;
    }

    public void setDisableContinuousFocus(boolean disableContinuousFocus) {
        this.disableContinuousFocus = disableContinuousFocus;
    }

    public boolean isInvertScan() {
        return invertScan;
    }

    public void setInvertScan(boolean invertScan) {
        this.invertScan = invertScan;
    }

    public boolean isDisableMetering() {
        return disableMetering;
    }

    public void setDisableMetering(boolean disableMetering) {
        this.disableMetering = disableMetering;
    }

    public boolean isDisableBarcodeSceneMode() {
        return disableBarcodeSceneMode;
    }

    public void setDisableBarcodeSceneMode(boolean disableBarcodeSceneMode) {
        this.disableBarcodeSceneMode = disableBarcodeSceneMode;
    }
}
