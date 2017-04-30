---
layout: post
author: Thomas Cashman
title:  "mini2Dx over HTTPS"
date:   2015-12-03 09:00:00 +0000
---

Hey there mini2Dx community,

Here’s some quick status updates…

__HTTPS Update__

Thanks to Let’s Encrypt, we now provide access to mini2Dx, downloads and maven repositories over HTTPS. We’ve configured all non-HTTPS links to redirect to HTTPS but it is recommend you update your Gradle files to use HTTPS directly.

__1.3.0 Status Update__

Our next major release, 1.3.0, is coming along nicely. We’ve received dozens of bug fixes and features through pull requests. All that is remaining is feature completion of our UI framework. In the meantime, if you’d like to work off the latest beta the tags available in the maven repositories.