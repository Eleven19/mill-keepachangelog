package io.eleven19.mill.morphir.elm

import io.eleven19.mill.morphir.MorphirModule
import mill.{PathRef, T, Target, Task, util}

trait MorphirElmModule extends ElmModule with MorphirModule {
    def morphirElmCommand: Target[String] = T("morphir-elm")
    def useFallbackCli:    Boolean        = false

    override def makeCommandRunner: Target[String] = T(morphirElmCommand())

    override def makeCommandArgs(cliCommand: String, destPath: os.Path): Task[Seq[String]] = T.task {
        val args                = super.makeCommandArgs(cliCommand, destPath)()
        val shouldUseMorphirElm = cliCommand.endsWith("morphir-elm")
        if (shouldUseMorphirElm && useFallbackCli) {
            args ++ Seq("--fallback-cli")
        } else {
            args
        }
    }
}
