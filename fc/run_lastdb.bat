@echo off
cls
grails -Dgrails.gsp.enable.reload=true -Dgrails.env=lastdb run-app
