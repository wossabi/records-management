 
package org.alfresco.module.org_alfresco_module_rm.jscript.app.evaluator;

/*
 * #%L
 * This file is part of Alfresco.
 * %%
 * Copyright (C) 2005 - 2016 Alfresco Software Limited
 * %%
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import org.alfresco.module.org_alfresco_module_rm.jscript.app.BaseEvaluator;
import org.alfresco.module.org_alfresco_module_rm.vital.VitalRecordService;
import org.alfresco.service.cmr.repository.NodeRef;

/**
 * @author Roy Wetherall
 */
public class VitalRecordEvaluator extends BaseEvaluator
{
    private VitalRecordService vitalRecordService;
    
    public void setVitalRecordService(VitalRecordService vitalRecordService)
    {
        this.vitalRecordService = vitalRecordService;
    }
    
    @Override
    protected boolean evaluateImpl(NodeRef nodeRef)
    {
        return vitalRecordService.isVitalRecord(nodeRef);
    }
}