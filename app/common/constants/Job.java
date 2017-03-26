package common.constants;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Job {
	BLNAK("", "- 未選択 -"),
	MANAGER("1", "マネージャー"),
	DIRECTOR("2", "ディレクター"),
	PROGRAMMER("3", "プログラマー"),
	DESIGNER("4", "デザイナー");
	public final String key;
	public final String value;
	Job(String key, String value){
		this.key = key;
		this.value = value;
	}
	public static Map<String, String> getMap(){
		return Arrays.asList(Job.values()).stream()
				.collect(Collectors.toMap(c -> c.key, c -> c.value));
	}
}
