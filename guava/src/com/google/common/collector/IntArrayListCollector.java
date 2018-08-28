package com.google.common.collector;

import com.google.common.collect.IntArrayList;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class IntArrayListCollector implements Collector<Integer,IntArrayList,IntArrayList> {


    @Override
    public Supplier<IntArrayList> supplier() {
        Collectors.toList();
        return () -> new IntArrayList();
    }

    @Override
    public BiConsumer<IntArrayList, Integer> accumulator() {
        return IntArrayList::add;
    }

    @Override
    public BinaryOperator<IntArrayList> combiner() {
        return (left,right) -> {left.addAll(right); return left;};
    }

    @Override
    public Function<IntArrayList, IntArrayList> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    }
}
