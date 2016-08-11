package com.ap.misc.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JavaStreamUtil {
	
	public static <T> Stream<T> of(Iterator<T> sourceIterator) {
        return of(sourceIterator, false);
    }

    public static <T> Stream<T> of(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
    
    public static <K, V> Stream<Entry<K, V>> of(Map<K, V> map) {
        return map.entrySet().stream();
    }

}
