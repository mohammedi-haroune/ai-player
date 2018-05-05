# AI Player
An Artificial Intelligence media player that can be controlled with hand postures

# Architecture
The project is made of three standalone applications : 
1. **collector-app :** Collector collect the input (points coordinates), 
send them to predictor to detect the gesture 
and then to the controller to interact with the player

2. **predictor-app :** Controller receive gestures and control the mpv player based
 on the provided configuration file example: start or stop the player
 
1. **controller-app :** Predictor receive input (points coordinates), 
pass them to the trained model and send output (gestures predictions)

# Installation

## Requirements
- [mpv player](https://mpv.io/) that can be ran from terminal
- python with following modules installed : 
    - tensorflow
    - keras
    - numpy

## How to use ?
In order to run the different applications we provide a simple daemon CLI (commande line interface).

We've worked very hard to make it the more self explanatory possible, just use the help
option to get in touch with how to run make it run

To use this CLI you have different options : 

### Install binaries
At this writing time we've just packaged to `jar` and `deb`. other platforms will be added very soon

1. Download and install the binary from [realeses](https://github.com/mohammedi-haroune/ai-player/releases)
2. The CLI command will be available in the path
3. Run `ai-player help`

### Build from source
#### Requirements
- jdk >= 1.8
- sbt >= 1.0
- wix toolset (for packaging to windows)

#### Build Jar
```bash
git clone 
cd ai-player
sbt assembly
```
You will find the outoput in the `target` directory `target/ai-player.jar` 

#### Package binaries
```bash
git clone 
cd ai-player
sbt <os>:packageBin
```
`<os>` could be one of the following : `debian`, `windows` or `universal`. 
for more information refer to [sbt-native-packager official docs](https://www.scala-sbt.org/sbt-native-packager/)

You will find the outoput in the `target` directory

### Using intellij idea IDE
1. Clone the repo `git clone <repo>`
2. Import the project as an sbt project
3. Create a run configuration with
    - main = `com.usthb.ai.main.AIPlayer`
    - argumets = `help`
3. Create a run configuration for each application

## Commands Configuration file Template
```hocon
gestures = ["stop", "point1", "point2", "fist", "grab"]
actions = [
  "com.usthb.ai.controller.Start",
  "com.usthb.ai.controller.Pause",
  "com.usthb.ai.controller.Resume",
  "com.usthb.ai.controller.Play",
  "com.usthb.ai.controller.Abort",
  "com.usthb.ai.controller.Seek",
  "com.usthb.ai.controller.RevertSeek",
  "com.usthb.ai.controller.PlayListNext",
  "com.usthb.ai.controller.PlayListPrev",
  "com.usthb.ai.controller.VolumeUp",
  "com.usthb.ai.controller.VolumeDown",
  "com.usthb.ai.controller.SpeedUp",
  "com.usthb.ai.controller.SpeedDown",
  "com.usthb.ai.controller.None"
]

aborted {
  stop = "com.usthb.ai.controller.None"
  fist = "com.usthb.ai.controller.Start"
  point1 = "com.usthb.ai.controller.None"
  point2 = "com.usthb.ai.controller.None"
  grab = "com.usthb.ai.controller.None"
}

paused {
  stop = "com.usthb.ai.controller.Resume"
  fist = "com.usthb.ai.controller.Abort"
  point1 = "com.usthb.ai.controller.VolumeUp"
  point2 = "com.usthb.ai.controller.VolumeDown"
  grab = "com.usthb.ai.controller.PlayListNext"
}

playing {
  stop = "com.usthb.ai.controller.Pause"
  fist = "com.usthb.ai.controller.Abort"
  point1 = "com.usthb.ai.controller.SpeedUp"
  point2 = "com.usthb.ai.controller.SpeedDown"
  grab = "com.usthb.ai.controller.PlayListPrev"
}
```

## Disturbing the system
The system is designed in such a way that can be executed on different host machines

You need to provide a costume configuration file changing the defalut values for
 `[predictor|collector|controller].actor.netty.tcp.[hostname|port]`
 
The default configuration file is : 
```hocon
collector {
  akka.actor.debug.receive = on
  akka.actor.warn-about-java-serializer-usage = off

  akka {
    loggers = ["akka.event.Logging$DefaultLogger"]
    logging-filter = "akka.event.DefaultLoggingFilter"
    loggers-dispatcher = "akka.actor.default-dispatcher"
    logger-startup-timeout = 5s
    loglevel = "DEBUG"
    stdout-loglevel = "DEBUG"
    log-config-on-start = off

    actor {
      provider = remote
      debug {
        receive = on
        autoreceive = on
        lifecycle = on
        fsm = on
        event-stream = on
        unhandled = on
        router-misconfiguration = on
      }
    }
    remote {
      enabled-transports = ["akka.remote.netty.tcp"]
      netty.tcp {
        hostname = "localhost"
        port = 2550
      }
    }
  }
}

player {
  akka.actor.debug.receive = on
  akka.actor.warn-about-java-serializer-usage = off

  akka {
    loggers = ["akka.event.Logging$DefaultLogger"]
    logging-filter = "akka.event.DefaultLoggingFilter"
    loggers-dispatcher = "akka.actor.default-dispatcher"
    logger-startup-timeout = 5s
    loglevel = "DEBUG"
    stdout-loglevel = "DEBUG"
    log-config-on-start = off

    actor {
      provider = remote
      debug {
        receive = on
        autoreceive = on
        lifecycle = on
        fsm = on
        event-stream = on
        unhandled = on
        router-misconfiguration = on
      }
    }
    remote {
      enabled-transports = ["akka.remote.netty.tcp"]
      netty.tcp {
        hostname = "localhost"
        port = 2551
      }
    }
  }
}

predictor {
  akka.actor.debug.receive = on
  akka.actor.warn-about-java-serializer-usage = off

  akka {
    loggers = ["akka.event.Logging$DefaultLogger"]
    logging-filter = "akka.event.DefaultLoggingFilter"
    loggers-dispatcher = "akka.actor.default-dispatcher"
    logger-startup-timeout = 5s
    loglevel = "DEBUG"
    stdout-loglevel = "DEBUG"
    log-config-on-start = off

    actor {
      provider = remote
      debug {
        receive = on
        autoreceive = on
        lifecycle = on
        fsm = on
        event-stream = on
        unhandled = on
        router-misconfiguration = on
      }
    }
    remote {
      enabled-transports = ["akka.remote.netty.tcp"]
      netty.tcp {
        hostname = "localhost"
        port = 2552
      }
    }
  }
}
```
