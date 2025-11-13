package com.vmonaco.bio;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.time.Instant;

public class Utility {

	private static Dimension mScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Convert an array of objects to a CSV string
	 */
	public static String csvString(Object[] objects) {
		String str = objects[0].toString();
		for (int i = 1; i < objects.length; i++) {
			str += "," + objects[i].toString();
		}
		return str;
	}

	/**
	 * Capture a section of the screen and zero out any part of the capture
	 * rectangle that is out of bounds (could contain noise)
	 * 
	 * @param capture
	 * @return
	 */
	public static BufferedImage screenCapture(Rectangle capture) {

		BufferedImage image = null;

		try {
			image = new Robot().createScreenCapture(capture);
		} catch (AWTException e) {
			BioLogger.LOGGER.severe(Utility.createCrashReport(e));
		}

		Rectangle fillX = new Rectangle();
		Rectangle fillY = new Rectangle();

		if (capture.x < 0) {
			fillX.x = 0;
			fillX.width = 0 - capture.x;
			fillX.height = capture.height;
		} else if (capture.x + capture.width > mScreenSize.width) {
			fillX.x = capture.width - ((capture.x + capture.width) - mScreenSize.width);
			fillX.width = (capture.x + capture.width) - mScreenSize.width;
			fillX.height = capture.height;
		}

		if (capture.y < 0) {
			fillY.y = 0;
			fillY.height = 0 - capture.y;
			fillY.width = capture.width;
		} else if (capture.y + capture.height > mScreenSize.height) {
			fillY.y = capture.height - ((capture.y + capture.height) - mScreenSize.height);
			fillY.height = (capture.y + capture.height) - mScreenSize.height;
			fillY.width = capture.width;
		}

		Graphics2D graph = image.createGraphics();
		graph.setColor(Color.BLACK);
		graph.fill(fillX);
		graph.fill(fillY);
		graph.dispose();

		return image;
	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String createCrashReport(Throwable e) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		printWriter.close();

		String header = "";
		header += "os.name    : " + System.getProperty("os.name") + "\n";
		header += "os.arch    : " + System.getProperty("os.arch") + "\n";
		header += "os.version : " + System.getProperty("os.version") + "\n";

		System.getProperties().keySet();
		for (Object k : System.getProperties().keySet()) {
			header += padRight(k.toString(), 40) + ": " + System.getProperty((String) k) + "\n";
		}

		String stacktrace = result.toString();
		return header + "\nStacktrace:\n" + stacktrace;
	}

	public static BufferedImage drawClickIndicator(BufferedImage img, int centerX, int centerY) {
		BufferedImage copy = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics2D g2d = copy.createGraphics();
		g2d.drawImage(img, 0, 0, null);

		int radius = 8;
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(3));
		g2d.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
		g2d.fillOval(centerX - radius + 3, centerY - radius + 3, radius * 2 - 6, radius * 2 - 6);

		g2d.dispose();
		return copy;
	}

	public static final DateTimeFormatter UTC_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS 'UTC'")
                             .withZone(ZoneOffset.UTC);

    public static String formatUtc(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC)
                      .format(UTC_FORMAT);
    }
}
