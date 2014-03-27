package org.alfresco.module.org_alfresco_module_rm.action.impl;


/**
 * File To action implementation.
 *
 * @author Roy Wetherall
 * @since 2.1
 */
public class FileToAction extends CopyMoveFileToBaseAction
{
    /** action name */
    public static final String NAME = "fileTo";

    @Override
    public void init()
    {
        super.init();
        this.mode = CopyMoveFileToActionMode.MOVE;
    }
}
