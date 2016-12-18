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

    private static final int MAXIMUM_WIDTH = 600;
    private static final int MAXIMUM_HEIGHT = 450;

    public ImageTransformer() {
    }

    public void createSquareImage(final File originalFile, final String imagePath) throws IOException {
        BufferedImage originalImage = ImageIO.read(originalFile);
        final int unit = (originalImage.getWidth() * 3 / 4) - originalImage.getHeight();
        final int width = unit >= 0 ? originalImage.getWidth() : (originalImage.getHeight() * 4 / 3);
        final int height = unit >= 0 ? (originalImage.getWidth() * 3 / 4) : originalImage.getHeight();
        BufferedImage alteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = alteredImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        fillImageRects(originalImage, width, height, unit, graphics2D);
        graphics2D.dispose();
        BufferedImage scaledImage = scaleImage(alteredImage, MAXIMUM_WIDTH, MAXIMUM_HEIGHT, Color.white);
        ImageIO.write(scaledImage, "JPG", new File(imagePath));
    }

    private BufferedImage scaleImage(BufferedImage img, int width, int height,
                                     Color background) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        if (imgWidth * height < imgHeight * width) {
            width = imgWidth * height / imgHeight;
        } else {
            height = imgHeight * width / imgWidth;
        }
        BufferedImage newImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setBackground(background);
            g.clearRect(0, 0, width, height);
            g.drawImage(img, 0, 0, width, height, null);
        } finally {
            g.dispose();
        }
        return newImage;
    }


    private void fillImageRects(BufferedImage originalImage, int width, int height, int unit, Graphics2D graphics2D) {
        if (unit >= 0) {
            fillRectInTopAndBottom(originalImage, width, height, (height - originalImage.getHeight()) / 2, graphics2D);
        } else {
            fillRectInLeftAndRight(originalImage, width, height, (width - originalImage.getWidth()) / 2, graphics2D);
        }
    }

    private void fillRectInLeftAndRight(BufferedImage originalImage, int width, int height, int propSize, Graphics2D graphics2D) {
        graphics2D.fillRect(0, 0, propSize, height);
        graphics2D.fillRect(width - propSize - 1, 0, propSize + 1, height);
        graphics2D.drawImage(originalImage, propSize, 0, null);
    }

    private void fillRectInTopAndBottom(BufferedImage originalImage, int width, int height, int propSize, Graphics2D graphics2D) {
        graphics2D.fillRect(0, 0, width, propSize);
        graphics2D.fillRect(0, height - propSize - 1, width, propSize + 1);
        graphics2D.drawImage(originalImage, 0, propSize, null);
    }

}
