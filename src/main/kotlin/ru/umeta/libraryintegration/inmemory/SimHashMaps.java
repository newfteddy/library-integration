package ru.umeta.libraryintegration.inmemory;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

/**
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 28.02.2016.
 */
public class SimHashMaps {
    IntObjectHashMap<MutableIntList>[][][][] troveMaps = new IntObjectHashMap[4][4][4][4];

    public IntObjectHashMap<MutableIntList> getOrCreateByIndex(int ti, int tj, int ai, int aj) {
        ti--;
        tj--;
        ai--;
        aj--;
        IntObjectHashMap<MutableIntList> result = troveMaps[ti][tj][ai][aj];
        if (result == null) {
            result = new IntObjectHashMap<>();
            troveMaps[ti][tj][ai][aj] = result;
        }
        return result;
    }

}
