package helsinki.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import helsinki.test_config.AbstractDaoTestCase;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.error.Result;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.test.ioc.UniversalConstantsForTesting;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.IUniversalConstants;


/**
 * Testing of the {@link AssetOwnership}
 * 
 * @author Helsinki Team
 *
 */
public class AssetOwnershipTest extends AbstractDaoTestCase {
    
    private final DateTime now = dateTime("2019-10-01 11:30:00");
    
    @Before
    public void setUp() {
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(now);
    }

    @Test
    public void if_no_owners_are_specified_then_all_are_required() {
        final Asset asset = co(Asset.class).findByKeyAndFetch(AssetOwnershipCo.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000000001");
        assertNotNull(asset);
        
        final AssetOwnershipCo co = co(AssetOwnership.class);
        final AssetOwnership ownership = co.new_();
        ownership.setAsset(asset).setStartDate(date("2021-12-10 00:00:00"));
        
        assertTrue(ownership.getProperty("role").isRequired());
        assertTrue(ownership.getProperty("organisation").isRequired());
        assertTrue(ownership.getProperty("bu").isRequired());
        final Result res = ownership.isValid();
        assertFalse(res.isSuccessful());
        assertEquals(AssetOwnershipCo.ERR_REQUIRED, res.getMessage());
    }
    
    @Test
    public void if_one_of_meps_is_populated_the_other_two_are_cleared_and_not_required() {
        final Asset asset = co(Asset.class).findByKeyAndFetch(AssetOwnershipCo.FETCH_PROVIDER.<Asset>fetchFor("asset").fetchModel(), "000000001");
        assertNotNull(asset);
        
        final AssetOwnershipCo co = co(AssetOwnership.class);
        final AssetOwnership ownership = co.new_();
        ownership.setAsset(asset).setStartDate(date("2021-12-10 00:00:00"));
        
        final var mpRole = ownership.getProperty("role");
        final var mpOrganisation = ownership.getProperty("organisation");
        final var mpBu = ownership.getProperty("bu");
        
        ownership.setRole("some role");
        
        assertTrue(mpRole.isRequired());
        assertFalse(mpOrganisation.isRequired());
        assertFalse(mpBu.isRequired());
        
        assertEquals("some role", ownership.getRole());
        assertNull(ownership.getOrganisation());
        assertNull(ownership.getBu());
        
        ownership.setOrganisation("some org");
        
        assertFalse(mpRole.isRequired());
        assertTrue(mpOrganisation.isRequired());
        assertFalse(mpBu.isRequired());
        
        assertNull(ownership.getRole());
        assertEquals("some org", ownership.getOrganisation());
        assertNull(ownership.getBu());
        
        ownership.setBu("some bu");
        
        assertFalse(mpRole.isRequired());
        assertFalse(mpOrganisation.isRequired());
        assertTrue(mpBu.isRequired());
        
        assertNull(ownership.getRole());
        assertNull(ownership.getOrganisation());
        assertEquals("some bu", ownership.getBu());
        
        ownership.setBu(null);
        assertFalse(mpBu.isValid());
        assertEquals(AssetOwnershipCo.ERR_REQUIRED, mpBu.getFirstFailure().getMessage());
    }
    
    @Override
    public boolean saveDataPopulationScriptToFile() {
        return false;
    }

    @Override
    public boolean useSavedDataPopulationScript() {
        return false;
    }

    @Override
    protected void populateDomain() {
        // Need to invoke super to create a test user that is responsible for data population 
        super.populateDomain();

        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(now);

        // If the use of saved data population script is indicated then there is no need to proceed with any further data population logic.
        if (useSavedDataPopulationScript()) {
            return;
        }
        
        save(new_(KeyNumber.class, AssetCo.ASSET_KEY_NAME).setValue("0"));
        
        final var ac1 = save(new_(AssetClass.class).setName("AC1").setDesc("Desc for AC1"));
        final var ac2 = save(new_(AssetClass.class).setName("AC2").setDesc("Desc for AC2"));
        
        final var at1 = save(new_(AssetType.class).setName("AT1").setDesc("Desc for AT1").setAssetClass(ac1));
        final var at2 = save(new_(AssetType.class).setName("AT2").setDesc("Desc for AT2").setAssetClass(ac2));
        final var at3 = save(new_(AssetType.class).setName("AT3").setDesc("Desc for AT3").setAssetClass(ac2));

        save(new_(Asset.class).setAssetType(at1).setDesc("test asset"));
    }

}
