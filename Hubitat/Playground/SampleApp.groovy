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
    log.info("App installed with settings -> ${settings}")
}

def updated() {
    log.info("App updated with settings -> ${settings}")
}