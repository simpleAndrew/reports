package org.challenge.test.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Statistics<Type> {

    public static final double MEDIAN_PERCENT = 0.5;

    private final Type undefinedValue;
    private final Comparator<Type> comparator;


    public static Statistics<Integer> ofIntegers() {
        return new Statistics<>(Integer.MIN_VALUE, Integer::compare);
    }

    public static Statistics<Double> ofDoubles() {
        return new Statistics<>(Double.NaN, Double::compare);
    }

    private Statistics(Type undefinedValue, Comparator<Type> comparator) {
        this.undefinedValue = undefinedValue;
        this.comparator = comparator;
    }

    public <CONTAINER extends Collection<Type>> Type calculateMedian(CONTAINER numbers) {
        return calculatePercentile(numbers, MEDIAN_PERCENT);
    }

    public Type calculatePercentile(Collection<Type> numbers, double percent) {
        if (numbers == null || numbers.isEmpty()) {
            return undefinedValue;
        }

        if (percent > 100 || percent <= 0) {
            throw new IllegalArgumentException("Percent should be in (0, 100]");
        }

        List<Type> sortedNumbers = new ArrayList<>(numbers);
        sortedNumbers.sort(comparator);

        int percentileIndex = (int) Math.ceil(sortedNumbers.size() * percent) - 1;
        return sortedNumbers.get(percentileIndex);
    }

}
