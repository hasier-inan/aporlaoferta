package com.aporlaoferta.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 09/07/15
 * Time: 14:45
 */
public class ImageTransformer {

    private final GraphicsConfiguration config;

    public ImageTransformer() {
        System.setProperty("java.awt.headless", "true");
        this.config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    public void createSquareImage(final File originalFile, final String imagePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(originalFile);
        final int squareSize = widthIsBiggerThanHeight(originalImage) ? originalImage.getWidth() : originalImage.getHeight();
        BufferedImage alteredImage = this.config.createCompatibleImage(squareSize, squareSize);
        Graphics2D graphics2D = alteredImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        fillImageRects(originalImage, squareSize, graphics2D);
        graphics2D.dispose();
        ImageIO.write(alteredImage, "JPG", new File(imagePath));
    }

    private void fillImageRects(BufferedImage originalImage, int squareSize, Graphics2D graphics2D) {
        if (widthIsBiggerThanHeight(originalImage)) {
            fillRectInTopAndBottom(originalImage, squareSize, graphics2D);
        } else {
            fillRectInLeftAndRight(originalImage, squareSize, graphics2D);
        }
    }

    private void fillRectInLeftAndRight(BufferedImage originalImage, int squareSize, Graphics2D graphics2D) {
        int diff = originalImage.getHeight() - originalImage.getWidth();
        int propSize = diff / 2;
        graphics2D.fillRect(0, 0, propSize, squareSize);
        graphics2D.fillRect(squareSize - propSize, 0, propSize, squareSize);
        graphics2D.drawImage(originalImage, propSize, 0, null);
    }

    private void fillRectInTopAndBottom(BufferedImage originalImage, int squareSize, Graphics2D graphics2D) {
        int diff = originalImage.getWidth() - originalImage.getHeight();
        int propSize = diff / 2;
        graphics2D.fillRect(0, 0, squareSize, propSize);
        graphics2D.fillRect(0, squareSize - propSize, squareSize, propSize);
        graphics2D.drawImage(originalImage, 0, propSize, null);
    }

    private boolean widthIsBiggerThanHeight(BufferedImage originalImage) {
        return originalImage.getWidth() > originalImage.getHeight();
    }

}
