package com.programme.Fortress.Util.TypeChange;

import java.util.ArrayList;
import java.util.List;

public class MyListUtil {

    /**
     * 拆分List
     * @param sourceList
     * @param groupSize
     * @return
     */
    public static List<List> splitList(List sourceList, int groupSize) {
        int length = sourceList.size();
        int num = (length + groupSize - 1) / groupSize;
        List<List> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
            newList.add(sourceList.subList(fromIndex, toIndex));
        }
        return newList;
    }
}
