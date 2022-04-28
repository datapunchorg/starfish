package datapunch.org.starfish.util;

import com.amazonaws.regions.Regions;

public class AwsUtil {
    public static String getRegionFromPrefix(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("str is empty");
        }
        str = str.toLowerCase();
        for (Regions region: Regions.values()) {
            if (str.startsWith(region.getName().toLowerCase())) {
                return region.getName();
            }
        }
        throw new IllegalArgumentException(String.format("str is not valid region: %s", str));
    }
}
