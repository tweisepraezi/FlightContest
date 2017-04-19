<g:applyLayout name="map">
	<html>
	    <head>
           <g:set var="titletext" value="${message(code:'fc.onlinemap')}"/>
	       <g:gpxViewerHead/>
	    </head>
	    <body>
	        <g:form method="post" params="${['filename':fileName,'testid':testID,'gpxviewerReturnAction':gpxviewerReturnAction,'gpxviewerReturnController':gpxviewerReturnController,'gpxviewerReturnID':gpxviewerReturnID]}">
	            <g:set var="ti" value="${[]+1}"/>
	            <g:gpxViewerForm showCancel="${showCancel}" showProfiles="${showProfiles}" gpxFileName="${createLinkTo(dir:'',file:fileName)}" infoText="${originalFilename}" gpxViewerSrc="${createLinkTo(dir:'GM_Utils',file:'')}" showLanguage="${showLanguage}" gpxShowPoints="${gpxShowPoints}" ti="${ti}"/>
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