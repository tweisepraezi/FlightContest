<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
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
