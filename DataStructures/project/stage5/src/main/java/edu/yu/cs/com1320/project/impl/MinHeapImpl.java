package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Beginnings of a MinHeap, for Stage 4 of project. Does not include logic needed throughout various methods to track
 * an element's array index, which is needed to reheapify an element after its last use time changes.
 * @param <E>
 */
public class MinHeapImpl<E extends Comparable>extends MinHeap
{
    protected E[] elements;
    protected int count;
    protected Map<E,Integer> elementsToArrayIndex; //used to store the index in the elements array

    public MinHeapImpl(){
    this.elements = (E[]) new Comparable [5];
    this.count = 0;
    this.elementsToArrayIndex = new HashMap<E, Integer>();
    }

    @Override
    public void reHeapify(Comparable element){
        int index = this.getArrayIndex(element);
        this.downHeap(index);
        this.upHeap(index);
    }

    protected int getArrayIndex(Comparable element){
        return elementsToArrayIndex.get(element);
    }

    protected void doubleArraySize(){
        E[] newArray = (E[]) new Comparable [this.elements.length * 2];
        for(int i = 0; i < this.elements.length; i++){
            newArray[i] = this.elements[i];
        }
        this.elements = newArray;
    }

    protected  boolean isEmpty()
    {
        return this.count == 0;
    }
    /**
     * is elements[i] > elements[j]?
     */
    protected  boolean isGreater(int i, int j)
    {
        return this.elements[i].compareTo(this.elements[j]) > 0;
    }

    /**
     * swap the values stored at elements[i] and elements[j]
     */
    protected  void swap(int i, int j)
    {
        E temp = this.elements[i];
        this.elements[i] = this.elements[j];
        this.elements[j] = temp;
        elementsToArrayIndex.put(this.elements[i], i);
        elementsToArrayIndex.put(this.elements[j], j);
    }

    /**
     *while the key at index k is less than its
     *parent's key, swap its contents with its parent’s
     */
    protected  void upHeap(int k)
    {
        while (k > 1 && this.isGreater(k / 2, k))
        {
            this.swap(k, k / 2);
            k = k / 2;
        }
    }

    /**
     * move an element down the heap until it is less than
     * both its children or is at the bottom of the heap
     */
    protected  void downHeap(int k)
    {
        while (2 * k <= this.count)
        {
            //identify which of the 2 children are smaller
            int j = 2 * k;
            if (j < this.count && this.isGreater(j, j + 1))
            {
                j++;
            }
            //if the current value is < the smaller child, we're done
            if (!this.isGreater(k, j))
            {
                break;
            }
            //if not, swap and continue testing
            this.swap(k, j);
            k = j;
        }
    }

    public void insert(Comparable x)
    {
        // double size of array if necessary
        if (this.count >= this.elements.length - 1)
        {
            this.doubleArraySize();
        }
        //add x to the bottom of the heap
        this.elements[++this.count] = (E) x;
        //percolate it up to maintain heap order property
        this.upHeap(this.count);
        elementsToArrayIndex.put((E) x, this.count);
    }

    public E removeMin()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException("Heap is empty");
        }
        E min = this.elements[1];
        //swap root with last, decrement count
        this.swap(1, this.count--);
        //move new root down as needed
        this.downHeap(1);
        this.elements[this.count + 1] = null; //null it to prepare for GC
        elementsToArrayIndex.remove(min);
        return min;
    }
}
