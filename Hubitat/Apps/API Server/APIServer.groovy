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
    path("/hubs") { action: [GET: "getHubs"]}
    path("/hubs/:id") { action: [GET: "getHubInfo"]}
}

preferences {
    section("Logging Setting") {
        input(name: "logEnable", type: "bool", title: "Enable logging?", required: false, default: false)
    }
}

def getHubs() {
    def hubList = []
    location.hubs?.each {
        hubList.add([
            "id": it.id,
            "name": it.name
        ])
    }
    render(contentType: APPLICATION_JSON, data: new JsonBuilder(hubList).toPrettyString())
}

def getHubInfo() {
    def hub = location.hubs?.find{String.valueOf(it.id) == params.id}
    if(!hub) {
        render(status: 404, contentType: APPLICATION_JSON, data: '')
    } else {
        def ret = [
            "id": hub.id,
            "name": hub.name,
            "localIP": hub.localIP,
            "firmwareVersion": hub.firmwareVersionString,
            "uptime": "${hub.uptime/60/60}h"
        ]
        render(contentType: APPLICATION_JSON, data: new JsonBuilder(ret).toPrettyString())
    }
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


final String APPLICATION_JSON = 'application/json'