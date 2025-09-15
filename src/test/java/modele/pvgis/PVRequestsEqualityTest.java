package modele.pvgis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests equals/hashCode pour les requêtes PV. */
class PVRequestsEqualityTest {

    @Test
    void offgrid_request_equals_and_hashcode() {
        PVOffGridRequest r1 = new PVOffGridRequest("46","6","5","10000","50","3000",null,null,null,true,null,null,null,false);
        PVOffGridRequest r2 = new PVOffGridRequest("46","6","5","10000","50","3000",null,null,null,true,null,null,null,false);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        PVOffGridRequest r3 = new PVOffGridRequest("47","6","5","10000","50","3000",null,null,null,true,null,null,null,false);
        assertNotEquals(r1, r3);
    }

    @Test
    void pvcalc_request_equals_and_hashcode() {
        PVGridAndTrackerRequest a = new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,true,"30","180",false,false,false,false,null,false,false,null,false,
                null,null,null,null,false,null,null,false,true);
        PVGridAndTrackerRequest b = new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,true,"30","180",false,false,false,false,null,false,false,null,false,
                null,null,null,null,false,null,null,false,true);
        assertEquals(a,b);
        assertEquals(a.hashCode(), b.hashCode());
        PVGridAndTrackerRequest c = new PVGridAndTrackerRequest(
                "46","6","6","14",null,null,null,true,"30","180",false,false,false,false,null,false,false,null,false,
                null,null,null,null,false,null,null,false,true);
        assertNotEquals(a,c);
    }
}
