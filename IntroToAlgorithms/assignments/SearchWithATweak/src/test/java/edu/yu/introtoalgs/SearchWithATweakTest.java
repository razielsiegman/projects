package edu.yu.introtoalgs;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchWithATweakTest {
    @Test
    public void regTest(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{1,3,4,5,6,7,8};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(1, search.findFirstInstance(list, 3));
        assertEquals(-1, search.elementEqualToItsIndex(list));
    }

    @Test
    public void OneNumberTest(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{4};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, 4));
        assertEquals(-1, search.findFirstInstance(list, -4));
        assertEquals(-1, search.elementEqualToItsIndex(list));
    }

    @Test
    public void zeroTest(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{0};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, 0));
        assertEquals(-1, search.findFirstInstance(list, 1));
        assertEquals(0, search.elementEqualToItsIndex(list));
    }

    @Test
    public void negativeNumbersTest(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{-8, -5, -4, -1, 0, 1, 12, 15};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, -8));
        assertEquals(2, search.findFirstInstance(list, -4));
        assertEquals(1, search.findFirstInstance(list, -5));
        assertEquals(-1, search.findFirstInstance(list, -3));
        assertEquals(3, search.findFirstInstance(list, -1));
        assertEquals(5, search.findFirstInstance(list, 1));
        assertEquals(4, search.findFirstInstance(list, 0));
        assertEquals(6, search.findFirstInstance(list, 12));
        assertEquals(-1, search.findFirstInstance(list, 13));
        assertEquals(-1, search.elementEqualToItsIndex(list));
    }

    @Test
    public void negativeNumbersTestTwo(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{-8, -5, -4, -1, 0, 1, 6, 12, 15};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, -8));
        assertEquals(2, search.findFirstInstance(list, -4));
        assertEquals(1, search.findFirstInstance(list, -5));
        assertEquals(-1, search.findFirstInstance(list, -3));
        assertEquals(3, search.findFirstInstance(list, -1));
        assertEquals(5, search.findFirstInstance(list, 1));
        assertEquals(4, search.findFirstInstance(list, 0));
        assertEquals(7, search.findFirstInstance(list, 12));
        assertEquals(-1, search.findFirstInstance(list, 13));
        assertEquals(6, search.elementEqualToItsIndex(list));
    }

    @Test
    public void onlyNegativeTest(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{-100, -90, -7, -5, -2, -1};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, -100));
        assertEquals(2, search.findFirstInstance(list, -7));
        assertEquals(-1, search.findFirstInstance(list, 1));
        assertEquals(-1, search.elementEqualToItsIndex(list));
    }

    @Test
    public void onlyNegativeTestTwo(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[]{-100, -90, -7, -5, -2, -1, 0};
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        assertEquals(0, search.findFirstInstance(list, -100));
        assertEquals(6, search.findFirstInstance(list, 0));
        assertEquals(2, search.findFirstInstance(list, -7));
        assertEquals(-1, search.findFirstInstance(list, 1));
        assertEquals(-1, search.elementEqualToItsIndex(list));
    }


    @Test(expected = IllegalArgumentException.class)
    public void noInput(){
        SearchWithATweak search = new SearchWithATweak();
        Integer[] array = new Integer[0];
        List<Integer> list = new ArrayList<Integer>();
        list.addAll(Arrays.asList(array));
        search.findFirstInstance(list, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullListTest(){
        SearchWithATweak search = new SearchWithATweak();
        search.findFirstInstance(null, 3);
    }

}
