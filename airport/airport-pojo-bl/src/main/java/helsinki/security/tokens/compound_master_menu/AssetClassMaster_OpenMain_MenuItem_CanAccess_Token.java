package helsinki.security.tokens.compound_master_menu;

import static java.lang.String.format;

import ua.com.fielden.platform.reflection.TitlesDescsGetter;
import ua.com.fielden.platform.security.tokens.Template;
import helsinki.assets.master.menu.actions.AssetClassMaster_OpenMain_MenuItem;
import helsinki.security.tokens.UsersAndPersonnelModuleToken;

/**
 * A security token for entity {@link AssetClassMaster_OpenMain_MenuItem} to guard Access.
 *
 * @author Developers
 *
 */
public class AssetClassMaster_OpenMain_MenuItem_CanAccess_Token extends UsersAndPersonnelModuleToken {
    public final static String TITLE = format(Template.MASTER_MENU_ITEM_ACCESS.forTitle(), AssetClassMaster_OpenMain_MenuItem.ENTITY_TITLE);
    public final static String DESC = format(Template.MASTER_MENU_ITEM_ACCESS.forDesc(), AssetClassMaster_OpenMain_MenuItem.ENTITY_TITLE);
}