package com.hradecek.reactive;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class ReactiveSum implements Observer<Double> {

    private double sum;

    public ReactiveSum(Observable<Double> a, Observable<Double> b) {
        this.sum = 0;
        Observable.combineLatest(a, b, (Func2<Double, Double, Object>) (a1, b1) -> a1 + b1)
                  .subscribe();
    }

    public void onCompleted() {
        System.out.println("Exiting last sum was: " + this.sum);
    }

    public void onError(Throwable throwable) {
        System.err.println("Got an Error!");
        throwable.printStackTrace();
    }

    public void onNext(Double sum) {
        this.sum = sum;
        System.out.println("Update: a + b = " + sum);
    }

    public static ConnectableObservable<String> from(final InputStream stream) {
        return from(new BufferedReader(new InputStreamReader(stream)));
    }

    public static ConnectableObservable<String> from(final BufferedReader reader) {
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            try {
                String line;
                while (!subscriber.isUnsubscribed() && (line = reader.readLine()) != null) {
                    if (line.equals("exit")) {
                        break;
                    }
                    subscriber.onNext(line);
                }
            } catch (IOException e) {
                subscriber.onError(e);
            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }).publish();
    }

    public static Observable<Double> varStream(final String varName, Observable<String> input) {
        final Pattern pattern = Pattern.compile("\\^s*" + varName + "\\s*[:|=]\\s*(-?\\d+\\.?\\d*)$");

        return input.map(pattern::matcher)
                    .filter(m -> m.matches() && m.group(1) != null)
                    .map(m -> Double.parseDouble(m.group(1)));
    }

    public static void main(String[] args) {
        ConnectableObservable<String> input = from(System.in);

        Observable<Double> a = varStream("a", input);
        Observable<Double> b = varStream("b", input);

        new ReactiveSum(a, b);

        input.connect();
    }
}
