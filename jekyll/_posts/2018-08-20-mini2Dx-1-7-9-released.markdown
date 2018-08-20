---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.7.9 released"
date:   2018-08-20 16:00:00 +0300
---

mini2Dx 1.7.9 has been released with the following changes:

 * [BREAKING] mini2Dx artemis systems moved from package com.artemis.system to com.artemis to provide access to artemis-odb scopes
 * Fix interpolation and rendering systems not checking disabled flag
 * Fix DispersedIntervalEntitySystem not removing entities from processing queue if deleted between intervals

To update your existing projects, see the [Updating mini2Dx](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx) wiki page.