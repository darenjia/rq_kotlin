package com.bkjcb.rqapplication.gascylindermanagement.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Administrator on 2019/7/26
 * <p>
 * desc:
 */
public class NfcUtils {
    public NfcAdapter mNfcAdapter;
    public IntentFilter[] mIntentFilter = null;
    public PendingIntent mPendingIntent = null;
    public String[][] mTechList = null;
    private Context context;


    /**
     * 构造函数，用于初始化nfc
     */
    public NfcUtils(Context activity) {
        this.context = activity;
        NfcInit();
    }


    public void onStart(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }
    /**
     * 判断手机是否具备NFC功能
     *
     * @return {@code true}: 具备 {@code false}: 不具备
     */
    public boolean isNfcExits() {
        return mNfcAdapter != null;
    }

    /**
     * 判断手机NFC是否开启
     * <p>
     * OPPO A37m 发现必须同时开启NFC以及Android Beam才可以使用
     * 20180108 发现OPPO单独打开NFC即可读取标签，不清楚是否是系统更新
     * </p>
     *
     * @return {@code true}: 已开启 {@code false}: 未开启
     */
    public boolean isNfcEnable() {
//        if (Build.MANUFACTURER.toUpperCase().contains("OPPO")) {
//            return nfcAdapter.isEnabled() && isAndroidBeamEnable(context);
//        }
        return mNfcAdapter != null && mNfcAdapter.isEnabled();
    }


    /**
     * 判断手机NFC的Android Beam是否开启，在API 16之后才有
     *
     * @return {@code true}: 已开启 {@code false}: 未开启
     */
    public boolean isAndroidBeamEnable() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        return nfcAdapter != null && nfcAdapter.isNdefPushEnabled();
    }

    /**
     * 判断手机是否具备Android Beam
     *
     * @return {@code true}:具备 {@code false}:不具备
     */
    public boolean isAndroidBeamExits() {
        return isNfcExits();
    }

    /**
     * 跳转至系统NFC设置界面.
     *
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public boolean intentToNfcSetting() {
        if (isNfcExits()) {
            return toIntent(context, Settings.ACTION_NFC_SETTINGS);
        }
        return false;

    }

    /**
     * 跳转至系统NFC Android Beam设置界面，同页面基本都有NFC开关.
     *
     * @param context {@link Context}
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public boolean intentToNfcShare(Context context) {
        if (isAndroidBeamExits()) {
            return toIntent(context, Settings.ACTION_NFCSHARING_SETTINGS);
        }
        return false;
    }

    /**
     * 跳转方法.
     *
     * @param context {@link Context}
     * @param action  意图
     * @return 是否跳转成功 {@code true } 成功<br>{@code false}失败
     */
    private boolean toIntent(Context context, String action) {
        try {
            Intent intent = new Intent(action);
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 初始化nfc设置
     */
    public void NfcInit() {
        Intent intent = new Intent(context, ((Activity) context).getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //做一个IntentFilter过滤你想要的action 这里过滤的是ndef
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        //如果你对action的定义有更高的要求，比如data的要求，你可以使用如下的代码来定义intentFilter
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{filter, filter2};
        mTechList = null;
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mTechList = new String[][]{
                {NfcV.class.getName()},
                {NdefFormatable.class.getName()}};
        //生成intentFilter
        mIntentFilter = new IntentFilter[]{filter};
//        方法二：
//        mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
//        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        try {
//            filter.addDataType("*/*");
//        } catch (IntentFilter.MalformedMimeTypeException e) {
//            e.printStackTrace();
//        }
//        mIntentFilter = new IntentFilter[]{filter, filter2};
//        mTechList = null;
    }

    public String[] readNFC(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return null;
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            return parseNfcv(intent);//方法实现在后面
        }
        return null;
    }

    /**
     * 读取NFC的数据
     */
    public String readNFCFromTag(Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            return "";
        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            parseNdef(intent);  //方法实现在后面
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            parseTech(intent);//方法实现在后面
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            parseTag(intent); //方法实现在后面
        }
        return "";
    }

    private String[] parseNfcv(Intent intent) {
        String[] content = new String[2];
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcV mNfcV = NfcV.get(tag);
        try {
            mNfcV.connect();
            NfcVUtil mNfcVutil = new NfcVUtil(mNfcV);
            // * 取得UID
            List<byte[]> list = mNfcVutil.readAllBlocksByte();
            StringBuilder builder = new StringBuilder();
            for (byte[] bytes : list) {
                for (byte aByte : bytes) {
                    builder.append(aByte).append(",");
                }
            }
            content[0] = "[" + builder.substring(0, builder.length() - 1) + "]";
            content[1] = mNfcVutil.getUID();
            // * 读取block在1位置的内容
//            mNfcVutil.readOneBlock(1);
            // * 从位置7开始读2个block的内容
//            mNfcVutil.readBlocks(1, 63);
            // * 取得block的个数
//            mNfcVutil.getBlockNumber();
            // * 取得1个block的长度
//            mNfcVutil.getOneBlockSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private String parseTag(Intent intent) {
        int sectorCount = 0;
        byte[] content = {};
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mifageClassic = MifareClassic.get(tag);
        try {
            mifageClassic.connect();
            sectorCount = mifageClassic.getSectorCount();
            int byteIndex = 0;
            for (int i = 1; i < sectorCount; i++) {
                boolean auth = mifageClassic.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT);
                if (!auth) {
                    return null;
                }
                int blockCount = mifageClassic.getBlockCountInSector(i);
                int blockIndex = mifageClassic.sectorToBlock(i);
                for (int j = 0; j < blockCount; j++) {
                    if (j + 1 == blockCount) {
                        continue;
                    }
                    byte buffer[] = new byte[16];
                    buffer = mifageClassic.readBlock(blockIndex);
                    if (blockIndex == 4) {
                        System.arraycopy(buffer, 9, content, byteIndex, 6);
                        byteIndex += 6;
                    } else {
                        System.arraycopy(buffer, 0, content, byteIndex, 16);
                        byteIndex += 16;
                    }
                    blockIndex++;
                    Log.i("content_buffer" + blockIndex, new String(content));
                }
            }
            String contentStr = new String(content);
            return contentStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseNdef(Intent intent) {
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage ndefs[];
        String data = "";
        if (messages != null) {
            ndefs = new NdefMessage[messages.length];
            for (int i = 0; i < messages.length; i++) {
                ndefs[i] = (NdefMessage) messages[i];
            }
            if (ndefs != null && ndefs.length > 0) {
                NdefRecord record = ndefs[0].getRecords()[0];
                byte[] payload = record.getPayload();
                String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                int languageCodeLength = payload[0] & 0077;
                try {
                    data = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    private String parseTech(Intent intent) {
        return parseTag(intent);
    }

    /**
     * 往nfc写入数据
     */
    public void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        NdefRecord ndefRecord = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ndefRecord = NdefRecord.createTextRecord(null, data);
        }
        NdefRecord[] records = {ndefRecord};
        NdefMessage ndefMessage = new NdefMessage(records);
        ndef.writeNdefMessage(ndefMessage);
    }

    /**
     * 读取nfcID
     */
    public String readNFCId(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        return ByteArrayToHexString(tag.getId());
    }

    public void enableForegroundDispatch() {
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch((Activity) context, mPendingIntent, mIntentFilter, mTechList);
        }
    }

    public void disableForegroundDispatch() {
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch((Activity)context);
        }
    }

    /**
     * 将字节数组转换为字符串
     */
    private static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
}