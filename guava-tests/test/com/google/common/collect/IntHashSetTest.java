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


}
