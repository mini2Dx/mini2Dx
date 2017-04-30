---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.4.4 released"
date:   2017-02-05 09:00:00 +0000
---

Hey there mini2Dx community,

I've published mini2Dx 1.4.4 to Maven Central. This is a small update which adds new methods:

 * Added additional methods to geometry classes
  * Shape#getCenterX + Shape#getCenterY
  * Shape#scale
  * Shape#setRadius
  * Polygon#isEquilateral
  * LineSegment#getLength
 * Added methods to preset singletons and prototypes for dependency injection
 * Added @PostInject annotation to trigger methods after dependency injection
 * @Autowired dependencies now also inject into super classes

To update your existing projects, see the [Updating mini2Dx](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx) wiki page.
<!--more-->