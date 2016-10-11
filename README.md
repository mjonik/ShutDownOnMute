# ShutDownOnMute
Shuts down your Android system if no music has been played for a configurable amount of time.
This project is intended for dedicated Android music players to make their batteries last longer.

## Requirements
- Android 5.1 Lollipop or higher
- Your phone must be rooted and a "su" shell command must be present. 

## Screenshots
![alt tag](https://raw.githubusercontent.com/mjonik/ShutDownOnMute/master/docs/screenshot.png)

## Known issues
The counter, responsible for counting the number of elapsed minutes since music has been active will be reset to zero, every time the application is closed.

## Supported devices / internals
The current release has only been tested with the Onkyo DP-X1 running on Android 5.1 rooted with Kingroot and Spotify as a music player app.
Anyway it should be possible to use it with all Android devices running Android 5.1 or higher.
Basically it should be possible to run in with Android 4.3 but you will need to compile it yourself.
The `cvs power shutdown` command is used to power the device off.
The application uses the `audioManger.isMusicActive()` method to determin if any music is playing. Be sure your music application supports this function, also if the application is running but the music is paused. The application will show you the current status.
