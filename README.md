# 9gag rss

![Build and Test](https://github.com/angel-git/9gag-rss/workflows/Build%20and%20Test/badge.svg?branch=master)

## Motivation

There are already few alternatives to get RSS from 9gag, such as [gag-rss.com](https://9gag-rss.com/) (which I totally recommend)
but nowadays it doesn't contain videos anymore. This one does.

## Limitations

You can only subscribe to:
- `default` (which is the fresh page)
- `comic`
- `nsfw`
- `among-us`

Feel free to create PR and add more!
This project is not yet battle tested, so they might be few bugs.

Also is not available so far in any domain, so you will have to host it yourself. 

## Running and Building

### Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

### Packaging and running the application

The application can be packaged using:
```shell script
./gradlew quarkusBuild
```
It produces the `9gag-rss-1.0.0-SNAPSHOT-runner.jar` file in the `/build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew quarkusBuild --uber-jar
```

The application is now runnable using `java -jar build/9gag-rss-1.0.0-SNAPSHOT-runner.jar`.

### Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/9gag-rss-1.0.0-SNAPSHOT-runner`

## How to deploy

You can deploy it in Google Cloud using only the free tier.
You will need an instance of the [Firestore database](https://cloud.google.com/firestore/docs/quickstart-servers),
[Google Cloud CLI](https://cloud.google.com/sdk/docs/initializing) and setup an [App Engine](https://cloud.google.com/appengine).

Once the application is built and your `gcloud` is installed you can run the following command:

```shell script
gcloud app deploy build/9gag-rss-1.0.0-SNAPSHOT-runner.jar
```

## How to subscribe

Once the application is running you can get the RSS feed from `your_domain/feed/[default|comic|...]`.

The feeds are updated every 5 minutes and the database doesn't contain posts older than 1 day (so Google Cloud won't charge us!).
