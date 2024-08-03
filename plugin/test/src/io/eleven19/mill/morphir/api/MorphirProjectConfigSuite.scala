package io.eleven19.mill.morphir.api
import upickle.default._
import mill.api.JsonFormatters._

class MorphirProjectConfigSuite extends munit.FunSuite {
    test("MorphirProjectConfig should serialize to JSON") {
        val config = MorphirProjectConfig(
            name = "test",
            sourceDirectory = "test",
            exposedModules = List("TestModule"),
            dependencies = List("TestDependency"),
            localDependencies = List("TestLocalDependency"),
        )
        val json = write(config)
        val expectedJson =
            """{"name":"test","sourceDirectory":"test","exposedModules":["TestModule"],"dependencies":["TestDependency"],"localDependencies":["TestLocalDependency"]}"""
        assertEquals(json, expectedJson)
    }

    test("MorphirProjectConfig should deserialize from JSON") {
        val json =
            """{"name":"test","sourceDirectory":"test","exposedModules":["TestModule"],"dependencies":["TestDependency"],"localDependencies":["TestLocalDependency"]}"""
        val config = read[MorphirProjectConfig](json)
        val expectedConfig = MorphirProjectConfig(
            name = "test",
            sourceDirectory = "test",
            exposedModules = List("TestModule"),
            dependencies = List("TestDependency"),
            localDependencies = List("TestLocalDependency"),
        )
        assertEquals(config, expectedConfig)
    }
}
