package com.vmonaco.bio.events;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.vmonaco.bio.BioLogger;
import com.vmonaco.bio.Utility;

import java.util.Base64;

public class BioClickEvent implements BioEvent {

	public static final String event_type = "mouseclick";

	public static final String[] HEADER = {
        "press_time_utc", "release_time_utc", "button_code",
        "press_x", "press_y", "release_x", "release_y",
        "modifier_code", "modifier_name", "image_filename"
    };

	public long press_time;
	public long release_time;
	public int button_code;
	public int press_x;
	public int press_y;
	public int release_x;
	public int release_y;
	public int modifier_code;
	public String modifier_name;
	public BufferedImage image;
	public String image_filename = "";

	@Override
	public String[] header() {
		return HEADER;
	}

	@Override
	public String[] values() {
		String pressUtc  = Utility.formatUtc(press_time);
		String releaseUtc = release_time == 0 ? "" : Utility.formatUtc(release_time);

		return new String[] {
			pressUtc,
			releaseUtc,
			"" + button_code,
			"" + press_x, "" + press_y,
			"" + release_x, "" + release_y,
			"" + modifier_code,
			modifier_name,
			image_filename
		};
	}

	@Override
	public String toString() {
		return Utility.csvString(this.values());
	}
}
