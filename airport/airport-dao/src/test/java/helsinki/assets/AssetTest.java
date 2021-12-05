package helsinki.assets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import helsinki.test_config.AbstractDaoTestCase;
import ua.com.fielden.platform.entity.meta.MetaProperty;
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.test.ioc.UniversalConstantsForTesting;
import ua.com.fielden.platform.utils.IUniversalConstants;


/**
 * Testing of the basic asset
 * 
 * @author Helsinki Team
 *
 */
public class AssetTest extends AbstractDaoTestCase {

    @Test
    public void new_assets_get_their_number_generated() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset asset = co.new_();
        assertEquals(AssetCo.DEFAULT_KEY_VALUE, asset.getNumber());
        asset.setAssetType(type);
        asset.setDesc("some desc");
        
        final Asset savedAsset = co.save(asset);
        assertNotEquals(AssetCo.DEFAULT_KEY_VALUE, savedAsset.getNumber());
        assertFalse(StringUtils.isEmpty(savedAsset.getNumber()));
        assertEquals("000000001", savedAsset.getNumber());
    }
    
    @Test
    public void existing_assets_cannot_get_their_number_changed() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset asset = co.new_();
        assertEquals(AssetCo.DEFAULT_KEY_VALUE, asset.getNumber());
        asset.setAssetType(type);
        asset.setDesc("some desc");
        
        final Asset savedAsset = co.save(asset);
        
        savedAsset.setNumber("some number");
        assertEquals("000000001", savedAsset.getNumber());
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
        constants.setNow(dateTime("2019-10-01 11:30:00"));

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
    }

}
