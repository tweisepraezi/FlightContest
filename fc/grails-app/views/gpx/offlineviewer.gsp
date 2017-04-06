<g:applyLayout name="map">
	<html>
	    <head>
           <g:set var="titletext" value="${message(code:'fc.offlinemap')}"/>
	       <g:offlineViewerHead/>
	    </head>
	    <body>
	        <g:form method="post" params="${['filename':fileName,'testid':testID,'gpxviewerReturnAction':gpxviewerReturnAction,'gpxviewerReturnController':gpxviewerReturnController,'gpxviewerReturnID':gpxviewerReturnID]}">
	            <g:set var="ti" value="${[]+1}"/>
	            <g:offlineViewerForm showCancel="${showCancel}" showProfiles="${showProfiles}" showZoom="${showZoom}" showPoints="${showPoints}" gpxFileName="${fileName}" infoText="${originalFilename}" gpxViewerSrc="/fc/GM_Utils" showLanguage="${showLanguage}" gpxShowPoints="${gpxShowPoints}" ti="${ti}"/>
		        <script type="text/javascript" src="/fc/GM_Utils/GPX2GM.js"></script>
		        <script>
		            function removeGpxFile() {
		                xmlhttp=new XMLHttpRequest();
		                xmlhttp.open("GET","/fc/gpx/deletegpxfile?filename=${fileName}",false);
		                xmlhttp.send();
		            }
		        </script>
	        </g:form>
	        <g:offlineViewerFooter />
	    </body>
	</html>
</g:applyLayout>