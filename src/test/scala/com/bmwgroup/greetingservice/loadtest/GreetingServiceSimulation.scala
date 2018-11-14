package com.bmwgroup.greetingservice.loadtest

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class GreetingServiceSimulation extends Simulation {

  val rampUpTimeSecs = 30
  val testTimeSecs = 60
  val initialUsers = 10
  val noOfUsers = 50
  val minWaitMs = 1000 milliseconds
  val maxWaitMs = 3000 milliseconds

  val baseURL = "http://localhost:8080"
  val baseName = "webservice-call-greeting"
  val requestName = baseName + "-request"
  val scenarioName = baseName + "-scenario"
  val URI = "/greetings"

  val httpConf = http
    .baseUrl(baseURL)
    .acceptHeader("text/plain") // 6
    //.doNotTrackHeader("1")

  val scn = scenario(scenarioName)
    .during(testTimeSecs) {
      exec(
        http(requestName)
          .get(URI)
          .check(status.is(200))
      )
    }

  setUp(
    scn.inject(
      atOnceUsers(initialUsers),
      rampUsers(noOfUsers) during (rampUpTimeSecs seconds)
    )
  ).protocols(httpConf)
}
