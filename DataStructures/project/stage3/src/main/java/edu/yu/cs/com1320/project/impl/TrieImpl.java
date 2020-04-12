package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl <Value> implements Trie <Value> {

    private static final int alphabetSize = 256; // extended ASCII
    private TrieImpl.Node root; // root of trie
    private Set tempSet;

    public TrieImpl(){
        this.root = new Node<>();
        tempSet = new HashSet<Value>();
    }

    private static class Node<Value> {
        private List<Value> values = new ArrayList<>();
        private Node[] links = new Node[TrieImpl.alphabetSize];
    }

    public void put(String key, Value val) {
        //deleteAll the value from this key
        if (val == null) {
            return;
        }
        else {
            this.root = put(this.root, key, val, 0);
        }
    }

    private Node put(TrieImpl.Node x, String key, Value val, int d) {
        //create a new node
        if (x == null) {
            x = new TrieImpl.Node();
        }
        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (d == key.length()) {
            if(x.values == null){
                x.values = new ArrayList<>();
            }
            if(x.values.contains(val) == false){
                    x.values.add(val);
            }
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        x.links[c] = this.put(x.links[c], key, val, d + 1);
        return x;
    }

    public List<Value> getAllSorted(String key, Comparator<Value> comparator) {
        TrieImpl.Node x = this.get(this.root, key, 0);
        List<Value> list = new ArrayList<Value>();
        if (x == null) {
            return list;
        }
        list.addAll(x.values);
        list.sort(comparator);
        return list;
    }

    private TrieImpl.Node get(TrieImpl.Node x, String key, int d) {
        //link was null - return null, indicating a miss
        if (x == null) {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length()) {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.links[c], key, d + 1);
    }

    public List<Value> getAllWithPrefixSorted(String prefix,  Comparator<Value> comparator) {
        TrieImpl.Node x = this.get(this.root, prefix, 0);
        List<Value> allValues = new ArrayList<Value>();
        if (x == null) {
            return allValues;
        }
        else{
            allValues = (List)this.getPrefix(x, allValues);
            allValues.sort(comparator);
            return allValues;
        }
    }

    private List getPrefix(TrieImpl.Node x, List<Value> list){
        for(Object value : x.values){
            if(list.contains(value) != true){
                list.add((Value)value);
            }
        }
        for(Node node : x.links){
            if(node != null){
                for(Object value : x.values) {
                    if(list.contains(value) != true) {
                        list.add((Value)value);
                    }
                }
                this.getPrefix(node, list);
            }
        }
        return list;
    }

    public Set<Value> deleteAllWithPrefix(String prefix) {
        Set<Value> deletedValues = new HashSet<Value>();
        return (Set)this.deletePrefix(this.root, prefix, deletedValues);
    }

    private Set deletePrefix(TrieImpl.Node x, String prefix, Set<Value> set){
        for(int i = 0; i < 256; i++){
            TrieImpl.Node node = x.links[i];
            if(node != null){
                set.addAll(node.values);
                this.deleteAll(x, prefix, 0);
                char character = (char)i;
                this.deletePrefix(node,prefix + character, set);
            }
            return set;
        }
        return null;
    }

    public Set<Value> deleteAll(String key) {
        this.root = deleteAll(this.root, key, 0);
        return tempSet;
    }

    private TrieImpl.Node deleteAll(TrieImpl.Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        //we're at the node to del - set the val to null
        if (d == key.length()) {
            tempSet.clear();
            tempSet.addAll(x.values);
            x.values = null;
        }
        //continue down the trie to the target node
        else {
            char c = key.charAt(d);
            x.links[c] = this.deleteAll(x.links[c], key, d + 1);
        }
        //this node has a val – do nothing, return the node
        if (x.values != null) {
            return x;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c < TrieImpl.alphabetSize; c++) {
            if (x.links[c] != null) {
                return x; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }

    public Value delete(String key, Value val) {
        if((key == null) || (val == null)){
            return null;
        }
        TrieImpl.Node x = this.root;
        int d = 0;
        if (x == null) {
            return null;
        }
        while(d != key.length()){
            char c = key.charAt(d);
            x = x.links[c];
            d++;
        }
        //we're at the node to del - set the val to null
        if(x == null || x.values == null){
            return null;
        }
        if((x.values.contains(val)) == false){
            return null;
        }
        else{
            x.values.remove(val);
        }
        if ((x.values.isEmpty()) == true) {
            x.values.add(val);
            this.deleteAll(this.root, key, 0);
        }
        return val;
    }
}
