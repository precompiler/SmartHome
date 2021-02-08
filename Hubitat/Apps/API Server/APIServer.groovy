import groovy.json.JsonBuilder

definition(
    name: "API Server",
    namespace: "precompiler",
    author: "Richard Li",
    description: "Control Devices",
    category: "API",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
)

mappings {
    path("/greeting") { action: [GET: "greeting"]}
}

preferences {
    section("Logging Setting") {
        input("logEnable", "bool", "Enable logging?", required: false, default: false)
    }
}

def greeting() {
    log.info("========hello!==========")
    render(contentType: "application/json", data: new JsonBuilder(["msg": "hello"]).toPrettyString())
}


def installed() {
    initialize()
}

def updated() {
    unsubscribe()
    initialize()
}

def initialize() {
    if (!state.accessToken) {
        createAccessToken()
    }
    // for test only
    log.info("${getLocalApiServerUrl()}/${app.id}/greeting?access_token=${state.accessToken}")
}

