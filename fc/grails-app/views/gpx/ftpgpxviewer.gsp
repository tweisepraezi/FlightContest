<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <g:gpxViewerHead/>
    </head>
	<body>
	    <g:form method="post">
            <g:gpxViewerForm showCancel="no" showProfiles="${showProfiles}" gpxFileName="${fileName}" infoText="${originalFilename}" showLanguage="${printLanguage}" gpxShowPoints="${gpxShowPoints}" />
		    <script type="text/javascript" src="../../GM_Utils/GPX2GM.js"></script>
	    </g:form>
        <g:gpxViewerFooter />
	</body>
</html>
