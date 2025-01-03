package kr.co.emfo.kpro_test.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;

@Component
@Scope("prototype")
public class MapManager {

    private final TreeMap<Long, String> resultCodeMap = new TreeMap<>();

    public void addToMap(Long idx, String code) {
        resultCodeMap.put(idx, code);
    }

    public boolean isMapEmpty() {
        return resultCodeMap.isEmpty();
    }

    public Map.Entry<Long, String> getMapEntryData() throws IllegalAccessException {
        if (resultCodeMap.isEmpty()) throw new IllegalAccessException("Map is empty");

        return resultCodeMap.pollFirstEntry();
    }
}
