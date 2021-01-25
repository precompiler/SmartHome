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
    section("Basic Configurations") {
        input name: "message", type: "text", title: "App message", description: "Enter message", required: true
    }

    section("Advanced Configurations") {

    }
}


def installed() {
    // called when the app is installed for the first time
    log.info("App installed with settings -> ${settings}")
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