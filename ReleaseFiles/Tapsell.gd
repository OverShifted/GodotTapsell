extends Node

signal request_ad_on_ad_available(message)
signal request_ad_on_error(message)
signal show_ad_on_opened
signal show_ad_on_closed
signal show_ad_on_error(message)
signal show_ad_on_rewarded(reward)

var plugin
var plugin_name = "GodotTapsell"

# You might want to calculate keys/tokens by using logical operators such as XOR and prevent hard-coding them to make reverse-engineering harder
var app_key := "app key/token goes here"
var zone_key := "zone key/token/id goes here"

func _ready():

	if Engine.has_singleton(plugin_name):
		plugin = Engine.get_singleton(plugin_name)
		plugin.init(app_key)
		
		plugin.connect("request_ad_on_ad_available", self, "_request_ad_on_ad_available")
		plugin.connect("request_ad_on_error", self, "_request_ad_on_error")
		plugin.connect("show_ad_on_opened", self, "_show_ad_on_opened")
		plugin.connect("show_ad_on_closed", self, "_show_ad_on_closed")
		plugin.connect("show_ad_on_error", self, "_show_ad_on_closed")
		plugin.connect("show_ad_on_rewarded", self, "_show_ad_on_closed")
	else:
		print("Could not load plugin: ", plugin_name)

func request_ad():
	plugin.request_ad(zone_key)
	
func _request_ad_on_ad_available(ad_id : String):
	emit_signal("request_ad_on_ad_available", ad_id)
	plugin.show_ad()

func _request_ad_on_error(message : String):
	emit_signal("request_ad_on_error", message)

func _show_ad_on_opened():
	emit_signal("show_ad_on_opened")

func _show_ad_on_closed():
	emit_signal("show_ad_on_closed")

func _show_ad_on_error(message : String):
	emit_signal("show_ad_on_error", message)

func _show_ad_on_rewarded(reward : bool):
	emit_signal("show_ad_on_rewarded", reward)
