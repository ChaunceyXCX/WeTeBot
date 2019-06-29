package com.chauncey.WeTeBot.utils.tools;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QRUtils
 * @Description 控制台打印图片
 * @Author https://github.com/ChaunceyCX
 * @Date 2019/6/23 下午3:54
 * @Version 1.0
 **/
public class QRUtils {

    /**
     * @return java.lang.String
     * @Author https://github.com/ChaunceyCX
     * @Description //解码识别二维码内容
     * @Date 下午4:14 2019/6/23
     * @Param [input]
     **/
    public static String decode(byte[] bytes)
            throws IOException, NotFoundException {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(input))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
        return qrCodeResult.getText();
    }

    /**
     * @return java.lang.String
     * @Author https://github.com/ChaunceyCX
     * @Description //生成二维码
     * @Date 下午4:14 2019/6/23
     * @Param [text, width, height]
     **/
    public static String generateQR(String text, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> params = new HashMap<>();
        params.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        params.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, params);
        return toAscii(bitMatrix);
    }

    private static String toAscii(BitMatrix bitMatrix) {
        StringBuilder builder = new StringBuilder();
        for (int r = 0; r < bitMatrix.getHeight(); r++) {
            for (int c = 0; c < bitMatrix.getWidth(); c++) {
                if (!bitMatrix.get(r, c)) {
                    builder.append("\033[47m   \033[0m");
                } else {
                    builder.append("\033[40m   \033[0m");
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
