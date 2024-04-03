package com.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Round {

    public static void main(String[] args) {
        String url = "https://www.socialnationnow.com/";
        String iconUrl = "https://wp-socialnation-assets.s3.ap-south-1.amazonaws.com/wp-content/uploads/2021/04/06213106/socialnation-1.png";
        String filePath = "colored_qrcode.png";

        try {
            generateColoredQRCode(url, iconUrl, filePath, Color.WHITE, new Color(0, 116, 204)); // Blue: #0074CC
            System.out.println("QR Code with Icon and URL generated successfully!");
        } catch (WriterException | IOException e) {
            System.err.println("Error generating QR Code with Icon and URL: " + e.getMessage());
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

        // Create rounded rectangle for the frame
        RoundRectangle2D.Double frame = new RoundRectangle2D.Double(0, 0, width, height, 20, 20);

        // Create gradient background
        GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, width, height, backgroundColor);
        graphics.setPaint(gradient);
        graphics.fill(frame);

        // Draw QR code pattern
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

        graphics.drawImage(icon, iconX, iconY, iconWidth, iconHeight, null);

        // Draw the rounded rectangle frame
        graphics.setColor(foregroundColor);
        graphics.draw(frame);

        graphics.dispose();

        ImageIO.write(qrCodeImage, "png", new File(filePath));
    }
}