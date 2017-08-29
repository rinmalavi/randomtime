package de.atron.randomtime.random.services;

import rx.Observable;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class RandomProducer {

    private Random random;
    private String vowels = "aoeiu";
    private String consonants = "bcdfghjklmnvzyx";

    public RandomProducer() {
        random = new Random();
    }
    public String string(int length) {

        Observable<String> chars =  Observable.range(0, length).map(
                i -> {
                    if (i % 2 == 0) {
                        int rand = random.nextInt(consonants.length());
                        return consonants.substring(rand, rand + 1);
                    } else {
                        int rand = random.nextInt(vowels.length());
                        return vowels.substring(rand, rand + 1);
                    }
                }
        );

        return rx.observables.StringObservable.stringConcat(chars).toBlocking().first();
    }

    public Integer integer(Integer maxValue) {
        return random.nextInt(maxValue);
    }

    public Boolean bool() {
        return random.nextInt(2) == 1;
    }

}
