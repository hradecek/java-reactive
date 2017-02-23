package com.hradecek.reactive;

import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class Basic {

    public static void main( String[] args ) {
        List<String> list = Arrays.asList("One", "Two", "Three", "Four", "Five");
        Observable<String> observable = Observable.from(list);

        observable.subscribe(System.out::println);
    }
}
