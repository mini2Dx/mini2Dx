# Contributing code to mini2Dx

By contributing code to this project you agree that the code contributed shall be publicly available under the [project's license](https://github.com/mini2Dx/mini2Dx/blob/master/LICENSE).

## Pull Requests

* Fork the project
* Make your changes
* Send a pull request on Github

## Changelog

When you fix a bug, add features or change the API, make sure to note this under the appropriate release in the [CHANGES](https://github.com/mini2Dx/mini2Dx/blob/master/CHANGES) file in the repository.

## Releasing

To upload a release to Maven Central you first must follow the [OSSRH guide](http://central.sonatype.org/pages/ossrh-guide.html) to register an account and receive permissions to the org.mini2Dx project group.

Once you've registered, set up your PGP key per the guide [here](http://nemerosa.ghost.io/2015/07/01/publishing-to-the-maven-central-using-gradle/) and add the required details to ~/.gradle/gradle.properties.

Then you can build and publish a release with the following command:
```bash
./gradlew -Prelease clean build publish closeAndReleaseRepository
```

## Building the website

First install RVM + Bundler, then run the following:

```
cd jeykll
bundle install
jekyll build
```