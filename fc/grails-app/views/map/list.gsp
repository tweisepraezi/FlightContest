<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.map.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="map" importaction="${message(code:'fc.map.import')}" taskcreatoraction="${message(code:'fc.map.taskcreator')}" />
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <table>
                <thead>
                    <tr>
                        <th colspan="7" class="table-head">${message(code:'fc.map.list')}</th>
                        <th class="table-head"><a href="../docs/help_${session.showLanguage}.html#route-planning" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
                    </tr>
                    <tr>
                        <th>${message(code:'fc.title')}</th>
                        <th>${message(code:'fc.map.taskcreator')}</th>
                        <th>${message(code:'fc.map.download.pdf')}</th>
                        <th>${message(code:'fc.map.download.png')}</th>
                        <th>${message(code:'fc.map.download.zip')}</th>
                        <th>${message(code:'fc.map.rename.short')}</th>
                        <th>${message(code:'fc.map.delete')}</th>
                        <th/>
                    </tr>
                </thead>
                <tbody>
                    <g:each var="map_entry" in="${mapList}" status="i">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td>${map_entry.title}</td>
                            <g:if test="${map_entry.projection == "4326"}">
                                <td><a href="${createLink(controller:'map',action:'start_taskcreator',params:[localref:map_entry.localref,top:map_entry.top,bottom:map_entry.bottom,right:map_entry.right,left:map_entry.left])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                            </g:if>
                            <g:else>
                                <td/>
                            </g:else>
                            <g:if test="${map_entry.projection == "3857"}">
                                <td><a href="${createLink(controller:'map',action:'download_pdf',params:[name:map_entry.name,title:map_entry.title,landscape:map_entry.landscape,size:map_entry.size])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                                <td><a href="${createLink(controller:'map',action:'download_png',params:[name:map_entry.name,title:map_entry.title])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                            </g:if>
                            <g:else>
                                <td/>
                                <td/>
                            </g:else>
                            <td><a href="${createLink(controller:'map',action:'download_pngzip',params:[name:map_entry.name,title:map_entry.title])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                            <td><a href="${createLink(controller:'map',action:'rename_question',params:[name:map_entry.name,title:map_entry.title])}">${message(code:'fc.map.here')}</a></td>
                            <g:set var="areyousure" value="${message(code:'fc.map.delete.map')} - ${message(code:'fc.areyousure')}"/>
                            <td><a href="${createLink(controller:'map',action:'delete',params:[name:map_entry.name,title:map_entry.title])}" onclick="return confirm('${map_entry.title} - ${areyousure}');" >${message(code:'fc.map.here')}</a></td>
                            <td/>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>