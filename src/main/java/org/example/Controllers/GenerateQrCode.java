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
import java.util.HashMap;
import java.util.Map;

public class GenerateQrCode {

    // Function to create the QR code
    public static void createQR(String data, String path,
                                String charset, int height, int width)
            throws WriterException, IOException {

        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        BitMatrix matrix = new MultiFormatWriter().encode(
                new String(data.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, width, height, hintMap);


        String fullPath = "C:\\path\\to\\your\\directory\\" + path;

        // Utiliser le chemin complet du fichier avec l'extension pour écrire le code QR
        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG", // Format du fichier
                new File(fullPath).toPath());
    }


}
