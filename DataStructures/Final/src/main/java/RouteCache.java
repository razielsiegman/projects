import java.util.HashMap;
import java.util.Map;

/**
 * This is a bounded cache that maintains only the most recently accessed IP Addresses
 * and their routes.  Only the least recently accessed route will be purged from the
 * cache when the cache exceeds capacity.  There are 2 closely coupled data structures:
 *   -  a Map keyed to IP Address, used for quick lookup
 *   -  a Queue of the N most recently accessed IP Addresses
 * All operations must be O(1).  A big hint how to make that happen is contained
 * in the type signature of the Map on line 38.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RouteCache
{
    // instance variables - add others if you need them
    // do not change the names of any fields as the test code depends on them
    
    // Cache total capacity and current fill count.
    private final int capacity;
    private int nodeCount;
    
    // private class for nodes in a doubly-linked queue
    // used to keep most-recently-used data
    private class Node {
        private Node prev, next;
        private final IPAddress elem; 
        private final int route;

        Node(IPAddress elem, int route) {
            prev = next = null;
            this.elem = elem;
            this.route = route;
        }  
    }
    private Node head = null;
    private Node tail = null;
    private Map<IPAddress, Node> nodeMap; // the cache itself

    /**
     * Constructor for objects of class RouteCache
     */
    public RouteCache(int cacheCapacity)
    {
        this.nodeMap = new HashMap<IPAddress, Node>();
        this.capacity = cacheCapacity;
    }

    /**
     * Lookup the output port for an IP Address in the cache
     * 
     * @param  addr   a possibly cached IP Address
     * @return     the cached route for this address, or null if not found 
     */
    public Integer lookupRoute(IPAddress addr)
    {
        Node node = nodeMap.get(addr);
        if(node == null){
            return null;
        }
        return node.route;
     }
     
    /**
     * Update the cache each time an element's route is looked up.
     * Make sure the element and its route is in the Map.
     * Enqueue the element at the tail of the queue if it is not already in the queue.  
     * Otherwise, move it from its current position to the tail of the queue.  If the queue
     * was already at capacity, remove and return the element at the head of the queue.
     * 
     * @param  elem  an element to be added to the queue, which may already be in the queue. 
     *               If it is, don't add it redundantly, but move it to the back of the queue
     * @return       the expired least recently used element, if any, or null
     */
    public IPAddress updateCache(IPAddress elem, int route)
    {
        if(nodeMap.get(elem) == null){
            Node node = new Node(elem, route);
            nodeMap.put(elem, node);
            nodeCount++;
            if(this.tail == null){
                this.tail = node;
                this.head = node;
            }
            else{
                this.tail.prev = node;
                node.next = this.tail;
                this.tail = node;
                if(nodeCount > capacity){
                    Node remove = this.head;
                    this.head = this.head.prev;
                    this.head.next = null;
                    nodeMap.remove(remove.elem);
                    return remove.elem;
                }
            }
            return null;
        }
        else{
            Node cached = nodeMap.get(elem);
            if(this.tail == cached) {
                return null;
            }
            if(this.head == cached){
                this.head = this.head.prev;
                this.head.next = null;
                this.tail.prev = cached;
                cached.next = this.tail;
                this.tail = cached;
                return null;
            }
            cached.next.prev = cached.prev;
            cached.prev.next = cached.next;
            this.tail.prev = cached;
            cached.next = this.tail;
            this.tail = cached;
            return null;
        }
    }

    
    /**
     * For testing and debugging, return the contents of the LRU queue in most-recent-first order,
     * as an array of IP Addresses in CIDR format. Return a zero length array if the cache is empty
     * 
     */
    String[] dumpQueue()
    {
        String[] queue = new String[nodeMap.size()];
        if(queue.length == 0){
            return queue;
        }
        Node current = this.tail;
        int count = 0;
        queue[0] = current.elem.toCIDR();
        while(current.next != null){
            current = current.next;
            count++;
            queue[count] = current.elem.toCIDR();
        }
    	return queue;
    }
}
