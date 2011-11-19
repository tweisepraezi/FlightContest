enum SecretCoordRouteIdentification 
{
	NONE       ('fc.aflos.routedefs.secretpoint.identification.none',      0),
	GATEWIDTH2 ('fc.aflos.routedefs.secretpoint.identification.gatewidth', 2)
	
	SecretCoordRouteIdentification(String titleCode, int gateWidth)
	{
		this.titleCode = titleCode
		this.gateWidth = gateWidth
	}
	
	final String titleCode
	final int gateWidth
	
}