---
layout: post
author: Thomas Cashman
title:  "gradle-butler-plugin 1.1.0 released"
date:   2016-11-13 09:00:00 +0000
---

I've pushed a new release of the gradle-butler-plugin. This adds two of the most requested features; configuration allows you to specify a custom release channel and support for OS-independent (e.g. HTML5) game releases :) See the full list of changes below:

 * Channels can now be specified for each OS
 * Added 'AnyOs' option for builds that can be built and run on any OS, e.g. HTML5 games
 * Fixed butler binary not being set as executable on some OSs
 * Update to gradle-download-task 3.1.2
 <!--more-->