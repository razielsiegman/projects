package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl <Key,Value> implements HashTable <Key,Value>{
    private ManualLinkedList[] hashArray;

    public HashTableImpl(){
        this.hashArray = new ManualLinkedList[5];
    }

    public Value get(Key k) {
        if(k == null){
            return null;
        }
        int index = this.hashFunction(k);
        if(hashArray[index] == null){
            return null;
        }
        ManualLinkedList<Key,Value> list = hashArray[index];
        ManualLinkedList<Key,Value>.Node<Key,Value> node = list.head;
        while(!(node.key.equals(k))){
            node = node.next;
            if(node == null){
                return null;
            }
        }
        return node.value;
    }

    public Value put(Key k,Value v) {
        Value val = this.nullHandler(k,v);
        if((k != null) && (v == null)){
            return val;
        }
        int index = this.hashFunction(k);
        if(hashArray[index] == null){
            ManualLinkedList<Key,Value> list = new ManualLinkedList<Key,Value>();
            hashArray[index] = list;
            list.addNode(k,v);
            return null;
        }
        else{
            ManualLinkedList<Key,Value> list = hashArray[index];
            ManualLinkedList<Key,Value>.Node<Key,Value> node = list.head;
            while(node != null) {
                if(k.equals(node.key)){
                    Value temp = node.value;
                    node.value = v;
                    return temp;
                }
                else {
                    node = list.nextNode(node);
                }
            }
            list.addNode(k,v);
            return null;
        }
    }

    //If key+value is deleted, the value gets returned to method
    private Value nullHandler(Key k, Value v){
        if((k != null) && (v != null)){
            return null;
        }
        else if(k == null){
            throw new IllegalArgumentException("No URI found");
        }
        //if key is defined and value is null
        else{
            Value val = this.deleteDoc(k);
            return val;
        }
    }

    private Value deleteDoc(Key k){
        int index = this.hashFunction(k);
        Value val;
        if(hashArray[index] == null){
            return null;
        }
        ManualLinkedList<Key,Value> list = hashArray[index];
        ManualLinkedList<Key,Value>.Node<Key,Value> node = list.head;
        if(node.key.equals(k)) {
            val = this.get(k);
            list.head = node.next;
            return val;
        }
        else{
            while((!(node.next.key.equals(k))) && (node.next != null)) {
                node = node.next;
            }
            if(node.next != null) {
                val = this.get(k);
                node.next = node.next.next;
                return val;
            }
            else{
                return null;
            }
        }
    }

    private <Key> int hashFunction(Key k){
        int hash = k.hashCode();
        hash = hash % hashArray.length;
        hash = Math.abs(hash);
        return hash;
    }

    private class ManualLinkedList<Key,Value>{
        private class Node<Key,Value>{
            private Node<Key,Value> next;
            private Key key;
            private Value value;
            private Node(Key key, Value value){
                this.key = key;
                this.value = value;
            }
        }
        private Node head;
        private Node tail;
        private void addNode(Key key, Value value){
            Node node = new Node(key, value);
            if(this.head == null){
                this.head = node;
                this.tail = node;
            }
            else {
                tail.next = node;
                this.tail = node;
            }
        }
        private Node nextNode(Node node){
            Node nextNode = node.next;
            return nextNode;
        }
    }
}