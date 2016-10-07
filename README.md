# MarketMatch

a simple engine , just for matching order.

the engine only support limit price matching.

# license

Apache License Version 2.0

# how to run

1. build war by gradle
    `gradle war`
2. put war into Tomcat
3. run Tomcat
4. type "http://localhost:8080/[war name]" in the browser's location bar to access the webpage

# used open source library

## java library

- slf4j
- logback
- fastjson
- junit

## js library

- jquery
- vue