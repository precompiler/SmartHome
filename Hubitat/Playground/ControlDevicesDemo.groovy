definition(
    name: "Device Controller",
    namespace: "precompiler",
    author: "Richard Li",
    description: "Control Devices",
    category: "Demo",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
    section {
        input("tempSensor", "capability.temperatureMeasurement")
    }
}


def installed() {
    // called when the app is installed for the first time
    log.info("App installed with settings -> ${settings}")
    def attributeList = tempSensor.getSupportedAttributes()
    attributeList.each{
        log.info(it)
    }

    log.info(tempSensor.currentState("temperature"))
    def tempState = tempSensor.currentValue("temperature")
    log.info("temperature retrieved  ${tempState} - ${tempState}")
}


def updated() {
    // called when the preferences of the app are updated
    log.info("App updated with settings -> ${settings}")
}

def uninstalled() {
    // called when the app is uninstalled
    log.info("App uninstalled")
}