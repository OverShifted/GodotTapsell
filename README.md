# GodotTapsell
[Tapsell](https://tapsell.ir) ads implementation for Godot game engine.
Currently only supports rewarded video ads.

Tested on Godot 3.3.2 and Android Q

## Installation
1. Download the zip file provided [here](https://github.com/OverShifted/GodotTapsell/releases)
.
2. Extract `arr` and `gdap` files in `res://android/plugins` of your Godot project
3. Extract the `gd` file in `res://commons` of your Godot project
4. Install `Android Build Template` using the `Project / Install Android Build Template...` option in the Godot editor
5. Open `Project / Project Settings...` ; head over to `Autoload` tab and add the extracted script as an Autoload with `Node Name` of `Tapsell`
6. Open `Project / Export...` . In your Android preset; enable `Use Custom Build` and `Godot Tapsell` options.

    ![Export Menu](https://github.com/OverShifted/GodotTapsell/raw/main/docs/ExportMenu.png)

## Usage
Specify your appid in the `Tapsell.gd` file.
### Video ad
Anywhere in your code:
```gd
Tapsell.request_video_ad("<zone id>")
```
In a `video_ad_request_response` signal handler:
```gd
func on_video_ad_request_response(zone: String, id: String):
    Tapsell.show_video_ad(id)
```
### Standard banner ad
> ⚠️ Warning: Banner add removal is not implemented yet.

Anywhere in your code:
```gd
Tapsell.create_banner_frame(width, height, gravity)
Tapsell.request_banner_ad("<zone id>", banner_type)

# Example:
Tapsell.create_banner_frame(320, 150, Tapsell.GRAVITY_TOP)
Tapsell.request_banner_ad("<zone id>", Tapsell.BANNER_TYPE_320x50)
```
In a `banner_ad_request_response` signal handler:
```gd
func on_banner_ad_request_response(zone: String, id: String):
    Tapsell.show_banner_ad(id)
```