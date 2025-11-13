package com.vmonaco.bio.events;

import com.vmonaco.bio.Utility;

public class BioKeystrokeEvent implements BioEvent {

	public static final String event_type = "keystroke";
	public static final String[] HEADER = {
		"press_time_utc", "release_time_utc", "key_code", "key_name",
		"modifier_code", "modifier_name", "location"
	};
	public long press_time;
	public long release_time;
	public int key_code;
	public String key_string;
	public int modifier_code;
	public String modifier_name;
	public int key_location;

	@Override
	public String[] header() {
		return HEADER;
	}

	@Override
	public String[] values() {
		String press  = Utility.formatUtc(press_time);
		String release = release_time == 0 ? "" : Utility.formatUtc(release_time);

		return new String[] {
			press,
			release,
			"" + key_code,
			key_string,
			"" + modifier_code,
			modifier_name,
			"" + key_location
		};
	}

	@Override
	public String toString() {
		return Utility.csvString(this.values());
	}
}
