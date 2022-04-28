package datapunch.org.starfish.util;

import java.util.List;
import java.util.Random;

public class ListUtil {
    public static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> T getRandomValue(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list is null or empty");
        }
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}
