package com.opzpy123.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessUtils {

    private static <T> void combine(int i, List<String> keyList,
                                    Map<String, List<T>> input, Map<String, T> obj,
                                    List<Map<String, T>> result, int notEmptyCount) {
        if (i >= keyList.size()) {
            if (notEmptyCount > 0)
                result.add(obj);
            return;
        }
        String key = keyList.get(i);
        List<T> l = input.get(key);
        if (l != null && !l.isEmpty()) {
            notEmptyCount++;
        } else {
            l = new ArrayList<T>();
            l.add(null);
        }
        for (T o : l) {
            Map<String, T> newObj = new HashMap<String, T>(obj);
            newObj.put(key, o);
            combine(i + 1, keyList, input, newObj, result, notEmptyCount);
        }
    }

    /**
     * 对多个list中的值进行组合<br/>
     * <blockquote>{key1:[1,2],key2:[5,6]}</blockquote>组合后结果为<br/>
     * <p>
     * <blockquote>
     *
     * <pre>
     * |-----+------|
     * |key1 | key2 |
     * |------------|
     * |1    | 5    |
     * |1    | 6    |
     * |2    | 5    |
     * |2    | 6    |
     * |-----+------|
     * </pre>
     *
     * </blockquote>
     * </p>
     *
     * @param listMap 每个value都是list的map <blockquote> {key1:[1,2],key2:[5,6]}
     *                如果map的value是空集合或null则这个map不参与运算 </blockquote>
     * @return 组合后的结果
     */
    public static <T> List<Map<String, T>> combine(Map<String, List<T>> listMap) {
        List<Map<String, T>> result = new ArrayList<Map<String, T>>();
        combine(0, new ArrayList<String>(listMap.keySet()), listMap,
                new HashMap<String, T>(), result, 0);
        return result;
    }

    /**
     * 校验对象为空，如果不为空则不作处理，为空的话抛出BusinessException
     *
     * @param o       判断是否为null的对象引用
     * @param message 抛出异常的message信息，可以包含'%s'这样的占位符,参考
     * @param args    message中要format进去的参数
     * @throws RuntimeException 抛出以{@link String#format(String, Object[])}
     *                          为message的异常
     * @see String#format(String, Object[])
     */
    public static void notNull(Object o, String message, Object... args) throws WriterException {
        if (null == o) {
            throw new WriterException(String.format(message, args));
        }
    }


    /**
     * 生成编图片,并写入输出流 默认400X400,格式为png
     *
     * @param code       要生成条码的编码
     * @param codeFormat 编码格式 QR_CODE/CODE_128/CODE_39
     * @param imageType  图片格式 jpg/png/gif/bmp
     * @param width      生成图片宽度
     * @param height     生成图片高度
     * @param output     输出流
     * @throws RuntimeException 编码失败会抛出WriterException
     * @throws IOException      输出失败会抛出IOException
     */
    public static void writeCodeImg(String code, String codeFormat, String imageType, int width, int height, OutputStream output) throws IOException, WriterException {
        BarcodeFormat formatEnum = BarcodeFormat.valueOf(codeFormat);
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(code,
                formatEnum, width, height, hints);// 生成矩阵
        MatrixToImageConfig cfg = new MatrixToImageConfig(0xff000001, 0xffffffff);
        MatrixToImageWriter.writeToStream(bitMatrix, "png", output, cfg);
    }

    /**
     * 生成二维码图片,并写入输出流 默认400X400,格式为png
     *
     * @param code   要生成条码的编码
     * @param output 输出流
     * @throws WriterException 编码失败会抛出WriterException
     * @throws IOException     输出失败会抛出IOException
     */
    public static void writeQrCodeImg(String code, OutputStream output) throws WriterException, IOException {
        writeCodeImg(code, "QR_CODE", "png", 400, 400, output);
    }

    /**
     * 生成条码图片,并写入输出流 默认400X100,格式为png,编码为类型为code_128
     *
     * @param code   要生成条码的编码
     * @param output 输出流
     * @throws WriterException 编码失败会抛出WriterException
     * @throws IOException     输出失败会抛出IOException
     */
    public static void writeBarCodeImg(String code, OutputStream output) throws WriterException, IOException {
        writeCodeImg(code, "CODE_128", "png", 400, 100, output);
    }


}
