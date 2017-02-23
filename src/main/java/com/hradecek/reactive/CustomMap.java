package com.hradecek.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CustomMap {

    interface Mapper<V, M> {
        M map(V value);
    }

    public static <V, M> List<M> map(List<V> list, Mapper<V, M> mapper) {
        List<M> mapped = new ArrayList<>(list.size());
        for (V v : list) {
            mapped.add(mapper.map(v));
        }

        return mapped;
    }

    public static <V, M> List<M> map(List<V> list, Function<V, M> function) {
        List<M> mapped = new ArrayList<>(list.size());
        for (V v : list) {
            mapped.add(function.apply(v));
        }

        return mapped;
    }

    public static void main(String[] args) {
        List<String> numbers = new ArrayList<String>() {{ add("1"); add("2"); add("3"); }};

        // List<Integer> mapped = map(numbers, v -> Integer.valueOf(v) * 2);
        List<Integer> mapped = map(numbers, new Mapper<String, Integer>() {
            @Override
            public Integer map(String value) {
                return Integer.valueOf(value) * 2;
            }
        });

        System.out.println("Numbers: " + numbers);
        System.out.println("Mapped:  " + mapped);

        // Without Mapper
        mapped = null;
        Function<String, Integer> toStrTimesTwo = v -> Integer.valueOf(v) * 2;
        mapped = map(numbers, toStrTimesTwo);
        System.out.println("Numbers: " + numbers);
        System.out.println("Mapped:  " + mapped);
    }
}
