package com.rittmann.common.datasource.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Testing {
    @Test
    public void testing3() {
        doTest3(
                new String[]{"pim", "pom"},
                new String[]{"999999999", "777888999"},
                "88999",
                "pom"
        );

        doTest3(
                new String[]{"sander", "amy", "ann", "michael"},
                new String[]{"123456789", "234567890", "789123456", "123123123"},
                "1",
                "ann"
        );

        doTest3(
                new String[]{"adam", "eva", "leo"},
                new String[]{"121212121", "111111111", "444555666"},
                "112",
                "NO CONTACT"
        );
    }

    private void doTest3(
            String[] names,
            String[] numbers,
            String phone,
            String expected
    ) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            if (numbers[i].contains(phone))
                map.put(names[i], numbers[i]);
        }

        if (map.size() == 0) {
            assertEquals("NO CONTACT", expected);
        } else {
            List<String> sortedKeys = new ArrayList<>(map.keySet());
            Collections.sort(sortedKeys);
            assertEquals(expected, sortedKeys.get(0));
        }
    }
}
