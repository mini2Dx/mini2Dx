---
layout: post
author: Thomas Cashman
title:  "mini2Dx 1.7.0 released and the roadmap ahead"
date:   2018-06-10 16:00:00 +0300
---

1.7.0
-----------------------

After several months work, I'm happy to announce the release of mini2Dx 1.7.0. This release comes with a lot of bug fixes, optimisations and improvements. You can find the full list in the [CHANGES file](https://github.com/mini2Dx/mini2Dx/blob/master/CHANGES) on the repository.

To update your existing projects, see the [Updating mini2Dx](https://github.com/mini2Dx/mini2Dx/wiki/Updating-mini2Dx) wiki page.

Roadmap
-----------------------

Now that 1.7.0 is released, it's time to talk about the future of mini2Dx.

Recently, Apple deprecated OpenGL on Mac. This means that at some point in the future, the current version of mini2Dx will stop working as it is tightly coupled to OpenGL via LibGDX. While I'm disappointed in this decision from Apple, I still want game developers using mini2Dx to be able to release on Mac.

Furthermore, I'm hoping to bring my next mini2Dx-based game [Alchemic Cutie](https://alchemiccutie.com) to consoles - this means that using a JRE-based environemt won't be possible due to performance/licensing/etc. However, I like coding games in Java and would like to continue to do so.

So with that, I'm beginning work on the next generation of mini2Dx - 2.0! The goal of the next generation is to support more platforms such as web browsers and consoles. But also, it aims to be the best 2D game development framework for both Java and C# developers. Here's how:

 * Core API will be implemented in both Java and C#
 * Java will use two runtimes; LibGDX and bgfx
 * C# will use MonoGame for its runtime
 * From the first version, it will be possible to cross-compile your Java game to the C# runtime

This development will take a lot of time and won't be completed until 2019. In the meantime, you can expect that I'll keep supporting 1.7.x of mini2Dx with fixes.

As an existing mini2Dx user, you're probably wondering what this means for your current game. My goal is to keep the 2.0 API as close as possible to the existing 1.7.x API (since I'm also implementing a game against 1.7.x). So there may be a small number of method changes needed during the upgrade but you can expect the same functionality to be available and more!
<!--more-->