definition(
    name: "Event Listener",
    namespace: "precompiler",
    author: "Richard Li",
    description: "Events and subscritions demo app",
    category: "Demo",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
    section {
        input("switches", "capability.switch", multiple: true)
    }
}


def installed() {
    // called when the app is installed for the first time
    log.info("App installed with settings -> ${settings}")
    subscribe(switches, "switch.on", switchHandler)

    subscribe(location, "mode", modeLocationHandler)
    subscribe(location, "position", modeLocationHandler)
    subscribe(location, "sunset", modeLocationHandler)
    subscribe(location, "sunrise", modeLocationHandler)
    subscribe(location, "sunsetTime", modeLocationHandler)
    subscribe(location, "sunriseTime", modeLocationHandler)
}

def switchHandler(evt) {
    log.info("switch status changed to ${evt.value}")
}

def modeLocationHandler(evt) {
    log.info("mode/location changes ${evt}")
}

def updated() {
    // called when the preferences of the app are updated
    log.info("App updated with settings -> ${settings}")
}

def uninstalled() {
    // called when the app is uninstalled
    log.info("App uninstalled")
}

def childUninstalled() {
    // called when child app is uninstalled
    log.info("Child app uninstalled")
}