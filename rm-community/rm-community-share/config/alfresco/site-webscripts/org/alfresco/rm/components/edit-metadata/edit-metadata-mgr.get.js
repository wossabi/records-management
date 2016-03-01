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
<import resource="/org/alfresco/components/edit-metadata/edit-metadata-mgr.get.js">

function alfresco_rm_main()
{
   // Call for meta data again (response is cached) so we can alter the nodeType
   var nodeRef = model.nodeRef,
      nodeType = model.nodeType;
      result = remote.connect("alfresco").get("/slingshot/edit-metadata/node/" + nodeRef.replace(":/", ""));

   if (result.status == 200)
   {
      // Determine the return page's nodeType and nodeRef depending on type being edited
      var metadata = eval('(' + result + ')');
      switch (String(metadata.node.type))
      {
         case "dod:recordSeries":
            nodeType = "record-series";
            break;

         case "dod:recordCategory":
            nodeType = "record-category";
            break;

         case "rma:recordFolder":
            nodeType = "record-folder";
            break;

         case "rma:dispositionSchedule":
            nodeType = "record-category";
            nodeRef = metadata.nodeRef;
            break;
      }
   }
   model.nodeRef = nodeRef;
   model.nodeType = nodeType;
}

alfresco_rm_main();