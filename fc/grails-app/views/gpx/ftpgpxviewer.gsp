<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <script>
            <g:set var="tiles_server" value="${BootStrap.global.GetMapTilesServer()}"/>
            <g:if test="${tiles_server}">
                FCTilesLink = '${tiles_server}';
            </g:if>
            <g:else>
                FCTilesLink = '';
            </g:else>
            <g:set var="tiles_tms" value="${BootStrap.global.GetMapTilesTMS()}"/>
            <g:if test="${tiles_tms}">
                FCTilesTMS = true;
            </g:if>
            <g:else>
                FCTilesTMS = false;
            </g:else>
        </script>
        <g:gpxViewerHead/>
    </head>
	<body>
	    <g:form method="post">
            <g:set var="ti" value="${[]+1}"/>
            <g:gpxViewerForm showCancel="no" showProfiles="${showProfiles}" gpxFileName="${fileName}" infoText="${originalFilename}" gpxViewerSrc="../../${Defs.GPX_VIEWER_VERSION}" showLanguage="${printLanguage}" gpxShowPoints="${gpxShowPoints}" gmApiKey="${gmApiKey}" ti="${ti}"/>
		    <script type="text/javascript" src="../../${Defs.GPX_VIEWER_VERSION}/GPX2GM.js"></script>
	    </g:form>
        <g:gpxViewerFooter />
	</body>
</html>
