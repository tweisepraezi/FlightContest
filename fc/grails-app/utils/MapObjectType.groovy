enum MapObjectType // DB-2.40
{
    None                ('', '', '', '', ''),
    Church              ('fc.coordroute.mapobjects.type.church',              'images/map/church.png',              'church.png',              'images/map/church.png',              'church.png'              ),
    Castle              ('fc.coordroute.mapobjects.type.castle',              'images/map/castle.png',              'castle.png',              'images/map/castle.png',              'castle.png'              ),
    CastleRuin          ('fc.coordroute.mapobjects.type.castleruin',          'images/map/castle_ruin.png',         'castle_ruin.png',         'images/map/castle_ruin.png',         'castle_ruin.png'         ),
    Chateau             ('fc.coordroute.mapobjects.type.chateau',             'images/map/chateau.png',             'chateau.png',             'images/map/chateau.png',             'chateau.png'             ),
    Peak                ('fc.coordroute.mapobjects.type.peak',                'images/map/peak.png',                'peak.png',                'images/map/peak.png',                'peak.png'                ),
    Tower               ('fc.coordroute.mapobjects.type.tower',               'images/map/tower.png',               'tower.png',               'images/map/tower.png',               'tower.png'               ),
    CommunicationsTower ('fc.coordroute.mapobjects.type.communicationstower', 'images/map/communicationstower.png', 'communicationstower.png', 'images/map/communicationstower.png', 'communicationstower.png' ),
    Lighthouse          ('fc.coordroute.mapobjects.type.lighthouse',          'images/map/lighthouse.png',          'lighthouse.png',          'images/map/lighthouse.png',          'lighthouse.png'          ),
    WindpowerStation    ('fc.coordroute.mapobjects.type.windpowerstation',    'images/map/windpowerstation.png',    'windpowerstation.png',    'images/map/windpowerstation.png',    'windpowerstation.png'    ),
    CircleCenter        ('fc.coordroute.mapobjects.type.circlecenter',        'images/map/circlecenter.svg',        'circlecenter.svg',        'images/map/circlecenter.png',        'circlecenter.png'        ),
    Airfield            ('fc.coordroute.mapobjects.type.airfield',            '',                                   '',                        'images/map/airfield.png',            'airfield.png'            ),
    Subtitle            ('fc.coordroute.mapobjects.type.subtitle',            '',                                   '',                        'images/map/subtitle.png',            'subtitle.png'            ),
    Symbol              ('fc.coordroute.mapobjects.type.symbol',              '',                                   '',                        'images/map/symbol.png',              'symbol.png'              )
    
    MapObjectType(String code, String imageName, String imageShortName, String kmlImageName, String kmlImageShortName)
    {
        this.code = code
        this.imageName = imageName
        this.imageShortName = imageShortName
        this.kmlImageName = kmlImageName
        this.kmlImageShortName = kmlImageShortName
    }
    
    final String code
    final String imageName
    final String imageShortName
    final String kmlImageName
    final String kmlImageShortName
}