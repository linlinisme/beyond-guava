## beyond-guava: 在guava原有基础上提供高性能的原生类型集合，并与java原有集合一样可以支持lambda-stream流操作、collecor收集


### maven引入

```xml
       <dependency>
          <groupId>com.github.linlinisme</groupId>
          <artifactId>beyond-guava</artifactId>
          <version>1.0.0</version>
      </dependency>
```

## 性能对比
原生类型性能上天生比包装类型更有优势。包装类型在java内存中是以对象的形式存在，java对象的构成包括对象头、数据部分、对齐填充三部分，所以包装类型更占内存空间，在进行运算时则需要频繁的拆箱、装箱,并且获取真正数值时因集合中保存的是对象引用并不是对象本身，所以还需要一次内存寻址。这些在很多场景下其实都是一些无谓的损耗。下面以intHashSet和HashSet为例随机add,remove,contains 数量[5000,100000]个随机整数进行一个性能测试
### 测试结果 
![avatar](http://i1.bvimg.com/660662/6ef0418ac51c6ae0.png)



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
