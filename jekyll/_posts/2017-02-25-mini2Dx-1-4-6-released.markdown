---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.4.6 released"
date:   2017-02-25 09:00:00 +0000
---

Hey there mini2Dx community,

I've published mini2Dx 1.4.6 to Maven Central. This is a small update which reduces memory allocations and adds some utility methods:

 * Reduce float array allocations in Polygon#setVertices
 * Added ScreenBasedGame#getScreenManager and ScreenManager#getGameScreens

To update your existing projects, see the [Updating mini2Dx](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx) wiki page.
<!--more-->