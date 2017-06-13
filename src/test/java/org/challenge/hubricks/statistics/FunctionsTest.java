package org.challenge.hubricks.statistics;

import org.challenge.hubricks.utils.Constants;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FunctionsTest {

    @Test
    public void shouldCalculateMedianWithOddNumberOFRecords() throws Exception {
        //given
        double expectedMedian = 1.0;
        List<Double> values = Arrays.asList(0.0, expectedMedian, 10.0);

        //when
        double median = Statistics.ofDoubles().calculateMedian(values);

        //then
        assertEquals(expectedMedian, median, Constants.ACCEPTABLE_DELTA);
    }

    @Test
    public void shouldCalculateMedianWithEvenNumberOFRecords() throws Exception {
        //given
        double expectedMedian = 4.0;
        List<Double> values = Arrays.asList(0.0, 4.0, 8.0, 10.0);

        //when
        double median = Statistics.ofDoubles().calculateMedian(values);

        //then
        assertEquals(expectedMedian, median, Constants.ACCEPTABLE_DELTA);
    }

    @Test
    public void shouldCalculateMedianOnUnsortedCollection() {
        //given
        double expectedMedian = 4.0;
        Set<Double> values = new HashSet<>(Arrays.asList(0.0, 4.0, 4.0, 10.0));

        //when
        double median = Statistics.ofDoubles().calculateMedian(values);

        //then
        assertEquals(expectedMedian, median, Constants.ACCEPTABLE_DELTA);
    }

    @Test
    public void shouldGiveNanOnEmptyCollection() {
        //given
        Collection<Double> values = Collections.emptySet();

        //when
        Double median = Statistics.ofDoubles().calculateMedian(values);

        //then
        assertEquals(Double.NaN, (Object) median);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailOnNullObjectInCollection() {
        //given
        Set<Double> values = new HashSet<>(Arrays.asList(0.0, 4.0, null, 4.0, 10.0));

        //when
        Double median = Statistics.ofDoubles().calculateMedian(values);

        //then
        fail("Should fail with NPE on nulls in collection");
    }
}