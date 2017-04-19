quartz {
    autoStartup = true
    jdbcStore = false
}

environments {
    lastdb {
    }
    development {
    }
    test {
        quartz {
            autoStartup = false
        }
    }
    production {
    }
    cloudfoundry {
        quartz {
            autoStartup = false
        }
    }
}
