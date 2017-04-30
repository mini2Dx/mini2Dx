---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.2.0 released"
date:   2015-08-22 09:00:00 +0000
---

Hey there mini2Dx community,

Today I've released 1.2.0 which includes an update to LibGDX 1.6.5, several bug fixes, Entity-Component-System framework integration and some API changes.<!--more--> For a full list of changes in this release, see the CHANGES file in the repository.

__New Feature: Artemis ODB integration__
During alpha development, mini2Dx had its own Entity-Component-System framework which was sub-optimal. This was removed in favour of eventually introducing support for the artemis-odb framework. Today I’m glad to announce that this integration is complete. Documentation on how to use artemis in mini2Dx can be found on the wiki.

__API Change: New method of launching mini2Dx games__
A major issue reported by Karl Andersson led to discovery that input was not being re-polled if an update call occurred more than once per frame. Solving this wasn’t easy and required mini2Dx to implements its own versions of LibGDX’s game runner classes.

If you are updating to 1.2.0 from a previous version, first view the wiki page on how to update to the latest version.

Next, you will need to change your main desktop, Android and iOS classes. For examples on how the new launchers work, see the following examples from the UATs projects.

 * [Desktop](https://github.com/mini2Dx/mini2Dx/blob/master/uats-desktop/src/main/java/org/mini2Dx/uats/desktop/DesktopUATApplication.java)
 * [Android](https://github.com/mini2Dx/mini2Dx/blob/master/uats-android/src/org/mini2Dx/uats/android/AndroidUATApplication.java)
 * [iOS](https://github.com/mini2Dx/mini2Dx/tree/master/uats-ios)

__mini2Dx Project Generator 1.1.0__
To facilitate the new changes, the mini2Dx project generator has been updated with new templates and features to detect deprecated releases.

As always, any new project generated using the Project Generator Tool will use the latest mini2Dx.