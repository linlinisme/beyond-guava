package com.google.common.collect;


import com.google.common.collector.IntArrayListCollector;

import java.util.List;

public class IntArrayListTest {

    public static void main(String[] args){
        List<Integer> list = Lists.newArrayList();
        list.add(1);
        list.add(2);
        IntArrayList intArrayList = list.stream().collect(new IntArrayListCollector());
        System.out.println(intArrayList);
    }
}
