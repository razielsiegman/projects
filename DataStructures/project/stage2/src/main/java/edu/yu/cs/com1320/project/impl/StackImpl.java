package edu.yu.cs.com1320.project.impl;
import edu.yu.cs.com1320.project.Stack;
public class StackImpl<T> implements Stack<T> {

    private int count;
    private Node head;

    public StackImpl(){
        this.count = 0;
        this.head = null;
    }

    private class Node{
        private Node next;
        private T value;
        private Node(T value){
            this.value = value;
            this.next = null;
        }
    }

    public void push(T element) {
        Node node = new Node(element);
        count++;
        if(this.head == null){
            this.head = node;
        }
        else{
            node.next = head;
            head = node;
        }
    }

    public T pop()
    {
        Node node = head;
        head = head.next;
        count--;
        return node.value;
    }

    public T peek() {
        if(this.head == null){
            return null;
        }
        else {
            return head.value;
        }
    }

    public int size() {
        return count;
    }
}
