definition(
    name: "Sample App",
    namespace: "precompiler",
    author: "Richard Li",
    description: "Hello World",
    category: "Demo",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "")

preferences {
    page(name: "pg1", title: "Page One", nextPage: "pg2", uninstall: true) {
        section("Basic Configurations") {
                input name: "message", type: "text", title: "App message", description: "Enter message", required: true
        }
    }
    
    page(name: "pg2", title: "Page Two", nextPage: "pg3", uninstall: true) {
        section("Advanced Configurations") {
            paragraph "This is a long description that rambles on and on and on..."

            href(name: "hrefNotRequired",
             title: "Hubitat",
             required: false,
             style: "external",
             url: "https://hubitat.com/",
             description: "tap to view hubitat website")
            
            input "modes", "mode", title: "only when mode is", multiple: true, required: false

            input(name: "color", type: "enum", title: "Color", options: ["Red","Green","Blue","Yellow"], multiple: false)

            input("myDevice", "device.dummy", hideWhenEmpty: true)
        }
    }

    page(name: "pg3", title: "Page Three", uninstall: true, nextPage: "pg4") {
        section {
            input("sensorType", "enum", options: [
                "contactSensor":"Open/Closed Sensor",
                "motionSensor":"Motion Sensor",
                "switch": "Switch",
                "moistureSensor": "Moisture Sensor"])

            input("actuatorType", "enum", options: [
                "switch": "Light or Switch",
                "lock": "Lock"]
            )
        }
    }

    page(name: "pg4", title: "Page Four", uninstall: true, install: true) {
        section {
            input(name: "sensor", type: "capability.$sensorType", title: "If the $sensorType device")
            input(name: "sensorAction", type: "enum", title: "is", options: attributeValues(sensorType))
        }
        section {
            input(name: "actuator", type: "capability.$actuatorType", title: "Set the $actuatorType")
            input(name: "actuatorAction", type: "enum", title: "to", options: actions(actuatorType))
         }

    }
}


private attributeValues(attributeName) {
    switch(attributeName) {
        case "switch":
            return ["on","off"]
        case "contactSensor":
            return ["open","closed"]
        case "motionSensor":
            return ["active","inactive"]
        case "moistureSensor":
            return ["wet","dry"]
        default:
            return ["UNDEFINED"]
    }
}

private actions(attributeName) {
    switch(attributeName) {
        case "switch":
            return ["on","off"]
        case "lock":
            return ["lock","unlock"]
        default:
            return ["UNDEFINED"]
    }
}

def installed() {
    // called when the app is installed for the first time
    log.info("App installed with settings -> ${settings}")
    state.installedTime = now()
}

def updated() {
    // called when the preferences of the app are updated
    log.info("App updated with settings -> ${settings}")
    log.info("Installed @ ${state.installedTime}")
}

def uninstalled() {
    // called when the app is uninstalled
    log.info("App uninstalled")
}

def childUninstalled() {
    // called when child app is uninstalled
    log.info("Child app uninstalled")
}