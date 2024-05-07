package org.example.Controllers;

        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.EncodeHintType;
        import com.google.zxing.MultiFormatWriter;
        import com.google.zxing.WriterException;
        import com.google.zxing.client.j2se.MatrixToImageWriter;
        import com.google.zxing.common.BitMatrix;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

        import java.io.File;
        import java.io.IOException;
        import java.nio.file.FileSystems;
        import java.nio.file.Path;
        import java.util.HashMap;
        import java.util.Map;

public class GenerateQrCode {

    public static void createQR(String data, String fileName)
            throws WriterException, IOException {

        int width = 400;
        int height = 400;
        String format = "PNG";

        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(
                data, BarcodeFormat.QR_CODE, width, height, hintMap);

        Path path = FileSystems.getDefault().getPath(fileName);
        MatrixToImageWriter.writeToPath(matrix, format, path);
    }
}
