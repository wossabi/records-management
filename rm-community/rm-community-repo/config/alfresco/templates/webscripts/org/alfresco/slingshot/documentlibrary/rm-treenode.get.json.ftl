<#--
 #%L
 This file is part of Alfresco.
 %%
 Copyright (C) 2005 - 2016 Alfresco Software Limited
 %%
 Alfresco is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 Alfresco is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.
  
 You should have received a copy of the GNU Lesser General Public License
 along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 #L%
-->
<#assign p = treenode.parent>
<#escape x as jsonUtils.encodeJSONString(x)>
{
   "totalResults": ${treenode.items?size?c},
   "resultsTrimmed": ${treenode.resultsTrimmed?string},
   "parent":
   {
      "nodeRef": "${p.nodeRef}",
      "userAccess":
      {
         "create": ${p.hasPermission("CreateChildren")?string},
         "edit": ${p.hasPermission("Write")?string},
         "delete": ${p.hasPermission("Delete")?string}
      }
   },
   "items":
   [
   <#list treenode.items as item>
      <#assign t = item.node>
      {
      <#if item.permissions??>
         "userAccess":
         {
         <#list item.permissions?keys as perm>
            <#if item.permissions[perm]?is_boolean>
            "${perm?string}": ${item.permissions[perm]?string}<#if perm_has_next>,</#if>
            </#if>
         </#list>
         },
      </#if>
         "nodeRef": "${t.nodeRef}",
         "name": "${t.name}",
         "description": "${(t.properties.description!"")}",
         "hasChildren": ${item.hasSubfolders?string}
      }<#if item_has_next>,</#if>
   </#list>
   ]
}
</#escape>