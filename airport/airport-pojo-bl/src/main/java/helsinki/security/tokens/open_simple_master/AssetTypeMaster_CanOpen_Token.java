package helsinki.security.tokens.open_simple_master;

import static java.lang.String.format;

import helsinki.assets.AssetType;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;
import ua.com.fielden.platform.security.tokens.Template;

/**
 * A security token for entity {@link AssetType} to guard Open.
 *
 * @author Developers
 *
 */
public class AssetTypeMaster_CanOpen_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.MASTER_OPEN.forTitle(), AssetType.ENTITY_TITLE + " Master");
    public final static String DESC = format(Template.MASTER_OPEN.forDesc(), AssetType.ENTITY_TITLE);
}