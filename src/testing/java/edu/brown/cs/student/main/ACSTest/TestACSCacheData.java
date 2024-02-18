package edu.brown.cs.student.main.ACSTest;

import com.google.common.cache.Cache;
import edu.brown.cs.student.main.server.API.ACSCacheData;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/***
 * This class tests for the methods of ACSCacheData, including:
 * - populating the state cache
 * - getting the state cache, country cache, and previous requests cache
 * - setting the state cache, country cache, and previous requests cache
 * */
public class TestACSCacheData {
    public ACSCacheData acsData;
    @Before
    public void setup(){
        this.acsData = new ACSCacheData();

        // adding data into county cache
        this.acsData.addCountyCache("County1", "01");
        this.acsData.addCountyCache("County2", "02");
        this.acsData.addCountyCache("County3", "03");

        // adding data into county cache
        this.acsData.addPastRequestCache("tommy", "21");
        this.acsData.addPastRequestCache("bob", "34");
        this.acsData.addPastRequestCache("janet", "96");

    }

    /**
     * Tests the populating state cache method of ACSCacheData
     * */
    @Test
    public void testPopulatingStateCache(){
        this.setup();
        Cache<String, String> stateCache = this.acsData.getStateCache();
        //checking if correctly populated
        assertEquals(stateCache.getIfPresent("California"), "06");
        assertEquals(stateCache.getIfPresent("Puerto Rico"), "72");
    }

    /**
     * Tests the getter methods of ACSCacheData
     * */
    @Test
    public void testGetterMethods(){
        this.setup();
        assertEquals("02", this.acsData.getCountyCache().getIfPresent("County2"));
        assertEquals("96", this.acsData.getPastRequestCache().getIfPresent("janet"));
    }

    /**
     * Tests the add methods of ACSCacheData
     * */
    @Test
    public void testAddMethods(){
        this.setup();
        this.acsData.addCountyCache("County4", "04");
        assertEquals("04", this.acsData.getCountyCache().getIfPresent("County4"));
        this.acsData.addPastRequestCache("lily", "39");
        assertEquals("39", this.acsData.getPastRequestCache().getIfPresent("lily"));
    }

}
