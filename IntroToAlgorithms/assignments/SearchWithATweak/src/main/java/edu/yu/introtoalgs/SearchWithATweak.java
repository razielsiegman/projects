package edu.yu.introtoalgs;

import java.util.List;

public class SearchWithATweak {

    /** Searches the specified sorted list of Integers for
     * the specified key. The list must be sorted prior to
     * making this call: otherwise , the results are
     * undefined. If the list contains multiple elements
     * with the specified value , will return the index of
     * the first instance of that value.
     *
     * @param list the list to be searched: the list is
     * assumed to be sorted
     * @param key the value to be searched for
     * @return Index of the search key, if it is contained
     * in the list; otherwise , returns -1.
     */
    public static int findFirstInstance(final List<Integer> list , final int key ){
        if(list == null){
            throw new IllegalArgumentException("List is null");
        }
        if(list.size() == 0){
            throw new IllegalArgumentException("List contains no elements");
        }
        if(list.get(list.size() - 1) < list.get(0)){
            return findFirstReverse(list, key);
        }
        int first = 0;
        int last = list.size() - 1;
        int middle = last / 2;
        while(first < last) {
            if (list.get(middle) == key) {
                while ((middle != 0) && (list.get(middle - 1) == key)){
                    middle = middle - 1;
                }
                return middle;
            }
            if (list.get(middle) > key) {
                if(middle == first){
                    return -1;
                }
                last = middle - 1;
                middle = (last + first) / 2;
                continue;
            }
            if(list.get(middle) < key){
                if(middle == last){
                    return -1;
                }
                first = middle + 1;
                middle = (last + first) / 2;
            }
        }
        if((first == last) && (list.get(first)) == key){
            return first;
        }
        else {
            return -1;
        }
    }

    private static int findFirstReverse(List<Integer> list , int key ){
        int first = 0;
        int last = list.size() - 1;
        int middle = last / 2;
        while(first < last) {
            if (list.get(middle) == key) {
                while ((middle != 0) && (list.get(middle - 1) == key)){
                    middle = middle - 1;
                }
                return middle;
            }
            if (list.get(middle) < key) {
                if(middle == first){
                    return -1;
                }
                last = middle - 1;
                middle = (last + first) / 2;
                continue;
            }
            if(list.get(middle) > key){
                if(middle == last){
                    return -1;
                }
                first = middle + 1;
                middle = (last + first) / 2;
            }
        }
        if((first == last) && (list.get(first)) == key){
            return first;
        }
        else {
            return -1;
        }
    }

    /** Searched the specified sorted list of distinct
     * Integers and returns an index i with the property
     * that the value of the ith element is itself i.
     *
     *
     * @param list the list to be searched: the list is
     * assumed to be sorted and contains distinct values
     * @return Index satisfying the specified property if any
     * such elements exist; otherwise , return -1
     */
    public static int elementEqualToItsIndex(final List<Integer> list){
        if(list == null){
            throw new IllegalArgumentException("List is null");
        }
        if(list.size() == 0){
            throw new IllegalArgumentException("List contains no elements");
        }
        if(list.get(list.size() - 1) < list.get(0)){
            return elementEqualReverse(list);
        }
        int first = 0;
        int last = list.size() - 1;
        int middle = last/2;
        while(first < last) {
            if (list.get(middle) == middle) {
                return middle;
            }
            if (list.get(middle) > middle) {
                if(middle == first){
                    return -1;
                }
                last = middle - 1;
                middle = (last + first) / 2;
                continue;
            }
            if(list.get(middle) < middle){
                if(middle == last){
                    return -1;
                }
                first = middle + 1;
                middle = (last + first) / 2;
            }
        }
        if((first == last) && (list.get(first)) == first){
            return first;
        }
        return -1;
    }

    private static int elementEqualReverse(List<Integer> list){
        int first = 0;
        int last = list.size() - 1;
        int middle = last/2;
        while(first < last) {
            if (list.get(middle) == middle) {
                return middle;
            }
            if (list.get(middle) < middle) {
                if(middle == first){
                    return -1;
                }
                last = middle - 1;
                middle = (last + first) / 2;
                continue;
            }
            if(list.get(middle) > middle){
                if(middle == last){
                    return -1;
                }
                first = middle + 1;
                middle = (last + first) / 2;
            }
        }
        if((first == last) && (list.get(first)) == first){
            return first;
        }
        return -1;
    }
}
