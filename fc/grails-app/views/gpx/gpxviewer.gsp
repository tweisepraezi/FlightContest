<g:applyLayout name="map">
	<html>
	    <head>
           <g:set var="titletext" value="${message(code:'fc.onlinemap')}"/>
	       <g:gpxViewerHead/>
	    </head>
	    <body>
	        <g:form method="post" params="${['filename':fileName,'testid':testID,'gpxviewerReturnAction':gpxviewerReturnAction,'gpxviewerReturnController':gpxviewerReturnController,'gpxviewerReturnID':gpxviewerReturnID]}">
	            <g:gpxViewerForm showCancel="${showCancel}" showProfiles="${showProfiles}" gpxFileName="/fc/${fileName}" infoText="${originalFilename}" showLanguage="${showLanguage}" gpxShowPoints="${gpxShowPoints}" />
		        <script type="text/javascript" src="/fc/GM_Utils/GPX2GM.js"></script>
		        <script>
		            function removeGpxFile() {
		                xmlhttp=new XMLHttpRequest();
		                xmlhttp.open("GET","/fc/gpx/deletegpxfile?filename=${fileName}",false);
		                xmlhttp.send();
		            }
		        </script>
	        </g:form>
	        <g:gpxViewerFooter />
	    </body>
	</html>
</g:applyLayout>