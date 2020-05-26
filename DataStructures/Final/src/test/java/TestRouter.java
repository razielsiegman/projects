
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

/**
 * The test class TestRouter.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TestRouter
{

    //private TrieST<Integer> router;
    private IPRouter router;

    /**
     * Handle an unroutable address
     */
    @Test
    public void testBadRoute() {
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("73.73.0.1");
        assertEquals(-1, this.router.getRoute(address));
    }

    /**
     * Handle an address that only matches one prefix
     */
    @Test
    public void port2Test()
    {
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("85.2.0.1");
        int res = this.router.getRoute(address);
        assertEquals(2, res);
    }

    /**
     * Handle an address that only matches multiple prefixes. Only the longest one counts
     */
    @Test
    public void port1Test()
    {
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes1.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("85.85.85.85");
        int res = this.router.getRoute(address);
        assertEquals(1, res);
    }

    //Test every address in routes2.txt, which includes a case of three overlaps
    @Test
    public void routes2Test(){
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        int res1 = this.router.getRoute(address1);
        assertEquals(4, res1);
        IPAddress address2 = new IPAddress("24.128.0.0");
        int res2 = this.router.getRoute(address2);
        assertEquals(3, res2);
        IPAddress address3 = new IPAddress("24.64.0.0");
        int res3 = this.router.getRoute(address3);
        assertEquals(4, res3);
        IPAddress address4 = new IPAddress("24.16.0.0");
        int res4 = this.router.getRoute(address4);
        assertEquals(4, res4);
        IPAddress address5 = new IPAddress("24.30.0.0");
        int res5 = this.router.getRoute(address5);
        assertEquals(5, res5);
        IPAddress address6 = new IPAddress("24.34.0.0");
        int res6 = this.router.getRoute(address6);
        assertEquals(6, res6);
        IPAddress address7 = new IPAddress("24.60.0.0");
        int res7 = this.router.getRoute(address7);
        assertEquals(5, res7);
        IPAddress address8 = new IPAddress("24.91.0.0");
        int res8 = this.router.getRoute(address8);
        assertEquals(7, res8);
        IPAddress address9 = new IPAddress("24.98.0.0");
        int res9 = this.router.getRoute(address9);
        assertEquals(6, res9);
        IPAddress address10 = new IPAddress("85.0.0.0");
        int res10 = this.router.getRoute(address10);
        assertEquals(2, res10);
        IPAddress address11 = new IPAddress("85.85.0.0");
        int res11 = this.router.getRoute(address11);
        assertEquals(1, res11);
    }

    //test an address not in the .txt file
    @Test
    public void newAddressTest(){
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("24.17.73.255");
        int res = this.router.getRoute(address);
        assertEquals(4, res);
    }

    //test proper rule deleting
    @Test
    public void deletedAddressTest(){
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        router.deleteRule("24.0.0.0/9");
        IPAddress address = new IPAddress("24.0.0.0");
        int res = this.router.getRoute(address);
        assertEquals(4, res);
        router.deleteRule("24.0.0.0/12");
        res = this.router.getRoute(address);
        assertEquals(1, res);
    }

    //test deleting a rule that is not present
    @Test
    public void deleteNotPresentTest(){
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        router.deleteRule("24.128.83.41/28");
        IPAddress address = new IPAddress("25.128.83.41");
        assertEquals(-1, router.getRoute(address));
    }

    //test retrieving a rule that is not present
    @Test
    public void getNotPresentTest(){
        this.router = new IPRouter(8,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address = new IPAddress("25.128.83.41");
        assertEquals(-1, router.getRoute(address));
    }

    //test illegal port range (too low)
    @Test(expected = IllegalArgumentException.class)
    public void outOfBoundsRangeTest(){
        this.router = new IPRouter(0,4);
    }

    //ensure no exception is thrown when number of ports is at min
    @Test
    public void IfBoundsRangeTest(){
        this.router = new IPRouter(1,4);
    }

    //test illegal port range (not high enough, relative to .txt file input)
    @Test(expected = IllegalArgumentException.class)
    public void lowRangeTest(){
        this.router = new IPRouter(7,4);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
    }

    //Test regular cache functionality when it is full
    @Test
    public void fullCacheTest(){
        this.router = new IPRouter(8, 3);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        IPAddress address2 = new IPAddress("24.128.0.0");
        IPAddress address3 = new IPAddress("24.64.0.0");
        IPAddress address4 = new IPAddress("24.16.0.0");
        int res1 = this.router.getRoute(address1);
        int res2 = this.router.getRoute(address2);
        int res3 = this.router.getRoute(address3);
        assertEquals(true, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(false, router.isCached(address4));
        assertEquals(true, router.isCached(address3));
        String[] addresses = router.dumpCache();
        assertEquals(3, addresses.length);
        assertEquals(address3.toCIDR(), addresses[0]);
        assertEquals(address2.toCIDR(), addresses[1]);
        assertEquals(address1.toCIDR(), addresses[2]);
    }

    //test empty cache
    @Test
    public void emptyCacheTest(){
        this.router = new IPRouter(8, 3);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        String[] addresses = router.dumpCache();
        assertEquals(0, addresses.length);
    }

    //test cache with 1 item
    @Test
    public void oneItemCacheTest(){
        this.router = new IPRouter(8, 3);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        int res1 = this.router.getRoute(address1);
        String[] addresses = router.dumpCache();
        assertEquals(1, addresses.length);
        assertEquals(address1.toCIDR(), addresses[0]);
    }

    //test cache that was overfilled
    @Test
    public void overFullCacheTest(){
        this.router = new IPRouter(8, 3);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        IPAddress address2 = new IPAddress("24.128.0.0");
        IPAddress address3 = new IPAddress("24.30.0.0");
        IPAddress address4 = new IPAddress("85.85.0.0");
        int res1 = this.router.getRoute(address1);
        int res2 = this.router.getRoute(address2);
        int res3 = this.router.getRoute(address3);
        int res4 = this.router.getRoute(address4);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        assertEquals(true, router.isCached(address4));
        String[] addresses = router.dumpCache();
        assertEquals(3, addresses.length);
        assertEquals(address4.toCIDR(), addresses[0]);
        assertEquals(address3.toCIDR(), addresses[1]);
        assertEquals(address2.toCIDR(), addresses[2]);
    }

    //test cache functionality when an address already in cache is retrieved for a second time
    @Test
    public void repeatCacheTest(){
        this.router = new IPRouter(8, 3);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        IPAddress address2 = new IPAddress("24.128.0.0");
        IPAddress address3 = new IPAddress("24.30.0.0");
        //test for case where most recent address is accessed
        int res1 = this.router.getRoute(address1);
        int res2 = this.router.getRoute(address2);
        int res3 = this.router.getRoute(address3);
        int res3b = this.router.getRoute(address3);
        assertEquals(true, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addresses = router.dumpCache();
        assertEquals(3, addresses.length);
        assertEquals(address3.toCIDR(), addresses[0]);
        assertEquals(address2.toCIDR(), addresses[1]);
        assertEquals(address1.toCIDR(), addresses[2]);
        //test for case where middle address is accessed
        int res2b = this.router.getRoute(address2);
        assertEquals(true, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addressesb = router.dumpCache();
        assertEquals(3, addressesb.length);
        assertEquals(address2.toCIDR(), addressesb[0]);
        assertEquals(address3.toCIDR(), addressesb[1]);
        assertEquals(address1.toCIDR(), addressesb[2]);
        //test for case where least recent address is accessed
        int res1b = this.router.getRoute(address1);
        assertEquals(true, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addressesc = router.dumpCache();
        assertEquals(3, addressesc.length);
        assertEquals(address1.toCIDR(), addressesc[0]);
        assertEquals(address2.toCIDR(), addressesc[1]);
        assertEquals(address3.toCIDR(), addressesc[2]);
    }

    //test cache functionality when cache size is 0
    @Test(expected = IllegalArgumentException.class)
    public void cacheTooSmallTest(){
        this.router = new IPRouter(8, 0);
    }

    //test cache functionality when cache size is 1
    @Test
    public void cacheSizeOneTest(){
        this.router = new IPRouter(8, 1);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        IPAddress address2 = new IPAddress("24.128.0.0");
        IPAddress address3 = new IPAddress("24.30.0.0");
        assertEquals(false, router.isCached(address1));
        assertEquals(false, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        String[] addresses = router.dumpCache();
        assertEquals(0, addresses.length);
        int res1 = this.router.getRoute(address1);
        assertEquals(true, router.isCached(address1));
        assertEquals(false, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        String[] addressesb = router.dumpCache();
        assertEquals(1, addressesb.length);
        assertEquals(address1.toCIDR(), addressesb[0]);
        int res2 = this.router.getRoute(address2);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        String[] addressesc = router.dumpCache();
        assertEquals(1, addressesc.length);
        assertEquals(address2.toCIDR(), addressesc[0]);
        int res2b = this.router.getRoute(address2);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        String[] addressesd = router.dumpCache();
        assertEquals(1, addressesd.length);
        assertEquals(address2.toCIDR(), addressesd[0]);
    }

    //test cache functionality when cache size is 2
    @Test
    public void cacheSizeTwoTest(){
        this.router = new IPRouter(8, 2);
        try {
            router.loadRoutes(System.getProperty("user.dir") + File.separator + "routes2.txt");
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Bad routes file name. Tests aborted");
        }
        IPAddress address1 = new IPAddress("24.0.0.0");
        IPAddress address2 = new IPAddress("24.128.0.0");
        IPAddress address3 = new IPAddress("24.30.0.0");
        assertEquals(false, router.isCached(address1));
        assertEquals(false, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        int res1 = this.router.getRoute(address1);
        int res2 = this.router.getRoute(address2);
        assertEquals(true, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(false, router.isCached(address3));
        String[] addresses = router.dumpCache();
        assertEquals(2, addresses.length);
        assertEquals(address2.toCIDR(), addresses[0]);
        assertEquals(address1.toCIDR(), addresses[1]);
        int res3 = this.router.getRoute(address3);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addressesb = router.dumpCache();
        assertEquals(2, addressesb.length);
        assertEquals(address3.toCIDR(), addressesb[0]);
        assertEquals(address2.toCIDR(), addressesb[1]);
        int res3b = this.router.getRoute(address3);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addressesc = router.dumpCache();
        assertEquals(2, addressesc.length);
        assertEquals(address3.toCIDR(), addressesc[0]);
        assertEquals(address2.toCIDR(), addressesc[1]);
        int res2b = this.router.getRoute(address2);
        assertEquals(false, router.isCached(address1));
        assertEquals(true, router.isCached(address2));
        assertEquals(true, router.isCached(address3));
        String[] addressesd = router.dumpCache();
        assertEquals(2, addressesd.length);
        assertEquals(address2.toCIDR(), addressesd[0]);
        assertEquals(address3.toCIDR(), addressesd[1]);
    }


}
