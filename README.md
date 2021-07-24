# GodotTapsell
[Tapsell](https://tapsell.ir) ads implementation for Godot game engine.
Currently only supports rewarded video ads.

Tested on Godot 3.3.2 and Android Q

## Installation
1. Download the zip file provided [here](https://github.com/OverShifted/GodotTapsell/releases)
.
2. Extract `arr` and `gdap` files in `res://android/plugins` of your Godot project
3. Extract the `gd` file in `res://commons` of your Godot project
4. Install `Android Build Template` using the `Project / Install Android Build Template...` option in the Godot editor.
5. Open `Project / Project Settings...` ; head over to `Autoload` tab and add the extracted script as an Autoload with `Node Name` of `Tapsell`
6. Open `Project / Export...` . In your Android preset; enable `Use Custom Build` and `Godot Tapsell` options.

    <img src="https://github.com/OverShifted/GodotTapsell/blob/main/docs/ExportMenu.png" alt="Export Menu"/>

## Using the API
Any where is your code; use `Tapsell.request_ad()` to show an ad to the user.

Connect signals using `Tapsell.connect("signal_name", self, "target_function_name")` to listen for diffrent events.

Available signals:
```
request_ad_on_ad_available(message: String)
request_ad_on_error(message: String)
show_ad_on_opened()
show_ad_on_closed()
show_ad_on_error(message: String)
show_ad_on_rewarded(reward: bool)
```

When `show_ad_on_rewarded` signal is emmited with `reward` being `true`; you can give a reward to the user.

For more information see [here (Farsi)](https://docs.tapsell.ir/tapsell-sdk/android/rewarded-interstitial/) or [here (English)](https://docs.tapsell.ir/en/tapsell-sdk/android/rewarded-interstitial/).
