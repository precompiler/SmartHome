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
    
    page(name: "pg2", title: "Page Two", uninstall: true, install: true) {
        section("Advanced Configurations") {
            paragraph "This is a long description that rambles on and on and on..."
            href(name: "hrefNotRequired",
             title: "Hubitat",
             required: false,
             style: "external",
             url: "https://hubitat.com/",
             description: "tap to view hubitat website")
        }
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