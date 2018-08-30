package com.google.common.collect;

import junit.framework.TestCase;
import org.junit.Assert;



/**
 * Unit test for {@code IntHashSet}.
 *
 * @author lianglin
 */
public class IntHashSetTest extends TestCase {

    public void testStrong_add(){
        IntHashSet intHashSet = Sets.newIntHashSet();
        for(int i = 0; i < 100; i++){
            intHashSet.add(i);
        }
        Assert.assertTrue(intHashSet.size() == 100);
        for(int i = 0; i < 100; i++){
            Assert.assertTrue(intHashSet.contains(i));
        }
    }

    public void testStrong_remove(){
        IntHashSet intHashSet = Sets.newIntHashSet();
        for(int i = 0; i < 100; i++){
            intHashSet.add(i);
        }
        int[] nums = new int[]{1,3,5,7,9};
        Assert.assertTrue(intHashSet.size() == 100);
        for(int num : nums){
            Assert.assertTrue(intHashSet.contains(num));
            intHashSet.remove(num);
            Assert.assertTrue(!intHashSet.contains(num));
        }
    }

    public void testString_onDuplicate(){
        int[] array = new int[] {1,1,2,2,3,3};
        IntHashSet intHashSet = Sets.newIntHashSet();
        for(int a : array){
            intHashSet.add(a);
        }
        Assert.assertTrue(intHashSet.size() == 3);

    }



}
