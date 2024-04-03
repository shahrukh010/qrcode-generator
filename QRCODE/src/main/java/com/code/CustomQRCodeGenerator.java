package com.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.unlogged.Unlogged;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomQRCodeGenerator {

    @Unlogged
    public static void main(String[] args) {
        String url = "https://mysurprisegift.socialnationnow.com";
        String iconUrl = "https://wp-socialnation-assets.s3.ap-south-1.amazonaws.com/wp-content/uploads/2021/04/06213106/socialnation-1.png";
        String filePath = "QRCode.png";

        try {
            generateColoredQRCode(url, iconUrl, filePath, Color.WHITE, Color.BLACK); // Blue: #0074CC
            System.out.println("Interactive QR Code with Icon and URL generated successfully!");
        } catch (WriterException | IOException e) {
            System.err.println("Error generating Interactive QR Code with Icon and URL: " + e.getMessage());
        }
    }

    private static void generateColoredQRCode(String url, String iconUrl, String filePath, Color backgroundColor, Color foregroundColor) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300, hints);

        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage qrCodeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = qrCodeImage.createGraphics();
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(foregroundColor);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x, y)) {
                    qrCodeImage.setRGB(x, y, foregroundColor.getRGB());
                }
            }
        }

        // Add icon to the center of the QR code
        Image icon = ImageIO.read(new URL(iconUrl));
        int iconWidth = Math.min(width / 5, height / 5);
        int iconHeight = iconWidth;
        int iconX = (width - iconWidth) / 2;
        int iconY = (height - iconHeight) / 2;

        RoundRectangle2D.Double roundedRect = new RoundRectangle2D.Double(iconX, iconY, iconWidth, iconHeight, 900, 900);
        graphics.setClip(roundedRect);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(iconX, iconY, iconWidth, iconHeight);
        graphics.setClip(null);
        graphics.drawImage(icon, iconX, iconY, iconWidth, iconHeight, null);
        graphics.dispose();

        ImageIO.write(qrCodeImage, "png", new File(filePath));
    }
}
