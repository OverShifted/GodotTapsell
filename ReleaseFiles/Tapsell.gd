extends Node

signal init_success()
signal init_failed(err_domain, err_message)

signal banner_ad_request_response(zone, id)
signal banner_ad_request_error(zone, id)

signal banner_ad_opened(id)
signal banner_ad_closed(id)
signal banner_ad_rewarded(id)
signal banner_ad_show_error(id, err)

signal video_ad_request_response(zone, id)
signal video_ad_request_error(zone, id)

signal video_ad_opened(id)
signal video_ad_closed(id)
signal video_ad_rewarded(id)
signal video_ad_show_error(id, err)

var plugin
var plugin_name = "GodotTapsell"

var app_key := "<app id goes here>"

const GRAVITY_AXIS_CLIP := 8
const GRAVITY_AXIS_PULL_AFTER := 4
const GRAVITY_AXIS_PULL_BEFORE := 2
const GRAVITY_AXIS_SPECIFIED := 1
const GRAVITY_AXIS_X_SHIFT := 0
const GRAVITY_AXIS_Y_SHIFT := 4
const GRAVITY_BOTTOM := 80
const GRAVITY_CENTER := 17
const GRAVITY_CENTER_HORIZONTAL := 1
const GRAVITY_CENTER_VERTICAL := 16
const GRAVITY_CLIP_HORIZONTAL := 8
const GRAVITY_CLIP_VERTICAL := 128
const GRAVITY_DISPLAY_CLIP_HORIZONTAL := 16777216
const GRAVITY_DISPLAY_CLIP_VERTICAL := 268435456
const GRAVITY_END := 8388613
const GRAVITY_FILL := 119
const GRAVITY_FILL_HORIZONTAL := 7
const GRAVITY_FILL_VERTICAL := 112
const GRAVITY_HORIZONTAL_GRAVITY_MASK := 7
const GRAVITY_LEFT := 3
const GRAVITY_NO_GRAVITY := 0
const GRAVITY_RELATIVE_HORIZONTAL_GRAVITY_MASK := 8388615
const GRAVITY_RELATIVE_LAYOUT_DIRECTION := 8388608
const GRAVITY_RIGHT := 5
const GRAVITY_START := 8388611
const GRAVITY_TOP := 48
const GRAVITY_VERTICAL_GRAVITY_MASK := 112

const BANNER_TYPE_320x50  := 0
const BANNER_TYPE_320x90  := 1
const BANNER_TYPE_320x100 := 2
const BANNER_TYPE_250x250 := 3
const BANNER_TYPE_300x250 := 4
const BANNER_TYPE_468x60  := 5
const BANNER_TYPE_728x90  := 6
const BANNER_TYPE_160x600 := 7

func _ready():
	if Engine.has_singleton(plugin_name):
		plugin = Engine.get_singleton(plugin_name)
		plugin.init(app_key, true)

		plugin.connect("init_success", self, "_init_success")
		plugin.connect("init_failed" , self, "_init_failed")

		plugin.connect("banner_ad_request_response", self, "_banner_ad_request_response")
		plugin.connect("banner_ad_request_error"   , self, "_banner_ad_request_error")

		plugin.connect("banner_ad_opened"    , self, "_banner_ad_opened")
		plugin.connect("banner_ad_closed"    , self, "_banner_ad_closed")
		plugin.connect("banner_ad_rewarded"  , self, "_banner_ad_rewarded")
		plugin.connect("banner_ad_show_error", self, "_banner_ad_show_error")

		plugin.connect("video_ad_request_response", self, "_video_ad_request_response")
		plugin.connect("video_ad_request_error"   , self, "_video_ad_request_error")

		plugin.connect("video_ad_opened"    , self, "_video_ad_opened")
		plugin.connect("video_ad_closed"    , self, "_video_ad_closed")
		plugin.connect("video_ad_rewarded"  , self, "_video_ad_rewarded")
		plugin.connect("video_ad_show_error", self, "_video_ad_show_error")
	else:
		print("Could not load plugin: ", plugin_name)

# for width and height, use -1 for MATCH_PARENT and -2 for WRAP_CONTENT
# for gravity, mix constants above with the bitwise or operator (`|`)
func create_banner_frame(width: int, height: int, gravity: int):
	if plugin: plugin.create_banner_frame(width, height, gravity)

func request_banner_ad(zone: String, banner_type: int):
	if plugin: plugin.request_banner_ad(zone, banner_type)

func show_banner_ad(id: String):
	if plugin: plugin.show_banner_ad(id)

func request_video_ad(zone: String):
	if plugin: plugin.request_video_ad(zone)

func show_video_ad(id: String):
	if plugin: plugin.show_video_ad(id)

# Signal handlers
func _init_success(): emit_signal("init_success")
func _init_failed(err_domain: String, err_message: String): emit_signal("init_failed", err_domain, err_message)

func _banner_ad_request_response(zone: String, id: String): emit_signal("banner_ad_request_response", zone, id)
func _banner_ad_request_error(zone: String, err: String): emit_signal("banner_ad_request_error", zone, err)

func _banner_ad_opened(id: String): emit_signal("banner_ad_opened", id)
func _banner_ad_closed(id: String): emit_signal("banner_ad_closed", id)
func _banner_ad_rewarded(id: String): emit_signal("banner_ad_rewarded", id)
func _banner_ad_show_error(id: String, err: String): emit_signal("banner_ad_show_error", id, err)

func _video_ad_request_response(zone: String, id: String): emit_signal("video_ad_request_response", zone, id)
func _video_ad_request_error(zone: String, err: String): emit_signal("video_ad_request_error", zone, err)

func _video_ad_opened(id: String): emit_signal("video_ad_opened", id)
func _video_ad_closed(id: String): emit_signal("video_ad_closed", id)
func _video_ad_rewarded(id: String): emit_signal("video_ad_rewarded", id)
func _video_ad_show_error(id: String, err: String): emit_signal("video_ad_show_error", id, err)
