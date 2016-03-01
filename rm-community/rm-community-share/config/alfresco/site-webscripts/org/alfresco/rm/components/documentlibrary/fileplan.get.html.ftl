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
<#assign filterIds = "">
<div class="filter fileplan-filter">
   <h2>${msg("header.fileplan")}</h2>
   <ul class="filterLink">
      <li><span class="transfers"><a rel="" href="#">${msg("label.transfers")}</a></span></li>
      <li><span class="holds"><a rel="" href="#">${msg("label.holds")}</a></span></li>
      <li><span class="unfiledRecords"><a rel="" href="#">${msg("label.unfiledRecords")}</a></span></li>
   </ul>
</div>
<script type="text/javascript">//<![CDATA[
   new Alfresco.component.BaseFilter("Alfresco.DocListFilePlan", "${args.htmlid?html}").setFilterIds(["transfers", "holds", "unfiledRecords"]);
//]]></script>