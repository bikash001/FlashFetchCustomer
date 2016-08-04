package com.buyer.flashfetch.Helper;

import com.buyer.flashfetch.Objects.Quote;

import java.util.Comparator;

public class Comparators {

    public static class NewestComparator implements Comparator<Quote> {

        @Override
        public int compare(Quote quote1, Quote quote2) {
            return 1;
        }
    }

    public static class HighToLowComparator implements Comparator<Quote> {

        @Override
        public int compare(Quote quote1, Quote quote2) {
            return quote1.getQPrice().compareTo(quote2.getQPrice());
        }
    }

    public static class LowToHighComparator implements Comparator<Quote>{

        @Override
        public int compare(Quote quote1, Quote quote2) {
            return quote2.getQPrice().compareTo(quote1.getQPrice());
        }
    }

    public static class DistaneComparator implements Comparator<Quote>{

        @Override
        public int compare(Quote quote1, Quote quote2) {
            return quote2.getDistance().compareTo(quote1.getDistance());
        }
    }
}
