<g:applyLayout name="map">
	<html>
	    <head>
            <g:set var="titletext" value="${message(code:'fc.onlinemap')}"/>
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
	        <g:form method="post" params="${['filename':fileName,'testid':testID,'gpxviewerReturnAction':gpxviewerReturnAction,'gpxviewerReturnController':gpxviewerReturnController,'gpxviewerReturnID':gpxviewerReturnID]}">
	            <g:set var="ti" value="${[]+1}"/>
	            <g:gpxViewerForm showCancel="${showCancel}" showProfiles="${showProfiles}" gpxFileName="${createLinkTo(dir:'',file:fileName)}" infoText="${originalFilename}" gpxViewerSrc="${createLinkTo(dir:'GM_Utils',file:'')}" showLanguage="${showLanguage}" gpxShowPoints="${gpxShowPoints}" gmApiKey="${gmApiKey}" ti="${ti}"/>
		        <script type="text/javascript" src="${createLinkTo(dir:'',file:'GM_Utils/GPX2GM.js')}"></script>
		        <script>
		            function removeGpxFile() {
		                xmlhttp=new XMLHttpRequest();
		                xmlhttp.open("GET","${createLinkTo(dir:'gpx/deletegpxfile',file:'')}?filename=${fileName}",false);
		                xmlhttp.send();
		            }
		        </script>
	        </g:form>
	        <g:gpxViewerFooter />
	    </body>
	</html>
</g:applyLayout>