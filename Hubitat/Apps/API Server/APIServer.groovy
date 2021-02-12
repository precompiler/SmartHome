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

    path("/devices") { action: [GET: "getDevices"]}
    path("/devices/:id") { action: [GET: "getDeviceInfo"]}
    path("/devices/:id/attributes") { action: [GET: "getDeviceAttributes"]}
    path("/devices/:id/commands") { action: [GET: "getDeviceCommands"]}
}

preferences {
    section("Select Allowed Devices") {
        input("allDevices", "capability.*", multiple: true)   
    }
    section("Logging Setting") {
        input(name: "logEnable", type: "bool", title: "Enable logging?", required: false, default: false)
    }
}

def getHubs() {
    def hubList = _getHubs()
    _renderJson(hubList)
}

def getHubInfo() {
    def hub = _getHubInfoById(params.id)
    if(!hub) {
        _renderError(404, '')
    } else {
        _renderJson(hub)
    }
}

def getDevices() {
    def deviceList = _getDevices()
    _renderJson(deviceList)
}

def getDeviceInfo() {
    def d = _getDeviceInfoById(params.id)
    if(!d) {
        _renderError(404, '')
    } else {
        _renderJson(d)
    }
}



def getDeviceAttributes() {
    def attributes = _getDeviceAttributesById(params.id)
    if(!attributes) {
        _renderError(404, '')
    } else {
        _renderJson(attributes)
    }
}

def getDeviceCommands() {
    def commands = _getDeviceCommandsById(params.id)
    if(!commands) {
        _renderError(404, '')
    } else {
        _renderJson(commands)
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


private _getHubs() {
    def hubList = []
    location.hubs?.each {
        hubList.add([
            "id": it.id,
            "name": it.name
        ])
    }
    return hubList
}

private _getDevices() {
    def deviceList = []
    allDevices?.each {
        deviceList.add(
            [
                "id": it.getId(),
                "name": it.getName(),
                "label": it.getLabel()
            ]
        )
    }
    return deviceList
}

private _getDeviceById(id) {
    return allDevices.find {it.id == id}
}

private _getHubInfoById(id) {
    def hub = location.hubs?.find{String.valueOf(it.id) == id}
    if(!hub) {
        return null
    } else {
        return [
            "id": hub.id,
            "name": hub.name,
            "localIP": hub.localIP,
            "firmwareVersion": hub.firmwareVersionString,
            "uptime": "${hub.uptime/60/60}h"
        ]
    }
}

private _getDeviceInfoById(id) {
    def d = _getDeviceById(id)
    if(!d) {
        return null
    } else {
        return [
            "id": d.getId(),
            "name": d.getName(),
            "label": d.getLabel(),
            "data": d.getData(),
            "status": d.getStatus()
        ]
    }
}

private _getDeviceAttributesById(id) {
    def d = _getDeviceById(id)
    def attributes = [:]
    if(!d) {
        return null
    } else {
        d.getSupportedAttributes().each {
            if (attributes.containsKey(it.name)) {
                // it's possible that a device has attributes with same name but different IDs. e.g. lidl color bulb
                // first attribute wins and log a warning message
                log.warn("Duplicate attribute found ${it.name}")
            } else {
                attributes[it.name] = d.currentState(it.name)
            }
        }
        return attributes
    }
}

private _getDeviceCommandsById(id) {
    def d = _getDeviceById(id)
    def commands = []
    if(!d) {
        return null
    } else {
        d.getSupportedCommands().each {
            commands.add(it)
        }
        return commands
    }
}

private _renderJson(data) {
    render(contentType: APPLICATION_JSON, data: new JsonBuilder(data).toPrettyString())
}

private _renderError(httpCode, data) {
    render(status: httpCode, contentType: APPLICATION_JSON, data: data)
}

final String APPLICATION_JSON = 'application/json'