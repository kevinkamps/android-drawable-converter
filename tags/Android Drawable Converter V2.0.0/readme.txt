


Description
I've found myself loosing a lot of time with creating drawables in different density versions. The Android Drawable Converter project intents to help you by supplying a tool that helps you converting drawables into other density versions like ldpi, mdpi, hdpi, xhdpi automatically.

Installation

To use this project you can download android-drawable-converter.version1.0.0.zip

This project needs imagemagick to convert the drawables so make sure you have imagemagick installed: http://www.imagemagick.org/script/binary-releases.php

1) Fill out the settings.ini (Most important convert_command, convert_source and convert_destination)
2) Then execute java -jar android-drawable-converter.jar from the root of your android project

Options:
you can use all the parameters in settings.ini as arguments when executing android-drawable-converter.jar. Ex: "java -jar android-drawable-converter.jar convert_to ldpi"
you can supply multiple convert_destinations like "convert_destinations=ldpi,mdpi,hdpi"
Technically you can use this tool for iPhone by defining your own formats and defining the convert_source_path and convert_destination_path settings.