package helsinki.security.tokens.open_compound_master;

import static java.lang.String.format;

import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.security.tokens.Template;
import helsinki.assets.ui_actions.OpenAssetClassMasterAction;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;

/**
 * A security token for entity {@link OpenAssetClassMasterAction} to guard Open.
 *
 * @author Developers
 *
 */
public class OpenAssetClassMasterAction_CanOpen_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.MASTER_OPEN.forTitle(), OpenAssetClassMasterAction.ENTITY_TITLE);
    public final static String DESC = format(Template.MASTER_OPEN.forDesc(), OpenAssetClassMasterAction.ENTITY_TITLE);
}