package org.datapunch.starfish.util;

import com.amazonaws.regions.Regions;

public class AwsUtil {
    public static String tryGetRegionFromPrefix(String str) {
        if (StringUtil.isNullOrEmpty(str)) {
            throw new IllegalArgumentException("str is empty");
        }
        str = str.toLowerCase();
        for (Regions region: Regions.values()) {
            if (str.startsWith(region.getName().toLowerCase())) {
                return region.getName();
            }
        }
        return null;
    }
}
