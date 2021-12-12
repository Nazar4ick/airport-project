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
import ua.com.fielden.platform.keygen.KeyNumber;
import ua.com.fielden.platform.test.ioc.UniversalConstantsForTesting;
import ua.com.fielden.platform.types.Money;
import ua.com.fielden.platform.utils.IUniversalConstants;


/**
 * Testing of the basic asset
 * 
 * @author Helsinki Team
 *
 */
public class AssetTest extends AbstractDaoTestCase {
    
    private final DateTime now = dateTime("2019-10-01 11:30:00");
    
    @Before
    public void setUp() {
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(now);
    }

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
    
    @Test
    public void asset_fin_details_created_with_every_new_asset() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset asset = co.new_();
        assertEquals(AssetCo.DEFAULT_KEY_VALUE, asset.getNumber());
        asset.setAssetType(type);
        asset.setDesc("some desc");
        
        final Asset savedAsset = co.save(asset);
        
        final AssetFinDet assetFinDet = co(AssetFinDet.class).findByKey(savedAsset);
        assertNotNull(assetFinDet);
    }
    
    @Test
    public void asset_fin_details_are_not_created_again_for_existing_asset() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset asset = co.new_();
        assertEquals(AssetCo.DEFAULT_KEY_VALUE, asset.getNumber());
        asset.setAssetType(type);
        asset.setDesc("some desc");
        final Asset savedAsset = co.save(asset);
        
        final var co$AssetFinDet = co$(AssetFinDet.class);
        final AssetFinDet assetFinDet = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        assertNotNull(assetFinDet);
        assetFinDet.setInitCost(Money.of("100.00"));
        assetFinDet.setCommissionDate(date("2021-11-06 10:00:00"));
        final var savedAssetFinDet = co$AssetFinDet.save(assetFinDet);
        assertEquals(Money.of("100.00"), savedAssetFinDet.getInitCost());
        
        final Asset savedAgain = co.save(savedAsset.setDesc("some other desc"));
        final AssetFinDet assetFinDetAfterAssetWasChanged = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        assertEquals(Money.of("100.00"), assetFinDetAfterAssetWasChanged.getInitCost());
    }
    
    @Test
    public void dependent_date_props_resolve_invalid_values_by_means_of_dependency() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset asset = co.new_();
        assertEquals(AssetCo.DEFAULT_KEY_VALUE, asset.getNumber());
        asset.setAssetType(type);
        asset.setDesc("some desc");
        final Asset savedAsset = co.save(asset);
        
        final var co$AssetFinDet = co$(AssetFinDet.class);
        final AssetFinDet assetFinDet = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        assetFinDet.setCommissionDate(date("2021-12-06 10:00:00"));
        final MetaProperty<Date> mpCommissionDate = assetFinDet.getProperty("commissionDate");
        assertTrue(mpCommissionDate.isValid());
        
        assertNull(assetFinDet.getDisposalDate());
        assetFinDet.setDisposalDate(date("2021-12-06 08:00:00"));
        final MetaProperty<Date> mpDisposalDate = assetFinDet.getProperty("disposalDate");
        assertFalse(mpDisposalDate.isValid());
        assertNull(assetFinDet.getDisposalDate());
        
        assetFinDet.setCommissionDate(date("2021-12-06 06:00:00"));
        assertTrue(mpCommissionDate.isValid());
        assertTrue(mpDisposalDate.isValid());
        
        assertEquals(date("2021-12-06 06:00:00"), assetFinDet.getCommissionDate());
        assertEquals(date("2021-12-06 08:00:00"), assetFinDet.getDisposalDate());
    }
    
    @Test
    public void commissionDate_is_required_where_initCost_is_entered() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset savedAsset = co.save(co.new_().setAssetType(type).setDesc("some desc"));
        
        final var co$AssetFinDet = co$(AssetFinDet.class);
        final AssetFinDet assetFinDet = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        
        final MetaProperty<Date> mpCommissionDate = assetFinDet.getProperty("commissionDate");
        final MetaProperty<Money> mpInitCost = assetFinDet.getProperty("initCost");
        
        assertFalse(mpCommissionDate.isRequired());
        assertFalse(mpInitCost.isRequired());
        
        assetFinDet.setInitCost(Money.of("100.00"));
        
        assertTrue(mpCommissionDate.isRequired());
        
        final Date commissionDate = date("2021-12-06 10:00:00");
        assetFinDet.setCommissionDate(commissionDate);
        assertTrue(mpCommissionDate.isRequired());
        assertEquals(commissionDate, assetFinDet.getCommissionDate());
    }
    
    @Test
    public void commissionDate_is_assigned_now_upon_initCost_change_is_empty() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset savedAsset = co.save(co.new_().setAssetType(type).setDesc("some desc"));
        
        final var co$AssetFinDet = co$(AssetFinDet.class);
        final AssetFinDet assetFinDet = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        
        final MetaProperty<Date> mpCommissionDate = assetFinDet.getProperty("commissionDate");
        final MetaProperty<Money> mpInitCost = assetFinDet.getProperty("initCost");
        
        assertFalse(mpCommissionDate.isRequired());
        assertFalse(mpInitCost.isRequired());
        assertNull(assetFinDet.getCommissionDate());
        
        assetFinDet.setInitCost(Money.of("100.00"));
        
        assertTrue(mpCommissionDate.isRequired());
        assertEquals(now.toDate(), assetFinDet.getCommissionDate());
        
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(now.plusMinutes(1));
        
        assetFinDet.setInitCost(Money.of("101.00"));
        assertEquals(now.toDate(), assetFinDet.getCommissionDate());
    }
    
    @Test
    public void commissionDate_does_not_change_upon_retrieval() {
        final AssetType type = co(AssetType.class).findByKeyAndFetch(AssetCo.FETCH_PROVIDER.<AssetType>fetchFor("assetType").fetchModel(), "AT1");
        final AssetCo co = co(Asset.class);
        final Asset savedAsset = co.save(co.new_().setAssetType(type).setDesc("some desc"));
        
        final var co$AssetFinDet = co$(AssetFinDet.class);
        final AssetFinDet assetFinDet = save(co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset).setInitCost(Money.of("100.00")));
        assertEquals(now.toDate(), assetFinDet.getCommissionDate());
        
        final UniversalConstantsForTesting constants = (UniversalConstantsForTesting) getInstance(IUniversalConstants.class);
        constants.setNow(now.plusMinutes(1));
        
        final AssetFinDet assetFinDet2 = co$AssetFinDet.findByKeyAndFetch(AssetFinDetCo.FETCH_PROVIDER.fetchModel(), savedAsset);
        assertEquals(assetFinDet.getCommissionDate(), assetFinDet2.getCommissionDate());
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
    }

}
