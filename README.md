## beyond-guava: 在guava原有基础上提供高性能的原生类型集合。原生类型集合与java原有集合一样可以支持lambda-stream流操作、collecor收集，性能要比java原有集合平均高50%以上



## Adding Beyond-Guava to your build


```xml
       <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>beyond-guava</artifactId>
            <version>1.0.0</version>
        </dependency>
```


## 性能测试结果 

```xml
Benchmark                                                Mode  Cnt     Score     Error  Units
BeyondGuavaBenchmark.beyondGuavaCollectorToListBeanmark  avgt    5    37.124 ±   2.570  us/op
BeyondGuavaBenchmark.beyondGuavaHashMapBenchmark         avgt    5  1664.142 ± 514.607  us/op
BeyondGuavaBenchmark.javaCollectorsToListBenchmark       avgt    5    80.586 ±   4.331  us/op
BeyondGuavaBenchmark.javaToMapBenchmark                  avgt    5  2069.030 ± 136.357  us/op  
```

##使用例子

```xml
 //原生list操作
 IntArrayList intArrayList = new IntArrayList();
        for(int i = 0; i < 50000; i++) intArrayList.add(i); 
 IntStream.rangeClosed(0,5000).boxed().collect(PrimitiveCollectors.toIntArrayList());
 
  //原生set操作
  IntHashSet intHashSet = new IntHashSet();
        for(int i = 0; i < 1000; i++){
            intHashSet.add(i);
        }
 intHashSet.stream().collect(Collectors.toList());
 IntHashSet collect = IntStream.rangeClosed(0, 100).boxed().collect(PrimitiveCollectors.toIntHashSet());                                
 
 //原生map操作
  Int2IntHashMap intHashMap = new Int2IntHashMap();

  for(int i = 0; i < 1000; i++){
            intHashMap.put(i,i);
        }
  List<Integer> keyList = intHashMap.keySet().stream().collect(Collectors.toList());
  Int2IntHashMap hashMap = keyList.stream().collect(PrimitiveCollectors.toInt2IntHashMap(i -> i, Function.identity()));
 
```
