Description
=============

I've found myself loosing a lot of time with creating drawables in different density sizes. The Android Drawable Converter project intents to help you by supplying a tool that is able to convert drawables into other density versions like ldpi, mdpi, hdpi, xhdpi automatically.

Installation
----------------------
To use this project you can download "Android Drawable Converter V2.0.0" on the download page.

This project needs imagemagick to convert the drawables so make sure you have imagemagick installed: http://www.imagemagick.org/script/binary-releases.php.

0. Create your drawables in the highest density you need.
0. Fill out the settings.ini (Most important convert_command, convert_source and convert_destination).
0. Move the android-drawable-converter.jar and settings.ini to the root of your android project or supply the convert_source_path and convert_destination_path options to convert an absolute source directory to multiple absolute destination directories.
0. Execute "java -jar android-drawable-converter.jar"
0. You are done all the drawables from the convert_source to all convert_destinations have been created.

Options
--------------------
* You can use all the parameters in settings.ini as arguments when executing android-drawable-converter.jar. Ex: "java -jar android-drawable-converter.jar convert_destinations ldpi"
* You can supply multiple convert_destinations like "convert_destinations=ldpi,mdpi,hdpi"
* Technically you could use this tool for iPhone by defining your own formats and defining the convert_source_path and convert_destination_path in the settings.
