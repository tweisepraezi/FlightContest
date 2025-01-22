<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <meta name="layout" content="main" />
        <title>${message(code:'fc.map.list')}</title>
    </head>
    <body>
        <g:mainnav link="${createLink(controller:'contest')}" controller="map" importaction="${message(code:'fc.map.import')}" downloadallzipaction="${message(code:'fc.map.download.all.zip')}"/>
        <div class="box">
            <g:viewmsg msg="${flash.message}" error="${flash.error}"/>
            <g:set var="add_col" value="${0}"/>
            <g:if test="${BootStrap.global.IsTaskCreatorExtern()}">
                <g:set var="add_col" value="${1}"/>
            </g:if>
            <table>
                <thead>
                    <tr>
                        <th colspan="${8+add_col}" class="table-head">${message(code:'fc.map.list')}</th>
                        <th class="table-head"><a href="/fc/docs/help_${session.showLanguage}.html#osm-contest-map-maps" target="_blank"><img src="${createLinkTo(dir:'images',file:'help.png')}"/></a></th>
                    </tr>
                    <tr>
                        <th>${message(code:'fc.title')}</th>
                        <th colspan="${1+add_col}">${message(code:'fc.map.taskcreator')}</th>
                        <th>${message(code:'fc.map.download.pdf')}</th>
                        <th>${message(code:'fc.map.download.png')}</th>
                        <th>${message(code:'fc.map.download.zip')}</th>
                        <th>${message(code:'fc.map.rename.short')}</th>
                        <th>${message(code:'fc.map.delete')}</th>
                        <th>${message(code:'fc.map.routes')}</th>
                        <th/>
                    </tr>
                    <g:if test="${add_col}">
                        <th/>
                        <th>${message(code:'fc.map.taskcreator.intern')}</th>
                        <th>${message(code:'fc.map.taskcreator.extern')}</th>
                        <th/>
                        <th/>
                        <th/>
                        <th/>
                        <th/>
                        <th/>
                        <th/>
                    </g:if>
                </thead>
                <tbody>
                    <g:each var="map_entry" in="${mapList}" status="i">
                        <tr class="${(i % 2) == 0 ? 'odd' : ''}">
                            <td>${map_entry.title}</td>
                            <g:if test="${map_entry.projection == "4326"}">
                                <td><a href="${createLink(controller:'map',action:'start_taskcreator_intern',params:[localref:map_entry.localref,top:map_entry.top,bottom:map_entry.bottom,right:map_entry.right,left:map_entry.left])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                                <g:if test="${add_col}">
                                    <td><a href="${createLink(controller:'map',action:'start_taskcreator_extern',params:[localref:map_entry.localref,top:map_entry.top,bottom:map_entry.bottom,right:map_entry.right,left:map_entry.left])}" target="_blank">${message(code:'fc.map.here')}</a></td>
                                </g:if>
                            </g:if>
                            <g:else>
                                <td/>
                                <g:if test="${add_col}">
                                    <td/>
                                </g:if>
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
                            <td>
                                <g:each var="route_instance" in="${Route.findAllByContest(contestInstance)}">
                                    <g:if test="${map_entry.title == route_instance.defaultOnlineMap || map_entry.title == route_instance.defaultPrintMap}">
                                        <g:route var="${route_instance}" link="${createLink(controller:'route',action:'show')}" />
                                        <g:if test="${map_entry.title == route_instance.defaultOnlineMap && map_entry.title == route_instance.defaultPrintMap}">
                                            (${message(code:'fc.route.onlinemap.default')}, ${message(code:'fc.route.printmap.default')})
                                        </g:if>    
                                        <g:elseif test="${map_entry.title == route_instance.defaultOnlineMap}">
                                            (${message(code:'fc.route.onlinemap.default')})
                                        </g:elseif>
                                        <g:elseif test="${map_entry.title == route_instance.defaultPrintMap}">
                                            (${message(code:'fc.route.printmap.default')})
                                        </g:elseif>
                                        <br/>
                                    </g:if>
                                </g:each>
                            </td>
                            <td/>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <p>${message(code:'fc.program.foot',args:[createLinkTo(dir:'',file:'licenses/GPL_license.txt'),createLinkTo(dir:'',file:'licenses/README.txt')])}</p>
        </div>
    </body>
</html>