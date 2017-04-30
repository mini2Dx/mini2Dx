---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.4.5 released"
date:   2017-02-12 09:00:00 +0000
---

Hey there mini2Dx community,

I've published mini2Dx 1.4.5 to Maven Central. This is a small update which adds some utility methods:

 * Added Shape#add and Shape#subtract methods for modifying position
 * QuadTree implementations can now specify a minimum quad size
 * Added Positionable#moveTowards for easier movement logic
 * Added Positionable#getDistanceTo(x,y)

To update your existing projects, see the [Updating mini2Dx](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx) wiki page.
<!--more-->