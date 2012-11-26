/*
 * Copyright (C) 2005-2011 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
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
 */
package org.alfresco.module.org_alfresco_module_rm.script.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.alfresco.module.org_alfresco_module_rm.RecordsManagementService;
import org.alfresco.module.org_alfresco_module_rm.capability.Capability;
import org.alfresco.module.org_alfresco_module_rm.capability.CapabilityService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.security.AccessStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

public class CapabilitiesGet extends DeclarativeWebScript
{
   private RecordsManagementService recordsManagementService;

   private CapabilityService capabilityService;

   public void setRecordsManagementService(RecordsManagementService recordsManagementService)
   {
      this.recordsManagementService = recordsManagementService;
   }

   public void setCapabilityService(CapabilityService capabilityService)
   {
      this.capabilityService = capabilityService;
   }

   /**
    * @see org.alfresco.repo.web.scripts.content.StreamContent#executeImpl(org.springframework.extensions.webscripts.WebScriptRequest, org.springframework.extensions.webscripts.Status, org.springframework.extensions.webscripts.Cache)
    */
   @Override
   protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache)
   {
      Map<String, String> templateVars = req.getServiceMatch().getTemplateVars();
      String storeType = templateVars.get("store_type");
      String storeId = templateVars.get("store_id");
      String nodeId = templateVars.get("id");

      NodeRef nodeRef = null;
      if (StringUtils.isNotBlank(storeType) && StringUtils.isNotBlank(storeId) && StringUtils.isNotBlank(nodeId))
      {
         nodeRef = new NodeRef(new StoreRef(storeType, storeId), nodeId);
      }
      else
      {
         // we are talking about the file plan node
         // TODO we are making the assumption there is only one file plan here!
         List<NodeRef> filePlans = recordsManagementService.getFilePlans();
         if (filePlans.isEmpty() == true)
         {
            throw new WebScriptException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No file plan node has been found.");
         }
         else if (filePlans.size() != 1)
         {
            throw new WebScriptException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "More than one file plan has been found.");
         }
         nodeRef = filePlans.get(0);
      }

      boolean grouped = false;
      String groupedString = req.getParameter("grouped");
      if (StringUtils.isNotBlank(groupedString))
      {
         grouped = Boolean.parseBoolean(groupedString);
      }

      Map<String, Object> model = new HashMap<String, Object>(1);
      if (grouped == true)
      {
         // Construct the map which is needed to build the model
         Map<String, GroupedCapabilities> groupedCapabilitiesMap = new HashMap<String, GroupedCapabilities>(13);

         Set<Capability> capabilities = capabilityService.getCapabilities();
         for (Capability capability : capabilities)
         {
            String capabilityGroupTitle = capability.getGroupTitle();
            if (StringUtils.isNotBlank(capabilityGroupTitle))
            {
               String capabilityGroupId = capability.getGroupId();
               String capabilityName = capability.getName();
               String capabilityTitle = capability.getTitle();

               if (groupedCapabilitiesMap.containsKey(capabilityGroupId))
               {
                  groupedCapabilitiesMap.get(capabilityGroupId).addCapability(capabilityName, capabilityTitle);
               }
               else
               {
                  GroupedCapabilities groupedCapabilities = new GroupedCapabilities(capabilityGroupId, capabilityGroupTitle, capabilityName, capabilityTitle);
                  groupedCapabilities.addCapability(capabilityName, capabilityTitle);
                  groupedCapabilitiesMap.put(capabilityGroupId, groupedCapabilities);
               }
            }
         }

         model.put("groupedCapabilities", groupedCapabilitiesMap);
      }
      else
      {
         boolean includePrivate = false;
         String includePrivateString = req.getParameter("includeAll");
         if (StringUtils.isNotBlank(includePrivateString))
         {
            includePrivate = Boolean.parseBoolean(includePrivateString);
         }

         Map<Capability, AccessStatus> map = capabilityService.getCapabilitiesAccessState(nodeRef, includePrivate);
         List<String> list = new ArrayList<String>(map.size());
         for (Map.Entry<Capability, AccessStatus> entry : map.entrySet())
         {
            AccessStatus accessStatus = entry.getValue();
            if (AccessStatus.DENIED.equals(accessStatus) == false)
            {
               Capability capability = entry.getKey();
               list.add(capability.getName());
            }
         }
         model.put("capabilities", list);
      }

      return model;
   }

   /**
    * Class to represent grouped capabilities for use in a Freemarker template
    *
    */
   public class GroupedCapabilities
   {
      private String capabilityGroupId;
      private String capabilityGroupTitle;
      private String capabilityName;
      private String capabilityTitle;
      private Map<String, String> capabilities;

      public GroupedCapabilities(String capabilityGroupId, String capabilityGroupTitle, String capabilityName, String capabilityTitle)
      {
         this.capabilityGroupId = capabilityGroupId;
         this.capabilityGroupTitle = capabilityGroupTitle;
         this.capabilityName = capabilityName;
         this.capabilityTitle = capabilityTitle;
         this.capabilities = new HashMap<String, String>(5);
      }

      public String getGroupId()
      {
         return this.capabilityGroupId;
      }

      public String getGroupTitle()
      {
         return this.capabilityGroupTitle;
      }

      public String getCapabilityName()
      {
         return this.capabilityName;
      }

      public String getCapabilityTitle()
      {
         return this.capabilityTitle;
      }

      public Map<String, String> getCapabilities()
      {
         return this.capabilities;
      }

      public void addCapability(String capabilityName, String capabilityTitle)
      {
         this.capabilities.put(capabilityName, capabilityTitle);
      }
   }
}