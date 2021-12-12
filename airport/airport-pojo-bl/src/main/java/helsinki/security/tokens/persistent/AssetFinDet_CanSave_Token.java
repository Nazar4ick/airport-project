package helsinki.security.tokens.persistent;

import static java.lang.String.format;

import helsinki.assets.AssetFinDet;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;
import ua.com.fielden.platform.security.tokens.Template;

/**
 * A security token for entity {@link AssetFinDet} to guard Save.
 *
 * @author Developers
 *
 */
public class AssetFinDet_CanSave_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.SAVE.forTitle(), AssetFinDet.ENTITY_TITLE);
    public final static String DESC = format(Template.SAVE.forDesc(), AssetFinDet.ENTITY_TITLE);
}