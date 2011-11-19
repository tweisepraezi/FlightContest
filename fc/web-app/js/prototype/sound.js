// script.aculo.us sound.js v1.8.0, Tue Nov 06 15:01:40 +0300 2007

// Copyright (c) 2005-2007 Thomas Fuchs (http://script.aculo.us, http://mir.aculo.us)
//
// Based on code created by Jules Gravinese (http://www.webveteran.com/)
//
// script.aculo.us is freely distributable under the terms of an MIT-style license.
// For details, see the script.aculo.us web site: http://script.aculo.us/

Sound = {
  legs: {},
  _enabled: true,
  template:
    new Template('<embed style="height:0" id="sound_#{leg}_#{id}" src="#{url}" loop="false" autostart="true" hidden="true"/>'),
  enable: function(){
    Sound._enabled = true;
  },
  disable: function(){
    Sound._enabled = false;
  },
  play: function(url){
    if(!Sound._enabled) return;
    var options = Object.extend({
      leg: 'global', url: url, replace: false
    }, arguments[1] || {});
    
    if(options.replace && this.legs[options.leg]) {
      $R(0, this.legs[options.leg].id).each(function(id){
        var sound = $('sound_'+options.leg+'_'+id);
        sound.Stop && sound.Stop();
        sound.remove();
      })
      this.legs[options.leg] = null;
    }
      
    if(!this.legs[options.leg])
      this.legs[options.leg] = { id: 0 }
    else
      this.legs[options.leg].id++;
      
    options.id = this.legs[options.leg].id;
    $$('body')[0].insert( 
      Prototype.Browser.IE ? new Element('bgsound',{
        id: 'sound_'+options.leg+'_'+options.id,
        src: options.url, loop: 1, autostart: true
      }) : Sound.template.evaluate(options));
  }
};

if(Prototype.Browser.Gecko && navigator.userAgent.indexOf("Win") > 0){
  if(navigator.plugins && $A(navigator.plugins).detect(function(p){ return p.name.indexOf('QuickTime') != -1 }))
    Sound.template = new Template('<object id="sound_#{leg}_#{id}" width="0" height="0" type="audio/mpeg" data="#{url}"/>')
  else
    Sound.play = function(){}
}
