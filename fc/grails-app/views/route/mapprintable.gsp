<html>
    <head>
        <style type="text/css">
            @page {
                <g:if test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_A4}">
                    <g:if test="${params.landscape=='true'}">
                        size: A4 landscape;
                    </g:if>
                    <g:else>
                        size: A4;
                    </g:else>
                </g:if>
                <g:elseif test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_A3}">
                    <g:if test="${params.landscape=='true'}">
                        size: A3 landscape;
                    </g:if>
                    <g:else>
                        size: A3;
                    </g:else>
                </g:elseif>
                <g:elseif test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_A2}">
                    <g:if test="${params.landscape=='true'}">
                        size: 594mm 420mm;
                    </g:if>
                    <g:else>
                        size: 420mm 594mm;
                    </g:else>
                </g:elseif>
                <g:elseif test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_A1}">
                    <g:if test="${params.landscape=='true'}">
                        size: 841mm 594mm;
                    </g:if>
                    <g:else>
                        size: 594mm 841mm;
                    </g:else>
                </g:elseif>
                <g:elseif test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_ANR}">
                    size: 140mm 198mm;
                </g:elseif>
                <g:elseif test="${params.printSize==Defs.CONTESTMAPPRINTSIZE_AIRPORTAREA}">
                    size: 840mm 840mm;
                </g:elseif>
                margin-top: 8mm;
                margin-left: 8mm;
                margin-right: 8mm;
                margin-bottom: auto;
            }
        </style>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="contestmap" />
    </head>
    <body>
        <img src="${mapFileName}" style="width:100%; height:100%;" />
    </body>
</html>