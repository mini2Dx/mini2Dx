---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.3.0 released"
date:   2016-11-06 09:00:00 +0000
---

You may have noticed that the tag was created a while ago in the repository but now is the official announcement :) mini2Dx 1.3.0 is now available in Maven Central and comes with a tonne of new features. You can generate a new project using the [mini2Dx Project Generator](https://mini2dx.org/downloads.html) or [update your existing project](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx). All changes in this release are [listed in the repository](https://github.com/mini2Dx/mini2Dx/blob/master/CHANGES) but here are some of the highlights.
<!--more-->
__Responsive UI framework__
You may be familiar with the idea of "Responsive Websites" that change their layout based on what device they're viewed on (i.e. desktop or mobile). mini2Dx has taken this idea further by implementing a UI framework that not only changes the visual layout of the UI based on the device but also automatically adapts UI navigation based on the device and input method. This allows you to implement a single UI that is truly cross-platform. Check out [the wiki pages](https://github.com/mini2Dx/mini2Dx/wiki/UI-Introduction) for more information.

__Improved compatibility with Slick2D API__
Several methods and features that are available in Slick2D were missing from the framework. These have now been added in 1.3.0 which should allow people to migrate to mini2Dx a lot easier. There is still one method missing in the Graphics class that allows for drawing of textures to a specified shape that I will add in the next major release (see [issue #59](https://github.com/mini2Dx/mini2Dx/issues/59)).

__Improved controller support__
The API now supports controller-specific input handling allowing you to change input based on the gamepad used. Currently only Xbox 360 and Xbox One mappings are provided but PS3 and PS4 mappings will be added soon. The controller API also hooks into the UI framework to allow UI navigation via controllers. The API also supports several deadzone handling implementations. Check out [the wiki page](https://github.com/mini2Dx/mini2Dx/wiki/Input-Handling) for more information.

This release took 1 year to implement because I'm still doing gamedev in my spare time. The majority of this time was spent implementing the API for the UI framework. I'm hoping to make time between releases a lot shorter going forward now that this is finished. As always, if you want to help contribute to the framework you can fork the repository on Github and send pull requests. If there are any issues or features you would like to see then please create an issue in the [Issue Tracker](https://github.com/mini2Dx/mini2Dx/issues).